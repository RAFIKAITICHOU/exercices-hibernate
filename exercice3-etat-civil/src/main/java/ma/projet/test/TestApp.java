package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;

public class TestApp {

    public static Homme[] hommes = new Homme[5];
    public static Femme[] femmes = new Femme[10];

    private static LocalDate d(int y, int m, int j) { return LocalDate.of(y, m, j); }

    /** Crée 10 femmes, 5 hommes et leurs mariages. */
    public static void seed() {
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();
        MariageService ms = new MariageService();

        String[][] f = {
            {"RAMI", "Salima", "1970-05-12"}, {"ALI", "Amal", "1975-03-08"},
            {"ALAOUI", "Wafa", "1978-11-21"}, {"ALAMI", "Karima", "1968-01-30"},
            {"CHRAIBI", "Nadia", "1965-07-14"}, {"IDRISSI", "Hajar", "1982-09-02"},
            {"TAZI", "Leila", "1985-12-25"}, {"FASSI", "Meryem", "1990-04-17"},
            {"BERRADA", "Sanae", "1972-06-06"}, {"LAHLOU", "Zineb", "1988-10-10"}
        };
        for (int i = 0; i < f.length; i++) {
            femmes[i] = new Femme(f[i][0], f[i][1], "06000000" + i, "Casablanca", LocalDate.parse(f[i][2]));
            fs.create(femmes[i]);
        }

        String[][] h = {
            {"SAFI", "Said", "1960-02-02"}, {"BENJELLOUN", "Karim", "1962-08-19"},
            {"OUAZZANI", "Youssef", "1958-03-27"}, {"KETTANI", "Rachid", "1975-12-01"},
            {"MOUTAWAKIL", "Adil", "1980-05-05"}
        };
        for (int i = 0; i < h.length; i++) {
            hommes[i] = new Homme(h[i][0], h[i][1], "07000000" + i, "Rabat", LocalDate.parse(h[i][2]));
            hs.create(hommes[i]);
        }

        // SAFI SAID : 1 mariage échoué + 3 en cours (comme l'exemple de l'énoncé)
        ms.create(new Mariage(hommes[0], femmes[3], d(1989, 9, 3), d(1990, 9, 3), 0));
        ms.create(new Mariage(hommes[0], femmes[0], d(1990, 9, 3), null, 4));
        ms.create(new Mariage(hommes[0], femmes[1], d(1995, 9, 3), null, 2));
        ms.create(new Mariage(hommes[0], femmes[2], d(2000, 11, 4), null, 3));

        // CHRAIBI Nadia est mariée deux fois
        ms.create(new Mariage(hommes[1], femmes[4], d(1985, 6, 1), d(1992, 3, 10), 2));
        ms.create(new Mariage(hommes[2], femmes[4], d(1995, 1, 15), null, 1));

        ms.create(new Mariage(hommes[3], femmes[5], d(2005, 7, 20), null, 2));
        ms.create(new Mariage(hommes[4], femmes[6], d(2010, 2, 14), null, 1));
    }

    public static void main(String[] args) {
        seed();
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();

        System.out.println("===== Liste des femmes =====");
        fs.afficherToutes();

        System.out.println("\n===== Femme la plus âgée =====");
        System.out.println(fs.findPlusAgee().nomComplet());

        System.out.println("\n===== Épouses d'un homme entre deux dates =====");
        hs.afficherEpousesEntre(hommes[0].getId(), d(1990, 1, 1), d(1996, 1, 1));

        System.out.println("\n===== Nombre d'enfants d'une femme entre deux dates (requête native nommée) =====");
        System.out.println(femmes[0].nomComplet() + " : "
                + fs.nombreEnfantsEntre(femmes[0].getId(), d(1990, 1, 1), d(2000, 1, 1)));

        System.out.println("\n===== Femmes mariées deux fois ou plus (requête nommée) =====");
        for (Femme f : fs.findMarieesAuMoinsDeuxFois()) {
            System.out.println(" - " + f.nomComplet());
        }

        System.out.println("\n===== Hommes mariés à quatre femmes entre deux dates (Criteria) =====");
        System.out.println(hs.compterHommesMariesAQuatreFemmes(d(1980, 1, 1), d(2010, 1, 1)));

        System.out.println("\n===== Mariages d'un homme =====");
        hs.afficherMariages(hommes[0].getId());

        HibernateUtil.shutdown();
    }
}
