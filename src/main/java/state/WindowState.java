package state;

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
}
