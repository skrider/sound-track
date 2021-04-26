package org.soundtrack;

import java.io.IOException;
import java.util.regex.Pattern;

public abstract class Song {
    private Timer timer;
    protected String name;
    protected long startTime, duration;
    protected Song next;
    protected int volumeLevel;

    public Song(String name, Timer timer) {
        this.name = name;
        this.timer = timer;
        volumeLevel = -1;
        duration = 0;
    }

    public Song(String name, int volumeLevel, Timer timer) {
        this(name, timer);
        this.volumeLevel = volumeLevel;
    }

    public Song(String name, int volumeLevel, Timer timer, long duration) {
        this(name, volumeLevel, timer);
        this.duration = duration;
    }

    public void setNext(Song next) {
        this.next = next;
    }

    public Song next() {
        return next;
    }

    public boolean hasStarted() {
        return startTime < timer.time();
    }

    public boolean hasFinished() {
        return timer.time() > startTime + duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void start()
            throws IOException, SoundtrackException
    {
        startTime = timer.time();
        this.setVolumeLevel();
    }

    public boolean checkNext() {
        if (hasFinished() && hasStarted()) {
            return true;
        }
        return false;
    }

    public void setVolumeLevel()
        throws IOException
    {
        if (volumeLevel < -1) {
            volumeLevel = 0;
        } else if (volumeLevel > 100) {
            volumeLevel = 100;
        }
    }

    public void setVolumeLevel(int volumeLevel)
        throws IOException
    {
        this.volumeLevel = volumeLevel;
        this.setVolumeLevel();
    }

    public Song copyChain() {
        Song s  = new SpotifySong(name, volumeLevel, timer, duration);
        if (next != null) {
            s.setNext(next.copyChain());
        }
        return s;
    }

    public Song endOfChain() {
        if (next == null) {
            return this;
        }
        return next.endOfChain();
    }

    @Override
    public String toString() {
        return name + " (" + duration + ") vol: " + volumeLevel;
    }

    public void print() {
        System.out.println(this);
        if (next != null) {
            next.print();
        }
    }

    public static SpotifySong sentinel() {
        return new SpotifySong("SENTINEL", -1, new Timer());
    }

}
