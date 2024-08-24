package all;

import java.awt.Color;

import javax.swing.JPanel;

public class OptionPanel extends JPanel{
    
    int WIDTH = 400;
    int HEIGHT = 100;
    int cornerX = 50;
    int cornerY = 100;

    public OptionPanel() {
        // panel settings 
        this.setSize(WIDTH, HEIGHT);
        this.setBackground(new Color(0, 40, 40));
        this.setLocation(cornerX, cornerY);
        // this.addKeyListener(keyHandler);
        this.setLayout(null);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.setVisible(true);
    }
}
