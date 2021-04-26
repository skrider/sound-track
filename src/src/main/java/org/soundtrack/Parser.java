package org.soundtrack;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private final String target;

    //Metadata for this packet
    private boolean shuffle;
    private int volumeLevel;
    private int rep;

    Song head, tail;

    public Parser(String target) {
        this(target, -1);
    }

    public Parser(String target, int volumeLevel) {
        this.target = target;

        // initialize linked list
        this.head = Song.sentinel();
        this.tail = this.head;
        head.setNext(tail);

        // default values
        this.volumeLevel = volumeLevel;
        this.rep = 1;
        this.shuffle = false;

        // override defaults
        parseMetadata();
    }

    private void parseMetadata() {
        Matcher m;
        m = fieldFinder("shuffle").matcher(target);
        if (m.find()) {
            shuffle = "true".equals(m.group());
        }
        m = fieldFinder("rep").matcher(target);
        if (m.find()) {
            rep = Integer.parseInt(m.group());
        }
        m = fieldFinder("volume").matcher(target);
        if (m.find()) {
            volumeLevel = Integer.parseInt(m.group());
        }
    }

    public Song getHead() {
        return head.next();
    }

    public Song getTail() {
        return tail;
    }

    public void parse(Timer timer) {
        Matcher delimiterStartDetector = Pattern.compile("(?<=[\\[,]\\s)\\S").matcher(target);
        Matcher delimiterEndDetector = Pattern.compile("\\S(?=\\s[,\\]])").matcher(target);
        Matcher durationDetector = Pattern.compile("(?<=\\h)\\d?\\d?:?\\d?\\d:\\d\\d(?=\\D)").matcher(target);

        int start;
        int end;
        Matcher region = delimiterStartDetector.region(0, target.length() - 1);
        while (region.find()) {
            start = region.start();
            if (region.group().equals("{")) {
                end = endOfPacket(start);
                Parser subParser = new Parser(target.substring(start, end), volumeLevel);
                subParser.parse(timer);
                getTail().setNext(subParser.getHead());
                tail = subParser.getTail();
            } else {
                delimiterEndDetector.find(start);
                end = delimiterEndDetector.end();
                Matcher m = durationDetector.region(start, end + 1);
                Song s;
                if (m.find()) {
                    long duration = Utils.parseTime(m.group());
                    s = new SpotifySong(target.substring(start, m.start() - 1), volumeLevel, timer, duration);
                } else {
                    s = new SpotifySong(target.substring(start, end), volumeLevel, timer);
                }
                getTail().setNext(s);
                tail = tail.next();
            }
            region = delimiterStartDetector.region(end, target.length());
        }
        LinkedList<Song> reps = new LinkedList<>();
        for (int i = 1; i < rep; i += 1) {
            reps.addLast(getHead().copyChain());
        }
        for (Song s : reps) {
            getTail().setNext(s);
            tail = tail.endOfChain();
        }
    }

    private Pattern fieldFinder(String field) {
        return Pattern.compile("(?<=" + Pattern.quote(field) + ":\\h)[\\d\\w]*(?=[\\s,])");
    }

    private int endOfPacket(int start) {
        int end = start + 1;
        while (end < target.length()) {
            char c = target.charAt(end);
            if (c == '}') {
                return end;
            } else if (c == '{') {
                end += endOfPacket(end);
            } else {
                end += 1;
            }
        }
        return end;
    }

    @Override
    public String toString() {
        return "Vol: " + volumeLevel + " Reps: " + rep + " Shuffle? " + shuffle;
    }
}
