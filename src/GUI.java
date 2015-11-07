package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jimmy on 8/1/2015.
 * Simple GUI
 */
public class GUI extends JFrame {

    private JButton buttonSearch;
    private JTextField textFieldSearch;

    public GUI() {
        createGUI();
        setTitle("LightNovel Reader");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(600, 750);
        setVisible(true);
    }

    private void createGUI() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        textFieldSearch = new JTextField();
        textFieldSearch.setPreferredSize(new Dimension(150, 25));
        panel.add(textFieldSearch);
        buttonSearch = new JButton("Search");
        panel.add(buttonSearch);
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }

}
