package exceptions.state;

/**
 * Исключение, возникающее при ошибке загрузки конфига состояний.
 */
public class LoadException extends Exception {
    public LoadException(String message) {
        super(message);
    }
}