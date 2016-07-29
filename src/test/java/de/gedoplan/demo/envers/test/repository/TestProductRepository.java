package de.gedoplan.demo.envers.test.repository;

import de.gedoplan.demo.envers.model.Product;
import de.gedoplan.demo.envers.model.Product_;
import de.gedoplan.demo.envers.model.RevisionData;
import de.gedoplan.demo.envers.repository.ProductRepository;
import de.gedoplan.demo.envers.test.TestBaseClass;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import junit.framework.Assert;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

/**
 * Test Product Repository mit erweitertem Auslesen von Revision Informationen.
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
    public void createProduct() {
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

        // Revision-Objekt und zusätzliche Daten auf Basis der ID und der ersten Revision ermitteln.
        AuditQuery revQuery = auditReader.createQuery().forRevisionsOfEntity(Product.class, false, true);
        revQuery.add(AuditEntity.revisionNumber().eq(revisions.get(0)));
        revQuery.add(AuditEntity.id().eq(product.getProductID()));

        // liefert Objekt Array mit Entät, RevisionObjekt und RevisionType
        Object[] revObject = (Object[]) revQuery.getSingleResult();

        Product revProduct = (Product) revObject[0];
        RevisionData revData = (RevisionData) revObject[1];
        RevisionType revType = (RevisionType) revObject[2];

        Assert.assertTrue(revProduct.getProductName().equals(oldName));
        Assert.assertEquals(revData.getUsername(), "Dummy-User");
    }

    @Test
    public void testDelete() {
        Product product = productRepository.getAll(Product_.productID, true).get(0);
        product.setProductName("Should be deleted...");
        product = productRepository.merge(product);
        productRepository.delete(product);
        Assert.assertTrue(productRepository.findByAttribute(Product_.productID, product.getProductID()).size() == 0);

        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        // letzten Stand vorm Löschen ermitteln
        AuditQuery revQuery = auditReader.createQuery().forRevisionsOfEntity(Product.class, false, true);
        revQuery.add(AuditEntity.revisionType().eq(RevisionType.MOD));
        revQuery.add(AuditEntity.id().eq(product.getProductID()));
        Object[] revObject = (Object[]) revQuery.getSingleResult();

        Product revProduct = (Product) revObject[0];

        Assert.assertTrue(revProduct.getProductName().equals(product.getProductName()));
        Assert.assertTrue(revProduct.getProductID().equals(product.getProductID()));
    }
}
