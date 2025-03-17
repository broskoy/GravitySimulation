package all;

import java.awt.Color;

import javax.swing.JFrame;


public class MainFrame extends JFrame{

    final static int WIDTH = 1920; // x
    final static int HEIGHT = 1080; // y
    SimPanel simPanel;
    ToolPanel toolPanel;
    OptionPanel optionPanel;

    public static void main(String[] args) {
        new MainFrame();
    }

    public MainFrame() {
        // add frame components
        addSimPanel();
        // addToolPanel();
        // addOptionPanel();

        // frame settings
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setTitle("Gravity Simulation");
        this.getContentPane().setBackground(Color.darkGray);
        this.setLayout(null);
        this.setVisible(true);

        // add particles to simulate
        SimPanel.create(10, 0, 16);
    }    

    private void addSimPanel() {
        // initialize the simulation panel
        simPanel = new SimPanel();
        this.getLayeredPane().add(simPanel, Integer.valueOf(1));
        simPanel.requestFocus();
        simPanel.startGameThread();
    }

    private void addToolPanel() {
        // initialize the tool panel
        toolPanel = new ToolPanel();
        this.getLayeredPane().add(toolPanel, Integer.valueOf(2));
    }

    private void addOptionPanel() {
        // initialize settings panel
        optionPanel = new OptionPanel();
        this.getLayeredPane().add(optionPanel, Integer.valueOf(2));
    }
}

// TODO: fix conservation of momentum in SimPanel.update()
// TODO: there is a better way to calculate acceleration
// TODO: make collision
// TODO: pairing by iterating for(j=i+1;j<n;j++) to avoid double checking
// TODO: test with higher resolotion (prbably make distance independent of pixels)
// TODO: make interface for configurating variables
// TODO: make an edit mode where you can add particles
// TODO: add minimap in the corner
// TODO: center SimPanel in Frame (with layout?)
// TODO: separate update into smaller bits