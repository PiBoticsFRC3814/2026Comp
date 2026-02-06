// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
//`import frc.robot.subsystems.GyroSwerveDrive;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import swervelib.SwerveInputStream;
//import swervelib.simulation.ironmaple.simulation.drivesims.GyroSimulation;

import java.io.File;

import com.ctre.phoenix6.SignalLogger;
//import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
//import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  //public final ADIS16470_IMU m_gyro = new ADIS16470_IMU();
  
  //calls all the JSON files for swervesubsystem
  private final SwerveSubsystem       drivebase  = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                                "swerve/neo"));

  
  // Replace with CommandPS4Controller or CommandJoystick if needed
 XboxController driveController = new XboxController(0);
 //CommandGenericHID ButtonBoard1 = new CommandGenericHID(OperatorConstants.kOperatorControllerPort1);
 //CommandGenericHID ButtonBoard2 = new CommandGenericHID(OperatorConstants.kOperatorControllerPort2);

 //Gets controller imputs and gives valiues to drive system
 SwerveInputStream driveDirectAngle = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                            () -> driveController.getLeftY() * -1,
                                                            () -> driveController.getLeftX() * -1)
                                                        .withControllerRotationAxis(driveController::getRightX)
                                                        .deadband(OperatorConstants.DEADBAND)
                                                        .scaleTranslation(0.8)
                                                        .allianceRelativeControl(true)
                                                        .withControllerHeadingAxis(()->driveController.getRightX()*1,
                                                                                   ()->driveController.getRightY()*1)
                                                           .headingWhile(true);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    // Setup Data Logging
   /* DriverStation.startDataLog(DataLogManager.getLog());
    SignalLogger.setPath("/media/PiBotics_Logging/");

    DataLogManager.start();
    SignalLogger.start(); */

    configureBindings();// no buttons here they go later
  }
 
  
  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Joysticks}.
   */

 private void configureBindings() { //button mappings go here
    Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle);
    drivebase.setDefaultCommand(driveFieldOrientedDirectAngle);

/*     new JoystickButton(driveController, XboxController.Button.kA.value)
        .whileTrue(drivebase.zeroGyroCommand()); EXAMPLE BUTTON MAPPING */ 

  }

  public void setupShuffleboard() {
    Shuffleboard.getTab("test")
    .add("Pose", drivebase.getSwerveDrive().getPose())
    .withWidget(BuiltInWidgets.kField);
  }
}

