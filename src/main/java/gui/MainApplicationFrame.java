package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import log.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Главное окно приложения с внутренними окнами и меню для управления отображением и выполнения тестовых команд.
 */
public class MainApplicationFrame extends JFrame {

    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("ru", "RU"));

    /**
     * Создает главное окно приложения.
     */
    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(new ProgramMenuBar(this));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

    }

    /**
     * Создает лог-окно.
     *
     * @return созданное лог-окно
     */
    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Добавляет внутреннее окно на рабочую область.
     *
     * @param frame внутреннее окно для добавления
     */
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Подтверждает выход из приложения с помощью модульного окна.
     */
    protected void confirmExit() {
        Object[] choices = {bundle.getString("quit"), bundle.getString("cancel")};
        Object defaultChoice = choices[0];
        int confirmed = JOptionPane.showOptionDialog(null,
                bundle.getString("quitQuestion"),
                bundle.getString("quitTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                defaultChoice);

        if (confirmed == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
