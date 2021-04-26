package org.soundtrack;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

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

    static byte[] readContents(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    static String readContentsAsString(File file) {
        return new String(readContents(file), StandardCharsets.UTF_8);
    }

    static String clean(String s) {
        Pattern p1 = Pattern.compile("\\s"); // turn all whitespace into spaces
        Pattern p2 = Pattern.compile("\\s\\s"); // ensure no two spaces are consecutive
        Pattern p3 = Pattern.compile("(?<=[\\[,])(?=[^\\s])"); // ensure all delimiters are followed by commas
        Pattern p4 = Pattern.compile("(?<=[^\\s])(?=[,\\]])"); // ensure all delimiters are preceded by commas
        Pattern p5 = Pattern.compile(",\\s(?=[,\\]])"); // ensure that there are no empty slots
        Matcher m1 = p1.matcher(s);
        s = m1.replaceAll(" ");
        Matcher m2 = p2.matcher(s);
        while (m2.find()) {
            s = m2.replaceAll(" ");
            m2 = p2.matcher(s);
        }
        Matcher m3 = p3.matcher(s);
        s = m3.replaceAll(" ");
        Matcher m4 = p4.matcher(s);
        s = m4.replaceAll(" ");
        Matcher m5 = p5.matcher(s);
        s = m5.replaceAll("");
        return s;
    }

    public static long parseTime(String target) {
        Matcher hours = Pattern.compile("\\d?\\d(?=:\\d\\d:\\d\\d)").matcher(target);
        Matcher minutes = Pattern.compile("(?<=\\d?\\d?:?)\\d\\d(?=:\\d\\d)").matcher(target);
        Matcher seconds = Pattern.compile("(?<=\\d?\\d?:?\\d?\\d:)\\d\\d").matcher(target);

        long time = 0;

        if (seconds.find()) {
            time += Long.parseLong(seconds.group()) * 1000;
        }
        if (minutes.find()) {
            time += Long.parseLong(minutes.group()) * 60 * 1000;
        }
        if (hours.find()) {
            time += Long.parseLong(hours.group()) * 60 * 60 * 1000;
        }

        return time;
    }
}
