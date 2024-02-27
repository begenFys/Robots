package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import log.Logger;

/**
 * Главное окно приложения с внутренними окнами и меню для управления отображением и выполнения тестовых команд.
 */
public class MainApplicationFrame extends JFrame {

    private final JDesktopPane desktopPane = new JDesktopPane();

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

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Создает лог-окно.
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
     * @param frame внутреннее окно для добавления
     */
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Генерирует меню.
     * @return созданное меню
     */
    private JMenuBar generateMenuBar() {
        MenuBar menuBar = new MenuBar(this);
        menuBar.attachLookAndFeelMenu();
        menuBar.attachTestMenu();
        menuBar.attachExitMenu();
        return menuBar.getMenuBar();
    }

    /**
     * Устанавливает внешний вид приложения.
     * @param className имя класса внешнего вида
     */
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
