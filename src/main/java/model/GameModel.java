package model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Модель для работы робота
 * Считает все движения и координаты робота
 */
public class GameModel {
    private volatile double mRobotPositionX = 100;
    private volatile double mRobotPositionY = 100;
    private volatile double mRobotDirection = 0;


    private volatile int mTargetPositionX = 150;
    private volatile int mTargetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addNewListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Устанавливает целевую позицию для объекта.
     *
     * @param p точка, представляющая новую целевую позицию
     */
    public void setTargetPosition(Point p) {
        mTargetPositionX = p.x;
        mTargetPositionY = p.y;
        support.firePropertyChange("targetPosition", new Point(mTargetPositionX, mTargetPositionY), p);
    }

    /**
     * Считаем следующие координаты и изменяют позицию робота
     */
    public void updateRobot() {
        double distance = distance(mTargetPositionX, mTargetPositionY,
                mRobotPositionX, mRobotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(mRobotPositionX, mRobotPositionY, mTargetPositionX, mTargetPositionY);
        double angleDifference = asNormalizedRadians(angleToTarget - mRobotDirection);
        double angularVelocity = (angleDifference < Math.PI) ? maxAngularVelocity : -maxAngularVelocity;
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        int duration = 10;
        double newX = mRobotPositionX + velocity / angularVelocity *
                (Math.sin(mRobotDirection + angularVelocity * duration) - Math.sin(mRobotDirection));

        if (!Double.isFinite(newX)) {
            newX = mRobotPositionX + velocity * duration * Math.cos(mRobotDirection);
        }

        double newY = mRobotPositionY - velocity / angularVelocity *
                (Math.cos(mRobotDirection + angularVelocity * duration) - Math.cos(mRobotDirection));

        if (!Double.isFinite(newY)) {
            newY = mRobotPositionY + velocity * duration * Math.sin(mRobotDirection);
        }
        double newDirection = asNormalizedRadians(mRobotDirection + angularVelocity * duration);
        support.firePropertyChange("robotPositionX", mTargetPositionX, newX);
        support.firePropertyChange("robotPositionY", mTargetPositionY, newY);
        support.firePropertyChange("robotDirection", mRobotDirection, newDirection);
        mRobotPositionX = newX;
        mRobotPositionY = newY;
        mRobotDirection = newDirection;
    }

    /**
     * Приводит угол к диапазону от 0 до 2π радиан.
     * @param angle угол для нормализации
     * @return нормализованный угол
     */
    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    /**
     * Применяет ограничения к значению в заданном диапазоне.
     * @param value значение, которое нужно ограничить
     * @param min минимальное допустимое значение
     * @param max максимальное допустимое значение
     * @return ограниченное значение
     */
    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    /**
     * Вычисляет расстояние между двумя точками.
     *
     * @param x1 координата X первой точки
     * @param y1 координата Y первой точки
     * @param x2 координата X второй точки
     * @param y2 координата Y второй точки
     * @return расстояние между точками
     */
    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Вычисляет угол между двумя точками.
     *
     * @param fromX координата X начальной точки
     * @param fromY координата Y начальной точки
     * @param toX   координата X конечной точки
     * @param toY   координата Y конечной точки
     * @return угол между точками в радианах
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public double getRobotPositionX() {
        return mRobotPositionX;
    }

    public double getRobotPositionY() {
        return mRobotPositionY;
    }

    public double getRobotDirection() {
        return mRobotDirection;
    }

    public int getTargetPositionX() {
        return mTargetPositionX;
    }

    public int getTargetPositionY() {
        return mTargetPositionY;
    }
}