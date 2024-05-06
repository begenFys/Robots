package gui.windows.log;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import state.WindowState;

import javax.swing.*;
import java.awt.*;

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
        panel.setLocation(10, 10);
        panel.setSize(300, 800);
        setMinimumSize(panel.getSize());
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
}