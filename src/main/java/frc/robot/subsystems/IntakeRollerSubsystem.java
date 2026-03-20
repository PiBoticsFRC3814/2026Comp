package frc.robot.subsystems;


import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Percent;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class IntakeRollerSubsystem extends SubsystemBase
{
  public static final double kWristMomentOfInertia = 0.00032; // kg * m^2

  private final TalonSRX m_IntakeRollers = new TalonSRX(40);

  //private final DCMotor m_rollerMotorGearbox = DCMotor.getVex775Pro(1);

  public IntakeRollerSubsystem()
  {
    // Reset the Talon SRX to factory defaults and set neutral (idle) mode to Coast.
    m_IntakeRollers.configFactoryDefault();
    m_IntakeRollers.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Coast);
    m_IntakeRollers.setInverted(false);
    m_IntakeRollers.enableCurrentLimit(true);
    m_IntakeRollers.configContinuousCurrentLimit(40);
    m_IntakeRollers.configPeakCurrentLimit(40);
    // Optionally configure current limits or inversion here as needed.
  }

  public Command setIntakeRoller(double speed)
  {
    return run(() -> {
      m_IntakeRollers.set(ControlMode.PercentOutput, speed);
    });
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

  public Voltage getVoltage()
  {
    return Volts.of(m_IntakeRollers.getMotorOutputPercent());
  }

  public boolean outtaking()
  {
    if(getCurrentCommand() != null)
        return getDutycycle() > 0.0 || getCurrentCommand().getName().equals("Outtake");
    return getDutycycle() > 0.0;
  }

  public double getDutycycle()
  {
    return m_IntakeRollers.getMotorOutputPercent();
  }

  public void intake(double speed){
    m_IntakeRollers.set(ControlMode.PercentOutput, speed);
  }

  public void outake(double speed){
    m_IntakeRollers.set(ControlMode.PercentOutput, -speed);
  }

  public void STOP(){
    m_IntakeRollers.set(ControlMode.PercentOutput, 0);
  }
}
