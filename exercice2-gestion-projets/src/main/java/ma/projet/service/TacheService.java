package ma.projet.service;

import ma.projet.classes.Tache;
import ma.projet.util.Dates;

import java.time.LocalDate;
import java.util.List;

public class TacheService extends AbstractFacade<Tache> {

    public TacheService() {
        super(Tache.class);
    }

    /** Requête nommée : tâches dont le prix est supérieur à un seuil. */
    public List<Tache> findByPrixSuperieur(double prix) {
        return read(s -> s.createNamedQuery("Tache.findByPrixSup", Tache.class)
                .setParameter("prix", prix)
                .list());
    }

    public void afficherPrixSuperieur(double prix) {
        System.out.printf("Tâches dont le prix est supérieur à %.0f DH :%n", prix);
        for (Tache t : findByPrixSuperieur(prix)) {
            System.out.printf("%-5d %-16s %.0f DH%n", t.getId(), t.getNom(), t.getPrix());
        }
    }

    /** Tâches réalisées (dates réelles) entre deux dates. */
    public List<Tache> findRealiseesEntre(LocalDate d1, LocalDate d2) {
        return read(s -> s.createQuery(
                "select distinct et.tache from EmployeTache et "
                        + "where et.dateDebutReelle >= :d1 and et.dateFinReelle <= :d2 order by et.tache.id",
                Tache.class)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .list());
    }

    public void afficherRealiseesEntre(LocalDate d1, LocalDate d2) {
        System.out.println("Tâches réalisées entre le " + Dates.courte(d1) + " et le " + Dates.courte(d2) + " :");
        for (Tache t : findRealiseesEntre(d1, d2)) {
            System.out.printf("%-5d %s%n", t.getId(), t.getNom());
        }
    }
}
