package ab1.impl.Pauritsch;

import ab1.TM;

import java.util.List;
import java.util.Set;

public class TMImpl implements TM {

    @Override
    public TM reset() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getActState() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public TM setNumberOfTapes(int numTapes) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TM setSymbols(Set<Character> symbols) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Character> getSymbols() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TM addTransition(int fromState, int tapeRead, char symbolRead, int toState, int tapeWrite, char symbolWrite,
                            Movement tapeReadMovement, Movement tapeWriteMovement) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNumberOfStates() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNumberOfTapes() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public TM setNumberOfStates(int numStates) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TM setHaltState(int state) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TM setInitialState(int state) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TM setInitialTapeContent(int tape, char[] content) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TM doNextStep() throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isHalt() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCrashed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<TMConfig> getTMConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TMConfig getTMConfig(int tape) {
        // TODO Auto-generated method stub
        return null;
    }

}
