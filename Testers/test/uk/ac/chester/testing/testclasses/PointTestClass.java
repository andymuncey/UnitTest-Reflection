package uk.ac.chester.testing.testclasses;

/**
 * Class used for testing purposes.
 * Designed to conform to convention
 */
public class PointTestClass {

    private int x;
    private int y;

    public PointTestClass(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
