package ab1.test;

import ab1.TM;
import ab1.TM.Movement;
import ab1.TM.TMConfig;
import ab1.impl.Pauritsch.TMImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class TMTest {

    TM getMachineOneTape() {
        TM tm = new TMImpl()
                .setNumberOfTapes(1)
                .setNumberOfStates(4)
                .setHaltState(0)
                .setSymbols(new HashSet<>(Arrays.asList('a', '#')))
                .setInitialState(3)
                .setInitialTapeContent(0, "#aaaaa".toCharArray());

        tm.addTransition(1, 0, 'a', 2, 0, '#', Movement.Stay, Movement.Stay);
        tm.addTransition(1, 0, '#', 0, 0, '#', Movement.Stay, Movement.Stay);
        tm.addTransition(2, 0, '#', 1, 0, '#', Movement.Left, Movement.Stay);
        tm.addTransition(3, 0, '#', 1, 0, '#', Movement.Left, Movement.Stay);

        return tm;
    }

    TM getMachineTwoTapes() {
        int h = 0;
        int z1 = 1;
        int z2 = 2;
        int z3 = 3;
        int z4 = 4;
        int z5 = 5;
        int z6 = 6;
        int z7 = 7;
        int z0 = 8;

        TM tm = new TMImpl()
                .setNumberOfTapes(2)
                .setSymbols(new HashSet<>(Arrays.asList('a', 'b', 'c', '#')))
                .setNumberOfStates(9)
                .setHaltState(h)
                .setInitialState(z0)
                .setInitialTapeContent(0, "#abcabc".toCharArray())
                .setInitialTapeContent(1, "#".toCharArray());

        int band1 = 0;
        int band2 = 1;

        /*
         * Kopiere von Band 1 auf Band 2
         */
        tm.addTransition(z0, band1, '#', z1, band1, '#', Movement.Left, Movement.Stay);
        // lese Zeichen von Band 1 und schreibe es auf Band 2. Gehe auf Band 0 nach
        // links
        tm.addTransition(z1, band1, 'a', z2, band2, 'a', Movement.Left, Movement.Stay);
        tm.addTransition(z1, band1, 'b', z2, band2, 'b', Movement.Left, Movement.Stay);
        tm.addTransition(z1, band1, 'c', z2, band2, 'c', Movement.Left, Movement.Stay);
        // Gehe auf Band 2 nach rechts
        tm.addTransition(z2, band2, 'a', z1, band2, 'a', Movement.Right, Movement.Stay);
        tm.addTransition(z2, band2, 'b', z1, band2, 'b', Movement.Right, Movement.Stay);
        tm.addTransition(z2, band2, 'c', z1, band2, 'c', Movement.Right, Movement.Stay);
        // Wechse in Zustand 3 und gehe auf Band 2 nach links
        tm.addTransition(z1, band1, '#', z3, band2, '#', Movement.Stay, Movement.Left);

        /*
         * Gehe auf Band 2 nach links bis zum ersten Zeichen nach dem #
         */
        tm.addTransition(z3, band2, 'a', z3, band2, 'a', Movement.Left, Movement.Stay);
        tm.addTransition(z3, band2, 'b', z3, band2, 'b', Movement.Left, Movement.Stay);
        tm.addTransition(z3, band2, 'c', z3, band2, 'c', Movement.Left, Movement.Stay);
        tm.addTransition(z3, band2, '#', z4, band2, '#', Movement.Right, Movement.Stay);

        // Gehe auf Band 1 einen Schritt nach rechts
        tm.addTransition(z4, band1, '#', z5, band1, '#', Movement.Right, Movement.Stay);

        /*
         * Kopiere von Band 2 auf Band 1
         */
        tm.addTransition(z5, band2, 'a', z6, band1, 'a', Movement.Right, Movement.Stay);
        tm.addTransition(z5, band2, 'b', z6, band1, 'b', Movement.Right, Movement.Stay);
        tm.addTransition(z5, band2, 'c', z6, band1, 'c', Movement.Right, Movement.Stay);
        // Gehe auf Band 1 nach rechts gehen
        tm.addTransition(z6, band1, 'a', z5, band1, 'a', Movement.Right, Movement.Stay);
        tm.addTransition(z6, band1, 'b', z5, band1, 'b', Movement.Right, Movement.Stay);
        tm.addTransition(z6, band1, 'c', z5, band1, 'c', Movement.Right, Movement.Stay);

        // Wechse in Zustand 7 und gehe auf Band 2 nach links
        tm.addTransition(z5, band2, '#', z7, band2, '#', Movement.Left, Movement.Stay);

        /*
         * Lösche Band 2
         */
        tm.addTransition(z7, band2, 'a', z7, band2, '#', Movement.Left, Movement.Stay);
        tm.addTransition(z7, band2, 'b', z7, band2, '#', Movement.Left, Movement.Stay);
        tm.addTransition(z7, band2, 'c', z7, band2, '#', Movement.Left, Movement.Stay);

        tm.addTransition(z7, band2, '#', h, band2, '#', Movement.Right, Movement.Stay);

        return tm;
    }


    //3 Pts
    @Test
    public void testMachineOneTape() {
        TM tm = getMachineOneTape();

        // Maschine hat 1 Band
        Assert.assertEquals(1, tm.getNumberOfTapes());

        System.out.println("test");

        // Lasse die Maschine bis zum Haltezustand laufen
        while (!tm.isHalt()) {
            tm.doNextStep();
        }

        // Prüfe den Bandinhalt
        TMConfig config = tm.getTMConfig(0);
        Assert.assertArrayEquals(new char[0], config.getLeftOfHead());
        Assert.assertEquals('#', config.getBelowHead());
        Assert.assertArrayEquals(new char[0], config.getRightOfHead());

        Assert.assertEquals(config, tm.getTMConfig().get(0));
    }


    //4 Pts
    @Test
    public void testMachineTwoTapes() {
        TM tm = getMachineTwoTapes();

        // Maschine hat 2 Bänder
        Assert.assertEquals(2, tm.getNumberOfTapes());

        // Lasse die Maschine bis zum Haltezustand laufen
        while (!tm.isHalt()) {
            tm.doNextStep();
        }

        // Prüfe den Bandinhalt
        TMConfig config = tm.getTMConfig(0);
        Assert.assertArrayEquals(new char[]{'#', 'c', 'b', 'a', 'c', 'b', 'a'}, config.getLeftOfHead());
        Assert.assertEquals('#', config.getBelowHead());
        Assert.assertArrayEquals(new char[0], config.getRightOfHead());

        config = tm.getTMConfig(1);
        Assert.assertArrayEquals(new char[]{'#'}, config.getLeftOfHead());
        Assert.assertEquals('#', config.getBelowHead());
        Assert.assertArrayEquals(new char[0], config.getRightOfHead());
    }
}
