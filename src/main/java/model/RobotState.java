package model;

public class RobotState {
    private final double positionX;
    private final double positionY;
    private final double direction;

    public RobotState(double positionX, double positionY, double direction) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = direction;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getDirection() {
        return direction;
    }
}