package gui;

import static AdditionalMath.Additional.round;

import AdditionalMath.RobotCondition;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

import logic.Robot;


public class GameVisualizer extends JPanel {

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    private ArrayList<Timer> timers = new ArrayList<>();
    private ArrayList<RobotStatus> robotStatuses = new ArrayList<>();

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    public GameVisualizer(Robot robot, Color robotColor) {
        addRobot(robot, robotColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    public void addRobot(Robot robot, Color robotColor) {
        Timer currentTimer = initTimer();
        timers.add(currentTimer);
        RobotStatus currentRobotStatus = new RobotStatus(robotColor);
        robotStatuses.add(currentRobotStatus);
        currentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        currentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                RobotCondition newRobotCondition = robot
                        .onModelUpdateEvent(currentRobotStatus.m_robotPositionX, currentRobotStatus.m_robotPositionY, currentRobotStatus.m_robotDirection,
                                m_targetPositionX, m_targetPositionY);
                currentRobotStatus.m_robotPositionX = newRobotCondition.X;
                currentRobotStatus.m_robotPositionY = newRobotCondition.Y;
                currentRobotStatus.m_robotDirection = newRobotCondition.DIRECTION;
            }
        }, 0, 10);
    }

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }



    public void setRobot(Robot robot, int id, Color robotColor) {

        timers.get(id).cancel();
        Timer currentTimer = initTimer();
        RobotStatus currentRobotStatus = new RobotStatus(robotColor);

        timers.set(id, currentTimer);
        robotStatuses.set(id, currentRobotStatus);

        timers.get(id).schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        timers.get(id).schedule(new TimerTask() {
            @Override
            public void run() {
                RobotCondition newRobotCondition = robot
                        .onModelUpdateEvent(currentRobotStatus.m_robotPositionX, currentRobotStatus.m_robotPositionY, currentRobotStatus.m_robotDirection,
                                m_targetPositionX, m_targetPositionY);
                currentRobotStatus.m_robotPositionX = newRobotCondition.X;
                currentRobotStatus.m_robotPositionY = newRobotCondition.Y;
                currentRobotStatus.m_robotDirection = newRobotCondition.DIRECTION;
            }
        }, 0, 10);

    }

    protected void setTargetPosition(Point p) {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < robotStatuses.size(); ++i) {
            RobotStatus robotStatus = robotStatuses.get(i);
            Graphics2D g2d = (Graphics2D) g;
            drawRobot(g2d, round(robotStatus.m_robotPositionX), round(robotStatus.m_robotPositionY), robotStatus.m_robotDirection, robotStatus, robotStatus.color);
            drawTarget(g2d, m_targetPositionX, m_targetPositionY);
        }
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction, RobotStatus robotStatus, Color color) {
        int robotCenterX = round(robotStatus.m_robotPositionX);
        int robotCenterY = round(robotStatus.m_robotPositionY);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(color);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    class RobotStatus {
        volatile double m_robotPositionX = 100;
        volatile double m_robotPositionY = 100;
        volatile double m_robotDirection = 0;
        volatile Color color;

        public RobotStatus(Color robotColor) {
            this.color = robotColor;
        }
    }
}
