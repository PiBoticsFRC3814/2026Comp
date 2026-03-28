package frc.robot.subsystems;


import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Percent;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class IntakeRollerSubsystem extends SubsystemBase
{
  private final SparkMax m_IntakeRollers = new SparkMax(40,MotorType.kBrushless);

  public IntakeRollerSubsystem()
  {
    SparkMaxConfig config = new SparkMaxConfig();
    config
        .inverted(false)
        .smartCurrentLimit(100)
        .idleMode(IdleMode.kCoast);
    m_IntakeRollers.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public Command setIntakeRoller(double speed)
  {
    return run(() -> {
      m_IntakeRollers.set(speed);
    });
  }

  public void intake(double speed){
    m_IntakeRollers.set(speed);
  }

  public void outake(double speed){
    m_IntakeRollers.set(-speed);
  }

  public void STOP(){
    m_IntakeRollers.set(0);
  }

  
  public Command out(double speed)
  {
    return setIntakeRoller(-speed);
  }

  public Command in(double speed)
  {
    return setIntakeRoller(speed);
  }

  public Command stop(){
    return setIntakeRoller(0);
  }

}
