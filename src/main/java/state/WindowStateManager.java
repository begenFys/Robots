package state;


import exceptions.state.LoadException;
import exceptions.state.SaveException;
import org.apache.commons.io.FileUtils;

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
    private File configLocation;
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

    public void setConfigLocation(File location) {
        configLocation = location;
    }

    public File getConfigLocation() {
        return configLocation;
    }

    /**
     * Сохранение параметров имплементации WindowState
     * @param windowState Имплементация WindowState(в нашем случае окна)
     */
    public void saveWindowState(WindowState windowState) {
        windowStates.put(windowState.getPrefix(), windowState.getProperties());
    }

    /**
     * Установка параметров для имплементации WindowState
     *
     * @param windowState Имплементация WindowState(в нашем случае окна)
     */
    public void loadWindowState(WindowState windowState) throws LoadException {
        if (windowStates.containsKey(windowState.getPrefix())) {
            windowState.setProperties(windowStates.get(windowState.getPrefix()));
        } else {
            System.err.printf("Нет данных для загрузки окна %s%n", windowState.getPrefix());
            return;
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
