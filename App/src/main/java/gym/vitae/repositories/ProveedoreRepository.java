package gym.vitae.repositories;

import gym.vitae.model.Proveedore;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ProveedoreRepository implements IRepository<Proveedore> {

    private final EntityManager em;

    public ProveedoreRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Proveedore save(Proveedore entity) {
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
    public boolean update(Proveedore entity) {
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
        em.createQuery("update Proveedore p set p.activo = false where p.id = :id")
          .setParameter("id", id)
          .executeUpdate();
    }

    @Override
    public Optional<Proveedore> findById(int id) {
        return Optional.ofNullable(em.find(Proveedore.class, id));
    }

    @Override
    public List<Proveedore> findAll() {
        TypedQuery<Proveedore> q = em.createQuery("from Proveedore p order by p.id", Proveedore.class);
        return q.getResultList();
    }

    @Override
    public List<Proveedore> findAll(int offset, int limit) {
        TypedQuery<Proveedore> q = em.createQuery("from Proveedore p order by p.id", Proveedore.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> q = em.createQuery("select count(p) from Proveedore p", Long.class);
        return q.getSingleResult();
    }

    @Override
    public boolean existsById(int id) {
        return em.find(Proveedore.class, id) != null;
    }
}
