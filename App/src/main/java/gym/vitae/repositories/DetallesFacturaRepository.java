package gym.vitae.repositories;

import gym.vitae.model.DetallesFactura;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class DetallesFacturaRepository implements IRepository<DetallesFactura> {

    private final EntityManager em;

    public DetallesFacturaRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public DetallesFactura save(DetallesFactura entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            if (!tx.isActive()) tx.begin();
            em.persist(entity);
            tx.commit();
            return entity;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        }
    }

    @Override
    public boolean update(DetallesFactura entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            if (!tx.isActive()) tx.begin();
            em.merge(entity);
            tx.commit();
            return true;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        }
    }

    @Override
    public void delete(int id) {
        em.remove(findById(id));
    }

    @Override
    public Optional<DetallesFactura> findById(int id) {
        return Optional.ofNullable(em.find(DetallesFactura.class, id));
    }

    @Override
    public List<DetallesFactura> findAll() {
        TypedQuery<DetallesFactura> q = em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
        return q.getResultList();
    }

    @Override
    public List<DetallesFactura> findAll(int offset, int limit) {
        TypedQuery<DetallesFactura> q = em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> q = em.createQuery("select count(d) from DetallesFactura d", Long.class);
        return q.getSingleResult();
    }

    @Override
    public boolean existsById(int id) {
        return em.find(DetallesFactura.class, id) != null;
    }
}
