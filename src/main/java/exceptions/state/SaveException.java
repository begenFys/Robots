package exceptions.state;

/**
 * Исключение, возникающее при ошибке сохранения конфига состояний.
 */
public class SaveException extends Exception {
    public SaveException(String message) {
        super(message);
    }
}
