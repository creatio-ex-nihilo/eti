package ab2.impl.Pauritsch;

import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class PDAContainer {
    Set<PDATransition> transitions;
    Vector<Stack> stacks;

    public PDAContainer(Set<PDATransition> transitions, Vector<Stack> stacks) {
        this.transitions = transitions;
        this.stacks = stacks;
    }

    public Set<PDATransition> getTransitions() {
        return transitions;
    }

    public void setTransitions(Set<PDATransition> transitions) {
        this.transitions = transitions;
    }

    public Vector<Stack> getStacks() {
        return stacks;
    }

    public void setStacks(Vector<Stack> stacks) {
        this.stacks = stacks;
    }
}
