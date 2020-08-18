package tk.lemetweaku.idequitypos;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.lemetweaku.idequitypos.entity.Order;
import tk.lemetweaku.idequitypos.entity.Positions;
import tk.lemetweaku.idequitypos.service.TransactionServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc // INJECT MOCKMVC
public class TransacServiceImplTest {
    private final static Logger logger = LoggerFactory.getLogger(OrderDaoTest.class);

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @Test
    public void testPositionsCal() throws Exception {
        //initialize orderlist
        List<Order> ordList = new ArrayList<Order>();
        Order ord1 = new Order(1, 1, 50, "REL", "INSERT", "Buy");
        Order ord2 = new Order(2, 1, 40, "ITC", "INSERT", "Sell");
        Order ord3 = new Order(3, 1, 70, "INF", "INSERT", "Buy");
        Order ord4 = new Order(1, 2, 60, "REL", "UPDATE", "Buy");
        Order ord5 = new Order(2, 2, 30, "ITC", "CANCEL", "Buy");
        Order ord6 = new Order(4, 1, 20, "INF", "INSERT", "Sell");
        ordList.add(ord1);
        ordList.add(ord2);
        ordList.add(ord3);
        ordList.add(ord4);
        ordList.add(ord5);
        ordList.add(ord6);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(6, 6, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        CyclicBarrier cb = new CyclicBarrier(6);
        for (int k = 0; k < 200; k++) {

            CountDownLatch cdl = new CountDownLatch(6);
            for (int i = 0; i < 6; i++) {
                int n = i;
                threadPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //await begin
                            cb.await();
                            //transact begin
                            transactionServiceImpl.insertTransac(ordList.get(n));
                            //await ending
                            cb.await();
                            cdl.countDown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            //await all threads ends
            cdl.await();
            logger.info("------" + transactionServiceImpl.getRTPositions() + "------");
            //erase all records
            for (Map.Entry<String, Positions> entry : transactionServiceImpl.getRealTimePositionsMap().entrySet()) {
                Positions positions = entry.getValue();
                positions.setCurrPositions(0);
                CopyOnWriteArrayList<Order> delayOrderList = positions.getDelayOrderList();
                delayOrderList.clear();
            }
        }
        //shutdown threads pool
        Thread.sleep(2000);
        threadPoolExecutor.shutdown();
        try {
            if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                // send soft interrupt to al timeout threads
                threadPoolExecutor.shutdownNow();
            }
            logger.info("AwaitTermination Finished");
        } catch (InterruptedException ignore) {
            threadPoolExecutor.shutdownNow();
        }
        threadPoolExecutor.shutdown();
    }
}
