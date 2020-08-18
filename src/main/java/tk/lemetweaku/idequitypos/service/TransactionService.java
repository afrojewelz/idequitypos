package tk.lemetweaku.idequitypos.service;

import tk.lemetweaku.idequitypos.entity.Order;

import java.util.List;

public interface TransactionService {

    List<String> getRTPositions() throws Exception;

    List<Order> getAllOrders() throws Exception;

    void PositionsCal(Order order);

    void insertTransac(Order order);
}
