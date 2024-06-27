package all;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.Math;

public class Particle {
    double x;
    double y;
    double vx;
    double vy;
    int type;
    double mass;
    double radius;
    boolean marked;

    public Particle(){
        this.x = 0;
        this.y = 0;
        this.vx = 0;
        this.vy = 0;
        this.type = 0;
        this.mass = 1;
        this.radius = Math.sqrt(mass);
        this.marked = false;
    }

    public void draw(Graphics2D g2d) {
        int drawx = (int)(x - radius);
        int drawy = (int)(y - radius);
        int drawrad = (int)(2*radius);
        switch (type) {
            case 0: g2d.setColor(Color.red); break;
            case 1: g2d.setColor(Color.cyan); break;
            case 2: g2d.setColor(Color.green); break;
            case 3: g2d.setColor(Color.orange); break;
            default: System.out.println("big fuk"); break;
        }
        g2d.fillOval(drawx, drawy, drawrad, drawrad);
    }

    public void updateRadius() {
        radius = Math.sqrt(mass);
    }
}
