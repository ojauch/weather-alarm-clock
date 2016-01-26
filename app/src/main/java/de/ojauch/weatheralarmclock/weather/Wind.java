package de.ojauch.weatheralarmclock.weather;

/**
 * Represents the wind
 * @author Oskar Jauch
 */
public class Wind {
    private float speed;
    private Direction direction;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public enum Direction {
        NORTH, EAST, SOUTH, WEST
    }
}
