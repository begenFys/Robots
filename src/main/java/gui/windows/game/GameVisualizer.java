package gui.windows.game;

import controller.GameController;
import model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Панель для визуализации игрового процесса с роботом и целью.
 */
public class GameVisualizer extends JPanel implements PropertyChangeListener {
    private final GameModel model;
    private final GameController mController;

    public GameVisualizer(GameModel model) {
        this.model = model;
        mController = new GameController(model);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mController.setTargetPosition(e.getPoint());
            }
        });
        setDoubleBuffered(true);
        model.addNewListener(this);
    }

    /**
     * Округляет значение до ближайшего целого.
     * @param value значение для округления
     * @return округленное значение
     */
    private static int round(double value) {
        return (int) (value + 0.5);
    }

    /**
     * Переопределяет метод отрисовки компонента, чтобы нарисовать робота и цель.
     * @param g объект Graphics для рисования
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(
            g2d,
            round(model.getRobotPositionX()),
            round(model.getRobotPositionY()),
            model.getRobotDirection()
        );
        drawTarget(g2d, model.getTargetPositionX(), model.getTargetPositionY());
    }

    /**
     * Заполняет овал в указанных координатах и размерах.
     * @param g объект Graphics для рисования
     * @param centerX координата X центра овала
     * @param centerY координата Y центра овала
     * @param diam1 диаметр овала по оси X
     * @param diam2 диаметр овала по оси Y
     */
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Рисует контур овала в указанных координатах и размерах.
     * @param g объект Graphics для рисования
     * @param centerX координата X центра овала
     * @param centerY координата Y центра овала
     * @param diam1 диаметр овала по оси X
     * @param diam2 диаметр овала по оси Y
     */
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Рисует изображение робота в указанных координатах и направлении.
     * @param g объект Graphics2D для рисования
     * @param robotCenterX координата X позиции робота
     * @param robotCenterY координата Y позиции робота
     * @param direction направление робота в радианах
     */
    private void drawRobot(Graphics2D g, int robotCenterX, int robotCenterY, double direction) {
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    /**
     * Рисует цель в указанных координатах.
     * @param g объект Graphics2D для рисования
     * @param x координата X цели
     * @param y координата Y цели
     */
    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        EventQueue.invokeLater(this::repaint);
    }
}
