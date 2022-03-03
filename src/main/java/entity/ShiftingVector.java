package entity;

public class ShiftingVector {
    private double x, y, translation;

    public ShiftingVector(double x, double y, double translation) {
        this.x = x;
        this.y = y;
        this.translation = translation;
    }

    public void decreaseTranslation() {
        translation--;
    }
    public void resetX() {
        x = 0;
    }
    public void resetY() {
        y = 0;
    }
    public void setTranslation(double translation) {
        this.translation = translation;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getTranslation() {
        return translation;
    }
}
