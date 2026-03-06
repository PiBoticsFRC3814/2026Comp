package frc.robot.subsystems;


import static edu.wpi.first.units.Units.Amps;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class IntakeMovement extends SubsystemBase
{

  public static final double kWristMomentOfInertia = 0.00032; // kg * m^2

  private final SparkMax m_IntakeMovement = new SparkMax(41, MotorType.kBrushless);

  //private final DCMotor m_rollerMotorGearbox = DCMotor.getNEO(1);

  public IntakeMovement()
  {
    SparkMaxConfig config = new SparkMaxConfig();
    config
        .inverted(false)
        .smartCurrentLimit(100)
        .idleMode(IdleMode.kCoast);
            m_IntakeMovement.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

      
    
  }
  public Command setIntakeMovement (double IntakeMovementSpeed)
  {
    return run(() -> {
      m_IntakeMovement.set(IntakeMovementSpeed);
    });
  }

  public Command extend(double IntakeMovementSpeed)
  {
    return setIntakeMovement(-IntakeMovementSpeed);
  }

  public Command retract(double IntakeMovementSpeed)
  {
    return setIntakeMovement(IntakeMovementSpeed);
  }

  public Command stop(){
    return setIntakeMovement(0);
  }

  public Current getCurrent()
  {
    return Amps.of(m_IntakeMovement.getOutputCurrent());
  }

  public boolean outtaking()
  {
    if(getCurrentCommand() != null)
        return getDutycycle() > 0.0 || getCurrentCommand().getName().equals("Outtake");
    return getDutycycle() > 0.0;
  }

  public double getDutycycle()
  {
    return m_IntakeMovement.getAppliedOutput();
  }
}