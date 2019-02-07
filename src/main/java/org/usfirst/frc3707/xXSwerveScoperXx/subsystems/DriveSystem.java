// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3707.xXSwerveScoperXx.subsystems;

import org.usfirst.frc3707.xXSwerveScoperXx.Robot;
import org.usfirst.frc3707.xXSwerveScoperXx.commands.*;
//import org.usfirst.frc3707.xXSwerveScoperXx.lidar.LidarLitePWM;
import org.usfirst.frc3707.xXSwerveScoperXx.swerve.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.*;



/**
 *
 */
public class DriveSystem extends Subsystem {

 

    private AnalogPotentiometer frontRightEncoder = new AnalogPotentiometer(1, 360.0, 0.0);
    private WPI_VictorSPX frontRightSwerve = new WPI_VictorSPX(1);
    private PIDController frontRightTwist = new PIDController(0.05, 0.0, 0.0, 0.0, frontRightEncoder, frontRightSwerve, 0.02);
    private AnalogPotentiometer frontLeftEncoder = new AnalogPotentiometer(0, 360.0, 0.0);;
    private WPI_VictorSPX frontLeftSwerve = new WPI_VictorSPX(4);
    private PIDController frontLeftTwist = new PIDController(0.05, 0.0, 0.0, 0.0, frontLeftEncoder, frontLeftSwerve, 0.02);;
    private AnalogPotentiometer backRightEncoder = new AnalogPotentiometer(2, 360.0, 0.0);
    private WPI_VictorSPX backRightSwerve = new WPI_VictorSPX(2);
    private PIDController backRightTwist = new PIDController(0.05, 0.0, 0.0, 0.0, backRightEncoder, backRightSwerve, 0.02);
    private AnalogPotentiometer backLeftEncoder = new AnalogPotentiometer(3, 360.0, 0.0);
    private WPI_VictorSPX backLeftSwerve = new WPI_VictorSPX(3);
    private PIDController backLeftTwist = new PIDController(0.05, 0.0, 0.0, 0.0, backLeftEncoder, backLeftSwerve, 0.02);
    private VictorSP frontRightDrive = new VictorSP(1);
    private VictorSP frontLeftDrive = new VictorSP(0);
    private VictorSP backRightDrive= new VictorSP(2);
    private VictorSP backLeftDrive= new VictorSP(3);

    private ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    
    private SwerveWheel frontLeftWheel = new SwerveWheel(frontLeftTwist, frontLeftDrive, 18);
    private SwerveWheel frontRightWheel = new SwerveWheel(frontRightTwist, frontRightDrive, 204);
    private SwerveWheel backLeftWheel = new SwerveWheel(backLeftTwist, backLeftDrive, -40);
    private SwerveWheel backRightWheel = new SwerveWheel(backRightTwist, backRightDrive, 108);
    public SwerveDrive swerve = new SwerveDrive(frontRightWheel, frontLeftWheel, backLeftWheel, backRightWheel, gyro);
    
   // LiveWindow.addSensor("Sensors", "gyro", gyro);

    
    public void init() {
        
        frontRightTwist.setInputRange(0.0, 360.0);
        frontRightTwist.setOutputRange(-1.0, 1.0);
        frontRightTwist.setContinuous(true);
        
        frontLeftTwist.setInputRange(0.0, 360.0);
        frontLeftTwist.setOutputRange(-1.0, 1.0);
        frontLeftTwist.setContinuous(true);
        
        backLeftTwist.setInputRange(0.0, 360.0);
        backLeftTwist.setOutputRange(-1.0, 1.0);
        backLeftTwist.setContinuous(true);
        
        backRightTwist.setInputRange(0.0, 360.0);
        backRightTwist.setOutputRange(-1.0, 1.0);
        backRightTwist.setContinuous(true);

        gyro.reset();

    }


    // SAVED MOTOR DECLARATIONS

  

    public void enable() {
    	frontLeftTwist.enable();
    	frontRightWheel.enable();
    	backLeftWheel.enable();
        backRightWheel.enable();
        System.out.println("Enable Twist");
    }
    public void disable() {
    	frontLeftTwist.disable();
    	frontRightWheel.disable();
    	backLeftWheel.disable();
    	backRightWheel.disable();
    }
    public void drive(double directionX, double directionY, double rotation, boolean useGyro, boolean slowSpeed) {
    	swerve.drive(directionX, directionY, rotation, useGyro, slowSpeed);
    	SmartDashboard.putNumber("frontRightAngle", frontRightEncoder.get());
    	SmartDashboard.putNumber("frontLeftAngle", frontLeftEncoder.get());
    	SmartDashboard.putNumber("backRightAngle", backRightEncoder.get());
    	SmartDashboard.putNumber("backLeftAngle", backLeftEncoder.get());

      
    }
    public void readGyro(){
        SmartDashboard.putData(gyro);
    }

public void setLastTime(long x) {
    lastTime = x;
}

long lastTime;
public double output = 0;
public double errSum, lastErr = 0;
double kp = 0.003;
double ki = 0;
public double computePIDPower(double input, double setpoint) {
   /*How long since we last calculated*/
   long now = System.currentTimeMillis();
   double timeChange = (double)(now - lastTime);
   /*Compute all the working error variables*/
   double error = setpoint - input;
   errSum += (error * timeChange);
  
   /*Compute PID Output*/
   output = kp * error + ki * errSum;
  
   /*Remember some variables for next time*/
   lastErr = error;
   lastTime = now;

    return output;
}
  
void SetTunings(double Kp, double Ki) {
   kp = Kp;
   ki = Ki;
}

public void moveLeftOrRight(double power){
    swerve.driveSimple(power, 270);
}

    public void moveLeft(){
        swerve.driveSimple(-0.2, 270);
        //Robot.driveSystem.swerve.drive(-.3, 0, 0, false, false);
    }
    public void moveRight(){
        swerve.driveSimple(0.2, 270);
        //Robot.driveSystem.swerve.drive(.3, 0, 0, false, false);
    }
    
    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new joystick_drive_swerve());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    NetworkTableEntry x_pos;
    NetworkTableEntry y_pos;
    NetworkTableInstance inst;
    double[] realx_pos;
    NetworkTable pixyData;
    double[] defaultValue = {-1};
    double error;
    double[] lastError = new double[10];
    
    public double getError ()
    {
        inst = NetworkTableInstance.getDefault();
        pixyData = inst.getTable("PixyData");

        x_pos = pixyData.getEntry("x_pos");
        y_pos = pixyData.getEntry("y_pos");
        
        realx_pos = x_pos.getDoubleArray(defaultValue);

        if (realx_pos.length > 0 && realx_pos[0] != -1)
        {
            error = Robot.driveSystem.computePIDPower(realx_pos[0], 159);
            return error;
        }
        return 0;
    }

    public boolean getSee ()
    {
        NetworkTableEntry seeHolder = pixyData.getEntry("object_detected");
        boolean see = seeHolder.getBoolean(false);
        return see;
    }
}