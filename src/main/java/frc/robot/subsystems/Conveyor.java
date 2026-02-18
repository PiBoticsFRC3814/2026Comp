package frc.robot.subsystems;


import static edu.wpi.first.units.Units.Amps;

import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Conveyor extends SubsystemBase
{


  public static final double kWristMomentOfInertia = 0.00032; // kg * m^2

  private final SparkMax m_conveyor = new SparkMax(30, MotorType.kBrushless);

  private final DCMotor m_rollerMotorGearbox = DCMotor.getNeo550(1);

  public Conveyor()
  {
    SparkMaxConfig config = new SparkMaxConfig();
    config
        .inverted(false)
        .smartCurrentLimit(100);
    config.idleMode(IdleMode.kCoast);
    
  }
  public Command setConveyor (double speed)
  {
    return runOnce(() -> {
      m_conveyor.set(speed);
    });
  }

  public Command out(double speed)
  {
    return setConveyor(speed * -1);
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
}