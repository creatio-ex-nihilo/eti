package ab2.impl.Pauritsch;

import ab2.Factory;
import ab2.Mealy;
import ab2.PDA;

import java.util.Set;

public class FactoryImpl implements Factory {

    @Override
    public Mealy getEmptyMealy() {
        return new MealyImpl();
    }

    @Override
    public PDA getEmptyPDA() {
        return null;
    }

    @Override
    public PDA getPDAFromCFG(char startSymbol, Set<String> rules) {
        return null;
    }
}
