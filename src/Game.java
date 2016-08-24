import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.*;

/**
 * 
 */

/**Game.java
 * This class is the main entry point to the game. Its purpose is to set up the
 * drawing canvas and initialize the graphical elements of the game, such as buttons
 * and status labels.
 * @author Jack Harkins
 *
 */
public class Game implements Runnable {

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        
        final JFrame mainFrame = new JFrame("Donkey Kong Country");
        mainFrame.setLocation(250, 50);
        
        final JPanel status_panel = new JPanel();
        mainFrame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running");
        final JLabel lives = new JLabel("Lives: ");
        final JLabel bananas = new JLabel("Bananas: ");
        final JLabel enemiesRemaining = new JLabel("Enemies remaining: ");

        status_panel.add(status);
        status_panel.add(lives);
        status_panel.add(bananas);
        status_panel.add(enemiesRemaining);
        
        final GameCourt court = new GameCourt(status, lives, bananas, enemiesRemaining);
        
        mainFrame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                court.requestFocusInWindow();
            }
        });
        
        final JFrame instructionsWindow = new JFrame("Instructions");
        instructionsWindow.setLocation(200, 200);
        
        
        final JTextArea insText = new JTextArea();
        BufferedReader insReader;
        try {
            insReader = new BufferedReader(new FileReader("instructions.txt"));
            String line = insReader.readLine();
            while (line != null) {
                insText.append(line + '\n');
                line = insReader.readLine();
            }
            insReader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        insText.setEditable(false);
        insText.setPreferredSize(new Dimension(400, 300));
        
        final JScrollPane scrollBar = new JScrollPane(insText);
        instructionsWindow.add(scrollBar);
        instructionsWindow.pack();
        instructionsWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        final JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionsWindow.setVisible(true);
                court.setPauseState(true);
            }
        });
        
        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                court.setPauseState(true);
                court.reset();
            }
        });
        
        final JPanel control_panel = new JPanel();
        control_panel.add(load);
        control_panel.add(instructions);
        control_panel.add(quit);
        mainFrame.add(control_panel, BorderLayout.NORTH);
        
        mainFrame.add(court);
        
        mainFrame.setResizable(false);
        
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        
        court.reset();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

}
