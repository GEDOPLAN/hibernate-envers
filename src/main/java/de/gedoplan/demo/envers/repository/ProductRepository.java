package de.gedoplan.demo.envers.repository;

import de.gedoplan.demo.envers.model.Product;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

/**
 * Datenbankzugriffe für Bestellungen.
 *
 * @author Dominik Mathmann
 */
@ApplicationScoped
@Transactional(rollbackOn = Throwable.class)
public class ProductRepository extends BasicRepository<Product, Integer> {

    public ProductRepository() {
        super(Product.class);
    }

}
