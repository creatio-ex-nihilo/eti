package ab1;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Interface zur Implementierung einer Turingmaschine (Mehrband, einseitig beschränkt)
 *
 * @author Raphael Wigoutschnigg
 */
public interface TM {
    /**
     * Setzt die Turingmaschine zurueck und loescht alle internen Daten. Dies
     * umfasst Ueberfuehrungen, Zustaende und Bandinhalte.
     *
     * @return liefert das Objekt selbst (return this)
     */
    TM reset();

    /**
     * Liefert den aktuellen Zustand der Maschine
     *
     * @return aktueller Zustand der Maschine
     */
    int getActState();

    /**
     * Gibt die Anzahl an zu verwendenen Schreib/Lese-Bändern an.
     *
     * @param numTapes Anzahl an Schreib/Lese-Bändern
     * @return liefert das Objekt selbst (return this)
     * @throws IllegalArgumentException wenn numTapes < 1 ist
     */
    TM setNumberOfTapes(int numTapes) throws IllegalArgumentException;

    /**
     * Gibt die Menge der von der Turingmaschine verarbeiteten Symbole an. Es ist
     * sicherzustellen, dass das Zeichen '#' auf jeden Fall in der Menge enthalten
     * ist.
     *
     * @param symbols Menge der Symbole
     * @return liefert das Objekt selbst (return this)
     * @throws IllegalArgumentException wenn die Menge nicht das Leerzeichen '#' enthält.
     */
    TM setSymbols(Set<Character> symbols) throws IllegalArgumentException;

    /**
     * Liefert die Menge der verarbeiteten Symbole.
     *
     * @return Menge der Symbole
     */
    Set<Character> getSymbols();

    /**
     * Fuegt eine Ueberfuehrung hinzu. fromState und toState sind ganze Zahlen (0,
     * 1, ...). 0 steht hierbei fuer den Haltezustand. Wird eine Zustandsnummer
     * verwendet, die bisher nicht verwendet wurde, ist die Zustandsmenge intern
     * entsprechend zu erweitern. Die optionale Bewegung wird nach der
     * Schreiboperation ausgeführt. tape=0 steht für das erste Schreib/Lese-Band.
     *
     * @param tapeRead          Band, von dem gelesen wird
     * @param tapeWrite         Band, auf das geschrieben wird
     * @param tapeMove          Band, auf dem der Schreib/Lesekopf bewergt wird
     * @param fromState         Ausgangszustand
     * @param symbolRead        gelesenes Symbol
     * @param toState           Folgezustand
     * @param symbolWrite       geschriebenes Symbol, wenn null keines
     * @param tapeReadMovement  Bewegung des Schreib-/Lesekopfes des Lesebandes
     * @param tapeWriteMovement Bewegung des Schreib-/Lesekopfes des Schreibbandes
     * @return liefert das Objekt selbst (return this)
     * @throws IllegalArgumentException Wird geworfen, wenn ausgehend vom Haltezustand ein Zeichen
     *                                  gelesen wird, wenn eine Transition nicht eindeutig ist
     *                                  (fromState, symbolRead), wenn ein Symbol nicht verarbeitet werden
     *                                  kann, wenn das Band nicht existiert oder ein Zustand nicht existiert
     */
    TM addTransition(int fromState, int tapeRead, char symbolRead, int toState, int tapeWrite,
                     char symbolWrite, Movement tapeReadMovement, Movement tapeWriteMovement) throws IllegalArgumentException;

    /**
     * Liefert die Anzahl der Zustaende.
     */
    int getNumberOfStates();

    /**
     * Liefert die Anzahl an Bändern zurück.
     *
     * @return Anzahl der Bänder
     */
    int getNumberOfTapes();

    /**
     * Setzt die Anzahl der Zustände der Maschine
     *
     * @param numStates Anzahl der Zustände (Haltezustand enthalten)
     * @throws IllegalArgumentException falls numStates < 1
     */
    TM setNumberOfStates(int numStates) throws IllegalArgumentException;

