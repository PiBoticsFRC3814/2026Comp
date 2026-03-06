// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Pounds;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Volt;
import static edu.wpi.first.units.Units.Volts;

import java.io.PrintStream;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

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
  private ChassisSpeeds driveInhib = new ChassisSpeeds(0,0,0);
  private SmartMotorControllerConfig smcConfig = new SmartMotorControllerConfig(this)
  .withControlMode(ControlMode.CLOSED_LOOP)
  .withVoltageCompensation(Volts.of(12))
  // Feedback Constants (PID Constants)
  .withClosedLoopController(1e-4, 0.0, 2e-4) //used pid values from 2024.
  // Feedforward Constants
  .withFeedforward(new SimpleMotorFeedforward(1, 0.19, 0)) //ks should be volts needed to barely make the flywheel spin.  kv should be voltes per RPM ideally 12/5000 ish would get you the v/RPM
  // Telemetry name and verbosity level
  //.withTelemetry("ShooterMotor", TelemetryVerbosity.LOW)
  // Gearing from the motor rotor to final shaft.
  .withGearing(1)
  // Motor properties to prevent over currenting.
  .withMotorInverted(false)
  .withIdleMode(MotorMode.COAST)
  .withStatorCurrentLimit(Amps.of(40)); // need to figure out the current draw when pulling a ball through the flywheeel. 


  // Vendor motor controller object
  private SparkMax spark = new SparkMax(44, MotorType.kBrushless);
  private SparkMaxConfig config = new SparkMaxConfig();
  private RelativeEncoder shooterEncoder;

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
  .withTelemetry("ShooterMech", TelemetryVerbosity.LOW);

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
    //sparkSmartMotorController.getMechanismVelocity().in(RPM);
    //sparkSmartMotorController.getMeasurementVelocity().in(RPM);
    //sparkSmartMotorController.getRotorVelocity().in(RPM);
    return shooterEncoder.getVelocity();

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

  public Shooter(SwerveSubsystem swerveDrive) {
    drive = swerveDrive;
    shooterEncoder = spark.getEncoder();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    shooter.updateTelemetry();
    SmartDashboard.putNumber("ShooterSpeed", getShootSpeed());
  }
}