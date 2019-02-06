package ab2.impl.Pauritsch;

import ab2.Mealy;
import ab2.Transition;

import java.util.HashSet;
import java.util.Set;

public class MealyImpl implements Mealy {

    private static final char PAIR_DELIMITER = '/';

    private Set<Integer> states;
    private int initialState;
    private Set<Transition> transitions;
    private Set<Character> readChars;
    private Set<Character> writeChars;

    MealyImpl() {
        this.transitions = new HashSet<>();
    }

    @Override
    public void addTransition(int fromState, char charRead, Character charWrite, int toState) throws IllegalArgumentException, IllegalStateException {
        if (this.states == null) {
            throw new IllegalStateException("states not set");
        } else {
            if (!this.states.contains(fromState) || !this.states.contains(toState)) {
                throw new IllegalArgumentException("fromState or toState isn't a valid state");
            }
        }
        if (this.writeChars == null) {
            throw new IllegalStateException("no write alphabet set");
        } else {
            // null as write character is allowed!
            if (charWrite != null) {
                if (!this.writeChars.contains(charWrite)) {
                    throw new IllegalArgumentException("charRead or charWrite isn't a valid symbol");
                }
            }
        }
        if (this.readChars == null) {
            throw new IllegalStateException("no read alphabet set");
        } else {
            if (!this.readChars.contains(charRead)) {
                throw new IllegalArgumentException("charRead or charWrite isn't a valid symbol");
            }
        }
        if (!isDeterministic(fromState, charRead, charWrite)) {
            throw new IllegalArgumentException("transition would create a nondeterminism");
        }

        Transition t = new Transition(fromState, toState, charRead, charWrite);
        // just for safety reasons
        if (this.transitions.contains(t)) {
            throw new IllegalArgumentException("transition already known");
        } else {
            this.transitions.add(t);
        }
    }

    @Override
    public Mealy toMoore() {
        if (this.states == null) {
            throw new IllegalStateException("states not set");
        }
        if (this.writeChars == null) {
            throw new IllegalStateException("no write alphabet set");
        }
        if (this.readChars == null) {
            throw new IllegalStateException("no read alphabet set");
        }
        return null;
    }

    @Override
    public String produced(String input) throws IllegalStateException {
        if (this.readChars == null || this.writeChars == null || this.transitions == null || this.states == null) {
            throw new IllegalStateException("further setup required");
        }
        // offset
        int tmpState = initialState;
        StringBuilder output = new StringBuilder();
        for (char tmpChar : input.toCharArray()) {
            Transition t = findTransition(tmpState, tmpChar);
            if (t == null) {
                return null;
            } else {
                if (t.getCharWrite() != null) {
                    output.append(t.getCharWrite());
                }
                tmpState = t.getToState();
            }
        }
        return output.toString();
    }

    @Override
    public Mealy minimize() {
        if (this.readChars == null || this.writeChars == null || this.transitions == null || this.states == null) {
            throw new IllegalStateException("further setup required");
        }
        // copy mealy
        MealyImpl m = new MealyImpl();
        m.setNumStates(this.getNumStates());
        m.setInitialState(this.getInitialState());
        Set<Character> rC = new HashSet<>();
        rC.addAll(this.getReadChars());
        m.setReadChars(rC);
        Set<Character> rW = new HashSet<>();
        rW.addAll(this.getWriteChars());
        m.setWriteChars(rW);
        for (Transition t : this.transitions) {
            m.addTransition(t.getFromState(), t.getCharRead(), t.getCharWrite(), t.getToState());
        }
        // create sets and states for RSA (minimization)
        Set<Integer> finalStates = new HashSet<>();
        finalStates.addAll(m.getStates());
        // additional state to complete DFA to RSA
        m.getStates().add(finalStates.size());
        int additionalState = m.getStates().size() - 1;
        // get all possible permutations
        Set<String> perms = this.allPermutations(m.getReadChars(), m.getWriteChars());

        this.createRSA(m, perms, additionalState);
        this.combineIndistinguishableStates(m, this.minimizeRSA(m, finalStates, perms));

        Set<Transition> delTransitions = new HashSet<>();
        // delete the additional state and transitions with it in it
        for (Transition t : m.getTransitions()) {
            if (t.getFromState() == additionalState || t.getToState() == additionalState) {
                delTransitions.add(t);
            }
        }
        m.getTransitions().removeAll(delTransitions);
        m.getStates().remove(additionalState);
        return m;
    }

    private Set<String> minimizeRSA(MealyImpl m, Set<Integer> finalStates, Set<String> perms) {
        Set<String> diagonal = this.createDiagonal(m.getStates());
        Set<String> min = this.createMinZero(finalStates);
        Set<String> superSet = new HashSet<>();
        // to be able to delete stuff from a set while you are operating on it
        // duplicate it
        Set<String> tmpMin = new HashSet<>();
        tmpMin.addAll(min);

        do {
            // (re)calc the superset
            superSet.clear();
            superSet.addAll(min);
            superSet.addAll(diagonal);
            // go through all entries in min
            for (String s : min) {
                Object[] tmp = this.extractPair(s);
                int state1in = Integer.parseInt((String) tmp[0]);
                int state2in = Integer.parseInt((String) tmp[1]);
                int state1out = 0, state2out = 0;
                // calc where each permutation goes
                Set<String> reached = new HashSet<>();
                for (String p : perms) {
                    boolean state1found = false;
                    boolean state2found = false;
                    for (Transition t : m.getTransitions()) {
                        String tmpPair = this.createPair(t.getCharRead(), t.getCharWrite());
                        if (t.getFromState() == state1in && tmpPair.equals(p)) {
                            state1out = t.getToState();
                            state1found = true;
                        }
                        if (t.getFromState() == state2in && tmpPair.equals(p)) {
                            state2out = t.getToState();
                            state2found = true;
                        }
                        if (state1found && state2found) {
                            break;
                        }
                    }
                    reached.add(this.createPair(state1out, state2out));
                }
                // check if all reached pairs are in the superset
                for (String r : reached) {
                    if (!containsSetCommutativeElement(superSet, r)) {
                        tmpMin.remove(s);
                        break;
                    }
                }
            }
            // remove all removed elements in tmpMin in actual min
        } while (min.retainAll(tmpMin));
        return min;
    }

