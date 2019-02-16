package ab2.impl.Pauritsch;

import ab1.impl.Pauritsch.TMImpl;
import ab1.impl.Pauritsch.Tape;
import ab2.PDA;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class PDAImpl implements PDA {

    private int initState;
    private int currentState;
    private Set<Integer> states;
    private Set<Integer> acceptStates;
    private Set<Character> allowedInputChars;
    private Set<Character> allowedStackChars;
    private Set<PDATransition> transitions;
    private Tape tape;

    PDAImpl() {
        this.transitions = new HashSet<>();
        this.tape = new Tape();
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
        this.currentState = this.initState;
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
            // you don't have to read something from the stack
            if (charReadTape != null) {
                if (!this.allowedInputChars.contains(charReadTape)) {
                    throw new IllegalArgumentException("charReadTape isn't a valid symbol");
                }
            }
        }
        PDATransition t = new PDATransition(fromState, charReadTape, charReadStack, charWriteStack, toState);
        // just for safety reasons
        if (this.transitions.contains(t)) {
            throw new IllegalArgumentException("transition already known");
        } else {
            this.transitions.add(t);
        }
        this.transitions.add(t);
    }

    @Override
    public boolean accepts(String input) throws IllegalArgumentException, IllegalStateException {
        if (this.states == null || this.allowedStackChars == null || this.allowedInputChars == null) {
            throw new IllegalStateException("further setup is required");
        }
        char[] in = input.toCharArray();
        for (char c : in) {
            if (!this.allowedInputChars.contains(c)) {
                throw new IllegalArgumentException("unknown symbol in input");
            }
        }
        this.tape.setTapeContent(this.getCharArray(in));
        this.tape.setHeadPosition(0);

        // create the first transition to be checked
        // which basically says "start from initState with an empty stack"
        PDAContainer offset = new PDAContainer(this.currentState, null, new Stack<>());
        Vector<Set<PDAContainer>> allTransitions = this.doAllPossibleTransitions(offset);

        Set<PDAContainer> relevantTransitions = allTransitions.lastElement();
        if (allTransitions.size() == 1) {
            if (relevantTransitions.size() == 0) {
                // special case if no transition at all is found
                return this.checkAcceptance(offset);
            }
            for (PDAContainer container : relevantTransitions) {
                if (this.checkAcceptance(container)) {
                    return true;
                }
            }
            return false;
        } else {
            if (relevantTransitions.size() == 0) {
                return false;
            }
            for (PDAContainer container : relevantTransitions) {
                if (this.checkAcceptance(container)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public PDA append(PDA pda) throws IllegalArgumentException, IllegalStateException {
        // give pda a type
        PDAImpl given = (PDAImpl) pda;
        // create copy
        PDAImpl copy = new PDAImpl();
        int offset = this.states.size();
        copy.setNumStates(offset + given.states.size());
        copy.setInitialState(this.initState);
        copy.setAcceptingState(this.acceptStates);
        // create superset of allowed chars
        Set<Character> superInput = new HashSet<>();
        superInput.addAll(this.allowedInputChars);
        superInput.addAll(given.allowedInputChars);
        copy.setInputChars(superInput);
        // create superset of allowed chars
        Set<Character> superStack = new HashSet<>();
        superStack.addAll(this.allowedStackChars);
        superStack.addAll(given.allowedStackChars);
        copy.setStackChars(superStack);

        // copy existing transitions to copy
        for (PDATransition t : this.transitions) {
            copy.addTransition(t.getFromState(), t.getReadTape(), t.getReadStack(), t.getWriteStack(), t.getToState());
        }

        // copy given and modified transitions to copy
        for (PDATransition t : given.transitions) {
            copy.addTransition(offset + t.getFromState(), t.getReadTape(), t.getReadStack(), t.getWriteStack(), offset + t.getToState());
        }

        // add epsilon-transition from all accepting states
        // to initial state of given PDA
        for (int s : copy.acceptStates) {
            copy.addTransition(s, null, null, null, offset + given.initState);
        }

        // change accepting states to those
        // of given
        Set<Integer> acceptingStates = new HashSet<>();
        for (int s : given.acceptStates) {
            acceptingStates.add(offset + s);
        }
        copy.setAcceptingState(acceptingStates);
        return copy;
    }

    @Override
    public PDA union(PDA pda) throws IllegalArgumentException, IllegalStateException {
        // give pda a type
        PDAImpl given = (PDAImpl) pda;
        // create copy
        PDAImpl copy = new PDAImpl();
        // the states in PDA1 start with 1
        // 0 is reserved for the new initial state
        int offsetLeft = 1;
        // the states in PDA2 start with all states in PDA1
        // plus it's offset
        int offsetRight = this.states.size() + offsetLeft;
        copy.setNumStates(offsetRight + given.states.size());
        copy.setInitialState(0);
        // just to have a set set
        copy.setAcceptingState(this.acceptStates);
        // create superset of allowed chars
        Set<Character> superInput = new HashSet<>();
        superInput.addAll(this.allowedInputChars);
        superInput.addAll(given.allowedInputChars);
        copy.setInputChars(superInput);
        // create superset of allowed chars
        Set<Character> superStack = new HashSet<>();
        superStack.addAll(this.allowedStackChars);
        superStack.addAll(given.allowedStackChars);
        copy.setStackChars(superStack);

        // copy existing and modified transitions to copy
        for (PDATransition t : this.transitions) {
            copy.addTransition(t.getFromState() + offsetLeft, t.getReadTape(), t.getReadStack(), t.getWriteStack(), t.getToState() + offsetLeft);
        }

        // copy given and modified transitions to copy
        for (PDATransition t : given.transitions) {
            copy.addTransition(t.getFromState() + offsetRight, t.getReadTape(), t.getReadStack(), t.getWriteStack(), t.getToState() + offsetRight);
        }

        // add epsilon-transition from new initial state
        // to the old initial states of PDA1 & PDA2
        copy.addTransition(0, null, null, null, this.initState + offsetLeft);
        copy.addTransition(0, null, null, null, given.initState + offsetRight);

        // add accepting states together
        Set<Integer> acceptingStates = new HashSet<>();
        for (int s : this.acceptStates) {
            acceptingStates.add(offsetLeft + s);
        }
        for (int s : given.acceptStates) {
            acceptingStates.add(offsetRight + s);
        }
        copy.setAcceptingState(acceptingStates);
        return copy;
    }

    @Override
    public boolean isDPDA() throws IllegalStateException {
        for (PDATransition t : this.transitions) {
            for (PDATransition s : this.transitions) {
                if (!t.equals(s) && t.getFromState() == s.getFromState() && t.getReadTape() == s.getReadTape()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Set<PDATransition> findTransitions(int fromState, Character charReadTape, Stack<Character> stack) {
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
            if (t.getFromState() == fromState && t.getReadTape() == charReadTape && t.getReadStack() == s) {
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
            // but the stack is empty, you can't do doDoableTransitions
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
        return this.acceptStates.contains(c.getCurrentState()) && c.getStack().empty();
    }

    private Character[] getCharArray(char[] in) {
        Character[] chars;
        // special case if input is empty
        if (in.length == 0) {
            chars = new Character[1];
            chars[0] = null;
        } else {
            int i = 0;
            chars = new Character[in.length];
            for (char c : in) {
                chars[i++] = c;
            }
        }
        return chars;
    }

    private Set<PDAContainer> doDoableTransitions(Character c, PDAContainer container) throws IllegalArgumentException {
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

    private Vector<Set<PDAContainer>> doAllPossibleTransitions(PDAContainer offset) {
        // create a set of transitions, which have to be checked
        Set<PDAContainer> toBeChecked = new HashSet<>();
        // add the first created transition
        toBeChecked.add(offset);
        // create a set for all further transitions from transitions in toBeChecked
        Set<PDAContainer> furtherTransitions = new HashSet<>();
        // create a vector with all done transitions
        Vector<Set<PDAContainer>> allTransitions = new Vector<>();

        Character readFromTape;
        while ((readFromTape = this.tape.getBelowHead()) != TMImpl.BLANK) {
            // check if epsilon transitions are possible before checking the "normal" ones
            this.epsilonTransitions(toBeChecked);
            // check "normal" and further epsilon transitions
            for (PDAContainer container : toBeChecked) {
                try {
                    furtherTransitions.addAll(this.doDoableTransitions(readFromTape, container));
                    furtherTransitions.addAll(this.doDoableTransitions(null, container));
                } catch (IllegalArgumentException e) {
                    // do nothing
                }
            }
            // check if epsilon transitions are possible after checking the "normal" ones
            this.epsilonTransitions(furtherTransitions);

            // create a copy of all further transitions
            Set<PDAContainer> copy = new HashSet<>(furtherTransitions);
            allTransitions.add(copy);

            // replace toBeChecked with all further transitions
            // because those need to be checked next
            toBeChecked.clear();
            toBeChecked.addAll(furtherTransitions);
            furtherTransitions.clear();
            this.tape.setHeadPosition(this.tape.getHeadPosition() + 1);
        }
        return allTransitions;
    }

    private void epsilonTransitions(Set<PDAContainer> in) {
        Set<PDAContainer> out = new HashSet<>();
        for (PDAContainer container : in) {
            try {
                out.addAll(this.doDoableTransitions(null, container));
            } catch (IllegalArgumentException e) {
                // do nothing
            }
        }
        if (out.size() != 0) {
            in.addAll(out);
        }
    }
}
