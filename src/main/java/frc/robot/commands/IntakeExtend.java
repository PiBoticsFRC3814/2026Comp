// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeMovement;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakeExtend extends Command {

  IntakeMovement m_IntakeMovement;

  public double speed;
  private Timer timeguy = new Timer();

  /** Creates a new IntakeExtend. */
  public IntakeExtend(IntakeMovement intakemovement) {
    m_IntakeMovement = intakemovement;

    addRequirements(intakemovement);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    speed = Constants.INTAKE_EXTEND_SPEED;
    timeguy.reset();
    timeguy.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_IntakeMovement.downies();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_IntakeMovement.STOP();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (timeguy.get() > Constants.INTAKE_DOWNIES_TIME) {
      m_IntakeMovement.STOP();
      return true;
    } else{
      return false;
    }
  }
}
