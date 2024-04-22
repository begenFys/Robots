package gui;
import java.util.Properties:

public interface WindowPersistence {
    void saveState(Properties props);
    void restoreState(Properties props);
}