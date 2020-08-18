package tk.lemetweaku.idequitypos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.lemetweaku.idequitypos.common.Actions;
import tk.lemetweaku.idequitypos.entity.Order;
import tk.lemetweaku.idequitypos.service.TransactionService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class TransactionController {
    private final static Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/getRTPositions")
    public List<String> getRTPositions() {
        try {
            return transactionService.getRTPositions();
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }

    public boolean checkParamValid(Order order) {
        if (order.getCommand().equals(Actions.UPDATE) ||
                order.getCommand().equals(Actions.CANCEL)) {
            if (order.getVersion() <= 1) {
                logger.error("parameters fail of validate，version value is not valid");
                return false;
            }
        }
        return true;
    }

    /**
     * @param Order
     * @return String returnType
     * @throws
     * @Title: insertTransac
     * @Description: transactOrders
     */
    @RequestMapping("/order")
    @ResponseBody
    public String insertTransac(@Valid Order order) {
        //下单
        try {
            if (!checkParamValid(order)) {
                throw new RuntimeException();
            }
            transactionService.insertTransac(order);
        } catch (Exception e) {
            logger.error(e.toString());
            return "Ordering failed,Error Message: " + e.toString();
        }
        return "Ordering Success";
    }
}
