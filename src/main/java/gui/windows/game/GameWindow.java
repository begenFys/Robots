package gui.windows.game;

import model.GameModel;
import state.WindowState;

import javax.swing.*;
import java.awt.*;

/**
 * Внутреннее окно для отображения игрового поля.
 */
public class GameWindow extends JInternalFrame implements WindowState {
    private final GameVisualizer m_visualizer;

    /**
     * Создает новое игровое окно.
     */
    public GameWindow(GameModel model) {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer(model);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        panel.setSize(400, 400);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public String getPrefix() {
        return "game";
    }
}