package org.soundtrack;

import java.io.IOException;

public class CLITest {
    public static void main(String[] args) {
        String s = "huh";
        try {
            s = SpotifySong.search("bruh");
        } catch (IOException e) {
            System.out.println("oof");
        }
        System.out.println(s);
    }
}
