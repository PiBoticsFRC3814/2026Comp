// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.DashboardShootRPM;
import frc.robot.commands.FixedShootRPM;
import frc.robot.commands.FullShoot;
import frc.robot.commands.DumbShootAuton;
import frc.robot.commands.Feeding;
import frc.robot.commands.IntakeExtend;
import frc.robot.commands.IntakeRunAuto;
import frc.robot.commands.ShootSetRPM;
import frc.robot.commands.ShootStop;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.IntakeMovement;
import frc.robot.subsystems.IntakeRollerSubsystem;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterIntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import swervelib.SwerveInputStream;

import static edu.wpi.first.units.Units.Meters;

import java.io.File;
import java.text.ChoiceFormat;

import com.ctre.phoenix6.SignalLogger;
import com.fasterxml.jackson.databind.util.Named;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.StadiaController.Button;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DumbShootAuton;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public final IntakeMovement m_intake = new IntakeMovement();
  public final IntakeRollerSubsystem m_rollerMotor = new IntakeRollerSubsystem();
  public final Conveyor m_conveyor = new Conveyor();
  public final ShooterIntakeSubsystem m_ShooterIntake = new ShooterIntakeSubsystem();
  public final ClimberSubsystem m_ClimberSubsystem = new ClimberSubsystem();

  public SendableChooser<String> chooserFirst = new SendableChooser<>();

  //calls all the JSON files for swervesubsystem
  private final SwerveSubsystem       drivebase  = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                                "swerve/neo"));

  //putting the shooter call here since it needs to be after drivebase to get the drivebase into the shooter for limelight/pose stuffs.
  public final Shooter m_shooter = new Shooter(drivebase);

 
  // Replace with CommandPS4Controller or CommandJoystick if needed
 CommandXboxController driveController = new CommandXboxController(0);
 CommandXboxController OperatorController = new CommandXboxController(1);

 //slew rate MUST have slew rate limits for BOTH X and Y axises separate.  if you combine the X and Y into one limiter it makes the robot move diagonally.
 //Note that the slew rate limiter is loooking at joystick values NOT motor speed values thus the positive and negative rates apply to joystick inputs not motor accelerations
 //this means different rates dont work in an acceleration vs deceleration of mors but instead a positive vs negative stick direction.
 SlewRateLimiter driveXSlewRateLimit = new SlewRateLimiter(Constants.JOYSTICK_X_SLEW_POS, Constants.JOYSTICK_X_SLEW_NEG, 0.0);
 SlewRateLimiter driveYSlewRateLimit = new SlewRateLimiter(Constants.JOYSTICK_Y_SLEW_POS, Constants.JOYSTICK_Y_SLEW_NEG, 0.0);

 //we cannot add a slew rate limit for the angle stick due to how that is implemented we must instead set speed limits for rotation rate and adjust PID for that speed limit

 
 //CommandGenericHID ButtonBoard1 = new CommandGenericHID(OperatorConstants.kOperatorControllerPort1);
 //CommandGenericHID ButtonBoard2 = new CommandGenericHID(OperatorConstants.kOperatorControllerPort2);

 //Gets controller imputs and gives values to drive system
 SwerveInputStream driveDirectAngle = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                            () -> driveYSlewRateLimit.calculate(driveController.getLeftY()) * -1,
                                                            () -> driveXSlewRateLimit.calculate(driveController.getLeftX()) * -1)
                                                        .deadband(OperatorConstants.DEADBAND)
                                                        .scaleTranslation(0.8)
                                                        .allianceRelativeControl(true)
                                                        .withControllerHeadingAxis(()->driveController.getRightX()*drivebase.headingFlipper(),
                                                                                   ()->driveController.getRightY()*drivebase.headingFlipper())
                                                        .headingWhile(true);


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    // Setup Data Logging
   DriverStation.startDataLog(DataLogManager.getLog());
    SignalLogger.setPath("/media/PiBotics/");

    DataLogManager.start("/media/PiBotics/");
    SignalLogger.start();

    //Set the default command to force the stuff to stop. --- these appear to be preventing the button commands from running?
    //m_shooter.setDefaultCommand(m_shooter.STOP());
    m_intake.setDefaultCommand(m_intake.stop());
    m_rollerMotor.setDefaultCommand(m_rollerMotor.stop());
    m_conveyor.setDefaultCommand(m_conveyor.stop());
    m_ShooterIntake.setDefaultCommand(m_ShooterIntake.stop());
    m_shooter.setDefaultCommand(new ShootStop(m_shooter));
    m_ClimberSubsystem.setDefaultCommand(m_ClimberSubsystem.stop());

    configureBindings();// no buttons here they go later

    NamedCommands.registerCommand("DumbShoot", new DumbShootAuton(m_shooter, m_conveyor, m_ShooterIntake));
    NamedCommands.registerCommand("IntakeDown", new IntakeExtend(m_intake));
    NamedCommands.registerCommand("IntakeRun", new IntakeRunAuto(m_rollerMotor));

    chooserFirst.setDefaultOption("CenterShoot", "Center Bck2Sht");
    chooserFirst.addOption("LeftShoot", "Left Mv2Sht");
    chooserFirst.addOption("RightShoot","Right Mv2Sht");
    chooserFirst.addOption("CenterShootIntakeDepot", "Center ShotItkShot");
    chooserFirst.addOption("RightShootIntakeDepot", "Right ShotItkShot");
    chooserFirst.addOption("LeftShootIntakeDepot", "Left ShotItkShot");
    chooserFirst.addOption("CenterShootIntakeCenterLft", "Center CntrPckpL");
    chooserFirst.addOption("CenterShootIntakeCenterRght", "Center CntrPckpR");
    chooserFirst.addOption("RightShootIntakeCenter", "Right CntrPckp");
    chooserFirst.addOption("LeftShootIntakeCenter", "Left CntrPckp");

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
    
