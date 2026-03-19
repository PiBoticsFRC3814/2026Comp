// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.Timer;
import swervelib.math.Matter;


/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  // Controller Constants

  //Robot Constants (weight, size)
  public static final double ROBOT_MASS = (148 - 20.3) * 0.453592; // mass in kilograms
  public static final Matter CHASSIS    = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
  public static final double LOOP_TIME  = 0.13; //s, 20ms + 110ms sprk max velocity lag.  this is used to slowdown non-important can calls i think
  public static final double MAX_HYP_SPEED  = Units.feetToMeters(14.5); // this is max theoretical speed not the speed we want to limit drivers to.
  // Maximum speed of the robot in meters per second, used to limit acceleration.

  //Robot Speed limits
  public static final double MAX_VELOCITY = Units.feetToMeters(10);
  public static final double MAX_ANGLE_VELOCITY = Units.degreesToRadians(120); // note that the maximum rotation speed will change what is needed in PID of controller properties
  public static final double DRIVE_ACCEL_LIMIT = Units.feetToMeters(0.1); //look into acceleration limits to limit acceleration and deceleration values on the motor side in adition to slew n the controller side.
  public static final double ANGLE_ACCEL_LIMIT = Units.degreesToRadians(0.1);

  //Robot Starting Positions
  public static final double[] ROBOT_BLUE_LEFT_START = {1,4,0};
  public static final double[] ROBOT_BLUE_RIGHT_START = {1,4,0};
  public static final double[] ROBOT_BLUE_CENTER_START = {3.527,4.024,0};

  public static final double[] ROBOT_RED_LEFT_START = {16,4,180};
  public static final double[] ROBOT_RED_RIGHT_START = {16,4,180};
  public static final double[] ROBOT_RED_CENTER_START = {16,4,180};


  /*Joystick Slew Rate Contsants  
  Note consideration may be needed on alternate slew rates for different "max speeds"
  Note that right now joystick slew effects the joystick inputs NOT the motor outputs.  this means that the slew deos not effect acceleration/deceleration directly
  it effects the stick values from -1 to +1 and limits how fast to gets to the real stick value the user has input.
  for more fine control we need to add acceleration limiters on the drive subsystem side of things.*/

  public static final double JOYSTICK_X_SLEW_POS = 4.0;
  public static final double JOYSTICK_Y_SLEW_POS = 4.0;
  public static final double JOYSTICK_X_SLEW_NEG = -4.0;  
  public static final double JOYSTICK_Y_SLEW_NEG = -4.0;

  //Camera Offset Constants in inches from the center of the robot.
  public static final double CAMERA_X_OFFSET = 5.0; //in inches
  public static final double CAMERA_Y_OFFSET = 5.0; //in inches
  public static final double CAMERA_Z_OFFSET = 5.0; //in inches
  public static final double CAMERA_ROLL = 0.0; // in degrees
  public static final double CAMERA_PITCH = 0.0; // in degrees the pitch is probabbly going to be the only angle we need unlessmounting is done weirdly
  public static final double CAMERA_YAW = 0.0; // in degrees

  //LimeLight useful constants
  public static final double TARGET_MINIMUM_DIST = Units.feetToMeters(2);
  public static final double TARGET_MAXIMUM_DIST = Units.feetToMeters(10);


  //standard deviation camera constants
  public static final double CAM_X_STD = 0.02013123117;
  public static final double CAM_Y_STD = 0.06917364986;
  public static final double CAM_THETA_STD = Math.toRadians(2.070498278);

  // standard deviation of swerve odometry
  public static final double ODOM_X_STD = 0.0;
  public static final double ODOM_Y_SYD = 0.0;
  public static final double ODOM_THETA_STD = Math.toRadians(12.0);

  // Intake Movement
  public static final double INTAKE_RETRACT_SPEED = 0.1;
  public static final double INTAKE_EXTEND_SPEED = 0.1;

  // Conveyor
  public static final double CONVEYOR_SPEED = 0.5;
  public static final double CONVEYOR_OUTTAKE_SPEED = 0.5;


  // Shooter Intake
  public static final double SHOOTER_INTAKE_SPEED = -0.7;
  public static final double SHOOTER_OUTTAKE_SPEED = -0.7;

  // Intake Roller
  public static final double INTAKE_ROLLER_SPEED = -1.0;
  public static final double INTAKE_ROLLER_OUTTAKE_SPEED = -1.0;

  //Climber
    public static final double CLIMBER_SPEED = 1.0;  



  // Shooter need to move the shooter number into this constants file.
  public static final double FIXED_SHOOT_SPEED = 3200;
  public static final double PEW_PEW_TIME = 4.0; //auto time to shoot
  public static final double INTAKE_DOWN_TIME = 2.0; //auto time to lower intake
  public static final double STALE_TARGET_DISTANCE_COUNTER = 100;
  public static final double DISTANCE_PERCENT_DIFFERENCE_TOLERANCE = 25;
  public static final double INTAKE_RUN_AUTO_TIME = 3.0;
  public static final double FIXED_FEEDING_SPEED = 0;



  
//  public static final class AutonConstants
//  {
//
//    public static final PIDConstants TRANSLATION_PID = new PIDConstants(0.7, 0, 0);
//    public static final PIDConstants ANGLE_PID       = new PIDConstants(0.4, 0, 0.01);
//  }

  public static final class DrivebaseConstants
  {
    // Hold time on motor brakes when disabled
    public static final double WHEEL_LOCK_TIME = 10; // seconds
  }

  public static class OperatorConstants
  {
    // Joystick Deadband
    public static final double DEADBAND        = 0.1;
    public static final double LEFT_Y_DEADBAND = 0.5;
    public static final double RIGHT_X_DEADBAND = 0.1;
    public static final double TURN_CONSTANT    = 6;
  }
  
   //PID and CAN IDs all in JSON files
}