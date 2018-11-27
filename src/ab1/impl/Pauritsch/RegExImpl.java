package ab1.impl.Pauritsch;

import ab1.RegEx;

public class RegExImpl implements RegEx {

    @Override
    public String getRegex1() {
        return ".*(a+b+).*";
    }

    @Override
    public String getRegex2() {
        return ".*(abc|def)+.*";
    }

    @Override
    public String getRegex3() {
        return ".*(abc)+.*(abc)+.*(abc)+.*";
    }

    @Override
    public String getRegexURL() {
        return "(http|https|ftp)(://)[a-z^äÄöÖüÜß]+(\\.[a-z^äÄöÖüÜß]+)*\\.[a-z^äÄöÖüÜß]{2,3}(:(\\d){0,5})?(/[a-zA-Z0-9\\.\\-/]*)?";
    }

    @Override
    public String multiMatch1() {
        return ".*foo.*";
    }

    @Override
    public String multiMatch2() {
        return "ick$";
    }

    @Override
    public String multiMatch3() {
        return "^(.*fu$)";
    }

    @Override
    public String multiMatch4() {
        return "(?!\\???|\\\\w\\+\\+|\\\\w\\*\\*|a\\(\\{1\\}\\|\\{3\\}\\|\\{9\\}\\))(\\\\w\\+|\\\\w\\*|a{1}|a{3}|a{9}|\\\\?\\?\\?|\\-|\\(\\\\001\\)\\\\1|\\(\\\\2\\)\\\\1)";
    }

}
