package all;

import java.awt.Color;

import javax.swing.JFrame;

public class Frame extends JFrame{
    
    SimPanel simPanel;

    public Frame() {
        // initialize the simulation panel
        simPanel = new SimPanel();
        this.getLayeredPane().add(simPanel, Integer.valueOf(1));
        simPanel.requestFocus();
        simPanel.startGameThread();

        // window settings
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Config.WIDTH + 50, Config.HEIGHT + 70);
        this.setResizable(false);
        this.setTitle("Gravity Simulation");
        this.getContentPane().setBackground(Color.darkGray);
        this.setLayout(null);
        this.setVisible(true);
    }    
}
