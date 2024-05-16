package gui.windows.game;

import model.GameModel;
import state.WindowState;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Окно отображения координат
 */
public class CoordinateWindow extends JInternalFrame implements PropertyChangeListener, WindowState {
    private final TextArea text = new TextArea();

    public CoordinateWindow(GameModel model) {
        super("Координаты", true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(text, BorderLayout.CENTER);
        panel.setSize(200, 400);
        getContentPane().add(panel);
        pack();
        model.addNewListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameModel model = (GameModel) evt.getSource();
        String newLabelText = "Target PositionX = " + model.getTargetPositionX() + "\n" +
                "Target PositionY = " + model.getTargetPositionY() + "\n" +
                "Robot PositionX = " + model.getRobotPositionX() + "\n" +
                "Robot PositionY = " + model.getRobotPositionY() + "\n" +
                "Robot Direction = " + model.getRobotDirection();
        text.setText(newLabelText);
    }

    @Override
    public String getPrefix() {
        return "coordinate";
    }
}
