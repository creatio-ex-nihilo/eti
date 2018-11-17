package ab1.impl.Pauritsch;

import ab1.TM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TMImpl implements TM {

    static final char BLANK = '#';

    private Set<Integer> states;
    private Set<Character> symbols;
    private Set<Transition> transitions;
    private Tape[] tapes;

    private int initialState;
    private int currentState;
    private int haltState;

    private boolean isCrashed;

    public TMImpl() {
        this.init();
    }

    private void init() {
        this.states = new HashSet<>();
        this.symbols = new HashSet<>();
        this.transitions = new HashSet<>();
    }

    @Override
    public TM reset() {
        this.init();
        return this;
    }

    @Override
    public int getActState() {
        return this.currentState;
    }

    @Override
    public TM setNumberOfTapes(int numTapes) throws IllegalArgumentException {
        if (numTapes < 1) {
            throw new IllegalArgumentException("there has to be at least 1 tape");
        }
        this.tapes = new Tape[numTapes];
        for (int i = 0; i < numTapes; i++) {
            this.tapes[i] = new Tape();
        }
        return this;
    }

    @Override
    public TM setSymbols(Set<Character> symbols) throws IllegalArgumentException {
        if (!symbols.contains(BLANK)) {
            throw new IllegalArgumentException("doesn't contain " + BLANK);
        }
        this.symbols = symbols;
        return this;
    }

    @Override
    public Set<Character> getSymbols() {
        return this.symbols;
    }

    @Override
    public TM addTransition(int fromState, int tapeRead, char symbolRead, int toState, int tapeWrite, char symbolWrite,
                            Movement tapeReadMovement, Movement tapeWriteMovement) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNumberOfStates() {
        return this.states.size();
    }

    @Override
    public int getNumberOfTapes() {
        return this.tapes.length;
    }

    @Override
    public TM setNumberOfStates(int numStates) throws IllegalArgumentException {
        if (numStates < 1) {
            throw new IllegalArgumentException("there has to be at least 1 state");
        }
        this.states.clear();
        for (int i = 0; i < numStates; i++) {
            this.states.add(i);
        }
        return this;
    }

    @Override
    public TM setHaltState(int state) throws IllegalArgumentException {
        if (!this.states.contains(state)) {
            throw new IllegalArgumentException("halt state has to be a known state");
        }
        this.haltState = state;
        return this;
    }

    @Override
    public TM setInitialState(int state) throws IllegalArgumentException {
        if (!this.states.contains(state)) {
            throw new IllegalArgumentException("initial state has to be a known state");
        }
        this.initialState = state;
        return this;
    }

    @Override
    public TM setInitialTapeContent(int tape, char[] content) {
        if (tape < 0 || tape >= this.tapes.length) {
            // ok then ...
        }
        this.tapes[tape].setTapeContent(content);
        return this;
    }

    @Override
    public TM doNextStep() throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isHalt() {
        return this.currentState == this.haltState;
    }

    @Override
    public boolean isCrashed() {
        return this.isCrashed;
    }

    @Override
    public List<TMConfig> getTMConfig() {
        if (this.isCrashed) {
            return null;
        }
        ArrayList<TMConfig> output = new ArrayList<>();
        for (Tape t : this.tapes) {
            output.add(new TMConfig(t.getLeftOfHead(), t.getBelowHead(), t.getRightOfHead()));
        }
        return output;
    }

    @Override
    public TMConfig getTMConfig(int tape) {
        if (this.isCrashed) {
            return null;
        }
        Tape tmp = this.tapes[tape];
        return new TMConfig(tmp.getLeftOfHead(), tmp.getBelowHead(), tmp.getRightOfHead());
    }

}
