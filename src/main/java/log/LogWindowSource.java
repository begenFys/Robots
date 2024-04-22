package log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Источник данных для лога, который хранит и управляет сообщениями лога.
 */
public class LogWindowSource {
    private int m_iQueueLength;
    private ArrayList<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    /**
     * Создает новый источник данных для лога с указанной длиной очереди сообщений.
     * @param iQueueLength максимальная длина очереди сообщений
     */
    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayList<LogEntry>(iQueueLength);
        m_listeners = new ArrayList<LogChangeListener>();
    }

    /**
     * Регистрирует слушателя для обновлений лога.
     * @param listener слушатель для регистрации
     */
    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    /**
     * Отменяет регистрацию слушателя для обновлений лога.
     * @param listener слушатель для отмены регистрации
     */
    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    /**
     * Добавляет новую запись в лог с указанным уровнем и сообщением.
     * @param logLevel уровень логирования
     * @param strMessage текст сообщения
     */
    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.add(entry);
        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null) {
            synchronized (m_listeners) {
                if (m_activeListeners == null) {
                    activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                    m_activeListeners = activeListeners;
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
        return m_messages.size();
    }

    /**
     * Возвращает диапазон сообщений из лога начиная с указанного индекса.
     * @param startFrom индекс, с которого начинать выборку
     * @param count количество сообщений для выборки
     * @return диапазон сообщений из лога
     */
    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= m_messages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());
        return m_messages.subList(startFrom, indexTo);
    }

    /**
     * Возвращает все сообщения из лога.
     * @return все сообщения из лога
     */
    public Iterable<LogEntry> all() {
        return m_messages;
    }
}