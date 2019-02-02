// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3707.xXSwerveScoperXx.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3707.xXSwerveScoperXx.Robot;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
/**
 *
 */
public class center_On_Tape extends Command {
   
    


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public center_On_Tape() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }
    NetworkTableEntry x_pos;
    NetworkTableEntry y_pos;
    NetworkTableInstance inst;
    double[] realx_pos;
    NetworkTable pixyData;
    double[] defaultValue = {-1};
    double error;
    double[] lastError = new double[10];

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        inst = NetworkTableInstance.getDefault();
        pixyData = inst.getTable("PixyData");
        Robot.driveSystem.setLastTime(System.currentTimeMillis());
        Robot.driveSystem.output = 0;
        Robot.driveSystem.errSum = 0;
        Robot.driveSystem.lastErr = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        x_pos = pixyData.getEntry("x_pos");
        y_pos = pixyData.getEntry("y_pos");
        
        realx_pos = x_pos.getDoubleArray(defaultValue);
        //System.out.println(realx_pos[0]);

        if (realx_pos.length > 0 && realx_pos[0] != -1){
            //double error = 159 - realx_pos[0];
            error = Robot.driveSystem.computePIDPower(realx_pos[0], 159);
            Robot.driveSystem.moveLeftOrRight(-error);
            for (int x = 0; x < lastError.length; x++)
            {
                if (lastError[x] != 0)
                {
                    lastError[x] = Math.abs(error);
                    break;
                }
            }
            for (int x = 1; x < lastError.length; x++)
            {
                lastError[x - 1] = lastError[x];
            }
            lastError[lastError.length - 1] = error;
            // if (realx_pos[0] < 156.0){
            //     Robot.driveSystem.moveLeft();
            // }else {
            //     Robot.driveSystem.moveRight();
            // }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        double errDiff = 0;

        for (int x = 0; x < lastError.length; x++)
        {
            errDiff += lastError[x];
        }
        errDiff = errDiff / lastError.length;

        if (Math.abs(error - errDiff) < 0.01 && Math.abs(error) < 0.13) {
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.driveSystem.drive(0, 0, 0, false, false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}