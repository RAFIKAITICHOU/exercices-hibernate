package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commande")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_commande")
    private LocalDate dateCommande;

    @OneToMany(mappedBy = "commande")
    private List<LigneCommandeProduit> lignes = new ArrayList<>();

    public Commande() {}
    public Commande(LocalDate dateCommande) { this.dateCommande = dateCommande; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDate dateCommande) { this.dateCommande = dateCommande; }
    public List<LigneCommandeProduit> getLignes() { return lignes; }

    @Override
    public String toString() { return "Commande{id=" + id + ", date=" + dateCommande + "}"; }
}
