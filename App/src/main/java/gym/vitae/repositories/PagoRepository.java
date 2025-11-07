package gym.vitae.repositories;

import gym.vitae.model.Pago;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class PagoRepository implements IRepository<Pago> {

    private final EntityManager em;

    public PagoRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Pago save(Pago entity) {
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
    public boolean update(Pago entity) {
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
        em.createQuery("update Pago p set p.estado = 'anulado' where p.id = :id")
          .setParameter("id", id)
          .executeUpdate();
    }

    @Override
    public Optional<Pago> findById(int id) {
        return Optional.ofNullable(em.find(Pago.class, id));
    }

    @Override
    public List<Pago> findAll() {
        TypedQuery<Pago> q = em.createQuery("from Pago p order by p.id", Pago.class);
        return q.getResultList();
    }

    @Override
    public List<Pago> findAll(int offset, int limit) {
        TypedQuery<Pago> q = em.createQuery("from Pago p order by p.id", Pago.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> q = em.createQuery("select count(p) from Pago p", Long.class);
        return q.getSingleResult();
    }

    @Override
    public boolean existsById(int id) {
        return em.find(Pago.class, id) != null;
    }
}
