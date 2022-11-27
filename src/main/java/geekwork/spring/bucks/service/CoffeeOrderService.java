package geekwork.spring.bucks.service;

import geekwork.spring.bucks.enums.OrderState;
import geekwork.spring.bucks.mapper.OrderCoffeeMapper;
import geekwork.spring.bucks.mapper.OrderMapper;
import geekwork.spring.bucks.model.Coffee;
import geekwork.spring.bucks.model.Order;
import geekwork.spring.bucks.model.OrderCoffee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CoffeeOrderService {
    @Autowired
    private OrderCoffeeMapper orderCoffeeMapper;

    @Autowired
    private OrderMapper orderMapper;

    public Order createOrder(String customer, Coffee... coffee) {
        Order order = new Order()
                .withCreateTime(new Date())
                .withUpdateTime(new Date())
                .withCustomer(customer)
                .withItems(new ArrayList<>(Arrays.asList(coffee)))
                .withState(OrderState.INIT);
        orderMapper.insert(order);
        List<Coffee> coffees = order.getItems();
        List<OrderCoffee> orderCoffees = coffees.stream()
                .map(c -> new OrderCoffee().withItemsId(c.getId()).withCoffeeOrderId(order.getId()))
                .collect(Collectors.toList());
        orderCoffees.forEach(orderCoffeeMapper::insert);
        log.info("New Order: {}", order);
        return order;
    }

    public boolean updateState(Order order, OrderState state) {
        if (state.compareTo(order.getState()) <= 0) {
            log.warn("Wrong State order: {}, {}", state, order.getState());
            return false;
        }
        order.setState(state);
        orderMapper.updateByPrimaryKey(order);
        log.info("Updated Order: {}", order);
        return true;
    }
}
