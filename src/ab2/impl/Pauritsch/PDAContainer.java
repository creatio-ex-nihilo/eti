package ab2.impl.Pauritsch;

import java.util.Stack;

class PDAContainer {
    private int currentState;
    private PDATransition transition;
    private Stack<Character> stack;

    PDAContainer(int currentState, PDATransition transition, Stack<Character> stack) {
        this.currentState = currentState;
        this.transition = transition;
        this.stack = stack;
    }

    int getCurrentState() {
        return currentState;
    }

    void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    PDATransition getTransition() {
        return transition;
    }

    Stack<Character> getStack() {
        return stack;
    }
}
