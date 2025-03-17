package all;

public class Vector {
    double x;
    double y;
    double mag;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        this.mag = Math.sqrt(x*x + y*y);
    }
}