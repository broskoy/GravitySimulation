package all;

public class Vector {
    double x;
    double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public double magSquared() {
        return (x*x + y*y);
    }

    public void sum(Vector vector) {
        x += vector.x;
        y += vector.y;
    }
}