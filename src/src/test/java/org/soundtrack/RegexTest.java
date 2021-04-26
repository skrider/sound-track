package org.soundtrack;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {
        Pattern reader = Pattern.compile("[dh]");
        Pattern regex = Pattern.compile("(?<=\\d\\h\\h).*(?=\\h\\h)");
        Pattern regex2 = Pattern.compile("(?<=\\h)\\d?\\d?:?\\d?\\d:\\d\\d");
        Pattern wordDetector = Pattern.compile("(?<=[\\[,]\\s)(?:\\{.*})(?=\\s?[\\],])");
        Pattern partial1 = Pattern.compile("(?<=\\[\\s)\\w");
        Pattern partial = Pattern.compile("[\\[,]");
        String test = "{\n" +
                "    rep: 1\n" +
                "    volume: 80\n" +
                "    shuffle: true\n" +
                "    tracks: [\n" +
                "        space song 11:11:20,\n" +
                "        { rep: 2 volume: 50 shuffle: false tracks: [ sunflower , take what you want ] },\n" +
                "        rockstar\n" +
                "    ]\n" +
                "}";
        Matcher m = wordDetector.matcher(Utils.clean(test));
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
