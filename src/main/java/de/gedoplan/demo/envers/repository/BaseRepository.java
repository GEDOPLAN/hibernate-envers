package de.gedoplan.demo.envers.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import javax.transaction.Transactional;

@Transactional(rollbackOn = Throwable.class)
abstract class BasicRepository<ENTITY, ID> {

    @PersistenceContext(name = "default")
    EntityManager entityManager;

    private final Class<ENTITY> entityClass;

    private BasicRepository() {
        this.entityClass = null;
    }

    public BasicRepository(Class<ENTITY> entityKlasse) {
        this.entityClass = entityKlasse;
    }

    public ENTITY findById(ID id) {
        return this.entityManager.find(entityClass, id);
    }

    public <Y> List<ENTITY> findByAttribute(SingularAttribute<ENTITY, Y> attributename, Y attributevalue) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ENTITY> query = cb.createQuery(entityClass);
        Root<ENTITY> root = query.from(entityClass);
        query.where(cb.equal(root.get(attributename), attributevalue));

        return this.getEntityManager().createQuery(query).getResultList();
    }

    public <Y> Long countByAttribute(SingularAttribute<ENTITY, Y> attributename, Y attributevalue) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<ENTITY> root = query.from(entityClass);
        query.select(cb.count(root));
        query.where(cb.equal(root.get(attributename), attributevalue));

        return this.getEntityManager().createQuery(query).getSingleResult();
    }

    public Long count() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<ENTITY> root = query.from(entityClass);
        query.select(cb.count(root));

        return this.getEntityManager().createQuery(query).getSingleResult();
    }

    public ENTITY merge(ENTITY entity) {
        return this.entityManager.merge(entity);
    }

    public void delete(Integer id) {
        ENTITY reference = this.entityManager.getReference(entityClass, id);
        this.entityManager.remove(reference);
    }

    public void delete(ENTITY entity) {
        this.entityManager.remove(this.entityManager.merge(entity));
    }

    public List<ENTITY> getAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ENTITY> query = cb.createQuery(entityClass);
        Root<ENTITY> root = query.from(entityClass);

        return this.getEntityManager().createQuery(query).getResultList();
    }

    public List<ENTITY> getAll(SingularAttribute<ENTITY, ?> sortAttribut, boolean descending) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ENTITY> query = cb.createQuery(entityClass);
        Root<ENTITY> root = query.from(entityClass);

        if (descending) {
            query.orderBy(cb.desc(root.get(sortAttribut)));
        } else {
            query.orderBy(cb.asc(root.get(sortAttribut)));
        }
        return this.getEntityManager().createQuery(query).getResultList();
    }

    public List<ENTITY> getAll(SingularAttribute<ENTITY, ?> sortAttribut, boolean descending, int max, int start) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ENTITY> query = cb.createQuery(entityClass);
        Root<ENTITY> root = query.from(entityClass);

        if (descending) {
            query.orderBy(cb.desc(root.get(sortAttribut)));
        } else {
            query.orderBy(cb.asc(root.get(sortAttribut)));
        }
        TypedQuery<ENTITY> typedQuery = this.getEntityManager().createQuery(query);
        typedQuery.setMaxResults(max);
        typedQuery.setFirstResult(start);

        return typedQuery.getResultList();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
