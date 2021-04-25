package org.soundtrack;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {
        Pattern reader = Pattern.compile("(?<=\\d\\h\\h)(\\w\\h|\\h\\w|\\w\\w)*(?=\\h\\h[^\\n]*Lemaitre)");
        Pattern regex = Pattern.compile("(?<=\\d\\h\\h).*(?=\\h\\h)");
        String test = "  1  Closer                         Lemaitre, Jennie A.\n" +
                "  2  Eyes Wide Open                 Lemaitre\n" +
                "  3  Higher                         Lemaitre, Maty Noyes\n" +
                "  4  Polygon Dust                   Porter Robinson, Lemaitre\n" +
                "  5  Not Too Late                   Lematre\n" +
                "  6  It's Not This                  Bearson, Lemitre, josh pan";

//        Matcher m = reader.matcher(test);
        List<String> s = Utils.allMatches(reader, test);
        for (String t : s) {
            System.out.println(t);
        }
    }
}
