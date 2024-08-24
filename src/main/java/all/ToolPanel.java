package all;

import java.awt.Color;

import javax.swing.JPanel;

public class ToolPanel extends JPanel{
    
    int WIDTH = 160;
    int HEIGHT = 1000;
    int cornerX = 1240;
    int cornerY = 100;

    public ToolPanel() {
        // panel settings 
        this.setSize(WIDTH, HEIGHT);
        this.setBackground(new Color(0, 40, 0));
        this.setLocation(cornerX, cornerY);
        // this.addKeyListener(keyHandler);
        this.setLayout(null);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.setVisible(true);
    }
}
