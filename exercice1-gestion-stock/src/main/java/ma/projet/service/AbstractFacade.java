package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/** Implémentation générique de IDao : les services la spécialisent. */
public abstract class AbstractFacade<T> implements IDao<T> {

    private final Class<T> entityClass;

    protected AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    private boolean inTransaction(Consumer<Session> action) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /** Lecture : ouvre une Session, exécute le travail, la ferme toujours. */
    protected <R> R read(Function<Session, R> work) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return work.apply(session);
        } finally {
            session.close();
        }
    }

    @Override
    public boolean create(T o) { return inTransaction(s -> s.save(o)); }

    @Override
    public boolean update(T o) { return inTransaction(s -> s.update(o)); }

    @Override
    public boolean delete(T o) { return inTransaction(s -> s.delete(o)); }

    @Override
    public T findById(int id) { return read(s -> s.get(entityClass, id)); }

    @Override
    public List<T> findAll() {
        return read(s -> s.createQuery("from " + entityClass.getSimpleName(), entityClass).list());
    }
}
