import ab2.Factory;
import ab2.Mealy;
import ab2.PDA;
import ab2.Transition;
import ab2.impl.Pauritsch.FactoryImpl;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests {
    private static Factory factory = new FactoryImpl();

    private static int mealyPoints = 0;
    private static int pdaPoints = 0;
    private static boolean mealyBasics = true;
    private static boolean pdaBasics = true;

    private static Set<Character> readChars = new HashSet<>(Arrays.asList('0', '1', 'a', 'b', 'c', 'd', 'e', 'f', '|'));
    private static Set<Character> writeChars = new HashSet<>(Arrays.asList('0', '1', 'a', 'b', 'c', 'd', 'e', 'f'));


    /**************************************************************************/
    /******************************* Mealy Part *******************************/
    /**************************************************************************/

    private static Mealy mealy1() {
        Mealy m = factory.getEmptyMealy();
        m.setNumStates(1);
        m.setInitialState(0);
        m.setReadChars(readChars);
        m.setWriteChars(writeChars);
        m.addTransition(0, 'a', 'b', 0);
        m.addTransition(0, 'b', 'c', 0);
        m.addTransition(0, 'c', 'a', 0);

        return m;
    }

    private static Mealy mealy2() {
        Mealy m = factory.getEmptyMealy();
        m.setNumStates(3);
        m.setInitialState(0);
        m.setReadChars(readChars);
        m.setWriteChars(writeChars);
        m.addTransition(0, '0', null, 0);
        m.addTransition(0, '1', null, 1);
        m.addTransition(1, '0', null, 1);
        m.addTransition(1, '1', null, 0);

        m.addTransition(0, '|', '0', 2);
        m.addTransition(1, '|', '1', 2);
        return m;
    }

    private static Mealy mealy3() {
        Mealy m = factory.getEmptyMealy();
        m.setNumStates(3);
        m.setInitialState(2);
        m.setReadChars(readChars);
        m.setWriteChars(writeChars);
        m.addTransition(2, '0', null, 0);
        m.addTransition(2, '1', null, 1);

        m.addTransition(1, '1', '1', 1);
        m.addTransition(1, '0', '1', 0);

        m.addTransition(0, '1', '0', 1);
        m.addTransition(0, '0', '0', 0);

        return m;
    }

    private static Mealy mealy4() {
        Mealy m = factory.getEmptyMealy();
        m.setNumStates(4);
        m.setInitialState(0);
        m.setReadChars(readChars);
        m.setWriteChars(writeChars);
        m.addTransition(0, '1', '0', 1);
        m.addTransition(0, '0', '1', 2);

        m.addTransition(1, '0', '1', 1);
        m.addTransition(1, '1', '0', 0);

        m.addTransition(2, '0', '0', 1);
        m.addTransition(2, '1', '1', 3);

        m.addTransition(3, '0', '1', 3);
        m.addTransition(3, '1', '0', 0);

        return m;
    }

    /************************************************************************/

    public static PDA pda1() {
        PDA m = factory.getEmptyPDA();
        m.setNumStates(1);
        m.setInitialState(0);
        m.setAcceptingState(new HashSet<>(Arrays.asList(0)));
        m.setInputChars(readChars);
        m.setStackChars(writeChars);

        m.addTransition(0, 'a', null, 'a', 0);
        m.addTransition(0, 'a', 'a', null, 0);

        return m;
    }

    private static PDA pda2() {
        PDA m = factory.getEmptyPDA();
        m.setNumStates(1);
        m.setInitialState(0);
        m.setAcceptingState(new HashSet<>(Arrays.asList(0)));
        m.setInputChars(readChars);
        m.setStackChars(writeChars);

        m.addTransition(0, 'a', null, 'a', 0);
        m.addTransition(0, 'b', null, 'b', 0);
        m.addTransition(0, 'c', null, 'c', 0);

        m.addTransition(0, 'a', 'a', null, 0);
        m.addTransition(0, 'b', 'b', null, 0);
        m.addTransition(0, 'c', 'c', null, 0);

        return m;
    }

    public static PDA pda3() {
        PDA m = factory.getEmptyPDA();
        m.setNumStates(2);
        m.setInitialState(0);
        m.setAcceptingState(new HashSet<>(Arrays.asList(1)));
        m.setInputChars(readChars);
        m.setStackChars(writeChars);

        m.addTransition(0, 'a', null, 'a', 0);
        m.addTransition(0, 'b', null, 'b', 0);
        m.addTransition(0, 'c', null, 'c', 0);

        m.addTransition(0, 'a', null, null, 1);
        m.addTransition(0, 'b', null, null, 1);
        m.addTransition(0, 'c', null, null, 1);

        m.addTransition(1, 'a', 'a', null, 1);
        m.addTransition(1, 'b', 'b', null, 1);
        m.addTransition(1, 'c', 'c', null, 1);

        return m;
    }

    private static PDA pda4() {
        PDA m = factory.getEmptyPDA();
        m.setNumStates(2);
        m.setInitialState(0);
        m.setAcceptingState(new HashSet<>(Arrays.asList(0, 1)));
        m.setInputChars(readChars);
        m.setStackChars(writeChars);

        m.addTransition(0, 'a', null, 'a', 0);

        m.addTransition(0, 'b', 'a', null, 1);

        m.addTransition(1, 'b', 'a', null, 1);

        return m;
    }

    @AfterClass
    public static void printPoints() {
        int sumPoints = 0;
        if (mealyBasics) {
            sumPoints += mealyPoints;
        }
        if (pdaBasics) {
            sumPoints += pdaPoints;
        }

        if (!mealyBasics) {
            System.out.println("Basischecks Mealy nicht erfüllt");
        }

        if (!pdaBasics) {
            System.out.println("Basischecks PDA nicht erfüllt");
        }

        System.out.println("Punkte: " + sumPoints);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMelayNumStates1() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(-1);

        mealyBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMelayNumStates2() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(0);

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayNumStates3() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.getNumStates();

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayInitStateNoStates() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setInitialState(2);

        mealyBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMelayInitStateNotValid1() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setInitialState(-1);

        mealyBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMelayInitStateNotValid2() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setInitialState(6);

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayTransitionNoStates() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setReadChars(readChars);
        mealy.setWriteChars(writeChars);

        mealy.addTransition(0, 'a', 'b', 1);

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayTransitionNoReadChars() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setWriteChars(writeChars);

        mealy.addTransition(0, 'a', 'b', 1);

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayTransitionNoWriteChars() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setReadChars(readChars);

        mealy.addTransition(0, 'a', 'b', 1);

        mealyBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMelayTransitionStateNotValid() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setReadChars(readChars);
        mealy.setWriteChars(writeChars);

        mealy.addTransition(0, 'a', 'b', 5);

        mealyBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMelayTransitionReadCharNotValid() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setReadChars(readChars);
        mealy.setWriteChars(writeChars);

        mealy.addTransition(0, 'z', 'b', 1);

        mealyBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMelayTransitionWriteCharNotValid() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setReadChars(readChars);
        mealy.setWriteChars(writeChars);

        mealy.addTransition(0, 'a', 'z', 1);

        mealyBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMelayTransitionNonDeterministic() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setReadChars(readChars);
        mealy.setWriteChars(writeChars);

        mealy.addTransition(0, 'a', 'b', 1);
        mealy.addTransition(0, 'a', 'b', 2);

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayToMooreNoStates() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setReadChars(readChars);
        mealy.setWriteChars(writeChars);

        mealy.toMoore();

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayToMooreNoReadChars() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setWriteChars(writeChars);

        mealy.toMoore();

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayToMooreNoWriteChars() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setReadChars(readChars);

        mealy.toMoore();

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayMinimizeNoStates() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setReadChars(readChars);
        mealy.setWriteChars(writeChars);

        mealy.minimize();

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayMinimizeNoReadChars() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setWriteChars(writeChars);

        mealy.minimize();

        mealyBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testMelayMinimizeNoWriteChars() {
        Mealy mealy = factory.getEmptyMealy();
        mealy.setNumStates(5);
        mealy.setReadChars(readChars);

        mealy.minimize();

        mealyBasics = false;
    }

    @Test
    public void testMealyProcudes1() {
        Mealy m = mealy1();

        assertEquals("", m.produced(""));
        assertEquals("bcabcabca", m.produced("abcabcabc"));
        assertEquals(null, m.produced("abcabcabc01"));

        mealyPoints += 1;
    }

    @Test
    public void testMealyProcudes2() {
        Mealy m = mealy2();

        assertEquals("0", m.produced("|"));
        assertEquals("0", m.produced("0000000|"));
        assertEquals("1", m.produced("0101010|"));
        assertEquals(null, m.produced("0101010||"));

        mealyPoints += 1;
    }

    @Test
    public void testMealyProcudes3() {
        Mealy m = mealy3();

        assertEquals("0000000", m.produced("00000000"));
        assertEquals("0101010", m.produced("01010101"));

        mealyPoints += 1;
    }

    @Test
    public void testMealyProcudes4() {
        Mealy m = mealy4();

        assertEquals("10111111", m.produced("00000000"));
        assertEquals("11101110", m.produced("01010101"));

        mealyPoints += 1;
    }

    @Test
    public void testMealyToMoore1() {
        Mealy m = mealy1();

        Mealy moore = m.toMoore();

        assertEquals("", m.produced(""));
        assertEquals("bcabcabca", moore.produced("abcabcabc"));
        assertEquals(null, moore.produced("abcabcabc01"));
        assertTrue(isDeterministic(moore));
        assertTrue(isMoore(moore));
        assertTrue(m != moore);

        mealyPoints += 1;
    }

    @Test
    public void testMealyToMoore2() {
        Mealy m = mealy2();

        Mealy moore = m.toMoore();

        assertEquals("0", m.produced("|"));
        assertEquals("0", moore.produced("0000000|"));
        assertEquals("1", moore.produced("0101010|"));
        assertEquals(null, m.produced("0101010||"));
        assertTrue(isDeterministic(moore));
        assertTrue(isMoore(moore));
        assertTrue(m != moore);

        mealyPoints += 1;
    }

    @Test
    public void testMealyToMoore3() {
        Mealy m = mealy3();

        Mealy moore = m.toMoore();

        assertEquals("0000000", moore.produced("00000000"));
        assertEquals("0101010", moore.produced("01010101"));
        assertTrue(isDeterministic(moore));
        assertTrue(isMoore(moore));
        assertTrue(m != moore);

        mealyPoints += 1;
    }

    @Test
    public void testMealyToMoore4() {
        Mealy m = mealy4();

        Mealy moore = m.toMoore();

        assertEquals("10111111", moore.produced("00000000"));
        assertEquals("11101110", moore.produced("01010101"));
        assertTrue(isDeterministic(moore));
        assertTrue(isMoore(moore));
        assertTrue(m != moore);

        mealyPoints += 1;
    }

    @Test
    public void testMealyMinimize1() {
        Mealy m = mealy1();

        Mealy minimized = m.minimize();

        assertEquals("", m.produced(""));
        assertEquals("bcabcabca", minimized.produced("abcabcabc"));
        assertEquals(null, minimized.produced("abcabcabc01"));
        assertEquals(1, minimized.getNumStates());
        assertTrue(m != minimized);

        mealyPoints += 1;
    }


    /************************************************************************/
    /******************************* PDA Part *******************************/

    @Test
    public void testMealyMinimize2() {
        Mealy m = mealy2();

        Mealy minimized = m.minimize();

        assertEquals("0", m.produced("|"));
        assertEquals("0", minimized.produced("0000000|"));
        assertEquals("1", minimized.produced("0101010|"));
        assertEquals(null, m.produced("0101010||"));
        assertEquals(3, minimized.getNumStates());
        assertTrue(m != minimized);

        mealyPoints += 1;
    }

    @Test
    public void testMealyMinimize3() {
        Mealy m = mealy3();

        Mealy minimized = m.minimize();

        assertEquals("0000000", minimized.produced("00000000"));
        assertEquals("0101010", minimized.produced("01010101"));
        assertEquals(3, minimized.getNumStates());
        assertTrue(m != minimized);

        mealyPoints += 1;
    }

    @Test
    public void testMealyMinimize4() {
        Mealy m = mealy4();

        Mealy minimized = m.minimize();

        assertEquals("10111111", minimized.produced("00000000"));
        assertEquals("11101110", minimized.produced("01010101"));
        assertEquals(3, minimized.getNumStates());
        assertTrue(m != minimized);

        mealyPoints += 1;
    }

    private boolean isMoore(Mealy mealy) {
        for (int i = 0; i < mealy.getNumStates(); i++) {
            int i2 = i;
            Set<Transition> transitions = mealy.getTransitions().stream().filter(t -> t.getToState() == i2)
                    .collect(Collectors.toSet());

            int numCharsWrite = transitions.stream().map(t -> t.getCharWrite()).collect(Collectors.toSet()).size();

            if (numCharsWrite > 1) {
                return false;
            }
        }

        return true;
    }

    private boolean isDeterministic(Mealy mealy) {
        for (int i = 0; i < mealy.getNumStates(); i++) {
            int i2 = i;
            Set<Transition> transitions = mealy.getTransitions().stream().filter(t -> t.getFromState() == i2)
                    .collect(Collectors.toSet());

            int numChars = transitions.stream().map(t -> t.getCharRead()).collect(Collectors.toSet()).size();

            if (numChars < transitions.size()) {
                return false;
            }
        }

        return true;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPDANumStates1() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(-1);

        pdaBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPDANumStates2() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(0);

        pdaBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testPDAInitStateNoStates() {
        PDA pda = factory.getEmptyPDA();
        pda.setInitialState(2);

        pdaBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPDAInitStateNotValid1() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(5);
        pda.setInitialState(-1);

        pdaBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPDAInitStateNotValid2() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(5);
        pda.setInitialState(6);

        pdaBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testPDATransitionNoStates() {
        PDA pda = factory.getEmptyPDA();
        pda.setInputChars(readChars);
        pda.setStackChars(writeChars);

        pda.addTransition(0, 'a', 'a', 'b', 1);

        pdaBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testPDATransitionNoInputChars() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(5);
        pda.setStackChars(writeChars);

        pda.addTransition(0, 'a', 'a', 'b', 1);

        pdaBasics = false;
    }

    @Test(expected = IllegalStateException.class)
    public void testPDATransitionNoStackChars() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(5);
        pda.setInputChars(readChars);

        pda.addTransition(0, 'a', 'a', 'b', 1);

        pdaBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPDATransitionStateNotValid() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(5);
        pda.setInputChars(readChars);
        pda.setStackChars(writeChars);

        pda.addTransition(0, 'a', 'a', 'b', 5);

        pdaBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPDATransitionInputCharNotValid() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(5);
        pda.setInputChars(readChars);
        pda.setStackChars(writeChars);

        pda.addTransition(0, 'z', 'a', 'b', 1);

        pdaBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPDATransitionStackReadCharNotValid() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(5);
        pda.setInputChars(readChars);
        pda.setStackChars(writeChars);

        pda.addTransition(0, 'a', 'z', 'a', 1);

        pdaBasics = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPDATransitionStackWriteCharNotValid() {
        PDA pda = factory.getEmptyPDA();
        pda.setNumStates(5);
        pda.setInputChars(readChars);
        pda.setStackChars(writeChars);

        pda.addTransition(0, 'a', 'a', 'z', 1);

        pdaBasics = false;
    }

    @Test
    public void testPDAAccepts1() {
        PDA pda = pda1();

        assertTrue(pda.accepts(""));
        assertTrue(pda.accepts("aa"));
        assertTrue(pda.accepts("aaaa"));
        assertTrue(pda.accepts("aaaaaa"));

        assertFalse(pda.accepts("a"));
        assertFalse(pda.accepts("aaa"));
        assertFalse(pda.accepts("aaaaa"));

        assertFalse(pda.accepts("aab"));
        assertFalse(pda.accepts("baa"));

        pdaPoints += 1;
    }

    @Test
    public void testPDAAccepts2() {
        PDA pda = pda2();

        assertTrue(pda.accepts(""));
        assertTrue(pda.accepts("aa"));
        assertTrue(pda.accepts("aabb"));
        assertTrue(pda.accepts("abba"));

        assertFalse(pda.accepts("a"));
        assertFalse(pda.accepts("aaa"));
        assertFalse(pda.accepts("aaaaa"));

        assertFalse(pda.accepts("aab"));
        assertFalse(pda.accepts("baa"));

        pdaPoints += 1;
    }

    @Test
    public void testPDAAccepts3() {
        PDA pda = pda3();

        assertTrue(pda.accepts("a"));
        assertTrue(pda.accepts("aaa"));
        assertTrue(pda.accepts("abcba"));
        assertTrue(pda.accepts("abcacba"));

        assertFalse(pda.accepts("aa"));
        assertFalse(pda.accepts("abccba"));
        assertFalse(pda.accepts("ccaabb"));

        pdaPoints += 1;
    }

    @Test
    public void testPDAAccepts4() {
        PDA pda = pda4();

        assertTrue(pda.accepts("ab"));
        assertTrue(pda.accepts("aabb"));
        assertTrue(pda.accepts("aaabbb"));

        assertFalse(pda.accepts("a"));
        assertFalse(pda.accepts("aabbab"));
        assertFalse(pda.accepts("ba"));

        pdaPoints += 1;
    }

    @Test
    public void testPDADet() {
        assertFalse(pda1().isDPDA());
        assertFalse(pda2().isDPDA());
        assertFalse(pda3().isDPDA());
        assertTrue(pda4().isDPDA());

        pdaPoints += 2;
    }

    @Test
    public void testPDAUnion1() {
        PDA pda1 = pda1();
        PDA pda2 = pda2();

        PDA pda = pda1.union(pda2);

        assertTrue(pda1 != pda);
        assertTrue(pda2 != pda);

        assertTrue(pda.accepts(""));
        assertTrue(pda.accepts("aa"));
        assertTrue(pda.accepts("aaaa"));
        assertTrue(pda.accepts("aaaaaa"));

        assertTrue(pda.accepts(""));
        assertTrue(pda.accepts("aa"));
        assertTrue(pda.accepts("aabb"));
        assertTrue(pda.accepts("abba"));

        assertFalse(pda.accepts("c"));
        assertFalse(pda.accepts("aba"));
        assertFalse(pda.accepts("aab"));

        pdaPoints += 1;
    }

    @Test
    public void testPDAUnion2() {
        PDA pda1 = pda1();
        PDA pda2 = pda3();

        PDA pda = pda1.union(pda2);

        assertTrue(pda1 != pda);
        assertTrue(pda2 != pda);

        assertTrue(pda.accepts(""));
        assertTrue(pda.accepts("aa"));
        assertTrue(pda.accepts("aaaa"));
        assertTrue(pda.accepts("aaaaaa"));

        assertTrue(pda.accepts("a"));
        assertTrue(pda.accepts("aaa"));
        assertTrue(pda.accepts("abcba"));
        assertTrue(pda.accepts("abcacba"));

        assertFalse(pda.accepts("cc"));
        assertFalse(pda.accepts("abba"));

        pdaPoints += 1;
    }

    @Test
    public void testPDAUnion3() {
        PDA pda1 = pda1();
        PDA pda2 = pda4();

        PDA pda = pda1.union(pda2);

        assertTrue(pda1 != pda);
        assertTrue(pda2 != pda);

        assertTrue(pda.accepts(""));
        assertTrue(pda.accepts("aa"));
        assertTrue(pda.accepts("aaaa"));
        assertTrue(pda.accepts("aaaaaa"));

        assertTrue(pda.accepts("ab"));
        assertTrue(pda.accepts("aabb"));
        assertTrue(pda.accepts("aaabbb"));

        assertFalse(pda.accepts("cc"));
        assertFalse(pda.accepts("abba"));

        pdaPoints += 1;
    }

    @Test
    public void testPDAUnion4() {
        PDA pda1 = pda3();
        PDA pda2 = pda4();

        PDA pda = pda1.union(pda2);

        assertTrue(pda1 != pda);
        assertTrue(pda2 != pda);

        assertTrue(pda.accepts("a"));
        assertTrue(pda.accepts("aaa"));
        assertTrue(pda.accepts("abcba"));
        assertTrue(pda.accepts("abcacba"));

        assertTrue(pda.accepts("ab"));
        assertTrue(pda.accepts("aabb"));
        assertTrue(pda.accepts("aaabbb"));

        assertFalse(pda.accepts("cc"));
        assertFalse(pda.accepts("abba"));

        pdaPoints += 1;
    }

    @Test
    public void testPDAAppend1() {
        PDA pda1 = pda1();
        PDA pda2 = pda2();

        PDA pda = pda1.append(pda2);

        assertTrue(pda1 != pda);
        assertTrue(pda2 != pda);

        assertTrue(pda.accepts(""));
        assertTrue(pda.accepts("aa"));
        assertTrue(pda.accepts("aaaa"));
        assertTrue(pda.accepts("aaaaaa"));
        assertTrue(pda.accepts("aa"));
        assertTrue(pda.accepts("aabb"));
        assertTrue(pda.accepts("abba"));
        assertTrue(pda.accepts("aaaaaabb"));
        assertTrue(pda.accepts("aaabba"));

        assertFalse(pda.accepts("c"));
        assertFalse(pda.accepts("aba"));
        assertFalse(pda.accepts("aab"));

        pdaPoints += 1;
    }

    @Test
    public void testPDAAppend2() {
        PDA pda1 = pda1();
        PDA pda2 = pda3();

        PDA pda = pda1.append(pda2);

        assertTrue(pda1 != pda);
        assertTrue(pda2 != pda);

        assertTrue(pda.accepts("aaaba"));
        assertTrue(pda.accepts("aaaaabcba"));
        assertTrue(pda.accepts("a"));
        assertTrue(pda.accepts("aaa"));
        assertTrue(pda.accepts("abcba"));
        assertTrue(pda.accepts("abcacba"));

        assertFalse(pda.accepts(""));
        assertFalse(pda.accepts("cc"));
        assertFalse(pda.accepts("abba"));

        pdaPoints += 1;
    }

    @Test
    public void testPDAAppend3() {
        PDA pda1 = pda1();
        PDA pda2 = pda4();

        PDA pda = pda1.append(pda2);

        assertTrue(pda1 != pda);
        assertTrue(pda2 != pda);

        assertTrue(pda.accepts(""));
        assertTrue(pda.accepts("aa"));
        assertTrue(pda.accepts("aaab"));
        assertTrue(pda.accepts("aaaabb"));
        assertTrue(pda.accepts("aaaa"));

        assertFalse(pda.accepts("cc"));
        assertFalse(pda.accepts("abba"));

        pdaPoints += 1;
    }

    /************************************************************************/
    /******************************* CFG->PDF *******************************/

    @Test
    public void testPDAAppend4() {
        PDA pda1 = pda3();
        PDA pda2 = pda4();

        PDA pda = pda1.append(pda2);

        assertTrue(pda1 != pda);
        assertTrue(pda2 != pda);

        assertTrue(pda.accepts("aab"));
        assertTrue(pda.accepts("aaaaabb"));
        assertTrue(pda.accepts("abcba"));
        assertTrue(pda.accepts("abcacba"));

        assertFalse(pda.accepts("cc"));
        assertFalse(pda.accepts("abba"));

        pdaPoints += 1;
    }

    /************************************************************************/

    @Test
    public void testCFGToPDA1() {
        String cfg = "S→aSa|bSb|cSc|a|b|c";
        Set<String> set = new HashSet<>();
        set.add(cfg);

        PDA pda = factory.getPDAFromCFG('S', set);

        assertTrue(pda.accepts("a"));
        assertTrue(pda.accepts("aaa"));
        assertTrue(pda.accepts("aba"));
        assertTrue(pda.accepts("abcba"));
        assertTrue(pda.accepts("aabcbaa"));

        assertFalse(pda.accepts(""));
        assertFalse(pda.accepts("aa"));
        assertFalse(pda.accepts("ababcb"));
        assertFalse(pda.accepts("abba"));
        assertFalse(pda.accepts("aabac"));

        pdaPoints += 2;
    }

    @Test
    public void testCFGToPDA2() {
        String cfg1 = "S→aS|bS|T";
        String cfg2 = "T→cT|c";
        Set<String> set = new HashSet<>();
        set.add(cfg1);
        set.add(cfg2);

        PDA pda = factory.getPDAFromCFG('S', set);

        assertTrue(pda.accepts("c"));
        assertTrue(pda.accepts("ac"));
        assertTrue(pda.accepts("abc"));
        assertTrue(pda.accepts("bac"));
        assertTrue(pda.accepts("babaac"));
        assertTrue(pda.accepts("cccc"));
        assertTrue(pda.accepts("acccc"));
        assertTrue(pda.accepts("babcccc"));

        assertFalse(pda.accepts(""));
        assertFalse(pda.accepts("aa"));
        assertFalse(pda.accepts("caa"));
        assertFalse(pda.accepts("bab"));
        assertFalse(pda.accepts("babaa"));
        assertFalse(pda.accepts("cabac"));

        pdaPoints += 2;
    }
}
