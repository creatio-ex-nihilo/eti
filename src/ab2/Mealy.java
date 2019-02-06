package ab2;

import java.util.Set;

public interface Mealy {
    /**
     * Setzt den Startzustand des Automaten
     *
     * @throws IllegalArgumentException
     *                                      falls numStates < 0 oder >= numStates
     * @throws IllegalStateException
     *                                      falls numStates noch nicht gesetzt wurde
     */
    void setInitialState(int initialState) throws IllegalArgumentException, IllegalStateException;

    /**
     * Fügt eine Transiation hinzu
     *
     * @throws IllegalArgumentException falls ein Zustand nicht valide ist, die
     *                                  Zeichen nicht erlaubt sind oder die
     *                                  Transition einen Nichtdeterminismus
     *                                  erzeugen würde
     * @throws IllegalStateException    falls numStates, readChars oder
     *                                  writeChars nicht gesetzt wurden
     */
    void addTransition(int fromState, char charRead, Character charWrite, int toState)
            throws IllegalArgumentException, IllegalStateException;

    /**
     * Setzt die Menge an erlaubten Eingabezeichen
     */
    void setReadChars(Set<Character> chars);

    /**
     * Setzt die Menge an erlaubten Ausgabezeichen
     */
    void setWriteChars(Set<Character> chars);

    /**
     * Wandelt den Mealy-Autoaten in einen Moore Automaten um. Die leere Ausgabe
     * (null) wird bezüglich des State splittings wie ein Zeichen behandelt. Der
     * bestehende Automat wird nicht verändert.
     *
     * @throws IllegalStateException
     *                                   falls numStates, readChars oder writeChars
     *                                   nicht gesetzt wurden
     */
    Mealy toMoore();

    /**
     * Erzeugt die Ausgabe, die durch die Verarbeitung der Eingabe entsteht. Kann
     * die Eingabe nicht verarbeitet werden, soll null zurück gegeben werden
     *
     * @throws IllegalStateException
     *                                   falls numStates, readChars oder writeChars
     *                                   nicht gesetzt wurden
     */
    String produced(String input) throws IllegalStateException;

    /**
     * Minimalisiert den Automaten. Der Automat wird nicht verändert.
     *
     * @throws IllegalStateException
     *                                   falls numStates, readChars oder writeChars
     *                                   nicht gesetzt wurden
     */
    Mealy minimize();

    /**
     * Liefert die Anzahl der Zustände
     *
     * @throws IllegalStateException falls numStates noch nicht gesetzt wurde
     */
    int getNumStates() throws IllegalStateException;

    /**
     * Setzt die Anzahl an Zuständen des Automaten
     *
     * @throws IllegalArgumentException wenn numStates <= 0
     */
    void setNumStates(int numStates) throws IllegalArgumentException;

    /**
     * Liefert die Transitionen des Automaten
     *
     * @return
     */
    Set<Transition> getTransitions();
}