// Shooter
    OperatorController.axisGreaterThan(3, 0.5).whileTrue(new Feeding(m_shooter,m_conveyor,m_ShooterIntake));
    OperatorController.button(Button.kB.value).whileTrue(new ShootSetRPM(m_shooter,m_conveyor,m_ShooterIntake));
    //OperatorController.axisLessThan(Axis.kRightTrigger.value, 0.5).whileTrue(new ShootStop(m_shooter));



// Intake Movement
    OperatorController.axisLessThan(Axis.kLeftY.value, -0.5).whileTrue(m_intake.extend(Constants.INTAKE_EXTEND_SPEED));
    OperatorController.axisGreaterThan(Axis.kLeftY.value, 0.5).whileTrue(m_intake.retract(Constants.INTAKE_RETRACT_SPEED));

// Intake Rollers
    OperatorController.axisGreaterThan(2, 0.5).whileTrue(m_rollerMotor.in(Constants.INTAKE_ROLLER_SPEED));
    OperatorController.button(Button.kLeftBumper.value).whileTrue(m_rollerMotor.out(Constants.INTAKE_ROLLER_OUTTAKE_SPEED));

  
//Climber Subsystem
      OperatorController.button(Button.kA.value).whileTrue(m_ClimberSubsystem.up(Constants.CLIMBER_SPEED));
      OperatorController.button(Button.kY.value).whileTrue(m_ClimberSubsystem.down(Constants.CLIMBER_SPEED));

  }

  public void setupShuffleboard() {
    Shuffleboard.getTab("test")
    .add("Pose", drivebase.getSwerveDrive().getPose())
    .withWidget(BuiltInWidgets.kField);
    Shuffleboard.getTab("test")
    .add("X Measure", drivebase.getSwerveDrive().getPose().getMeasureX().in(Meters));
    Shuffleboard.getTab("test")
    .add("Y Measure", drivebase.getSwerveDrive().getPose().getMeasureY().in(Meters));
    Shuffleboard.getTab("test")
    .add("Angle Measure", drivebase.getSwerveDrive().getPose().getRotation().getDegrees());

    Shuffleboard.getTab("test")
    .add("Auton", chooserFirst)
    .withWidget(BuiltInWidgets.kSplitButtonChooser)
    .withSize(3,1)
    .withPosition(0,0);

  }
  public double stickAngle() {
      return Math.toDegrees(Math.atan(driveController.getRightX()/driveController.getRightY()));
    }

  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return new PathPlannerAuto(chooserFirst.getSelected());
  }
  
   
}



