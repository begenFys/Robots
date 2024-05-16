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
    private LogWindowSource mLogSource;
    private TextArea mLogContent;

    /**
     * Создает новое окно логов.
     * @param logSource источник данных для логов
     */
    public LogWindow(LogWindowSource logSource) {
        super("Логи", true, true, true, true);
        mLogSource = logSource;
        mLogSource.registerListener(this);
        mLogContent = new TextArea("");
        mLogContent.setSize(200, 500);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mLogContent, BorderLayout.CENTER);
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
        for (LogEntry entry : mLogSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        mLogContent.setText(content.toString());
        mLogContent.invalidate();
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