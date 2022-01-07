package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

public class DriverController {
    RobotHardware robot;

    int speedFactor = 1;

    public DriverController(RobotHardware r){robot = r;}

    TeleOpDriverLogic DL = new TeleOpDriverLogic(robot, this);

    public double heading; // Angle that the robot is facing
    public double desiredHeading = 0; // Angle that the robot wants to go

    double tThreshold = 3; // Angle that the robot tries to correct to within
    double correction; // Amount that the robot is correction for the error

    double current_error; // The difference between the heading and the desired heading
    double previous_error;

    long current_time;
    long previous_time;

    boolean adjusting_p = true;
    boolean adjusting_d = false;

    long endTime = 0;

    //PID Weights
    double k_p = 0.025;
    double k_d = 0.85;

    public double getError(){

        double diff = desiredHeading - heading;

        while (diff > 180)  diff -= 360;
        while (diff <= -180) diff += 360;

        return diff;
    }
    public double getPIDSteer(){

        current_time = System.currentTimeMillis();
        current_error = getError();


        double p = k_p * current_error;
        double d = k_d * (current_error - previous_error) / (current_time - previous_time);

        previous_error = current_error;
        previous_time = current_time;

        //If the error is within the threshold, we correct only on d
        if(Math.abs(current_error) <= tThreshold){

            return p+d;

        } else{

            return p+d;

        }
    }
    public void drive(Gamepad gamepad){
        double LX = gamepad.left_stick_x;
        double LY = -gamepad.left_stick_y;
        double RX = -gamepad.right_stick_x;


        //heading = robot.getAngle();

        if(Math.abs(gamepad.right_stick_x) >= 0.05){
            desiredHeading = heading;
        }

        if(gamepad.dpad_up){
            desiredHeading = 0;
        }else if(gamepad.dpad_right){
            desiredHeading = -90;
        }else if(gamepad.dpad_left){
            desiredHeading = 90;
        }else if(gamepad.dpad_down){
            desiredHeading = 180;
        }


        //correction = getPIDSteer();


        // Calculates the value to put each motor to
        double powerLF = (LY + LX + correction + RX);
        double powerLB = (LY - LX + correction + RX);
        double powerRF = (LY - LX - correction - RX);
        double powerRB = (LY + LX - correction - RX);

        // Makes sure that the power values are not truncated
        if (Math.abs(powerLF) > 1 || Math.abs(powerRF) > 1 || Math.abs(powerLB) > 1 || Math.abs(powerRB) > 1) {

            // Find the largest power

            double max = 0;

            max = Math.max(Math.abs(powerLF), Math.abs(powerLB));
            max = Math.max(Math.abs(powerRF), max);
            max = Math.max(Math.abs(powerRB), max);

            // Divide everything by max (it's positive so we don't need to worry about signs)
            powerLB /= max;
            powerLF /= max;
            powerRB /= max;
            powerRF /= max;

        }

        // Set the power of the drive train
        robot.RF.setPower(-powerRF/speedFactor);
        robot.RB.setPower(-powerRB/speedFactor);
        robot.LF.setPower(-powerLF/speedFactor);
        robot.LB.setPower(-powerLB/speedFactor);
    }

    public void manualWheelControl(double bl, double fl, double br, double fr){
        robot.LF.setPower(fl);
        robot.LB.setPower(bl);
        robot.RF.setPower(fr);
        robot.RB.setPower(br);
    }

    public void correctHeading(double targetHeading ){
        ElapsedTime runtime = new ElapsedTime();
        boolean breakout = false;
        double currentTime = runtime.milliseconds();
        double breakoutTime = currentTime + 500;
        heading = robot.getAngle();
        while(currentTime < breakoutTime && !breakout) {
            if (heading > 0.5+targetHeading || heading < -0.5+targetHeading) {

                current_error = getError();
                correction = getPIDSteer();
                currentTime = runtime.milliseconds();
                // Calculates the value to put each motor to
                double powerLF = (correction);
                double powerLB = (correction);
                double powerRF = (-correction);
                double powerRB = (-correction);

                // Makes sure that the power values are not truncated
                if (Math.abs(powerLF) > 1 || Math.abs(powerRF) > 1 || Math.abs(powerLB) > 1 || Math.abs(powerRB) > 1) {
                    // Find the largest power
                    double max = 0;
                    max = Math.max(Math.abs(powerLF), Math.abs(powerLB));
                    max = Math.max(Math.abs(powerRF), max);
                    max = Math.max(Math.abs(powerRB), max);

                    // Divide everything by max (it's positive so we don't need to worry
                    // about signs)
                    powerLB /= max;
                    powerLF /= max;
                    powerRB /= max;
                    powerRF /= max;
                }

                // Set the power of the motors
                robot.RF.setPower(powerRF/speedFactor);
                robot.RB.setPower(powerRB/speedFactor);
                robot.LF.setPower(powerLF/speedFactor);
                robot.LB.setPower(powerLB/speedFactor);
                heading = robot.getAngle();
            }else{
                breakout = true;
            }
            currentTime = runtime.milliseconds();
        }
    }

    public void inputs(Gamepad gamepad){

        speedFactor = (gamepad.right_trigger > 0.1) ? 2 : 1;


    }
}
