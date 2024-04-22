package state;
import java.util.Properties;

/**
 * Интерфейс, отвечающий за состояния окна
 * с помощью java.util.Properties
 */
public interface WindowState {
    /**
     * Получение уникального префикса окна
     * @return уникальный префикс окна
     */
    String getPrefix();

    /**
     * Получение Properties который хранить о себе окно
     * @return Properties окна
     */
    Properties getProperties();

    /**
     * Установка Properties окна
     * @param properties параметры, которые окно хранит о себе
     */
    void setProperties(Properties properties);
}
