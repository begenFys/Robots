package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

/**
 * Представляет собой панель меню приложения.
 * Он содержит методы для добавления различных меню на панель.
 */
public class ProgramMenuBar extends JMenuBar {
    private final MainApplicationFrame appFrame;

    /**
     * Конструктор класса MenuBar.
     *
     * @param appFrame фрейм приложения
     */
    public ProgramMenuBar(MainApplicationFrame appFrame) {
        attachProgramMenu();
        attachLookAndFeelMenu();
        attachTestMenu();
        this.appFrame = appFrame;
    }

    /**
     * Добавляет меню "Режим отображения" на панель меню.
     */
    private void attachLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.appFrame.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_U);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.appFrame.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);

        this.add(lookAndFeelMenu);
    }

    /**
     * Добавляет меню "Тесты" на панель меню.
     */
    private void attachTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        testMenu.add(addLogMessageItem);

        this.add(testMenu);
    }

    /**
     * Добавляет меню "Выход" на панель меню.
     */
    private void attachProgramMenu() {
        JMenu exitMenu = new JMenu("Robots");
        exitMenu.setMnemonic(KeyEvent.VK_X);
        exitMenu.getAccessibleContext().setAccessibleDescription("Выход из приложения");

        JMenuItem exitAcceptItem = new JMenuItem("Выход", KeyEvent.VK_X);
        exitAcceptItem.addActionListener((event) -> {
            SwingUtilities.invokeLater(() -> {
                this.appFrame.dispatchEvent(new WindowEvent(this.appFrame, WindowEvent.WINDOW_CLOSING));
            });
        });

        JMenuItem exitForceItem = new JMenuItem("Принудительный выход", KeyEvent.VK_X);
        exitForceItem.addActionListener((event) -> {
            System.exit(0);
        });

        exitMenu.add(exitAcceptItem);
        exitMenu.add(exitForceItem);

        this.add(exitMenu);
    }

    /**
     * Устанавливает внешний вид приложения.
     *
     * @param className имя класса внешнего вида
     */
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this.appFrame);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
