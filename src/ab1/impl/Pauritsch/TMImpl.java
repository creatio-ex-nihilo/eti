package ab1.impl.Pauritsch;

import ab1.TM;

import java.util.*;

public class TMImpl implements TM {

    public static final Character BLANK = '#';

    private Set<Integer> states;
    private Set<Character> symbols;
    private Set<Transition> transitions;
    private Tape[] tapes;

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

        Transition tmp = new Transition(fromState, toState, symbolRead, symbolWrite, tapeRead, tapeWrite, tapeReadMovement, tapeWriteMovement);

        if (fromState == this.haltState && symbolRead != Character.MIN_VALUE) {
            throw new IllegalArgumentException("can't be in halt and read a symbol");
        }
        if (transitions.contains(tmp)) {
            throw new IllegalArgumentException("transition already exists");
        }
        if (!this.symbols.contains(symbolRead) || !this.symbols.contains(symbolWrite)) {
            throw new IllegalArgumentException("can't write unknown symbol");
        }
        if (tapeRead < 0 || tapeRead >= this.tapes.length || tapeWrite < 0 || tapeWrite >= this.tapes.length) {
            throw new IllegalArgumentException("read or write tape doesn't exist");
        }
        if (!this.states.contains(fromState) || !this.states.contains(toState)) {
            this.isCrashed = true;
            throw new IllegalArgumentException("from or to state is unknown");
        }

        // if you have made it this far, add it.
        this.transitions.add(tmp);
        return this;
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
        this.currentState = state;
        return this;
    }

    @Override
    public TM setInitialTapeContent(int tape, char[] content) {
        /*
        if (tape < 0 || tape >= this.tapes.length) {
            // ok then ...
        }
        */
        Character[] tmp = new Character[content.length];
        int i = 0;
        for (char c : content) {
            tmp[i++] = c;
        }
        this.tapes[tape].setTapeContent(tmp);
        this.tapes[tape].setHeadPosition(tmp.length);
        return this;
    }

    @Override
    public TM doNextStep() throws IllegalStateException {
        if (this.isHalt()) {
            throw new IllegalStateException("already in halt");
        }

        Iterator iter = this.transitions.iterator();
        boolean foundOne = false;
        Transition tmp;

        do {
            tmp = (Transition) iter.next();
            // find transition with fromState and read tape with read symbol
            if (tmp.getFromState() == this.currentState && tmp.getSymbolRead() == this.tapes[tmp.getTapeRead()].getBelowHead()) {
                foundOne = true;
            }
        } while (iter.hasNext() && !foundOne);

        if (foundOne) {
            // change state
            this.currentState = tmp.getToState();
            // overwrite head on the write tape
            this.tapes[tmp.getTapeWrite()].writeHead(tmp.getSymbolWrite());
            // move heads according to the chosen transition
            this.moveHeads(tmp);
        } else {
            throw new IllegalStateException("no matching transition found");
        }
        return this;
    }

    private void moveHeads(Transition tmp) throws IllegalStateException {
        Tape[] tapes = {this.tapes[tmp.getTapeRead()], this.tapes[tmp.getTapeWrite()]};
        Movement[] moves = {tmp.getTapeReadMovement(), tmp.getTapeWriteMovement()};
        for (int i = 0; i < tapes.length; i++) {
            Tape t = tapes[i];
            int head = t.getHeadPosition();
            Movement m = moves[i];

            // left
            if (m.equals(Movement.Left)) {
                try {
                    t.setHeadPosition(head - 1);
                } catch (IllegalArgumentException e) {
                    this.isCrashed = true;
                    throw new IllegalStateException("reached left end of tape");
                }
            }
            // right
            else if (m.equals(Movement.Right)) {
                t.setHeadPosition(head + 1);
            }
            // stay
            /*
            else if (m.equals(Movement.Stay)) {
                // do nothing
            }
            */
        }
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
            output.add(this.generateConfig(t.getLeftOfHead(), t.getBelowHead(), t.getRightOfHead()));
        }
        return output;
    }

    @Override
    public TMConfig getTMConfig(int tape) {
        if (this.isCrashed) {
            return null;
        }
        Tape tmp = this.tapes[tape];
        return this.generateConfig(tmp.getLeftOfHead(), tmp.getBelowHead(), tmp.getRightOfHead());
    }

    private TMConfig generateConfig(Character[] l, Character b, Character[] r) {
        char[] lnew = new char[l.length];
        int lindex = 0;
        for (Character c : l) {
            lnew[lindex++] = c;
        }
        char bnew = b;
        char[] rnew = new char[r.length];
        int rindex = 0;
        for (Character c : r) {
            rnew[rindex++] = c;
        }
        return new TMConfig(lnew, bnew, rnew);
    }
}
