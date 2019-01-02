package ab2;

import java.util.Set;

public interface Mealy {
    /**
     * Setzt die Anzahl an Zuständen des Automaten
     *
     * @throws IllegalArgumentException wenn numStates <= 0
     */
    void setNumStates(int numStates);

    /**
     * Setzt den Startzustand des Automaten
     *
     * @throws IllegalArgumentException falls numStates < 0 oder >= numStates
     * @throws IllegalStateException    falls numStates noch nicht gesetzt wurde
     */
    void setInitialState(int initialState);

    /**
     * Setzt die Menge an erlaubten Eingabezeichen
     */
    void setReadChars(Set<Character> chars);

    /**
     * Setzt die Menge an erlaubten Ausgabezeichen
     */
    void setWriteChars(Set<Character> chars);

    /**
     * Fügt eine Transiation hinzu
     *
     * @throws IllegalArgumentException falls ein Zustand nicht valide ist, die Zeichen nicht erlaubt
     *                                  sind oder die Transition einen Nichtdeterminismus erzeugen würde
     * @throws IllegalStateException    falls numStates, readChars oder writeChars nicht gesetzt wurden
     */
    void addTransition(int fromState, char charRead, Character charWrite, int toState) throws IllegalStateException;

    /**
     * Wandelt den Mealy-Autoaten in einen Moore Automaten um. Der bestehende Automat wird nicht verändert.
     *
     * @throws IllegalStateException falls numStates, readChars oder writeChars nicht gesetzt wurden
     */
    Mealy toMoore();

    /**
     * Erzeugt die Ausgabe, die durch die Verarbeitung der Eingabe entsteht.
     *
     * @throws IllegalStateException falls numStates, readChars oder writeChars nicht gesetzt wurden
     */
    String produced(String input);

    /**
     * Minimalisiert den Automaten. Der Automat wird nicht verändert.
     *
     * @throws IllegalStateException falls numStates, readChars oder writeChars nicht gesetzt wurden
     */
    Mealy minimize();
}