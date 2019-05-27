package gui;

import logic.RightLogic;
import logic.Robot;
import logic.StandardRobotLogic;

import java.awt.*;
import java.util.Random;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame {
    private final GameVisualizer m_visualizer;
    public Robot robot = new StandardRobotLogic();

    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer(robot, Color.RED);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public GameVisualizer getGameVisualizer() {
        return m_visualizer;
    }

    public void addRobot(Robot robot) {
        Random random = new Random();
        float r = random.nextFloat();
//        float g = random.nextFloat();
//        float b = random.nextFloat();
        getGameVisualizer().addRobot(robot, Color.getHSBColor(r, 1, 1));
    }

    public void setRobot(Robot robot) {
        Random random = new Random();
        float r = random.nextFloat();
        getGameVisualizer().setRobot(robot, 0, Color.getHSBColor(r, 1, 1));
    }
}
