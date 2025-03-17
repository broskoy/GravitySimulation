package all;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    // active while pressed
    public boolean upPressed;
    public boolean leftPressed;
    public boolean downPressed;
    public boolean rightPressed;

    // active on toggle
    public boolean spacePressed;

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            default -> System.out.println(e.getKeyChar() + " pressed");
            case 87 -> upPressed = true; // w
            case 65 -> leftPressed = true; // a
            case 83 -> downPressed = true; // s
            case 68 -> rightPressed = true; // d
            case 32 -> spacePressed = !spacePressed;
            case 75 -> System.exit(43); // k
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            default -> System.out.println(e.getKeyChar() + " released");
            case 87 -> upPressed = false; // w
            case 65 -> leftPressed = false; // a
            case 83 -> downPressed = false; // s
            case 68 -> rightPressed = false; // d
            case 75 -> System.exit(43); // k
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}