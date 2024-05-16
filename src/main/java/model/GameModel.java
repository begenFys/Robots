package model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Модель для работы робота
 * Считает все движения и координаты робота
 */
public class GameModel {
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;


    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

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
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
        support.firePropertyChange("targetPosition", new Point(m_targetPositionX, m_targetPositionY), p);
    }

    /**
     * Считаем следующие координаты и изменяют позицию робота
     */
    public void updateRobot() {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angleDifference = asNormalizedRadians(angleToTarget - m_robotDirection);
        double angularVelocity = (angleDifference < Math.PI) ? maxAngularVelocity : -maxAngularVelocity;
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        int duration = 10;
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection + angularVelocity * duration) - Math.sin(m_robotDirection));

        if (!Double.isFinite(newX)) {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }

        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection + angularVelocity * duration) - Math.cos(m_robotDirection));

        if (!Double.isFinite(newY)) {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        support.firePropertyChange("robotPositionX", m_targetPositionX, newX);
        support.firePropertyChange("robotPositionY", m_targetPositionY, newY);
        support.firePropertyChange("robotDirection", m_robotDirection, newDirection);
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        m_robotDirection = newDirection;
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
        return m_robotPositionX;
    }

    public double getRobotPositionY() {
        return m_robotPositionY;
    }

    public double getRobotDirection() {
        return m_robotDirection;
    }

    public int getTargetPositionX() {
        return m_targetPositionX;
    }

    public int getTargetPositionY() {
        return m_targetPositionY;
    }
}