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

    PDAImpl() {
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

        // offset
        int cnt = 0;
        Set<PDAContainer> all = new HashSet<>();
        PDAContainer tmp = new PDAContainer(this.currentState, null, this.stack);
        all.add(tmp);

        Set<PDAContainer> tmpAll = new HashSet<>();
        char c;
        while ((c = this.tape.getBelowHead()) != TMImpl.BLANK) {
            for (PDAContainer container : all) {
                try {
                    tmpAll.addAll(this.stuff(c, container));
                } catch (IllegalArgumentException e) {
                    //return false;
                }
            }
            all.clear();
            all.addAll(tmpAll);
            tmpAll.clear();
            this.tape.setHeadPosition(this.tape.getHeadPosition() + 1);
        }
        // check each remaining container with the accepting set of criteria
        for (PDAContainer container : all) {
            if (this.checkAcceptance(container)) {
                return true;
            }
        }
        return false;
    }

    private Set<PDAContainer> stuff(char c, PDAContainer container) throws IllegalArgumentException {
        // find all transitions from currentState with charReadTape c
        Set<PDATransition> transitions = this.findTransitions(container.getCurrentState(), c, container.getStack());
        if (transitions == null) {
            throw new IllegalArgumentException("can't find a fitting transition");
        }
        Set<PDAContainer> successfulTransitions = new HashSet<>();
        for (PDATransition t : transitions) {
            // create copy of current stack
            Stack<Character> copy = new Stack<>();
            copy.addAll(container.getStack());

            PDAContainer ret = this.doTransition(new PDAContainer(container.getCurrentState(), t, copy));
            if (ret != null) {
                successfulTransitions.add(ret);
            }
        }
        return successfulTransitions;
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
        for (PDATransition t : this.transitions) {
            for (PDATransition s : this.transitions) {
                if (!t.equals(s)) {
                    if (t.getFromState() == s.getFromState() && t.getReadTape() == s.getReadTape()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Set<PDATransition> findTransitions(int fromState, char charReadTape, Stack<Character> stack) {
        Set<PDATransition> out = new HashSet<>();
        Character topOfStack = this.getTopOfStack(stack);
        Character s;
        for (PDATransition t : this.transitions) {
            s = topOfStack;
            if (t.getReadStack() == null) {
                // if you don't want to read from the stack
                // ignore the top of the stack
                s = null;
            }
            if (t.getFromState() == fromState && t.getReadTape().equals(charReadTape) && t.getReadStack() == s) {
                out.add(t);
            }
        }
        return out.size() == 0 ? null : out;
    }

    private PDAContainer doTransition(PDAContainer c) {
        PDATransition t = c.getTransition();
        Stack<Character> s = c.getStack();
        Character topOfStack = this.getTopOfStack(s);
        // pop of the stack
        if (t.getReadStack() != null) {
            // if you want to read something from the stack
            // but the stack is empty, you can't do stuff
            if (topOfStack == null) {
                return null;
            } else if (topOfStack == t.getReadStack()) {
                // remove the top of the stack
                s.pop();
            }
        }
        // push onto stack
        if (t.getWriteStack() != null) {
            s.push(t.getWriteStack());
        }
        // change state
        if (t.getFromState() == c.getCurrentState()) {
            c.setCurrentState(t.getToState());
        }
        return c;
    }

    private Character getTopOfStack(Stack<Character> s) {
        if (!s.empty()) {
            return s.peek();
        }
        return null;
    }

    private boolean checkAcceptance(PDAContainer c) {
        boolean ret = this.acceptStates.contains(c.getCurrentState()) && c.getStack().empty();
        c.getStack().clear();
        return ret;
    }
}
