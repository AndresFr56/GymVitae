package gym.vitae.repositories;

import gym.vitae.model.Iva;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class IvaRepository implements IRepository<Iva> {

    private final EntityManager em;

    public IvaRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Iva save(Iva entity) {
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
    public boolean update(Iva entity) {
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
        // Soft-delete: mark inactive
        em.createQuery("update Iva i set i.activo = false where i.id = :id")
          .setParameter("id", id)
          .executeUpdate();
    }

    @Override
    public Optional<Iva> findById(int id) {
        return Optional.ofNullable(em.find(Iva.class, id));
    }

    @Override
    public List<Iva> findAll() {
        TypedQuery<Iva> q = em.createQuery("from Iva i order by i.id", Iva.class);
        return q.getResultList();
    }

    @Override
    public List<Iva> findAll(int offset, int limit) {
        TypedQuery<Iva> q = em.createQuery("from Iva i order by i.id", Iva.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> q = em.createQuery("select count(i) from Iva i", Long.class);
        return q.getSingleResult();
    }

    @Override
    public boolean existsById(int id) {
        return em.find(Iva.class, id) != null;
    }
}
