package frc.robot.subsystems;


import static edu.wpi.first.units.Units.Amps;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Conveyor extends SubsystemBase
{

  public static final double kWristMomentOfInertia = 0.00032; // kg * m^2

  private final SparkMax m_conveyor = new SparkMax(42, MotorType.kBrushless);

  private final DCMotor m_rollerMotorGearbox = DCMotor.getNEO(1);

  public Conveyor()
  {
    SparkMaxConfig config = new SparkMaxConfig();
    config
        .inverted(false)
        .smartCurrentLimit(100)
        .idleMode(IdleMode.kCoast);
    m_conveyor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //not deprecated.  for some reason spark put these config modes in a different folder (revrobotics.BLAmode instead of revrobotics.spark.config.BLAmode) for some reason both "files" still exist in the different folder and itellesence for some reason grabbed the deprecated imnport instead of the preferred import

  }
  

  public Command setConveyor (double speed)
  {
    return run(() -> {
      m_conveyor.set(speed);
    });
  }

  public Command out(double speed)
  {
    return setConveyor(-speed);
  }

  public Command in(double speed)
  {
    return setConveyor(speed);
  }

  public Command stop(){
    return setConveyor(0);
  }

  public Current getCurrent()
  {
    return Amps.of(m_conveyor.getOutputCurrent());
  }

  public boolean outtaking()
  {
    if(getCurrentCommand() != null)
        return getDutycycle() > 0.0 || getCurrentCommand().getName().equals("Outtake");
    return getDutycycle() > 0.0;
  }

  public double getDutycycle()
  {
    return m_conveyor.getAppliedOutput();
  }

  public void intake(double speed){
    m_conveyor.set(speed);
  }

  public void STOP(){
    m_conveyor.set(0);
  }
}