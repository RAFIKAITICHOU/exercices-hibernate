package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.util.Dates;

import java.util.List;

public class ProjetService extends AbstractFacade<Projet> {

    public ProjetService() {
        super(Projet.class);
    }

    /** Tâches planifiées d'un projet. */
    public List<Tache> findTachesPlanifiees(int projetId) {
        return read(s -> s.createQuery(
                "from Tache t where t.projet.id = :id order by t.dateDebut", Tache.class)
                .setParameter("id", projetId)
                .list());
    }

    public void afficherTachesPlanifiees(int projetId) {
        System.out.println("Tâches planifiées du projet " + projetId + " :");
        System.out.printf("%-5s %-16s %-12s %-12s %s%n", "Num", "Nom", "Date Début", "Date Fin", "Prix");
        for (Tache t : findTachesPlanifiees(projetId)) {
            System.out.printf("%-5d %-16s %-12s %-12s %.0f DH%n", t.getId(), t.getNom(),
                    Dates.courte(t.getDateDebut()), Dates.courte(t.getDateFin()), t.getPrix());
        }
    }

    /** Tâches réalisées d'un projet, avec les dates réelles. */
    public List<EmployeTache> findTachesRealisees(int projetId) {
        return read(s -> s.createQuery(
                "select et from EmployeTache et join fetch et.tache t "
                        + "where t.projet.id = :id order by et.dateDebutReelle", EmployeTache.class)
                .setParameter("id", projetId)
                .list());
    }

    /** Affichage au format de l'énoncé. */
    public void afficherTachesRealisees(int projetId) {
        Projet p = findById(projetId);
        if (p == null) {
            System.out.println("Projet " + projetId + " introuvable.");
            return;
        }
        System.out.println("Projet : " + p.getId() + "     Nom : " + p.getNom()
                + "     Date début : " + Dates.longue(p.getDateDebut()));
        System.out.println("Liste des tâches:");
        System.out.printf("%-5s %-16s %-18s %s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        for (EmployeTache et : findTachesRealisees(projetId)) {
            System.out.printf("%-5d %-16s %-18s %s%n", et.getTache().getId(), et.getTache().getNom(),
                    Dates.courte(et.getDateDebutReelle()), Dates.courte(et.getDateFinReelle()));
        }
    }
}
