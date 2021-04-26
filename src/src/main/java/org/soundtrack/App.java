package org.soundtrack;

import java.io.File;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Timer t = new Timer();
        File f = new File(
                "C:\\Users\\steph\\Documents\\GitHub\\sound-track\\soundtracks\\basic.txt");
        String snippet = Utils.readContentsAsString(f);
        snippet = Utils.clean(snippet);
        Parser parser = new Parser(snippet);
        parser.parse(t);

        try {
            Song current = parser.getHead();
            current.start();

            while (true) {
                if (current.checkNext()) {
                    current = current.next();
                    if (current == null) {
                        System.exit(0);
                    }
                    current.start();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
