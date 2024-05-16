package log;

public class LogEntry {
    private LogLevel mLogLevel;
    private String mStrMessage;

    public LogEntry(LogLevel logLevel, String strMessage) {
        mStrMessage = strMessage;
        mLogLevel = logLevel;
    }

    public String getMessage() {
        return mStrMessage;
    }

    public LogLevel getLevel() {
        return mLogLevel;
    }
}

