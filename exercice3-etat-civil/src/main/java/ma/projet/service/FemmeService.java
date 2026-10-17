package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.util.Dates;

import java.time.LocalDate;
import java.util.List;

public class FemmeService extends AbstractFacade<Femme> {

    public FemmeService() {
        super(Femme.class);
    }

    public void afficherToutes() {
        System.out.println("Liste des femmes :");
        for (Femme f : findAll()) {
            System.out.printf(" %-3d %-22s née le %s%n", f.getId(), f.nomComplet(), Dates.courte(f.getDateNaissance()));
        }
    }

    /** La femme la plus âgée (date de naissance la plus ancienne). */
    public Femme findPlusAgee() {
        return read(s -> s.createQuery("from Femme f order by f.dateNaissance asc", Femme.class)
                .setMaxResults(1)
                .uniqueResult());
    }

    /** Requête native nommée : nombre d'enfants sur les mariages qui chevauchent [d1, d2]. */
    public int nombreEnfantsEntre(int femmeId, LocalDate d1, LocalDate d2) {
        return read(s -> {
            Object r = s.createNamedQuery("Femme.nbEnfantsEntreDates")
                    .setParameter("femmeId", femmeId)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .uniqueResult();
            return r == null ? 0 : ((Number) r).intValue();
        });
    }

    /** Requête nommée : femmes mariées au moins deux fois. */
    public List<Femme> findMarieesAuMoinsDeuxFois() {
        return read(s -> s.createNamedQuery("Femme.mariesAuMoinsDeuxFois", Femme.class).list());
    }
}
