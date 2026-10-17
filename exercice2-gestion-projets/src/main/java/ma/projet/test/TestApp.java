package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;

public class TestApp {

    public static Employe emp1, emp2, emp3;
    public static Projet projet1, projet2, projet3;
    public static Tache analyse, conception, developpement, recette;

    public static void seed() {
        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService etService = new EmployeTacheService();

        emp1 = new Employe("ALAMI", "Karim", "0600000001");
        emp2 = new Employe("BENNANI", "Sara", "0600000002");
        emp3 = new Employe("CHRAIBI", "Omar", "0600000003");
        employeService.create(emp1);
        employeService.create(emp2);
        employeService.create(emp3);

        projet1 = new Projet("Gestion de stock", LocalDate.of(2013, 1, 14), LocalDate.of(2013, 6, 30), emp1);
        projet2 = new Projet("Site web", LocalDate.of(2013, 2, 1), LocalDate.of(2013, 5, 31), emp1);
        projet3 = new Projet("Application mobile", LocalDate.of(2013, 3, 1), LocalDate.of(2013, 9, 30), emp2);
        projetService.create(projet1);
        projetService.create(projet2);
        projetService.create(projet3);

        analyse = new Tache("Analyse", LocalDate.of(2013, 2, 1), LocalDate.of(2013, 2, 15), 1200, projet1);
        conception = new Tache("Conception", LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 15), 800, projet1);
        developpement = new Tache("Développement", LocalDate.of(2013, 4, 1), LocalDate.of(2013, 4, 30), 2500, projet1);
        recette = new Tache("Recette", LocalDate.of(2013, 5, 1), LocalDate.of(2013, 5, 15), 900, projet2);
        tacheService.create(analyse);
        tacheService.create(conception);
        tacheService.create(developpement);
        tacheService.create(recette);

        etService.create(new EmployeTache(emp2, analyse, LocalDate.of(2013, 2, 10), LocalDate.of(2013, 2, 20)));
        etService.create(new EmployeTache(emp2, conception, LocalDate.of(2013, 3, 10), LocalDate.of(2013, 3, 15)));
        etService.create(new EmployeTache(emp3, developpement, LocalDate.of(2013, 4, 10), LocalDate.of(2013, 4, 25)));
        etService.create(new EmployeTache(emp3, recette, LocalDate.of(2013, 5, 2), LocalDate.of(2013, 5, 14)));
    }

    public static void main(String[] args) {
        seed();
        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();

        System.out.println("===== Tâches réalisées par un employé =====");
        employeService.afficherTachesRealisees(emp2.getId());

        System.out.println("\n===== Projets gérés par un employé =====");
        employeService.afficherProjetsGeres(emp1.getId());

        System.out.println("\n===== Tâches planifiées d'un projet =====");
        projetService.afficherTachesPlanifiees(projet1.getId());

        System.out.println("\n===== Tâches réalisées d'un projet (dates réelles) =====");
        projetService.afficherTachesRealisees(projet1.getId());

        System.out.println("\n===== Tâches de prix > 1000 DH (requête nommée) =====");
        tacheService.afficherPrixSuperieur(1000);

        System.out.println("\n===== Tâches réalisées entre deux dates =====");
        tacheService.afficherRealiseesEntre(LocalDate.of(2013, 2, 1), LocalDate.of(2013, 3, 31));

        HibernateUtil.shutdown();
    }
}
