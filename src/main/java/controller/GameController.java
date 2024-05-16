package controller;

import model.GameModel;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс контролера для управления моделью
 */
public class GameController {
    private final GameModel mModel;
    private final Timer mTimer = new Timer("events generator", true);

    public GameController(GameModel model) {
        mModel = model;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mModel.updateRobot();
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
        mModel.setTargetPosition(point);
    }
}