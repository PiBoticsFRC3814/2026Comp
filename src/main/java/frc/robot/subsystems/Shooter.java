// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Volt;

import java.io.PrintStream;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry3d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.VoltageUnit;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import yams.mechanisms.config.FlyWheelConfig;
import yams.mechanisms.velocity.FlyWheel;
import yams.motorcontrollers.SmartMotorController;
import yams.motorcontrollers.SmartMotorControllerConfig;
import yams.motorcontrollers.SmartMotorControllerConfig.ControlMode;
import yams.motorcontrollers.SmartMotorControllerConfig.MotorMode;
import yams.motorcontrollers.SmartMotorControllerConfig.TelemetryVerbosity;
import yams.motorcontrollers.local.SparkWrapper;

public class Shooter extends SubsystemBase {

  private double Distance = 0.0;
  private double shootSpeed = 0.0;
  private double ManualShooterRPM = SmartDashboard.getNumber("ManShooterRPM", 0.0);
  private ChassisSpeeds driveInhib = new ChassisSpeeds(0,0,0);

  private SmartMotorController motor; //this should not be need the config we setup is placed into the sparkSmartMotorController we have below

  private SmartMotorControllerConfig smcConfig = new SmartMotorControllerConfig(this)
  .withControlMode(ControlMode.CLOSED_LOOP)
  // Feedback Constants (PID Constants)
  .withClosedLoopController(1e-4, 0.0, 2e-4) //used pid values from 2024.
  //.withSimClosedLoopController(1, 0, 0) // sim
  // Feedforward Constants
  .withFeedforward(new SimpleMotorFeedforward(12, 0, 0)) //not sure what these should be
  //.withSimFeedforward(new SimpleMotorFeedforward(0, 0, 0)) // sim
  // Telemetry name and verbosity level
  //.withTelemetry("ShooterMotor", TelemetryVerbosity.HIGH)
  // Gearing from the motor rotor to final shaft.
  .withGearing(0.5)
  // Motor properties to prevent over currenting.
  .withMotorInverted(false)
  .withIdleMode(MotorMode.COAST)
  .withStatorCurrentLimit(Amps.of(40)); // need to figure out the current draw when pulling a ball through the flywheeel.  

  // Vendor motor controller object
  private SparkMax spark = new SparkMax(44, MotorType.kBrushless);

  // Create our SmartMotorController from our Spark and config with the NEO.
  private SmartMotorController sparkSmartMotorController = new SparkWrapper(spark, DCMotor.getNEO(1), smcConfig); //does the getNeo need to be 2?  does it matter?
  
  private final FlyWheelConfig shooterConfig = new FlyWheelConfig(sparkSmartMotorController)
  // Diameter of the flywheel.
  .withDiameter(Inches.of(4))
  // Mass of the flywheel.
  .withMass(Pounds.of(1))
  // Maximum speed of the shooter.
  .withUpperSoftLimit(RPM.of(5000)) // i think the neo can go faster but limit to round number of 5000 -- it can hit 5676 not quite 6000.
  // Telemetry name and verbosity for the arm.
  .withTelemetry("ShooterMech", TelemetryVerbosity.HIGH);

  // Shooter Mechanism
  private FlyWheel shooter = new FlyWheel(shooterConfig);

  private SwerveSubsystem drive;

   /**
   * Set the shooter velocity setpoint.
   *
   * @param speed Speed to set
   */
  public void setDesiredVelocity() {
    // limelight distance checks here
    Distance = drive.shareTargetDistance;  
    // may want to change how this is currently done right now i am allways getting the distance to target in the periodic of the swerve system even if we dont need it
    // it might be less intrusive on the code times to instead call the getTargetDistance() here instead of allwyas sicne we may only need it when trying to shoot.
    //not sure what option is better since it may be better to allways gettarget distances since we want the driverstationto contiuosly update the humans with "in range" information.
    
    //smart dashbord stuff for troublwshooting remove when we see this number is getting here
    SmartDashboard.putNumber("shooter Distance", Distance);

    //add math stuff for distance to rpm needs.
    shootSpeed = Distance*1; //math is fun  Distance would be the "X" in the f(x) function that we come up with through testing.

    sparkSmartMotorController.setVelocity(RPM.of(shootSpeed)); //math to do to distance to make rpm go
  }

  public double getDesiredVelocity(){
    return shootSpeed;
  }
  
  public void driveInhibit(){
    drive.drive(driveInhib);
  }

  public void shootSpeed(double speed){
    sparkSmartMotorController.setVelocity(RPM.of(speed));
  }

  public double getShootSpeed(){
    return sparkSmartMotorController.getRotorVelocity().in(RPM);
  }

  public void STOP(){
    spark.stopMotor();
  }


  /**
   * Set the dutycycle of the shooter.
   *
   * @param dutyCycle DutyCycle to set.
   * @return {@link edu.wpi.first.wpilibj2.command.RunCommand}
   */
  public Command set(double dutyCycle) {return shooter.set(dutyCycle);}

  /** Creates a new ExampleSubsystem. */
  public Shooter(SwerveSubsystem swerveDrive) {
    drive = swerveDrive;
  }

  

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    shooter.updateTelemetry();
    SmartDashboard.putNumber("ShooterSpeed", getShootSpeed());
  }
}