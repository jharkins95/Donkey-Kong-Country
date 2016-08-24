import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * LevelParser.java
 * Reads in an input level file and provides collections of tokens that
 * can be used to initialize various game objects.
 * @author Jack Harkins
 *
 */
public class LevelParser {
    
    private Scanner sc;
    
    /**
     * 
     * @param filename the name of the file to be read
     * @throws IOException upon an error in reading
     */
    public LevelParser(String filename) {
        try {
            sc = new Scanner(new BufferedReader(new FileReader(filename)));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error reading input level file!", 
                    "File read error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    /**
     * 
     * @return does the file being read have another line of text?
     */
    public boolean hasNext() {
        return sc.hasNext();
    }
    
    /**
     * 
     * @return a list of string tokens corresponding to a game object's
     * initial state
     */
    public List<String> readNextTokens() {
        List<String> lineTokens = new ArrayList<>();
        String line = sc.nextLine();
        String currentToken = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '!' && line.length() > 1) {
                String mode = line.substring(i + 1).trim();
                lineTokens.add(mode);
                return lineTokens;
            }
            if (c == ';') {
                return lineTokens;
            }
            if (c == '~') {
                return lineTokens;
            }
            if (c == ',') {
                lineTokens.add(currentToken.trim());
                currentToken = "";
            } else {
                currentToken += c;
            }     
        }
        return lineTokens;
    }
    
    /**
     * Closes the parser and frees system resources associated with it
     */
    public void close() {
        sc.close();
    }
}
