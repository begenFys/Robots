package state;


import exceptions.state.LoadException;
import exceptions.state.SaveException;
import org.apache.commons.io.FileUtils;

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
     * Map для хранения состояний окон.
     */
    private Map<String, Properties> windowStates = new HashMap<>();

    /**
     * Устанавливает путь по умолчанию для сохранения конфига
     */
    public WindowStateManager() {
        configLocation = new File(
                System.getProperty("user.home") +
                        File.separator + "Robots" +
                        File.separator + "config" +
                        File.separator + "windowStates.conf");
    }

    /**
     * Устанавливает кастомный путь для сохранения конфига
     */
    public WindowStateManager(File configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * Сохранение параметров имплементации WindowState
     * @param window Component, который реализует интерфейс WindowState
     */
    public void saveWindowState(WindowState window) {
        Properties props = new Properties();
        Dimension size = ((Component) window).getSize();
        Point location = ((Component) window).getLocation();
        props.setProperty("width", String.valueOf(size.width));
        props.setProperty("height", String.valueOf(size.height));
        props.setProperty("x", String.valueOf(location.x));
        props.setProperty("y", String.valueOf(location.y));
        windowStates.put(window.getPrefix(), props);
    }

    /**
     * Установка параметров для имплементации WindowState
     *
     * @param window Имплементация WindowState(в нашем случае окна)
     */
    public void loadWindowState(WindowState window) throws LoadException {
        if (windowStates.containsKey(window.getPrefix())) {
            Properties props = windowStates.get(window.getPrefix());
            int width = Integer.parseInt(props.getProperty("width"));
            int height = Integer.parseInt(props.getProperty("height"));
            int x = Integer.parseInt(props.getProperty("x"));
            int y = Integer.parseInt(props.getProperty("y"));

            ((Component) window).setLocation(x, y);
            ((Component) window).setSize(width, height);
        } else {
            System.err.printf("Нет данных для загрузки окна %s\n", window.getPrefix());
        }
    }

    /**
     * Сохранение HashMap в файл конфига
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
                windowStates = (HashMap<String, Properties>) ois.readObject();
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
