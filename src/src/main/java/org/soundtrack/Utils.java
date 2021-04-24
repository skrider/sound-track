package org.soundtrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

    public static String readFromInputStream(InputStream stream)
        throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder s = new StringBuilder();
        String next;

        do {
            next = reader.readLine();
            s.append(next);
        } while (next != null);

        System.out.println(s.toString());
        return s.toString();
    }
}
