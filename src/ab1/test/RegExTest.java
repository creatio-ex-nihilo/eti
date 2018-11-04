package ab1.test;

import ab1.RegEx;
import ab1.impl.Pauritsch.RegExImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


/**
 * JUnit-Tests
 *
 * @author Raphael Wigoutschnigg
 */
public class RegExTest {
    private final RegEx sol = new RegExImpl();

    // 1 Pts
    @Test
    public void testRegex1() {
        String regEx = sol.getRegex1();

        Assert.assertEquals(true, Pattern.matches(regEx, "ab"));
        Assert.assertEquals(true, Pattern.matches(regEx, "abb"));
        Assert.assertEquals(true, Pattern.matches(regEx, "aaab"));
        Assert.assertEquals(true, Pattern.matches(regEx, "aabb"));
        Assert.assertEquals(true, Pattern.matches(regEx, "caabbd"));
        Assert.assertEquals(true, Pattern.matches(regEx, "cabd"));

        Assert.assertEquals(false, Pattern.matches(regEx, ""));
        Assert.assertEquals(false, Pattern.matches(regEx, "a"));
        Assert.assertEquals(false, Pattern.matches(regEx, "b"));
        Assert.assertEquals(false, Pattern.matches(regEx, "ca"));
        Assert.assertEquals(false, Pattern.matches(regEx, "cb"));
        Assert.assertEquals(false, Pattern.matches(regEx, "ac"));
        Assert.assertEquals(false, Pattern.matches(regEx, "bc"));
    }

    // 1 Pts
    @Test
    public void testRegex2() {
        String regEx = sol.getRegex2();

        Assert.assertEquals(true, Pattern.matches(regEx, "abc"));
        Assert.assertEquals(true, Pattern.matches(regEx, "def"));
        Assert.assertEquals(true, Pattern.matches(regEx, "abcdef"));
        Assert.assertEquals(true, Pattern.matches(regEx, "gabgabcgdegde"));
        Assert.assertEquals(true, Pattern.matches(regEx, "abcabcabc"));
        Assert.assertEquals(true, Pattern.matches(regEx, "fedcbabcdef"));

        Assert.assertEquals(false, Pattern.matches(regEx, ""));
        Assert.assertEquals(false, Pattern.matches(regEx, "ab"));
        Assert.assertEquals(false, Pattern.matches(regEx, "abb"));
        Assert.assertEquals(false, Pattern.matches(regEx, "cba"));
        Assert.assertEquals(false, Pattern.matches(regEx, "deab"));
        Assert.assertEquals(false, Pattern.matches(regEx, "ABC"));
        Assert.assertEquals(false, Pattern.matches(regEx, "DEF"));
    }

    // 1 Pts
    @Test
    public void testRegex3() {
        String regEx = sol.getRegex3();

        Assert.assertEquals(true, Pattern.matches(regEx, "abcabcabc"));
        Assert.assertEquals(true, Pattern.matches(regEx, "abcdabcdabcd"));
        Assert.assertEquals(true, Pattern.matches(regEx, "abc abc abc"));
        Assert.assertEquals(true, Pattern.matches(regEx, "def abc def abc def abc def"));

        Assert.assertEquals(false, Pattern.matches(regEx, ""));
        Assert.assertEquals(false, Pattern.matches(regEx, "ab"));
        Assert.assertEquals(false, Pattern.matches(regEx, "abb"));
        Assert.assertEquals(false, Pattern.matches(regEx, "ab c"));
        Assert.assertEquals(false, Pattern.matches(regEx, "abc abc"));
        Assert.assertEquals(false, Pattern.matches(regEx, "def abc def abc"));
        Assert.assertEquals(false, Pattern.matches(regEx, "abc abc ab c def"));
    }

