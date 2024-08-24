package all;

import java.awt.Color;

import javax.swing.JFrame;


public class MainFrame extends JFrame{

    final static int HEIGHT = 1080;
    final static int WIDTH = 1440;
    SimPanel simPanel;
    ToolPanel toolPanel;

    public static void main(String[] args) {
        new MainFrame();
    }

    public MainFrame() {
        // add frame components
        addSimPanel();
        addToolPanel();

        // frame settings
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH + 50, HEIGHT + 70);
        this.setResizable(false);
        this.setTitle("Gravity Simulation");
        this.getContentPane().setBackground(Color.darkGray);
        this.setLayout(null);
        this.setVisible(true);

        // add particles to simulate
        SimPanel.create(20, 0, 4);
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
}

// TODO: fix conservation of momentum in SimPanel.update()
// TODO: there is a better way to calculate acceleration
// TODO: make optional collisions
// TODO: test with higher resolotion (prbably make distance independent of pixels)
// TODO: make interface for configurating variables
// TODO: make an edit mode where you can add particles
// TODO: panel can be bigger but with camera movement (and a minimap?)
// TODO: make merge delete both particles and create a new one to delete the if
// TODO: center SimPanel in Frame (with layout?)
// TODO: separate update into smaller bits