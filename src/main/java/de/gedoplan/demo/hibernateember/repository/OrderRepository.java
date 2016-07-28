package de.gedoplan.demo.hibernateember.repository;

import de.gedoplan.demo.hibernateember.model.Order;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

/**
 * Datenbankzugriffe für Bestellungen.
 *
 * @author Dominik Mathmann
 */
@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class OrderRepository extends BasicRepository<Order, Integer> {

    public OrderRepository() {
        super(Order.class);
    }

}
