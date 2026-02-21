// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.IntakeMovement;
import frc.robot.subsystems.IntakeRollerSubsystem;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterIntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AutoShoot extends Command {
  /** Creates a new AutoShoot. */

 Shooter  m_Shooter;

 AngularVelocity shooterspeed;
 

  public AutoShoot(Shooter shooter, ShooterIntakeSubsystem shooterIntakeSubsystem, Conveyor conveyor) {
    // Use addRequirements() here to declare subsystem dependencies.
  
    m_Shooter = shooter;

    addRequirements(shooter,shooterIntakeSubsystem,conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterspeed = m_Shooter.getVelocity();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
