package frc.robot.subsystems;


import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ShooterIntakeSubsystem extends SubsystemBase
{
  private final TalonSRX m_shooterIntake = new TalonSRX(46);

  private final DCMotor m_rollerMotorGearbox = DCMotor.getVex775Pro(1);

  public ShooterIntakeSubsystem()
  {
    // Reset the Talon SRX to factory defaults and set neutral (idle) mode to Coast.
    m_shooterIntake.configFactoryDefault();
    m_shooterIntake.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Coast);
    m_shooterIntake.setInverted(false);
    m_shooterIntake.enableCurrentLimit(true);
    // Optionally configure current limits or inversion here as needed.
  }

  public Command setShooterIntake (double speed)
  {
    return run(() -> {
      m_shooterIntake.set(ControlMode.PercentOutput, speed);
    });
  }

  public Command out(double speed)
  {
    return setShooterIntake (-speed);
  }

  public Command in(double speed)
  {
    return setShooterIntake (speed);
  }

  public Command stop(){
    return setShooterIntake (0);
  }

  public Current getCurrent()
  {
    return Amps.of(m_shooterIntake.getMotorOutputVoltage());
  }

  public boolean outtaking()
  {
    if(getCurrentCommand() != null)
        return getDutycycle() > 0.0 || getCurrentCommand().getName().equals("OuttakeShooter");
    return getDutycycle() > 0.0;
  }

  public double getDutycycle()
  {
    return m_shooterIntake.getMotorOutputPercent();
  }

  public void intake(double speed){
    m_shooterIntake.set(ControlMode.PercentOutput, speed);
  }

  public void STOP(){
    m_shooterIntake.set(ControlMode.PercentOutput, 0);
  }
}
