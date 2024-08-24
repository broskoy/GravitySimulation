package all;

import java.awt.Color;

import javax.swing.JFrame;

public class Frame extends JFrame{
    
    final static int HEIGHT = 1080;
    final static int WIDTH = 1400;
    SimPanel simPanel;

    public Frame() {
        addSimPanel();

        // window settings
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH + 50, HEIGHT + 70);
        this.setResizable(false);
        this.setTitle("Gravity Simulation");
        this.getContentPane().setBackground(Color.darkGray);
        this.setLayout(null);
        this.setVisible(true);
    }    

    private void addSimPanel() {
        // initialize the simulation panel
        simPanel = new SimPanel();
        this.getLayeredPane().add(simPanel, Integer.valueOf(1));
        simPanel.requestFocus();
        simPanel.startGameThread();
    }
}
