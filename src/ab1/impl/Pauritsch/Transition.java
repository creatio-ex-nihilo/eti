package ab1.impl.Pauritsch;

import ab1.TM;

import java.util.Objects;

public class Transition {
    private int fromState;
    private int toState;
    private char symbolRead;
    private char symbolWrite;
    private int tapeRead;
    private int tapeWrite;
    private TM.Movement tapeReadMovement;
    private TM.Movement tapeWriteMovement;

    Transition(int fromState, int toState, char symbolRead, char symbolWrite, int tapeRead, int tapeWrite, TM.Movement tapeReadMovement, TM.Movement tapeWriteMovement) {
        this.fromState = fromState;
        this.toState = toState;
        this.symbolRead = symbolRead;
        this.symbolWrite = symbolWrite;
        this.tapeRead = tapeRead;
        this.tapeWrite = tapeWrite;
        this.tapeReadMovement = tapeReadMovement;
        this.tapeWriteMovement = tapeWriteMovement;
    }

    int getFromState() {
        return fromState;
    }

    int getToState() {
        return toState;
    }

    char getSymbolRead() {
        return symbolRead;
    }

    char getSymbolWrite() {
        return symbolWrite;
    }

    int getTapeRead() {
        return tapeRead;
    }

    int getTapeWrite() {
        return tapeWrite;
    }

    TM.Movement getTapeReadMovement() {
        return tapeReadMovement;
    }

    TM.Movement getTapeWriteMovement() {
        return tapeWriteMovement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return fromState == that.fromState &&
                symbolRead == that.symbolRead;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromState, symbolRead);
    }
}