    @Override
    public int getNumStates() throws IllegalStateException {
        if (this.states == null) {
            throw new IllegalStateException("no known states");
        }
        return this.states.size();
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

    private Set<Integer> getStates() {
        return states;
    }

    private int getInitialState() {
        return initialState;
    }

    @Override
    public void setInitialState(int initialState) throws IllegalArgumentException, IllegalStateException {
        if (this.states == null) {
            throw new IllegalStateException("no states known");
        }
        if (!this.states.contains(initialState)) {
            throw new IllegalArgumentException("can't set unknown state as initial state");
        }
        this.initialState = initialState;
    }

    private Set<Character> getReadChars() {
        return readChars;
    }

    @Override
    public void setReadChars(Set<Character> chars) {
        this.readChars = chars;
    }

    private Set<Character> getWriteChars() {
        return writeChars;
    }

    @Override
    public void setWriteChars(Set<Character> chars) {
        this.writeChars = chars;
    }

    @Override
    public Set<Transition> getTransitions() {
        return this.transitions;
    }

    // is there a already accepted transition, which leads somewhere
    // from state s via char c ?
    private boolean isDeterministic(int newState, char charRead, Character charWrite) {
        for (Transition t : this.transitions) {
            if (t.getFromState() == newState && t.getCharRead() == charRead && t.getCharWrite() == charWrite) {
                return false;
            }
        }
        return true;
    }

    private void createRSA(MealyImpl m, Set<String> perms, int additionalState) {
        for (int s : m.getStates()) {
            // get all transitions from state s to x; save all read/write-pairs
            Set<String> subPerms = new HashSet<>();
            for (Transition t : m.getTransitions()) {
                if (t.getFromState() == s) {
                    subPerms.add(createPair(t.getCharRead(), t.getCharWrite()));
                }
            }
            // go through all saved perms; add transitions if necessary
            for (String p : perms) {
                if (!subPerms.contains(p)) {
                    // split perm into read & write
                    Object[] tmp = extractPair(p);
                    String tmpRead = (String) tmp[0];
                    String tmpWrite = (String) tmp[1];
                    // add transition to the additional state
                    m.addTransition(s, tmpRead.charAt(0), tmpWrite.charAt(0), additionalState);
                }
            }
        }
    }

    private void combineIndistinguishableStates(MealyImpl m, Set<String> indistinguishableStates) {
        Set<Transition> delTransitions = new HashSet<>();
        // delete all transitions with state2 as fromState
        // replace state2 in all transitions with state1
        for (String s : indistinguishableStates) {
            Object[] tmp = this.extractPair(s);
            int state1 = Integer.valueOf((String) tmp[0]);
            int state2 = Integer.valueOf((String) tmp[1]);
            for (Transition t : m.getTransitions()) {
                if (t.getFromState() == state2) {
                    delTransitions.add(t);
                }
            }
            m.getTransitions().removeAll(delTransitions);
            // you have to go through the transitions again for the special case,
            // that you want to delete a transition, but also have to alter it.
            for (Transition t : m.getTransitions()) {
                if (t.getToState() == state2) {
                    t.setToState(state1);
                }
            }
            m.getStates().remove(state2);
        }
    }

    private boolean containsSetCommutativeElement(Set<String> set, String element) {
        Object[] tmp = this.extractPair(element);
        int one = Integer.valueOf((String) tmp[0]);
        int two = Integer.valueOf((String) tmp[1]);
        String tmpPair = this.createPair(two, one);
        return set.contains(element) || set.contains(tmpPair);
    }

    private Transition findTransition(int oldState, char readChar) {
        for (Transition t : this.transitions) {
            if (t.getFromState() == oldState && t.getCharRead() == readChar) {
                return t;
            }
        }
        return null;
    }

    private Set<String> allPermutations(Set<Character> in, Set<Character> out) {
        Set<String> output = new HashSet<>();
        for (Character c : in) {
            for (Character d : out) {
                output.add(this.createPair(c, d));
            }
        }
        return output;
    }

    private Set<String> createMinZero(Set<Integer> finalStates) {
        // perms without symmetrical duplicates
        Set<String> output = new HashSet<>();
        for (int i = 0; i < finalStates.size() - 1; i++) {
            for (int j = i + 1; j < finalStates.size(); j++) {
                output.add(this.createPair(i, j));
            }
        }
        return output;
    }

    private Set<String> createDiagonal(Set<Integer> allStates) {
        // diagonal set
        Set<String> output = new HashSet<>();
        for (int k : allStates) {
            output.add(this.createPair(k, k));
        }
        return output;
    }

    private String createPair(Object in, Object out) {
        return "" + in + PAIR_DELIMITER + out;
    }

    private Object[] extractPair(String pair) {
        return pair.split("" + PAIR_DELIMITER);
    }
}
