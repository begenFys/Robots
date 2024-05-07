package controller;

import model.GameModel;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс контролера для управления моделью
 */
public class GameController {
    private final GameModel m_model;
    private final Timer m_timer = new Timer("events generator", true);

    public GameController(GameModel model) {
        m_model = model;
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                m_model.updateRobot();
            }
        }, 0, 10);
    }

    /**
     * Метод для отправки новой позиции робота
     * Позволяет view влиять на модель.
     *
     * @param point - новая позиция
     */
    public void setTargetPosition(Point point) {
        m_model.setTargetPosition(point);
    }
}