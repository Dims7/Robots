package gui;

import logic.Robot;
import logic.StandardRobotLogic;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    public Robot robot = new StandardRobotLogic();
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer(robot);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public GameVisualizer getGameVisualizer() {
        return m_visualizer;
    }
    public void setRobot(Robot robot) {
        getGameVisualizer().setRobot(robot);
    }
}
