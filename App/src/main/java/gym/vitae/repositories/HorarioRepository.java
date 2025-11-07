package gym.vitae.repositories;

import gym.vitae.model.Horario;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class HorarioRepository implements IRepository<Horario> {

    private final EntityManager em;

    public HorarioRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Horario save(Horario entity) {
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
    public boolean update(Horario entity) {
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
        em.createQuery("update Horario h set h.activo = false where h.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Optional<Horario> findById(int id) {
        return Optional.ofNullable(em.find(Horario.class, id));
    }

    @Override
    public List<Horario> findAll() {
        TypedQuery<Horario> q = em.createQuery("from Horario h order by h.id", Horario.class);
        return q.getResultList();
    }

    @Override
    public List<Horario> findAll(int offset, int limit) {
        TypedQuery<Horario> q = em.createQuery("from Horario h order by h.id", Horario.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> q = em.createQuery("select count(h) from Horario h", Long.class);
        return q.getSingleResult();
    }

    @Override
    public boolean existsById(int id) {
        return em.find(Horario.class, id) != null;
    }
}
