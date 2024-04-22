package gui.windows.log;

import java.awt.*;
import java.util.Properties;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import state.WindowState;

/**
 * Внутреннее окно для отображения логов.
 */
public class LogWindow extends JInternalFrame implements LogChangeListener, WindowState {
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    /**
     * Создает новое окно логов.
     * @param logSource источник данных для логов
     */
    public LogWindow(LogWindowSource logSource) {
        super("Логи", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    /**
     * Обновляет содержимое логов.
     */
    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public String getPrefix() {
        return "log";
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