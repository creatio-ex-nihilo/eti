package ab2.impl.Pauritsch;

import ab1.impl.Pauritsch.TMImpl;
import ab1.impl.Pauritsch.Tape;
import ab2.PDA;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class PDAImpl implements PDA {

    private int initState;
    private int currentState;
    private Set<Integer> states;
    private Set<Integer> acceptStates;
    private Set<Character> allowedInputChars;
    private Set<Character> allowedStackChars;
    private Set<PDATransition> transitions;
    private Tape tape;
    private Stack<Character> stack;

    public PDAImpl() {
        this.transitions = new HashSet<>();
        this.tape = new Tape();
        this.stack = new Stack<>();
    }

    @Override
    public void setNumStates(int numStates) throws IllegalArgumentException {
        if (numStates <= 0) {
            throw new IllegalArgumentException("has to have at least 1 state");
        }
        this.states = new HashSet<>(numStates);
        for (int i = 0; i < numStates; i++) {
            this.states.add(i);
        }
    }

    @Override
    public void setInitialState(int initialState) throws IllegalArgumentException, IllegalStateException {
        if (this.states == null) {
            throw new IllegalStateException("no states known");
        }
        if (!this.states.contains(initialState)) {
            throw new IllegalArgumentException("can't set unknown state as initial state");
        }
        this.initState = initialState;
    }

    @Override
    public void setAcceptingState(Set<Integer> acceptingStates) throws IllegalArgumentException, IllegalStateException {
        if (this.states == null) {
            throw new IllegalStateException("no states known");
        }
        for (int s : acceptingStates) {
            if (s < 0 || s >= this.states.size()) {
                throw new IllegalArgumentException("accepting state needs to be a known state");
            }
        }
        this.acceptStates = acceptingStates;
    }

    @Override
    public void setInputChars(Set<Character> chars) {
        this.allowedInputChars = chars;
    }

    @Override
    public void setStackChars(Set<Character> chars) {
        this.allowedStackChars = chars;
    }

    @Override
    public void addTransition(int fromState, Character charReadTape, Character charReadStack, Character charWriteStack, int toState) throws IllegalArgumentException, IllegalStateException {
        if (this.states == null) {
            throw new IllegalStateException("states not set");
        } else {
            if (!this.states.contains(fromState) || !this.states.contains(toState)) {
                throw new IllegalArgumentException("fromState or toState isn't a valid state");
            }
        }
        if (this.allowedStackChars == null) {
            throw new IllegalStateException("no write alphabet set");
        } else {
            // you don't have to read something from the stack
            if (charReadStack != null) {
                if (!this.allowedStackChars.contains(charReadStack)) {
                    throw new IllegalArgumentException("charReadStack isn't a valid symbol");
                }
            }
            // you don't have to write something onto the stack
            if (charWriteStack != null) {
                if (!this.allowedStackChars.contains(charWriteStack)) {
                    throw new IllegalArgumentException("charWriteStack isn't a valid symbol");
                }
            }
        }
        if (this.allowedInputChars == null) {
            throw new IllegalStateException("no read alphabet set");
        } else {
            if (!this.allowedInputChars.contains(charReadTape)) {
                throw new IllegalArgumentException("charReadTape isn't a valid symbol");
            }
        }
        PDATransition t = new PDATransition(fromState, charReadTape, charReadStack, charWriteStack, toState);
        // just for safety reasons
        if (this.transitions.contains(t)) {
            throw new IllegalArgumentException("transition already known");
        } else {
            this.transitions.add(t);
        }
    }

    @Override
    public boolean accepts(String input) throws IllegalArgumentException, IllegalStateException {
        if (this.states == null || this.allowedStackChars == null || this.allowedInputChars == null) {
            throw new IllegalStateException("further setup is required");
        }
        char[] chars = input.toCharArray();
        for (char s : chars) {
            if (!this.allowedInputChars.contains(s)) {
                throw new IllegalArgumentException("input contains unknown char(s)");
            }
        }
        if (chars.length == 0) {
            chars = new char[1];
            chars[0] = TMImpl.BLANK;
        }
        this.tape.setTapeContent(chars);
        this.tape.setHeadPosition(0);
        this.currentState = this.initState;
        char c;
        while ((c = this.tape.getBelowHead()) != TMImpl.BLANK) {
            // find a transition from currentState with charReadTape c
            PDATransition t = this.findTransition(this.currentState, c);
            // no (further) transition is found
            if (t == null) {
                // if there is still something on the tape
                // but there are no fitting transitions found
                return false;
            }
            this.doTransition(t);
            this.tape.setHeadPosition(this.tape.getHeadPosition() + 1);
        }
        return this.checkAcceptance();
    }

    @Override
    public PDA append(PDA pda) throws IllegalArgumentException, IllegalStateException {
        return null;
    }

    @Override
    public PDA union(PDA pda) throws IllegalArgumentException, IllegalStateException {
        return null;
    }

    @Override
    public boolean isDPDA() throws IllegalStateException {
        return false;
    }

    private PDATransition findTransition(int fromState, char charReadTape) {
        Character topOfStack = this.getTopOfStack();
        Character s;
        for (PDATransition t : this.transitions) {
            s = topOfStack;
            if (t.getReadStack() == null) {
                // if you don't want to read from the stack
                // ignore the top of the stack
                s = null;
            }
            if (t.getFromState() == fromState && t.getReadTape().equals(charReadTape) && t.getReadStack() == s) {
                return t;
            }
        }
        return null;
    }

    private void doTransition(PDATransition t) {
        Character topOfStack = this.getTopOfStack();
        // pop of the stack
        if (t.getReadStack() != null) {
            if (topOfStack == t.getReadStack()) {
                // remove the top of the stack
                this.stack.pop();
            }
        }
        // push onto stack
        if (t.getWriteStack() != null) {
            this.stack.push(t.getWriteStack());
        }
        // change state
        if (t.getFromState() == this.currentState) {
            this.currentState = t.getToState();
        }
    }

    private Character getTopOfStack() {
        if (!this.stack.empty()) {
            return this.stack.peek();
        }
        return null;
    }

    private boolean checkAcceptance() {
        boolean ret;
        ret = this.acceptStates.contains(this.currentState) && this.stack.empty();
        this.stack.clear();
        return ret;
    }
}
