package log;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 */
public class LogWindowSource {
    private int m_iQueueLength;

    private ArrayDeque<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayDeque<LogEntry>(iQueueLength);
        m_listeners = new ArrayList<LogChangeListener>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        if (m_iQueueLength == size())
            m_messages.removeFirst();
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_messages.addLast(entry);
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

    private int size() {
        return m_messages.size();
    }

    // Надо переписывать под очередь
    /*
    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= m_messages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_messages.size());

        ArrayList<LogEntry> subList = new ArrayList<>();
        for (int i = 0; i < startFrom; i++) {
            subList.add(m_messages.);
        }
        return m_messages.subList(startFrom, indexTo);
    }
       */
    public Iterable<LogEntry> all() {
        return m_messages;
    }
}
