package net.dolpen.gae.libs.jpa;

import javax.persistence.*;

/**
 * JPA
 */
public class JPA {
    private static final String UNIT_NAME = "GAEDatastorePersistenceUnit";

    static ThreadLocal<EntityManager> currentEntityManager = new ThreadLocal<>();

    private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory(UNIT_NAME);

    private JPA() {
    }

    private static EntityManagerFactory get() {
        return emfInstance;
    }


    public static void bindForCurrentThread() {
        EntityManager em = currentEntityManager.get();
        if (em == null) {
            if (emfInstance == null) {
                throw new RuntimeException("No JPA EntityManagerFactory configured for name [" + UNIT_NAME + "]");
            }
            em = emfInstance.createEntityManager();
            currentEntityManager.set(em);
        }
    }

    public static void unbindFromCurrentThread() {
        EntityManager em = currentEntityManager.get();
        if (em == null) return;
        closeQuietly();
        currentEntityManager.remove();
    }

    public static void closeQuietly() {
        EntityManager em = currentEntityManager.get();
        if (em == null) return;
        if (em.isOpen()) em.close();
    }


    public static EntityManager em() {
        EntityManager em = currentEntityManager.get();
        if (em == null) {
            throw new RuntimeException("No JPA EntityManager bind for this thread.");
        }
        return em;
    }

}
