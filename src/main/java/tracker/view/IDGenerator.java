package tracker.view;

import java.util.Random;

public class IDGenerator {
    public String generation_id() {
        /*Generate an ID :
            In a for loop, it generates a number between 1 and 2 :
                if 1 - it will be a number (ASCII 48-57)
                if 2 - it will be a MAJ letter (ASCII 65-90)
         */
        String id = "";
        for (int i = 0; i <= 7; i++) { //We do an 8 caracters string.
                Random rand = new Random();
                int randomNumLetter = rand.nextInt(2) + 1; //Will decide if it will be a number or a MAJ
                    if (randomNumLetter == 1) { //Generate an ASCII Number
                        int asciiNumber = rand.nextInt(57 - 48 + 1) + 48;
                        char numtoascii = (char) asciiNumber;
                        id += numtoascii;
                    }

                    if (randomNumLetter == 2) { //Generate an ASCII letter
                        int asciiChar = rand.nextInt( 90 - 65 + 1) + 65;
                        char numtoascii = (char) asciiChar;
                        id += numtoascii;
                    }
            }
        System.out.println(id);
        return id;
    }

    public static void main(String[] args) {
        IDGenerator idGen = new IDGenerator();
        idGen.generation_id();
        idGen.generation_id();
        idGen.generation_id();
    }
}
