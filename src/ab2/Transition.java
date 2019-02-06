package ab2;

public class Transition {
    private int fromState;
    private int toState;
    private char charRead;
    private Character charWrite;

    public Transition() {

    }

    public Transition(int fromState, int toState, char charRead, Character charWrite) {
        super();
        this.fromState = fromState;
        this.toState = toState;
        this.charRead = charRead;
        this.charWrite = charWrite;
    }

    public int getFromState() {
        return fromState;
    }

    public void setFromState(int fromState) {
        this.fromState = fromState;
    }

    public int getToState() {
        return toState;
    }

    public void setToState(int toState) {
        this.toState = toState;
    }

    public char getCharRead() {
        return charRead;
    }

    public void setCharRead(char charRead) {
        this.charRead = charRead;
    }

    public Character getCharWrite() {
        return charWrite;
    }

    public void setCharWrite(Character charWrite) {
        this.charWrite = charWrite;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + charRead;
        result = prime * result + (charWrite == null ? 0 : charWrite.hashCode());
        result = prime * result + fromState;
        result = prime * result + toState;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Transition other = (Transition) obj;
        if (charRead != other.charRead) {
            return false;
        }
        if (charWrite == null) {
            if (other.charWrite != null) {
                return false;
            }
        } else if (!charWrite.equals(other.charWrite)) {
            return false;
        }
        if (fromState != other.fromState) {
            return false;
        }
        return toState == other.toState;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "fromState=" + fromState +
                ", toState=" + toState +
                ", charRead=" + charRead +
                ", charWrite=" + charWrite +
                '}';
    }
}