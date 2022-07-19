package fr.minesales.dardaillon.graphics;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    public Frame() {
        setTitle("Secure Code");
        setSize(new Dimension(800, 480));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(new MainPanel(this));

        setVisible(true);
    }
}
