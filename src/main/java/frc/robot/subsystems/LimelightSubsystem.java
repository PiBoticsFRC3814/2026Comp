// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Meter;
import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Degree;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Inches;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import limelight.Limelight;
import limelight.networktables.LimelightPoseEstimator;
import limelight.networktables.LimelightPoseEstimator.EstimationMode;
import limelight.networktables.LimelightSettings.LEDMode;

public class LimelightSubsystem extends SubsystemBase {

  Limelight limelight;
  LimelightPoseEstimator poseEstimator;
  Pose3d cameraOffset;
  Rotation3d cameraAngle;
  
  /** Creates a new LimelightSubsystem. */
  public LimelightSubsystem() {

    Rotation3d cameraAngle = new Rotation3d(Degree.of(Constants.CAMERA_ROLL).in(Radians), 
                                            Degree.of(Constants.CAMERA_PITCH).in(Radians), 
                                            Degree.of(Constants.CAMERA_YAW).in(Radians));

    Pose3d cameraOffset = new Pose3d(Inches.of(Constants.CAMERA_X_OFFSET).in(Meter), //where limelight is in comparison to the center of the robot
                                     Inches.of(Constants.CAMERA_Y_OFFSET).in(Meter),
                                     Inches.of(Constants.CAMERA_Z_OFFSET).in(Meter),
                                     cameraAngle);

    limelight = new Limelight("limelight");
    limelight.getSettings()
             .withLimelightLEDMode(LEDMode.PipelineControl)
             .withCameraOffset(cameraOffset)
             .save();
    
    poseEstimator = limelight.createPoseEstimator(EstimationMode.MEGATAG2);

  }

  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
