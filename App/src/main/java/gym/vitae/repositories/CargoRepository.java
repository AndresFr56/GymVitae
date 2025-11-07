package gym.vitae.repositories;

import gym.vitae.model.Cargo;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CargoRepository implements IRepository<Cargo> {

    private final EntityManager em;

    public CargoRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Cargo save(Cargo entity) {
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
    public boolean update(Cargo entity) {
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
        em.createQuery("update Cargo C set C.activo = false where C.id = :id")
          .setParameter("id", id)
          .executeUpdate();
    }

    @Override
    public Optional<Cargo> findById(int id) {
        return Optional.ofNullable(em.find(Cargo.class, id));
    }

    @Override
    public List<Cargo> findAll() {
        TypedQuery<Cargo> q = em.createQuery("from Cargo c order by c.id", Cargo.class);
        return q.getResultList();
    }

    @Override
    public List<Cargo> findAll(int offset, int limit) {
        TypedQuery<Cargo> q = em.createQuery("from Cargo c order by c.id", Cargo.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> q = em.createQuery("select count(c) from Cargo c", Long.class);
        return q.getSingleResult();
    }

    @Override
    public boolean existsById(int id) {
        return em.find(Cargo.class, id) != null;
    }
}
