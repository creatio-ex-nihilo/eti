package ab2;

import java.util.Set;

public interface PDA {
    /**
     * Setzt die Anzahl an Zuständen des Automaten
     *
     * @throws IllegalArgumentException wenn numStates <= 0
     */
    void setNumStates(int numStates) throws IllegalArgumentException;

    /**
     * Setzt den Startzustand des Automaten
     *
     * @throws IllegalArgumentException falls numStates < 0 oder >= numStates
     * @throws IllegalStateException falls numStates noch nicht gesetzt wurde
     */
    void setInitialState(int initialState) throws IllegalArgumentException, IllegalStateException;

    /**
     * Setzt die akzeptierenden Zustände
     *
     * @throws IllegalArgumentException falls ein Zustand < 0 oder >= numStates ist
     * @throws IllegalStateException falls numStates noch nicht gesetzt wurde
     */
    void setAcceptingState(Set<Integer> acceptingStates) throws IllegalArgumentException, IllegalStateException;

    /**
     * Setzt die Menge an erlaubten Zeichen der Eingabe
     */
    void setInputChars(Set<Character> chars);

    /**
     * Setzt die Menge an erlaubten Zeichen des Stacks
     */
    void setStackChars(Set<Character> chars);

    /**
     * Fügt eine Transiation hinzu
     *
     * @throws IllegalArgumentException falls ein Zustand nicht valide ist, die Zeichen nicht erlaubt sind
     * @throws IllegalStateException falls numStates, inputChars oder stackChars nicht gesetzt wurden
     */
    void addTransition(int fromState, Character charReadTape, Character charReadStack, Character charWriteStack,
                       int toState) throws IllegalArgumentException, IllegalStateException;

    /**
     * Prüft, ob eine Eingbe von dem PDA akzeptiert wird (dh Teil der Sprache des
     * PDA ist)
     *
     * @throws IllegalArgumentException der Input aus nicht erlaubten Zeichen besteht
     * @throws IllegalStateException falls numStates, inputChars oder stackChars nicht gesetzt wurden
     */
    boolean accepts(String input) throws IllegalArgumentException, IllegalStateException;

    /**
     * Erzeugt einen neuen PDA, indem an den PDA (this) der überegebene PDA
     * angehängt wird, sodass die aktzeptierte Sprache des zurückgegebenen PDAs der
     * Konkatenation der Sprachen der beiden PDAs entspricht. Keiner der beiden PDAs
     * darf verändert werden. es muss ein neuer PDA erzeugt werden.
     *
     * @throws IllegalArgumentException falls numStates, inputChars oder stackChars des übergebenen PDA nicht gesetzt wurden
     * @throws IllegalStateException falls numStates, inputChars oder stackChars nicht gesetzt wurden
     */
    PDA append(PDA pda) throws IllegalArgumentException, IllegalStateException;

    /**
     * Erzeugt einen neuen PDA, indem der PDA (this) und der überegebene PDA
     * vereinigt werden. Die Sprache des zurückgegebenen PDAs entspricht der
     * Vereinigung der Sprachen der beiden PDAs. Keiner der beiden PDAs darf
     * verändert werden. es muss ein neuer PDA erzeugt werden.
     *
     * @throws IllegalArgumentException falls numStates, inputChars oder stackChars des übergebenen PDA nicht gesetzt wurden
     * @throws IllegalStateException falls numStates, inputChars oder stackChars nicht gesetzt wurden
     */
    PDA union(PDA pda) throws IllegalArgumentException, IllegalStateException;

    /**
     * Gibt an, ob der PDA ein DPDA ist (es wird nicht überprüft ob L(PDA) Element von DCFL ist)
     *
     * @throws IllegalStateException falls numStates, inputChars oder stackChars von zumindest einem PDA nicht gesetzt wurden
     */
    boolean isDPDA() throws IllegalStateException;
}