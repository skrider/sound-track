package org.soundtrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifySong extends Song {

    protected static final Pattern durationReader = Pattern.compile("\\d\\d:\\d\\d(?=\\))");
    protected static final Pattern sessionNotStartedError =
            Pattern.compile(Pattern.quote("Error: No playback session detected."));
    protected static final Pattern songNotFoundError =
            Pattern.compile(Pattern.quote("No results found for \""));

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
        super.start();
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
    }

    @Override
    public void setVolumeLevel()
        throws IOException
    {
        super.setVolumeLevel();
        Runtime.getRuntime().exec("spotify volume to " + volumeLevel);
    }
}
