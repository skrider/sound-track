package org.soundtrack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {
        Pattern reader = Pattern.compile("\\d\\d:\\d\\d(?=\\))");
        Pattern reader2 = Pattern.compile("a");
        String test = "Track   Space Song (00:01 / 05:20)Artist  Beach HouseAlbum   Depression CherryStatus  Playing (80% volume)null";

        Matcher m = reader.matcher(test);
        m.find();
        System.out.println(m.group().substring(3, 5));
    }
}
