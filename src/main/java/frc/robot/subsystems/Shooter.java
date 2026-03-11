// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class Shooter extends SubsystemBase {

  private double Distance = 0.0;
  private double shootSpeed = 0.0;
  private ChassisSpeeds driveInhib = new ChassisSpeeds(0,0,0);
  
  private SparkMax spark = new SparkMax(44, MotorType.kBrushless);
  private SparkMaxConfig config = new SparkMaxConfig();
  private RelativeEncoder shooterEncoder = spark.getEncoder();
  private SparkClosedLoopController motorController = spark.getClosedLoopController();
  
  // Shooter Mechanism

  private SwerveSubsystem drive;

  public Shooter(SwerveSubsystem swerveDrive) {
    drive = swerveDrive;
    
    config.smartCurrentLimit(40,40)
          .idleMode(IdleMode.kCoast)
          .inverted(false)
          .voltageCompensation(12.0)
          ;
    config.closedLoop//.feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                     .p(4.5e-4)
                     .i(0.0001)
                     .d(1.5e-4)
                     .iZone(0.1)
                     .outputRange(0, 1)
                     .feedForward.kS(1.0)
                                 .kV(0.0021)
                                 .kA(0)//velocity control does not use this acceleration term according to REV docs.  MAXMotion Velocity control does.  probably can just use normal velocity control since theis is not a complex mechanism
                                 ;
    config.encoder.velocityConversionFactor(1); //this will allow us to set the speed of the flywheel directly instead of the motor rpm (right now gear is 1:1 so really no difference)
    
    spark.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  /**
   * Set the shooter velocity setpoint.
   *
   * @param speed Speed to set
   */
  public void setDesiredVelocity() {
    // limelight distance checks here
    Distance = drive.shareTargetDistance;  
    //may want to change how this is currently done right now i am allways getting the distance to target in the periodic of the swerve system even if we dont need it
    //it might be less intrusive on the code times to instead call the getTargetDistance() here instead of allwyas sicne we may only need it when trying to shoot.
    //not sure what option is better since it may be better to allways gettarget distances since we want the driverstationto contiuosly update the humans with "in range" information.

    //add math stuff for distance to rpm needs.
    shootSpeed = Distance*1; //math is fun  Distance would be the "X" in the f(x) function that we come up with through testing.

    motorController.setSetpoint(shootSpeed, ControlType.kVelocity); //make moter go the speed wee calculated it to go
  }

  public double getDesiredVelocity(){
    return shootSpeed;
  }
  
  public void driveInhibit(){
    drive.drive(driveInhib);
  }

  public void shootSpeed(double speed){
    motorController.setSetpoint(speed, ControlType.kVelocity);
  }

  public double getShootSpeed(){
    return shooterEncoder.getVelocity();
  }
  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("ShooterSpeed", getShootSpeed());
  }

  public void STOP(){
    spark.set(0.0);
  }


public void setDefaultCommand(Object stop) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setDefaultCommand'");
}

}