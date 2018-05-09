package main;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/** @author group2 TestFiles handles all of the automated testing of the project */
class TestFiles {

    @Test
    void testMain() {
        String dir = System.getProperty("user.dir");
        String[] expected = {
            "Path of the best fit individual: [(1,1), (1,2), (2,2), (3,2), (3,1), (4,1), (5,1), (5,2), (5,3), (5,4)]",
            "Path of the best fit individual: [(1,1), (1,2), (1,3), (2,3), (3,3), (4,3), (5,3), (5,4)]",
            "Path of the best fit individual: [(1,1), (1,2), (1,3), (1,4), (1,5), (2,5), (3,5), (3,6), (4,6), (4,7), (5,7), (5,8), (5,9), (6,9), (6,10), (7,10), (8,10)]",
            "Path of the best fit individual: [(3,6), (4,6), (5,6), (6,6), (7,6), (8,6), (8,5), (8,4), (9,4), (10,4)]"
        };

        // Create a stream to hold the output
        for (Integer i = 0; i < 1; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            String filepath = dir + "/TEST/test_" + i.toString() + ".xml";
            System.out.println(filepath);
            System.setOut(ps);
            Main.main(new String[] {filepath});
            System.out.flush();
            System.setOut(old);

            BufferedReader bufferedReader =
                    new BufferedReader(new StringReader(new String(baos.toByteArray())));
            String line;
            String output = "";
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("Path of the best fit individual:")) {
                        output = line;
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(output);
            assertTrue(output.contains(expected[i]));
        }
    }
}
