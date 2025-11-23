package gym.vitae.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Manager que mantiene el EntityManagerFactory y crea EntityManager por petición.
 * Permite init(), reconnect() y close(). Lanza DatabaseUnavailableException si no hay EMF.
 */
public class DBConnectionManager {

    private static final String PERSISTANCE = "gym_vitaePU";
    private EntityManagerFactory emf;
    private volatile boolean connected = false;

    public synchronized void init() {
        try {
            if (emf != null && emf.isOpen()) return;
            emf = Persistence.createEntityManagerFactory(PERSISTANCE);
            connected = emf != null && emf.isOpen();
        } catch (RuntimeException ex) {
            connected = false;
            System.err.println("DBConnectionManager.init() failed: " + ex.getMessage());
        }
    }

    public synchronized boolean isConnected() {
        return connected && emf != null && emf.isOpen();
    }

    /**
     * Crea y devuelve un EntityManager nuevo. Lanza DatabaseUnavailableException si EMF no disponible.
     */
    public synchronized EntityManager getEntityManager() {
        if (!isConnected()) {
            throw new DatabaseUnavailableException("sin conexión a BD");
        }
        return emf.createEntityManager();
    }

    public synchronized EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public synchronized void close() {
        try {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } finally {
            emf = null;
            connected = false;
        }
    }

    public synchronized boolean reconnect() {
        close();
        init();
        return isConnected();
    }
}