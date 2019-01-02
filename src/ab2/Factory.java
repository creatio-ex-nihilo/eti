package ab2;

import java.util.Set;

public interface Factory {
    /**
     * Pfeil-Trennzeichen für CFG
     */
    String CFG_DELIMITER1 = "→";

    /**
     * Oder-Trennzeichen für CFG
     */
    String CFG_DELIMITER2 = "|";

    /**
     * Erzeugt einen leeren Mealy-Automaten
     *
     * @return
     */
    Mealy getEmptyMealy();

    /**
     * Erzeugt einen leeren PDA
     */
    PDA getEmptyPDA();

    /**
     * Erzeugt einen PDA ausgehend von der gegebenen Grammatik. Es sind nur Groß-
     * und Kleinbuchstaben erlaubt. Großbuchstaben stellen Nichtterminale und
     * Kleinbuchstaben Terminale dar. Die Regel sind der Form "S→aBcD|bde|ABA|SaS".
     * Dh Auf das Nichtterminal links folgt ein "→" sowie weitere Termine und
     * Nichtterminale. Das leere Wort Epsilon wird als "" (dh leerer String)
     * dargestellt. Eine entsprechende Regel wäre "S→" oder "S→A|"
     */
    PDA getPDAFromCFG(char startSymbol, Set<String> rules);
}
