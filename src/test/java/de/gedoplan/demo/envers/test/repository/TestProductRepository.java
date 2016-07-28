package de.gedoplan.demo.envers.test.repository;

import de.gedoplan.demo.envers.model.Product;
import de.gedoplan.demo.envers.model.Product_;
import de.gedoplan.demo.envers.repository.ProductRepository;
import de.gedoplan.demo.envers.test.TestBaseClass;
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
public class TestProductRepository extends TestBaseClass {

    @Inject
    ProductRepository productRepository;

    @PersistenceContext(name = "default")
    EntityManager entityManager;

    @Before
    public void createOrder() {
        Product product = new Product();
        product.setProductName("Product 1");
        product.setQuantityPerUnit("stk");
        product.setUnitPrice(122.2);
        product = productRepository.merge(product);
    }

    @Test
    public void testRevision() {
        Product product = productRepository.getAll(Product_.productID, true).get(0);

        String oldName = product.getProductName();

        product.setUnitPrice(5.);
        product.setProductName("Changed...");
        product = productRepository.merge(product);

        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Product.class, product.getProductID());
        Product revProduct = auditReader.find(Product.class, product.getProductID(), revisions.get(0));

        Assert.assertTrue(revProduct.getProductName().equals(oldName));
        Assert.assertNull(revProduct.getUnitPrice());
    }
}
