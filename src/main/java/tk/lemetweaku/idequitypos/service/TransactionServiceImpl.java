package tk.lemetweaku.idequitypos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.lemetweaku.idequitypos.dao.OrderDao;
import tk.lemetweaku.idequitypos.entity.Order;
import tk.lemetweaku.idequitypos.entity.Positions;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    private ConcurrentHashMap<String, Positions> realTimePositionsMap = new ConcurrentHashMap<String, Positions>();

    public ConcurrentHashMap<String, Positions> getRealTimePositionsMap() {
        return realTimePositionsMap;
    }

    @PostConstruct
    public void init() {
        realTimePositionsMap.put("REL", new Positions());
        realTimePositionsMap.put("ITC", new Positions());
        realTimePositionsMap.put("INF", new Positions());
    }

    /**
     * @param @param order
     * @return void
     * @throws
     * @Title: setSign
     * @Description: mark sign of +/-，if order tells sell,marks -
     */
    public void setSign(Order order) {
        if (order.getTradeMark().equals("Sell")) {
            if (order.getQuantity() > 0) {
                order.setQuantity((-1) * order.getQuantity());
            }
        }
    }

    public List<String> getRTPositions() throws Exception {
        List<String> positList = new ArrayList<String>();
        String strValue = "";
        for (Map.Entry<String, Positions> entry : realTimePositionsMap.entrySet()) {
            //getDelayTransacList
            Positions positions = entry.getValue();
            CopyOnWriteArrayList<Order> delayOrderList = positions.getDelayOrderList();
            //merge PositionCalc
            for (int i = 0; i < delayOrderList.size(); i++) {
                positions.setPositions(delayOrderList.get(i));
            }
            for (int i = delayOrderList.size() - 1; i >= 0; i--) {
                delayOrderList.remove(i);
            }
            strValue = Integer.toString(entry.getValue().getCurrPositions());
            if (entry.getValue().getCurrPositions() > 0) {
                strValue = "+" + strValue;
            }
            positList.add("stockName:" + entry.getKey() + ", CurrentPosition:" + strValue);
        }
        return positList;
    }


    public List<Order> getAllOrders() throws Exception {
        return orderDao.getAll();
    }

    /**
     * @param @param order    param
     * @return void    returnType
     * @throws
     * @Title: PositionsCal
     * @Description: positionCal
     */
    public void PositionsCal(Order order) {
        try {
            //  accquire current stock
            String strKey = order.getSecurityCode();
            Positions positions = realTimePositionsMap.get(strKey);

            //lock current contract object
            synchronized (positions) {
                // whatif laterversion came first
                if (order.getVersion() > 1) {
                    positions.addToOrderList(order);
                }
                // accquireCurrentPosition&Merge
                positions.setPositions(order);
                // writeInto Buffer
                realTimePositionsMap.put(order.getSecurityCode(), positions);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * @param @param order    param
     * @return void    returnType
     * @throws
     * @Title: insert
     * @Description: insertTransac
     */
    @Transactional
    public void insertTransac(Order order) {
        // 正负号处理
        setSign(order);
        // 持仓计算
        PositionsCal(order);
        // 写数据库
        orderDao.insertTransac(order);
    }
}
