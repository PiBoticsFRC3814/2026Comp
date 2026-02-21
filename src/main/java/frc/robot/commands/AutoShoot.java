// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
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

 Shooter  m_shooter;
 ShooterIntakeSubsystem m_shooterIntake;
 Conveyor m_conveyor;

 double shooterSpeed;
 double desiredSpeed;
 

  public AutoShoot(Shooter shooter, ShooterIntakeSubsystem shooterIntake, Conveyor conveyor) {
    // Use addRequirements() here to declare subsystem dependencies.
  
    m_shooter = shooter;
    m_shooterIntake = shooterIntake;
    m_conveyor = conveyor;

    addRequirements(shooter,shooterIntake,conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooter.setDesiredVelocity();
    shooterSpeed = m_shooter.getActualVelocity();
    desiredSpeed = m_shooter.getDesiredVelocity();
    if (shooterSpeed <= desiredSpeed-(desiredSpeed*0.1) || shooterSpeed >= desiredSpeed+(desiredSpeed*0.1)){
      m_shooterIntake.stop();
      m_conveyor.stop();
    }else{
      m_shooterIntake.in(Constants.SHOOTER_INTAKE_SPEED);
      m_conveyor.in(Constants.CONVEYOR_SPEED);
    }
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
