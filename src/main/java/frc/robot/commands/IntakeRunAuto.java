// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeRollerSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakeRunAuto extends Command {
  /** Creates a new IntakeRunAuto. */
  IntakeRollerSubsystem m_IntakeRollers;

  private Timer timeguy = new Timer();
  public double speed;

  public IntakeRunAuto(IntakeRollerSubsystem intakerollers) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_IntakeRollers = intakerollers;

      addRequirements(intakerollers);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    speed = Constants.INTAKE_ROLLER_SPEED;
    timeguy.reset();
    timeguy.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_IntakeRollers.intake(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_IntakeRollers.STOP();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (timeguy.get() > Constants.INTAKE_RUN_AUTO_TIME) {
      m_IntakeRollers.STOP();
      return true;
    }else{
      return false;
    }
  }
}
