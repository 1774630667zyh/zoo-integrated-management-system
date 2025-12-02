package com.zoo.zims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoo.zims.entity.TicketOrder;

public interface TicketService extends IService<TicketOrder> {

    /**
     * 创建订单
     * @param order 订单信息
     * @return 生成的订单号
     */
    String createOrder(TicketOrder order);

    /**
     * 核销/验票
     * @param orderNo 订单号
     * @return 是否成功
     */
    boolean verifyTicket(String orderNo);
}