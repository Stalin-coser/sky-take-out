package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.enumeration.OperationType;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    void update(Orders orders);

    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String outTradeNo);


    Page<OrderVO> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id} and user_id = #{userId}")
    Orders getByIds(Long id , Long userId);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    Page<OrderVO> condtitionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select count(*) from orders where status = #{status}")
    Integer countStatus(Integer status);

}
