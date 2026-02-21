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
  private final TalonSRX m_rollerMotor = new TalonSRX(90);

  private final DCMotor m_rollerMotorGearbox = DCMotor.getVex775Pro(1);

  public ShooterIntakeSubsystem()
  {
    // Reset the Talon SRX to factory defaults and set neutral (idle) mode to Coast.
    m_rollerMotor.configFactoryDefault();
    m_rollerMotor.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Coast);
    m_rollerMotor.setInverted(false);
    m_rollerMotor.enableCurrentLimit(true);
    // Optionally configure current limits or inversion here as needed.
  }

  public Command setShooterIntake (double speed)
  {
    return runOnce(() -> {
      m_rollerMotor.set(ControlMode.PercentOutput, speed);
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
    return Amps.of(m_rollerMotor.getMotorOutputVoltage());
  }

  public boolean outtaking()
  {
    if(getCurrentCommand() != null)
        return getDutycycle() > 0.0 || getCurrentCommand().getName().equals("OuttakeShooter");
    return getDutycycle() > 0.0;
  }

  public double getDutycycle()
  {
    return m_rollerMotor.getMotorOutputPercent();
  }
}
