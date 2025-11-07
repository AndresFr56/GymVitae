package gym.vitae.repositories;

import gym.vitae.model.Producto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ProductoRepository implements IRepository<Producto> {

    private final EntityManager em;

    public ProductoRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Producto save(Producto entity) {
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
    public boolean update(Producto entity) {
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
        em.createQuery("update Producto p set p.activo = false where p.id = :id")
          .setParameter("id", id)
          .executeUpdate();
    }

    @Override
    public Optional<Producto> findById(int id) {
        return Optional.ofNullable(em.find(Producto.class, id));
    }

    @Override
    public List<Producto> findAll() {
        TypedQuery<Producto> q = em.createQuery("from Producto p order by p.id", Producto.class);
        return q.getResultList();
    }

    @Override
    public List<Producto> findAll(int offset, int limit) {
        TypedQuery<Producto> q = em.createQuery("from Producto p order by p.id", Producto.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> q = em.createQuery("select count(p) from Producto p", Long.class);
        return q.getSingleResult();
    }

    @Override
    public boolean existsById(int id) {
        return em.find(Producto.class, id) != null;
    }
}
