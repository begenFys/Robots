package gui;

import exceptions.state.LoadException;
import exceptions.state.SaveException;
import gui.components.ProgramMenuBar;
import gui.windows.game.GameWindow;
import gui.windows.game.CoordinateWindow;
import gui.windows.log.LogWindow;
import log.Logger;
import model.GameModel;
import state.WindowState;
import state.WindowStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Главное окно приложения с внутренними окнами и меню для управления отображением и выполнения тестовых команд.
 */
public class MainApplicationFrame extends JFrame implements WindowState {

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

        addWindow(new LogWindow(Logger.getDefaultLogSource()));
        Logger.debug("Протокол работает");

        GameModel gameModel = new GameModel();
        addWindow(new GameWindow(gameModel));

        CoordinateWindow coordinateWindow = new CoordinateWindow(gameModel);
        addWindow(coordinateWindow);

        WindowStateManager windowStateManager = new WindowStateManager(this);
        try {
            windowStateManager.load();
        } catch (LoadException e) {
            System.err.println("Не удалось загрузить состояния окон");
            e.printStackTrace();
        }

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
            WindowStateManager windowStateManager = new WindowStateManager(this);
            try {
                windowStateManager.save();
            } catch (SaveException e) {
                System.err.println("Не удалось сохранить состояния окон");
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    @Override
    public String getPrefix() {
        return "main";
    }
}
