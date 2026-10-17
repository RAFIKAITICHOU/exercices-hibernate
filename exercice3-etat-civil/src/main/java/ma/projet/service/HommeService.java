package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.util.Dates;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

public class HommeService extends AbstractFacade<Homme> {

    public HommeService() {
        super(Homme.class);
    }

    /**
     * Épouses d'un homme sur une période : tous les mariages qui chevauchent [d1, d2]
     * (commencés avant d2 et non terminés avant d1).
     */
    public List<Femme> findEpousesEntre(int hommeId, LocalDate d1, LocalDate d2) {
        return read(s -> s.createQuery(
                "select distinct m.femme from Mariage m where m.homme.id = :id "
                        + "and m.dateDebut <= :d2 and (m.dateFin is null or m.dateFin >= :d1)", Femme.class)
                .setParameter("id", hommeId)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .list());
    }

    public void afficherEpousesEntre(int hommeId, LocalDate d1, LocalDate d2) {
        Homme h = findById(hommeId);
        System.out.println("Épouses de " + (h == null ? hommeId : h.nomComplet())
                + " entre le " + Dates.courte(d1) + " et le " + Dates.courte(d2) + " :");
        for (Femme f : findEpousesEntre(hommeId, d1, d2)) {
            System.out.println(" - " + f.nomComplet());
        }
    }

    /** API Criteria : nombre d'hommes ayant exactement quatre épouses (distinctes) sur la période. */
    public int compterHommesMariesAQuatreFemmes(LocalDate d1, LocalDate d2) {
        return read(s -> {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Mariage> m = cq.from(Mariage.class);

            cq.select(m.<Homme>get("homme").<Integer>get("id"))
              .where(cb.lessThanOrEqualTo(m.<LocalDate>get("dateDebut"), d2),
                     cb.or(cb.isNull(m.<LocalDate>get("dateFin")),
                           cb.greaterThanOrEqualTo(m.<LocalDate>get("dateFin"), d1)))
              .groupBy(m.<Homme>get("homme").<Integer>get("id"))
              .having(cb.equal(cb.countDistinct(m.<Femme>get("femme").<Integer>get("id")), 4L));

            return s.createQuery(cq).getResultList().size();
        });
    }

    /** Mariages d'un homme avec tous les détails, au format de l'énoncé. */
    public void afficherMariages(int hommeId) {
        Homme h = findById(hommeId);
        if (h == null) {
            System.out.println("Homme " + hommeId + " introuvable.");
            return;
        }
        List<Mariage> mariages = read(s -> s.createQuery(
                "select m from Mariage m join fetch m.femme where m.homme.id = :id order by m.dateDebut",
                Mariage.class)
                .setParameter("id", hommeId)
                .list());

        System.out.println("Nom : " + h.nomComplet());
        System.out.println("Mariages En Cours :");
        int i = 1;
        for (Mariage m : mariages) {
            if (m.getDateFin() == null) {
                System.out.printf("%d. Femme : %s   Date Début : %s   Nbr Enfants : %d%n",
                        i++, m.getFemme().nomComplet(), Dates.courte(m.getDateDebut()), m.getNbrEnfant());
            }
        }
        System.out.println("Mariages échoués :");
        i = 1;
        for (Mariage m : mariages) {
            if (m.getDateFin() != null) {
                System.out.printf("%d. Femme : %s   Date Début : %s%n    Date Fin : %s   Nbr Enfants : %d%n",
                        i++, m.getFemme().nomComplet(), Dates.courte(m.getDateDebut()),
                        Dates.courte(m.getDateFin()), m.getNbrEnfant());
            }
        }
    }
}
