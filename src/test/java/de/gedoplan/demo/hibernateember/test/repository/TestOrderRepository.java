package de.gedoplan.demo.hibernateember.test.repository;

import de.gedoplan.demo.hibernateember.model.Order;
import de.gedoplan.demo.hibernateember.repository.OrderRepository;
import de.gedoplan.demo.hibernateember.test.TestBaseClass;
import java.util.ArrayList;
import java.util.Date;
import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import de.gedoplan.demo.hibernateember.model.Order_;
import org.junit.Before;

/**
 * Test Order Repository
 *
 * @author Dominik Mathmann
 */
@RunWith(Arquillian.class)
public class TestOrderRepository extends TestBaseClass {

    @Inject
    OrderRepository orderRepository;

    @Before
    public void createOrder() {
        Order order = new Order();
        order.setFreight(12.);
        order.setOrderDate(new Date());
        order.setOrderDetails(new ArrayList<>());
        order.setShipName("D12005");
        order.setShippedDate(new Date());

        orderRepository.merge(order);
    }

    @Test
    public void testOrderExists() {
        Assert.assertTrue(orderRepository.getAll().size() > 0);
        Assert.assertEquals(orderRepository.getAll(Order_.orderID, true).get(0).getFreight(), 12.);
    }

    @Test
    public void testUpdate() {
        Order order = orderRepository.getAll().get(0);
        String newShipName = order.getShipName() + "_changed";
        order.setShipName(newShipName);
        order = orderRepository.merge(order);
        Assert.assertEquals(orderRepository.findById(order.getOrderID()).getShipName(), newShipName);
    }
}
