package all;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ToolPanel extends JPanel implements ActionListener{
    
    int WIDTH = 60;
    int HEIGHT = 400;
    int cornerX = 900;
    int cornerY = 60;

    public ToolPanel() {
        setPanelDefaults();
        addButtons();
    }

    // panel defaul settings 
    private void setPanelDefaults() {
        
        this.setSize(WIDTH, HEIGHT);
        this.setBackground(new Color(0, 100, 0));
        this.setLocation(cornerX, cornerY);
        FlowLayout flowLayout = new FlowLayout();
        this.setLayout(flowLayout);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.setVisible(true);
    }

    private void addButtons() {
        // the prefered size of the buttons in pixels
        Dimension buttonSize = new Dimension(50, 50);
        
        // first jbutton
        JButton button1 = new JButton();
        button1.setPreferredSize(buttonSize);
        button1.setOpaque(false);
        button1.setBorderPainted(false);
        button1.setFocusable(false);
        //button1.setIcon(image);
        //button1.setActionCommand("action1");
        button1.addActionListener(this);

        // second jbutton
        JButton button2 = new JButton();
        button2.setPreferredSize(buttonSize);
        button2.setOpaque(false);
        button2.setBorderPainted(false);
        button2.setFocusable(false);
        //button2.setIcon(image);
        //button2.setActionCommand("action2");
        //button2.addActionListener(this);

        // third jbutton
        JButton button3 = new JButton();
        button3.setPreferredSize(buttonSize);
        button3.setOpaque(false);
        button3.setBorderPainted(false);
        button3.setFocusable(false);
        //button3.setIcon(image);
        //button3.setActionCommand("action3");
        //button3.addActionListener(this);

        // finally adding the buttons
        this.add(button1);
        this.add(button2);
        this.add(button3);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

}
