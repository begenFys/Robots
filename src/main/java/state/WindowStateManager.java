package state;


import exceptions.state.LoadException;
import exceptions.state.SaveException;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Класс, который позволяет сохранять и загружать состояния окон.
 */
public class WindowStateManager {
    /**
     * Файл, куда сохраняются конфигурации окон.
     */
    private final File configLocation;
    /**
     * Ссылка на контейнер окон
     */
    private final JFrame parentFrame;

    /**
     * Устанавливает путь по умолчанию для сохранения конфига
     */
    public WindowStateManager(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        configLocation = new File(
                System.getProperty("user.home") +
                        File.separator + "Robots" +
                        File.separator + "config" +
                        File.separator + "windowStates.conf");
    }

    /**
     * Устанавливает кастомный путь для сохранения конфига
     */
    public WindowStateManager(File configLocation, JFrame parentFrame) {
        this.configLocation = configLocation;
        this.parentFrame = parentFrame;
    }

    /**
     * Получение параметров имплементации WindowState
     * @param window Component, который реализует интерфейс WindowState
     */
    public Properties getProperties(WindowState window) {
        Properties props = new Properties();
        Dimension size = ((Component) window).getSize();
        Point location = ((Component) window).getLocation();
        props.setProperty("width", String.valueOf(size.width));
        props.setProperty("height", String.valueOf(size.height));
        props.setProperty("x", String.valueOf(location.x));
        props.setProperty("y", String.valueOf(location.y));
        return props;
    }

    /**
     * Установка параметров для имплементации WindowState
     *
     * @param window Имплементация WindowState(в нашем случае окна)
     */
    public void setProperties(WindowState window, Properties props) {
        int width = Integer.parseInt(props.getProperty("width"));
        int height = Integer.parseInt(props.getProperty("height"));
        int x = Integer.parseInt(props.getProperty("x"));
        int y = Integer.parseInt(props.getProperty("y"));

        ((Component) window).setLocation(x, y);
        ((Component) window).setSize(width, height);
    }

    /**
     * Сохранение состояний в файл конфига
     */
    public void save() throws SaveException {
        try {
            FileUtils.forceMkdir(configLocation.getParentFile());
        } catch (IOException e) {
            throw new SaveException("Не удалось создать иерархию директорий для сохранения");
        }

        try {
            configLocation.createNewFile();
        } catch (IOException e) {
            throw new SaveException("Не удалось создать конфигурационный файл");
        }

        try (OutputStream os = new FileOutputStream(configLocation);
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
            Map<String, Properties> windowStates = new HashMap<>();
            if (parentFrame instanceof WindowState) {
                windowStates.put(((WindowState) parentFrame).getPrefix(), getProperties((WindowState) parentFrame));
            }
            for (Component component : parentFrame.getContentPane().getComponents()) {
                if (component instanceof WindowState) {
                    windowStates.put(((WindowState) component).getPrefix(), getProperties((WindowState) component));
                }
            }
            oos.writeObject(windowStates);
        } catch (FileNotFoundException e) {
            throw new SaveException("Не удалось создать поток вывода. Файл конфига не найден");
        } catch (IOException e) {
            throw new SaveException("Не удалось записать состояния в файл");
        }
    }

    /**
     * Получение HashMap из файла конфига
     */
    public void load() throws LoadException {
        try {
            InputStream is = new FileInputStream(configLocation);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            try {
                Map<String, Properties> windowStates = (HashMap<String, Properties>) ois.readObject();
                if (parentFrame instanceof WindowState) {
                    setProperties((WindowState) parentFrame, windowStates.get(((WindowState) parentFrame).getPrefix()));
                }
                for (Component component : parentFrame.getContentPane().getComponents()) {
                    if (component instanceof WindowState) {
                        setProperties((WindowState) component, windowStates.get(((WindowState) component).getPrefix()));
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new LoadException("Не удалось прочитать файл. класс hashmap не нашелся.");
            } finally {
                ois.close();
                is.close();
            }
        } catch (FileNotFoundException e) {
            throw new LoadException("Не удалось найти файл конфигурации");
        } catch (IOException e) {
            throw new LoadException("Не удалось создать иерархию фильтров");
        }
    }
}