    // 1 Pts
    @Test
    public void testRegexURL() {
        String regEx = sol.getRegexURL();

        Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at"));
        Assert.assertEquals(true, Pattern.matches(regEx, "ftp://aau.at"));
        Assert.assertEquals(true, Pattern.matches(regEx, "https://campus.aau.at"));
        Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80"));
        Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/"));
        Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/login"));
        Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/login.intern"));
        Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/login-intern"));
        Assert.assertEquals(true, Pattern.matches(regEx, "http://www.aau.at:80/sub/sub.sub/sub-sub-sub/"));

        Assert.assertEquals(false, Pattern.matches(regEx, "ssh://www.aau.at"));
        Assert.assertEquals(false, Pattern.matches(regEx, "://aau.at"));
        Assert.assertEquals(false, Pattern.matches(regEx, "http:/campus.aau.at"));
        Assert.assertEquals(false, Pattern.matches(regEx, "http//campus.aau.at"));
        Assert.assertEquals(false, Pattern.matches(regEx, "//campus.aau.at"));
        Assert.assertEquals(false, Pattern.matches(regEx, "w-w-w.aau.at"));
        Assert.assertEquals(false, Pattern.matches(regEx, "www.aau.ataeh"));
        Assert.assertEquals(false, Pattern.matches(regEx, "www.auto"));
        Assert.assertEquals(false, Pattern.matches(regEx, "www"));
        Assert.assertEquals(false, Pattern.matches(regEx, "at"));
        Assert.assertEquals(false, Pattern.matches(regEx, ".www.aau.at"));
        Assert.assertEquals(false, Pattern.matches(regEx, "http://www.aau.at:-80"));
        Assert.assertEquals(false, Pattern.matches(regEx, "http://www.aau.at:80login"));
        Assert.assertEquals(false, Pattern.matches(regEx, "http://www.aau.at:80a/login.intern"));
    }

    // 1 Pts
    @Test
    public void testMatch1() {
        String regEx = sol.multiMatch1();

        testGoodList(regEx,
                Arrays.asList("afoot", "catfoot", "dogfoot", "fanfoot", "foody", "foolery", "foolish", "fooster",
                        "footage", "foothot", "footle", "footpad", "footway", "hotfoot", "jawfoot", "mafoo", "nonfood",
                        "padfoot", "prefool", "sfoot", "unfool"));
        testBadList(regEx,
                Arrays.asList("Atlas", "Aymoro", "Iberic", "Mahran", "Ormazd", "Silipan", "altared", "chandoo",
                        "crenel", "crooked", "fardo", "folksy", "forest", "hebamic", "idgah", "manlike", "marly",
                        "palazzi", "sixfold", "tarrock", "unfold"));
    }

    // 1 Pts
    @Test
    public void testMatch2() {
        String regEx = sol.multiMatch2();

        testGoodList(regEx,
                Arrays.asList("Mick", "Rick", "allocochick", "backtrick", "bestick", "candlestick", "counterprick",
                        "heartsick", "lampwick", "lick", "lungsick", "potstick", "quick", "rampick", "rebrick",
                        "relick", "seasick", "slick", "tick", "unsick", "upstick"));
        testBadList(regEx,
                Arrays.asList("Kickapoo", "Nickneven", "Rickettsiales", "billsticker", "borickite", "chickell",
                        "fickleness", "finickily", "kilbrickenite", "lickpenny", "mispickel", "quickfoot", "quickhatch",
                        "ricksha", "rollicking", "slapsticky", "snickdrawing", "sunstricken", "tricklingly", "unlicked",
                        "unnickeled"));
    }

    // 1 Pts
    @Test
    public void testMatch3() {
        String regEx = sol.multiMatch3();

        testGoodList(regEx, Arrays.asList("fu", "tofu", "snafu"));
        testBadList(regEx, Arrays.asList("futz", "fusillade", "functional", "discombobulated"));
    }

    // 1 Pts
    @Test
    public void testMatch4() {
        String regEx = sol.multiMatch4();

        testGoodList(regEx,
                Arrays.asList("\\w+", "\\w*", "a{1}|a{3}|a{9}", "\\???", "a{1,8}", "-", "-+-+-+-+-+", "(\\w+?)\\7",
                        "(\\ufacdf)*?", "[A-Z0-9]+", "[a-fk-ov-z]*", "(?:[aeiou]+)\\1+", "[a\\-z\\-9]", "(\\001)\\1",
                        "(\\2)\\1"));
        testBadList(regEx,
                Arrays.asList("\\w++", "\\w**", "a({1}|{3}|{9})", "???", "a{8,1}", "+", "+-+-+-+-+-", "(\\w?+)\\7",
                        "(\\ufacdf)?*", "[Z-A0-9]+", "[f-bp-iy-t]*", "(:?[abced]+)\\1*", "[a-z-9]", "(\\1)\\1",
                        "(\\8)\\2"));

    }

    private void testGoodList(String regEx, List<String> textList) {
        Pattern p = Pattern.compile(regEx);

        for (String w : textList) {
            if (!p.matcher(w).find()) {
                Assert.assertEquals(w + " muss matchen", "matcht aber nicht");
            }
        }
    }

    private void testBadList(String regEx, List<String> textList) {
        Pattern p = Pattern.compile(regEx);

        for (String w : textList) {
            if (p.matcher(w).find()) {
                Assert.assertEquals(w + " darf nicht matchen", "matcht aber");
            }
        }
    }
}
