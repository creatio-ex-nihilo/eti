package ab2.impl.Pauritsch;

public class PDATransition {

    private int fromState;
    private Character readTape;
    private Character readStack;
    private Character writeStack;
    private int toState;

    public PDATransition(int fromState, Character charReadTape, Character charReadStack, Character charWriteStack,
                         int toState) {
        this.fromState = fromState;
        this.readTape = charReadTape;
        this.readStack = charReadStack;
        this.writeStack = charWriteStack;
        this.toState = toState;
    }

    public int getFromState() {
        return fromState;
    }

    public void setFromState(int fromState) {
        this.fromState = fromState;
    }

    public Character getReadTape() {
        return readTape;
    }

    public void setReadTape(Character readTape) {
        this.readTape = readTape;
    }

    public Character getReadStack() {
        return readStack;
    }

    public void setReadStack(Character readStack) {
        this.readStack = readStack;
    }

    public Character getWriteStack() {
        return writeStack;
    }

    public void setWriteStack(Character writeStack) {
        this.writeStack = writeStack;
    }

    public int getToState() {
        return toState;
    }

    public void setToState(int toState) {
        this.toState = toState;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + readTape;
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
