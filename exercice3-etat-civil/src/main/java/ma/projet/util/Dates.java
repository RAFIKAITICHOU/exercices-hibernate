package ma.projet.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Dates {
    private static final DateTimeFormatter LONG = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
    private static final DateTimeFormatter SHORT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Dates() {}

    /** 14 mars 2013 */
    public static String longue(LocalDate d) { return d == null ? "-" : LONG.format(d); }

    /** 14/03/2013 */
    public static String courte(LocalDate d) { return d == null ? "-" : SHORT.format(d); }
}
