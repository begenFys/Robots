package log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Источник данных для лога, который хранит и управляет сообщениями лога.
 */
public class LogWindowSource {
    private int mIQueueLength;
    private ArrayList<LogEntry> mMessages;
    private final ArrayList<LogChangeListener> mListeners;
    private volatile LogChangeListener[] mActiveListeners;

    /**
     * Создает новый источник данных для лога с указанной длиной очереди сообщений.
     * @param iQueueLength максимальная длина очереди сообщений
     */
    public LogWindowSource(int iQueueLength) {
        mIQueueLength = iQueueLength;
        mMessages = new ArrayList<LogEntry>(iQueueLength);
        mListeners = new ArrayList<LogChangeListener>();
    }

    /**
     * Регистрирует слушателя для обновлений лога.
     * @param listener слушатель для регистрации
     */
    public void registerListener(LogChangeListener listener) {
        synchronized (mListeners) {
            mListeners.add(listener);
            mActiveListeners = null;
        }
    }

    /**
     * Отменяет регистрацию слушателя для обновлений лога.
     * @param listener слушатель для отмены регистрации
     */
    public void unregisterListener(LogChangeListener listener) {
        synchronized (mListeners) {
            mListeners.remove(listener);
            mActiveListeners = null;
        }
    }

    /**
     * Добавляет новую запись в лог с указанным уровнем и сообщением.
     * @param logLevel уровень логирования
     * @param strMessage текст сообщения
     */
    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        mMessages.add(entry);
        LogChangeListener[] activeListeners = mActiveListeners;
        if (activeListeners == null) {
            synchronized (mListeners) {
                if (mActiveListeners == null) {
                    activeListeners = mListeners.toArray(new LogChangeListener[0]);
                    mActiveListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    /**
     * Возвращает количество сообщений в логе.
     * @return количество сообщений в логе
     */
    public int size() {
        return mMessages.size();
    }

    /**
     * Возвращает диапазон сообщений из лога начиная с указанного индекса.
     * @param startFrom индекс, с которого начинать выборку
     * @param count количество сообщений для выборки
     * @return диапазон сообщений из лога
     */
    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= mMessages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, mMessages.size());
        return mMessages.subList(startFrom, indexTo);
    }

    /**
     * Возвращает все сообщения из лога.
     * @return все сообщения из лога
     */
    public Iterable<LogEntry> all() {
        return mMessages;
    }
}