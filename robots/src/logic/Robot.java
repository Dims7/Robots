package logic;

import AdditionalMath.RobotCondition;

public interface Robot {
    RobotCondition onModelUpdateEvent(double m_robotPositionX, double m_robotPositionY,
                                      double m_robotDirection, double m_targetPositionX, double m_targetPositionY);

    RobotCondition moveRobot(double velocity, double angularVelocity, double duration,
                             double m_robotPositionX, double m_robotPositionY, double m_robotDirection);

}
