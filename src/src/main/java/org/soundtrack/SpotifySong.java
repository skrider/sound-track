package org.soundtrack;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifySong extends Song {

    protected static final Pattern durationReader = Pattern.compile("\\d\\d:\\d\\d(?=\\))");
    protected static final Pattern sessionNotStartedError =
            Pattern.compile(Pattern.quote("error"));
    protected static final Pattern songNotFoundError =
            Pattern.compile(Pattern.quote("No results found for \""));

    public SpotifySong(String name, Timer t) {
        super(name, t);
    }

    public SpotifySong(String name, int volumeLevel, Timer t) {
        super(name, volumeLevel, t);
    }

    public SpotifySong(String name, int volumeLevel, Timer t, long duration) {
        super(name, volumeLevel, t, duration);
    }

    @Override
    public void start()
        throws IOException, SoundtrackException
    {
        Runtime runtime = Runtime.getRuntime();
        String s = Utils.readFromInputStream(
                runtime.exec("spotify play -v " + name)
                       .getInputStream());
        if (sessionNotStartedError.matcher(s).find()) {
            throw new SoundtrackException(s);
        } else if (songNotFoundError.matcher(s).find()) {
            throw new SoundtrackException(s);
        } else if (duration == 0){
            Matcher m = durationReader.matcher(s);
            m.find();
            String digits = m.group();
            setDuration(
                    (60 * (long) Integer.parseInt(digits.substring(0, 2))
                            + Integer.parseInt(digits.substring(3, 5))) * 1000);
        }
        super.start();
    }

    @Override
    public void setVolumeLevel()
        throws IOException
    {
        super.setVolumeLevel();
        if (volumeLevel != -1) {
            Runtime.getRuntime().exec("spotify volume to " + volumeLevel);
        }
    }

    public static SpotifySong fromKeywordAtRandom(String keyword, int volumeLevel, Timer t, int count)
        throws IOException, SoundtrackException
    {
        Pattern regex = Pattern.compile("(?<=\\d\\h\\h).*(?=\\h\\h)");
        List<String> matches = Utils.allMatches(regex, search(keyword));
        if (matches.isEmpty()) {
            throw new SoundtrackException("No songs found.");
        }
        Utils.padFromSelf(matches, count);
        Collections.shuffle(matches);
        return from(matches, volumeLevel, t);
    }

    public static SpotifySong fromArtistAtRandom(String artist, int volumeLevel, Timer t, int count)
            throws IOException
    {
        Pattern regex = Pattern.compile(
                "(?<=\\d\\h\\h)(\\w\\h|\\h\\w|\\w\\w)*(?=\\h\\h[^\\n]*"+Pattern.quote(artist)+")");
        List<String> matches = Utils.allMatches(regex, search(artist));
        Utils.padFromSelf(matches, count);
        Collections.shuffle(matches);
        return from(matches, volumeLevel, t);
    }

    public static SpotifySong from(List<String> names, int volumeLevel, Timer t) {
        SpotifySong head = null;
        SpotifySong prev = null;
        for (String name : names) {
            head = new SpotifySong(name, volumeLevel, t);
            head.setNext(prev);
            prev = head;
        }
        return head;
    }

    public static SpotifySong from(List<String> names, int volumeLevel, Timer t, int count) {
        Utils.padFromSelf(names, count);
        return from(names, volumeLevel, t);
    }

    public static String search(String keyword)
            throws IOException
    {
        Process p = Runtime.getRuntime().exec("spotify search " + keyword);
        String result = Utils.readFromInputStream(p.getInputStream());
        p.destroy();
        System.out.println(result);
        return result;
    }

}
