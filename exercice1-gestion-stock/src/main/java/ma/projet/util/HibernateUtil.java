package ma.projet.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Construit la SessionFactory à partir de application.properties.
 * Une propriété système de même nom (-Dhibernate.connection.url=...) a priorité :
 * c'est ce qui permet aux tests JUnit d'utiliser H2.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Properties props = new Properties();
            try (InputStream in = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (in == null) {
                    throw new IllegalStateException("application.properties introuvable dans le classpath");
                }
                props.load(in);
            } catch (IOException e) {
                throw new ExceptionInInitializerError(e);
            }
            for (String key : props.stringPropertyNames()) {
                String override = System.getProperty(key);
                if (override != null) {
                    props.setProperty(key, override);
                }
            }
            Configuration cfg = new Configuration().setProperties(props);
            cfg.addAnnotatedClass(ma.projet.classes.Categorie.class);
            cfg.addAnnotatedClass(ma.projet.classes.Produit.class);
            cfg.addAnnotatedClass(ma.projet.classes.Commande.class);
            cfg.addAnnotatedClass(ma.projet.classes.LigneCommandeProduit.class);
            sessionFactory = cfg.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        sessionFactory = null;
    }
}
