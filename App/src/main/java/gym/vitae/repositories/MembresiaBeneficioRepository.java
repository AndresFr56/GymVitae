package gym.vitae.repositories;

import gym.vitae.model.MembresiaBeneficio;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class MembresiaBeneficioRepository implements IRepository<MembresiaBeneficio> {

    private final EntityManager em;

    public MembresiaBeneficioRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public MembresiaBeneficio save(MembresiaBeneficio entity) {
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
    public boolean update(MembresiaBeneficio entity) {
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
        em.remove(em.find(MembresiaBeneficio.class, id));
    }

    @Override
    public Optional<MembresiaBeneficio> findById(int id) {
        return Optional.ofNullable(em.find(MembresiaBeneficio.class, id));
    }

    @Override
    public List<MembresiaBeneficio> findAll() {
        TypedQuery<MembresiaBeneficio> q = em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
        return q.getResultList();
    }

    @Override
    public List<MembresiaBeneficio> findAll(int offset, int limit) {
        TypedQuery<MembresiaBeneficio> q = em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> q = em.createQuery("select count(mb) from MembresiaBeneficio mb", Long.class);
        return q.getSingleResult();
    }

    @Override
    public boolean existsById(int id) {
        return em.find(MembresiaBeneficio.class, id) != null;
    }
}
