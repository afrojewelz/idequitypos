package tk.lemetweaku.idequitypos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import tk.lemetweaku.idequitypos.dao.OrderDao;
import tk.lemetweaku.idequitypos.entity.Order;

import java.util.List;

@RunWith(SpringRunner.class)
@Transactional//rollbackALLUPDATE
@SpringBootTest
@AutoConfigureMockMvc//INJECT MOCKMVC
public class OrderDaoTest {

    private final static Logger logger = LoggerFactory.getLogger(OrderDaoTest.class);

    @Autowired
    private OrderDao orderDao;

    @Test
    public void testInsert() throws Exception {

        orderDao.insertTransac(new Order(1, 1, 50,"REL","INSERT","Buy"));
        orderDao.insertTransac(new Order(2, 1, 40,"ITC","INSERT","Sell"));
        orderDao.insertTransac(new Order(3, 1, 70,"INF","INSERT","Buy"));
        orderDao.insertTransac(new Order(1, 2, 60,"REL","UPDATE","Buy"));
        orderDao.insertTransac(new Order(2, 2, 30,"ITC","CANCEL","Buy"));
        orderDao.insertTransac(new Order(4, 1, 20,"INF","INSERT","Sell"));

        Assert.assertEquals(6, orderDao.getAll().size());
    }

    @Test
    public void testQuery() throws Exception {
        List<Order> order = orderDao.getAll();
        logger.info(order.toString());
    }
}
