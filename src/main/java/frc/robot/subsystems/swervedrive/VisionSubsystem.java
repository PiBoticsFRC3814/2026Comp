// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swervedrive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N3;

import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import limelight.Limelight;
import limelight.networktables.PoseEstimate;
import edu.wpi.first.wpilibj.Timer;

public class VisionSubsystem extends SubsystemBase {

  private final SwerveSubsystem swerveDrive;

  /** Creates a new VisionSubsystem. */
  public VisionSubsystem(SwerveSubsystem swerveDrive) {
    this.swerveDrive = swerveDrive;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    //starting cheks to see if limelight is seeing legit data.  if no data them return and dont update vision estimators.
    //check if we have 2d pose data.  if not return and dont update vision estimators
    if (!swerveDrive.limelight.getData().targetData.getTargetStatus()){
      return;
    }
    //check if we have a pose estimate data.  if not return and dont update vision estimators
    if (!swerveDrive.poseEstimator.getPoseEstimate().get().hasData) {
      return;
    }

    //


  }
}
