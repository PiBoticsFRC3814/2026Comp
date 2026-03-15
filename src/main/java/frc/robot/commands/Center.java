// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html

public class Center extends InstantCommand {
  double[] startingPositon = {0,0};
  boolean blueSide = true;

  SwerveSubsystem m_drive;
  
    public Center(SwerveSubsystem drive) {
      m_drive = drive;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(drive);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    blueSide = !isRedAlliance();
    if (blueSide){
      startingPositon = Constants.ROBOT_BLUE_CENTER_START;
    } else {
      startingPositon = Constants.ROBOT_RED_CENTER_START;
    }
    m_drive.setStartingPosition(startingPositon);
  }

  private boolean isRedAlliance()
  {
    var alliance = DriverStation.getAlliance();
    return alliance.isPresent() ? alliance.get() == DriverStation.Alliance.Red : false;
  }

}
