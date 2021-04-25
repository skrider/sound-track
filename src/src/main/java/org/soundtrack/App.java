package org.soundtrack;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Timer t = new Timer();

        Song s1 = new SpotifySong("Space Song", 80, t);
        Song s2 = new SpotifySong("Heat Waves", 80, t);
        s1.setNext(s2);
        Song s3 = new SpotifySong("Ni**as in Paris", 80, t);
        s2.setNext(s3);
        Song s4 = new SpotifySong("Halo Theme", 80, t);
        s3.setNext(s4);

        try {
            Song t1 = SpotifySong.fromKeywordAtRandom("beach house", -1, t, 5);
            Song current = t1;
            current.start();

            while (true) {
                if (current.checkNext()) {
                    System.out.println("next");
                    current = current.next();
                    current.start();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
