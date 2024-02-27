package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Представляет собой панель меню приложения.
 * Он содержит методы для добавления различных меню на панель.
 */
public class MenuBar {
    private final MainApplicationFrame appFrame;
    private final JMenuBar menuBar;

    /**
     * Конструктор класса MenuBar.
     *
     * @param appFrame фрейм приложения
     */
    public MenuBar(MainApplicationFrame appFrame) {
        this.menuBar = new JMenuBar();
        this.appFrame = appFrame;
    }

    /**
     * Возвращает панель меню.
     *
     * @return панель меню
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Добавляет меню "Режим отображения" на панель меню.
     */
    public void attachLookAndFeelMenu() {
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

        this.menuBar.add(lookAndFeelMenu);
    }

    /**
     * Добавляет меню "Тесты" на панель меню.
     */
    public void attachTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        testMenu.add(addLogMessageItem);

        this.menuBar.add(testMenu);
    }

    /**
     * Добавляет меню "Выход" на панель меню.
     */
    public void attachProgramMenu() {
        JMenu exitMenu = new JMenu("Robots");
        exitMenu.setMnemonic(KeyEvent.VK_X);
        exitMenu.getAccessibleContext().setAccessibleDescription("Выход из приложения");

        JMenuItem exitAcceptItem = new JMenuItem("Выход", KeyEvent.VK_X);
        exitAcceptItem.addActionListener((event) -> {
            this.appFrame.confirmExit();
        });

        JMenuItem exitForceItem = new JMenuItem("Принудительный выход", KeyEvent.VK_X);
        exitForceItem.addActionListener((event) -> {
            System.exit(0);
        });

        exitMenu.add(exitAcceptItem);
        exitMenu.add(exitForceItem);

        this.menuBar.add(exitMenu);
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
