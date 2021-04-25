package org.soundtrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String readFromInputStream(InputStream stream)
        throws IOException
    {
        Pattern exit = Pattern.compile(Pattern.quote("[Ctrl+C] exit"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder s = new StringBuilder();
        String next;
        do {
            next = reader.readLine();
            s.append(next);
            s.append("\n");
        } while (next != null && !exit.matcher(next).find());
        return s.toString();
    }

    public static List<String> allMatches(Pattern regex, String input) {
        Matcher matcher = regex.matcher(input);
        ArrayList<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public static List<String> allMatches(String regex, String input) {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        ArrayList<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public static void padFromSelf(List l, int targetSize) {
        while (l.size() > targetSize) {
            l.remove(0);
        }
        while (l.size() < targetSize) {
            l.add(l.get(0));
        }
    }
}
