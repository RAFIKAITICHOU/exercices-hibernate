package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "femme")
@NamedQuery(name = "Femme.mariesAuMoinsDeuxFois",
        query = "from Femme f where (select count(m) from Mariage m where m.femme = f) >= 2")
@NamedNativeQuery(name = "Femme.nbEnfantsEntreDates",
        query = "SELECT COALESCE(SUM(m.nbr_enfant), 0) FROM mariage m "
                + "WHERE m.femme_id = :femmeId AND m.date_debut <= :d2 "
                + "AND (m.date_fin IS NULL OR m.date_fin >= :d1)")
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme")
    private List<Mariage> mariages = new ArrayList<>();

    public Femme() {}

    public Femme(String nom, String prenom, String telephone, String adresse, LocalDate dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() { return mariages; }
    public void setMariages(List<Mariage> mariages) { this.mariages = mariages; }
}
