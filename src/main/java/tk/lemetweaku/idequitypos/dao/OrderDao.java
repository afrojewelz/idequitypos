package tk.lemetweaku.idequitypos.dao;

import org.apache.ibatis.annotations.Mapper;
import tk.lemetweaku.idequitypos.entity.Order;

import java.util.List;

@Mapper
public interface OrderDao {

    List<Order> getAll();

    void insertTransac(Order order);

}
