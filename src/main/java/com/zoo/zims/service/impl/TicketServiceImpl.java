package com.zoo.zims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoo.zims.common.StatusCode;
import com.zoo.zims.entity.TicketOrder;
import com.zoo.zims.mapper.TicketOrderMapper;
import com.zoo.zims.service.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TicketServiceImpl extends ServiceImpl<TicketOrderMapper, TicketOrder> implements TicketService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(TicketOrder order) {
        // 1. 生成唯一订单号 (这里简单使用 时间戳 + 随机数 模拟 Snowflake)
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
        String orderNo = "T" + timeStr + randomNum;
        order.setOrderNo(orderNo);

        // 2. 初始化状态
        if (order.getPayStatus() == null) {
            order.setPayStatus(0); // 未支付
        }
        order.setCreateTime(LocalDateTime.now());

        this.save(order);
        return orderNo;
    }

    @Override
    public boolean verifyTicket(String orderNo) {
        TicketOrder order = this.getById(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getPayStatus() != 1) {
            throw new RuntimeException("订单未支付或已退款");
        }

        // 实际业务中可能需要判断是否已使用，这里简化处理，假设核销就是记录入园
        // 可以增加一个 status 字段记录 '已核销'

        return true;
    }
}