// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterIntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShootSetRPM extends Command {
  Shooter m_shooter;
  Conveyor m_conveyor;
  ShooterIntakeSubsystem m_shooterintake;

  public double speed = Constants.FIXED_SHOOT_SPEED;
  public double actualSpeed;
  public double conveyorspeed = Constants.CONVEYOR_SPEED;
  public double intakespeed = Constants.SHOOTER_INTAKE_SPEED;

  /** Creates a new IntakeRoller. */
public ShootSetRPM(Shooter shooter, Conveyor conveyor, ShooterIntakeSubsystem shooterintake) {
    m_shooter = shooter;
    m_conveyor = conveyor;
    m_shooterintake = shooterintake;

     addRequirements(shooter, conveyor, shooterintake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
        actualSpeed = 0.0; 


  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println(speed);
    m_shooter.shootSpeed(speed);
    actualSpeed = m_shooter.getShootSpeed();
    if (actualSpeed*0.85 >= speed || actualSpeed*1.15 <= speed){
      m_conveyor.STOP();
      m_shooterintake.STOP();
    }
    else{
      m_conveyor.intake(conveyorspeed);
      m_shooterintake.intake(intakespeed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_conveyor.STOP();
    m_shooterintake.STOP();
    m_shooter.STOP();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
