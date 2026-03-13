package frc.robot.subsystems;


import static edu.wpi.first.units.Units.Amps;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ClimberSubsystem extends SubsystemBase
{
  private final SparkMax m_climber = new SparkMax(49, MotorType.kBrushless);

  private final DCMotor m_ClimberMotor = DCMotor.getNEO(1);

  public ClimberSubsystem()
  {
    SparkMaxConfig config = new SparkMaxConfig();
    config
        .inverted(false)
        .smartCurrentLimit(100)
        .idleMode(IdleMode.kBrake);
    m_climber.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //not deprecated.  for some reason spark put these config modes in a different folder (revrobotics.BLAmode instead of revrobotics.spark.config.BLAmode) for some reason both "files" still exist in the different folder and itellesence for some reason grabbed the deprecated imnport instead of the preferred import

  }
  

  public Command setClimb (double speed)
  {
    return run(() -> {
      m_climber.set(speed);
    });
  }

  public Command down(double speed)
  {
    return setClimb(-speed);
  }

  public Command up(double speed)
  {
    return setClimb(speed);
  }

  public Command stop(){
    return setClimb(0);
  }
}