    /**
     * Setzt die Nummer des Haltezustandes
     *
     * @param state Nummer des Haltezustandes
     * @return liefert das Objekt selbst (return this)
     * @throws IllegalArgumentException falls state nicht möglich ist
     */
    TM setHaltState(int state) throws IllegalArgumentException;

    /**
     * Setzt den initialen Zustand der Maschine.
     *
     * @param state Startzutand
     * @return liefert das Objekt selbst (return this)
     * @throws IllegalArgumentException falls state nicht möglich ist
     */
    TM setInitialState(int state) throws IllegalArgumentException;

    /**
     * Setzt den initialen Inhalt des definierten Bandes und setzt den
     * Schreib-/Lesekopf hinter das letzte Zeichen des Inhaltes. "#abc" liefert
     * somit den Inhalt "#abc#...." wobei der Schreib-/Lesekopf unter dem zweiten #
     * steht.
     *
     * @param tape
     * @param content
     * @return liefert das Objekt selbst (return this)
     */
    TM setInitialTapeContent(int tape, char[] content);

    /**
     * Fuehrt einen Abarbeitungsschritt der Turingmaschine aus.
     *
     * @return liefert das Objekt selbst (return this)
     * @throws IllegalStateException Wird geworfen, wenn die Maschine bereits im Haltezustand ist,
     *                               keine passende Transition vorhanden ist oder die Maschine links
     *                               in die "Wand" laeuft.
     */
    TM doNextStep() throws IllegalStateException;

    /**
     * Liefert true, wenn sich die Maschine im Haltezustand befindet.
     *
     * @return
     */
    boolean isHalt();

    /**
     * Liefert true, wenn die Maschine haengt (Bandende ueberschritten oder
     * unbekannter Zustand).
     */
    boolean isCrashed();

    /**
     * Liefert die Konfiguration der Maschine für jedes Band. Ist isCrashed() ==
     * true, wird null zurück geliefert.
     *
     * @return Konfiguration der Maschine.
     */
    List<TMConfig> getTMConfig();

    /**
     * Liefert die Konfiguration der Maschine für das angegebene Band. Ist
     * isCrashed() == true, wird null zurück geliefert.
     *
     * @param tape
     * @return Konfiguration des angegebenen Bandes
     */
    TMConfig getTMConfig(int tape);

    /**
     * @author Raphael Wigoutschnigg Bewegerichtungen des Schreib/Lese-Kopfes
     */
    enum Movement {
        /**
         * Kopf bewegt sich nach links
         */
        Left,

        /**
         * Kopf bewegt sich nach rechts
         **/
        Right,

        /**
         * Kopf bewegt sich nicht
         */
        Stay
    }

    /**
     * @author Raphael Wigoutschnigg
     */
    class TMConfig {
        /**
         * Alle Zeichen auf dem Band vom Beginn des Bandes bis zur Stelle links vom Kopf
         * (darf leer sein).
         */
        private char[] leftOfHead;
        /**
         * Das Zeichen, das sich aktuell unter dem Kopf befindet.
         */
        private char belowHead;
        /**
         * Alle Zeichen von der Stelle rechts vom Kopf bis zum letzten von '#'
         * verschiedenen Zeichen (darf leer sein).
         */
        private char[] rightOfHead;

        public char[] getLeftOfHead() {
            return leftOfHead;
        }

        public char getBelowHead() {
            return belowHead;
        }

        public char[] getRightOfHead() {
            return rightOfHead;
        }

        public TMConfig(char[] leftOfHead, char belowHead, char[] rightOfHead) {
            super();
            this.leftOfHead = leftOfHead;
            this.belowHead = belowHead;
            this.rightOfHead = rightOfHead;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TMConfig) {
                TMConfig tmc = (TMConfig) obj;
                if (belowHead != tmc.belowHead) {
                    return false;
                }

                if (!Arrays.equals(leftOfHead, tmc.leftOfHead)) {
                    return false;
                }

                return Arrays.equals(rightOfHead, tmc.rightOfHead);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return printArray(leftOfHead) + "'" + belowHead + "'" + printArray(rightOfHead);
        }

        private String printArray(char[] arr) {
            String s = "";
            for (char c : arr) {
                s += c;
            }
            return s;
        }
    }
}
