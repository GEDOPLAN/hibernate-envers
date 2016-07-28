package de.gedoplan.demo.envers.test.repository;

import de.gedoplan.demo.envers.model.Order;
import de.gedoplan.demo.envers.model.Order_;
import de.gedoplan.demo.envers.repository.OrderRepository;
import de.gedoplan.demo.envers.test.TestBaseClass;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import junit.framework.Assert;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    @PersistenceContext(name = "default")
    EntityManager entityManager;

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
        Order order = orderRepository.getAll(Order_.orderID, true).get(0);
        String newShipName = order.getShipName() + "_changed";
        order.setShipName(newShipName);
        order.setOrderDate(new Date());
        order = orderRepository.merge(order);
        Assert.assertEquals(orderRepository.findById(order.getOrderID()).getShipName(), newShipName);
    }

    @Test
    public void testRevision() {
        Order order = orderRepository.getAll(Order_.orderID, true).get(0);
        String oldShipName = order.getShipName();
        String newShipName = order.getShipName() + "_changed";
        order.setShipName(newShipName);
        order.setOrderDate(new Date());
        order = orderRepository.merge(order);

        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Order.class, order.getOrderID());
        Assert.assertTrue(revisions.size() > 1);
        Order revOrder = auditReader.find(Order.class, order.getOrderID(), revisions.get(0));
        Assert.assertTrue(revOrder.getShipName().equals(oldShipName));

        //Revert to First Revision
        order = orderRepository.merge(revOrder);
        Assert.assertEquals(orderRepository.findById(order.getOrderID()).getShipName(), oldShipName);
    }
}
