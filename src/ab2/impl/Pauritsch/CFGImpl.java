package ab2.impl.Pauritsch;

import ab2.PDA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

class CFGImpl {

    private Character start;
    private Set<String> rules;
    private Set<PDATransition> generatedTransitions;
    private int stateCnt;
    private int startState = 0;
    private int actualState = 1;
    private Set<Character> writtenChars;

    CFGImpl(Character start, Set<String> rules) {
        this.start = start;
        this.rules = rules;
        this.generatedTransitions = new HashSet<>();
        this.stateCnt = this.actualState;
        this.writtenChars = new HashSet<>();
    }

    PDA generatePDA() {
        this.generateTransitions();
        PDAImpl pda = new PDAImpl();
        pda.setNumStates(this.stateCnt);
        pda.setInitialState(this.startState);
        pda.setAcceptingState(new HashSet<>(this.actualState));
        pda.setInputChars(new HashSet<>(Arrays.asList('a', 'b', 'c', 'S', 'T')));
        pda.setStackChars(new HashSet<>(Arrays.asList('a', 'b', 'c', 'S', 'T')));

        for (PDATransition t : this.generatedTransitions) {
            pda.addTransition(t.getFromState(), t.getReadTape(), t.getReadStack(), t.getWriteStack(), t.getToState());
        }

        return pda;
    }

    private void generateTransitions() {
        PDATransition start = new PDATransition(this.startState, null, null, this.start, this.actualState);
        generatedTransitions.add(start);

        Vector<PDATransition> set = new Vector<>();
        int offset = 0;
        for (String rule : this.rules) {
            String[] splitted = rule.split(FactoryImpl.CFG_DELIMITER1);
            Character st = splitted[0].charAt(0);
            String replace = splitted[1];
            for (String s : replace.split("\\" + FactoryImpl.CFG_DELIMITER2)) {
                Character[] chars = PDAImpl.getCharArray(s.toCharArray());
                boolean firstSet = false;
                for (Character c : chars) {
                    if (!firstSet) {
                        set.add(new PDATransition(this.actualState, null, st, c, this.actualState + ++offset));
                        firstSet = true;
                    } else {
                        set.add(new PDATransition(this.actualState + offset, null, null, c, this.actualState + ++offset));
                    }
                    this.writtenChars.add(c);
                }
                offset -= 1;
                PDATransition tmp = set.lastElement();
                tmp.setToState(this.actualState);
                this.generatedTransitions.addAll(set);
                set.clear();
            }
            this.writtenChars.remove(st);
        }
        for (Character c : this.writtenChars) {
            this.generatedTransitions.add(new PDATransition(this.actualState, c, c, null, this.actualState));
        }
        this.stateCnt = offset + 2;
    }
}
