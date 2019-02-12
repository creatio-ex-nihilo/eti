package ab2.impl.Pauritsch;

public class PDATransition {

    private int fromState;
    private Character readTape;
    private Character readStack;
    private Character writeStack;
    private int toState;

    PDATransition(int fromState, Character charReadTape, Character charReadStack, Character charWriteStack,
                  int toState) {
        this.fromState = fromState;
        this.readTape = charReadTape;
        this.readStack = charReadStack;
        this.writeStack = charWriteStack;
        this.toState = toState;
    }

    int getFromState() {
        return fromState;
    }

    Character getReadTape() {
        return readTape;
    }

    Character getReadStack() {
        return readStack;
    }

    Character getWriteStack() {
        return writeStack;
    }

    int getToState() {
        return toState;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (readTape == null ? 0 : readTape.hashCode());
        result = prime * result + (readStack == null ? 0 : readStack.hashCode());
        result = prime * result + (writeStack == null ? 0 : writeStack.hashCode());
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
        PDATransition other = (PDATransition) obj;
        if (readTape != other.readTape) {
            return false;
        }
        if (readStack == null) {
            if (other.readStack != null) {
                return false;
            }
        } else if (!readStack.equals(other.readStack)) {
            return false;
        }
        if (writeStack == null) {
            if (other.writeStack != null) {
                return false;
            }
        } else if (!writeStack.equals(other.writeStack)) {
            return false;
        }
        if (fromState != other.fromState) {
            return false;
        }
        return toState == other.toState;
    }

    @Override
    public String toString() {
        return "PDATransition{" +
                "fromState=" + fromState +
                ", readTape=" + readTape +
                ", readStack=" + readStack +
                ", writeStack=" + writeStack +
                ", toState=" + toState +
                '}';
    }
}
