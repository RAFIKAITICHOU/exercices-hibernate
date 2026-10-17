package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.util.Dates;

import java.util.List;

public class EmployeService extends AbstractFacade<Employe> {

    public EmployeService() {
        super(Employe.class);
    }

    /** Tâches réalisées par un employé (avec dates réelles). */
    public List<EmployeTache> findTachesRealisees(int employeId) {
        return read(s -> s.createQuery(
                "select et from EmployeTache et join fetch et.tache "
                        + "where et.employe.id = :id order by et.dateDebutReelle", EmployeTache.class)
                .setParameter("id", employeId)
                .list());
    }

    public void afficherTachesRealisees(int employeId) {
        Employe e = findById(employeId);
        System.out.println("Tâches réalisées par " + (e == null ? employeId : e.getNom() + " " + e.getPrenom()) + " :");
        System.out.printf("%-5s %-16s %-18s %s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        for (EmployeTache et : findTachesRealisees(employeId)) {
            System.out.printf("%-5d %-16s %-18s %s%n", et.getTache().getId(), et.getTache().getNom(),
                    Dates.courte(et.getDateDebutReelle()), Dates.courte(et.getDateFinReelle()));
        }
    }

    /** Projets dont l'employé est chef de projet. */
    public List<Projet> findProjetsGeres(int employeId) {
        return read(s -> s.createQuery(
                "from Projet p where p.chefProjet.id = :id order by p.dateDebut", Projet.class)
                .setParameter("id", employeId)
                .list());
    }

    public void afficherProjetsGeres(int employeId) {
        Employe e = findById(employeId);
        System.out.println("Projets gérés par " + (e == null ? employeId : e.getNom() + " " + e.getPrenom()) + " :");
        for (Projet p : findProjetsGeres(employeId)) {
            System.out.printf("%-5d %-20s début : %s%n", p.getId(), p.getNom(), Dates.longue(p.getDateDebut()));
        }
    }
}
