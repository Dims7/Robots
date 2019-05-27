package logic;


import static AdditionalMath.Additional.angleTo;
import static AdditionalMath.Additional.applyLimits;
import static AdditionalMath.Additional.asNormalizedRadians;
import static AdditionalMath.Additional.distance;

import AdditionalMath.RobotCondition;

public class RightLogic implements Robot {

  private static final double maxVelocity = 0.1;
  private static final double maxAngularVelocity = 0.005;

  @Override
  public RobotCondition onModelUpdateEvent(double m_robotPositionX, double m_robotPositionY,
                                           double m_robotDirection, double m_targetPositionX, double m_targetPositionY) {
    double distance = distance(m_targetPositionX, m_targetPositionY,
            m_robotPositionX, m_robotPositionY);
    if (distance < 0.5) {
      return new RobotCondition(m_robotPositionX, m_robotPositionY, m_robotDirection);
    }
    double velocity = maxVelocity;
    double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
    double angularVelocity = 0;
    double diffBetwenRobotAndTargetDitection = angleToTarget - m_robotDirection;
    double pogreshnost = 0.025d;
    if (diffBetwenRobotAndTargetDitection >= Math.PI
            || diffBetwenRobotAndTargetDitection >= -Math.PI && diffBetwenRobotAndTargetDitection < pogreshnost)
      angularVelocity = -maxAngularVelocity;
    if (diffBetwenRobotAndTargetDitection < -Math.PI
            || diffBetwenRobotAndTargetDitection > pogreshnost && diffBetwenRobotAndTargetDitection < Math.PI) {
      angularVelocity = maxAngularVelocity;
    }
    int duration = 10;
    if (Math.abs(diffBetwenRobotAndTargetDitection) > 0.5d) {
      duration = 1;
    }

    return moveRobot(maxVelocity, angularVelocity, duration, m_robotPositionX, m_robotPositionY,
            m_robotDirection);
  }

  @Override
  public RobotCondition moveRobot(double velocity, double angularVelocity, double duration,
                                  double m_robotPositionX, double m_robotPositionY, double m_robotDirection) {
    velocity = applyLimits(velocity, 0, maxVelocity);
    angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
    double newX = m_robotPositionX + velocity / angularVelocity *
            (Math.sin(m_robotDirection + angularVelocity * duration) -
                    Math.sin(m_robotDirection));
    if (!Double.isFinite(newX)) {
      newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
    }
    double newY = m_robotPositionY - velocity / angularVelocity *
            (Math.cos(m_robotDirection + angularVelocity * duration) -
                    Math.cos(m_robotDirection));
    if (!Double.isFinite(newY)) {
      newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
    }
    m_robotDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
    return new RobotCondition(newX, newY, m_robotDirection);
  }

}