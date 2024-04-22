package gui.windows.game;

import state.WindowState;

import java.awt.*;
import java.util.Properties;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Внутреннее окно для отображения игрового поля.
 */
public class GameWindow extends JInternalFrame implements WindowState {
    private final GameVisualizer m_visualizer;

    /**
     * Создает новое игровое окно.
     */
    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public String getPrefix() {
        return "game";
    }

    @Override
    public Properties getProperties() {
        Properties props = new Properties();
        Dimension size = this.getSize();
        Point location = this.getLocation();
        props.setProperty("width", String.valueOf(size.width));
        props.setProperty("height", String.valueOf(size.height));
        props.setProperty("x", String.valueOf(location.x));
        props.setProperty("y", String.valueOf(location.y));
        return props;
    }

    @Override
    public void setProperties(Properties properties) {
        int width = Integer.parseInt(properties.getProperty("width"));
        int height = Integer.parseInt(properties.getProperty("height"));
        int x = Integer.parseInt(properties.getProperty("x"));
        int y = Integer.parseInt(properties.getProperty("y"));

        this.setLocation(x, y);
        this.setSize(width, height);
    }
}