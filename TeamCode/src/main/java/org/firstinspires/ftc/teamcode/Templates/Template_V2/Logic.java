package org.firstinspires.ftc.teamcode.Templates.Template_V2;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.Arrays;

public class Logic extends Robot_Logic {

    Robot robot;
    public Logic(Robot r) {robot = r;}

    //as long as there are no errors, this is the only thing you have to change :) (and auton ofc); possible error - in robot.java :/
    //there are 5 sections, arranged roughly in how often they have to be changed

    //Game Variables
    public double[] servoPositions = {1.0, 0.8, 0.6};
    public ArrayList<String> servoPositionNames = new ArrayList<>(Arrays.asList("Dump", "Mid", "Bottom"));

    public int[] armPositions = {200, 850, 950, 1300};
    public ArrayList<String> armPositionNames = new ArrayList<>(Arrays.asList("Reset_Arm", "Low_Goal", "Middle_Goal", "High_Goal"));

    public double distance_between_squares = 5.5;
    public double DuckWheelPowerA = 0.3;
    public double DuckWheelPowerB = 0.55;

    public String elementPosition;




    //TeleOp Logic

    long DuckWheelStarted = -1000000000;
    double DuckWheelPower;
    int DuckWheelDirection = 0;

    double armPower = 0.0;
    int desiredArmPos = 0;

    int servoPos = 0;

    double IntakePower = 0.0;

    long time_started = -1000000000; //nanoseconds -> 10^9 = 1000000000 = 1 second

    public void update_motors_operator(boolean a, boolean b, boolean x, boolean y, boolean dpad_up,
                                       boolean dpad_down, boolean dpad_left, boolean dpad_right,
                                       boolean left_bumper, boolean right_bumper, double left_stick_x,
                                       double left_stick_y, double right_stick_x, double right_stick_y,
                                       double left_trigger_depth, double right_trigger_depth) {

        //Telemetry
        if (dpad_right) time_started = System.nanoTime();
        robot.telemetry.addData("Dpad Right Button: ", System.nanoTime() - time_started < 500000000 ? "On" : "Off");
        //will be active for 0.5 seconds
        robot.telemetry.addData("A toggle: ", a ? "On" : "Off");

        //Intake
        IntakePower = a ? 0.3 : y ? -0.3 : 0.0;

        //DuckWheel
        if ((x || b) && DuckWheelPower == 0.0) DuckWheelStarted = System.nanoTime();
        DuckWheelDirection = b ? -1 : x ? 1 : 0;
        DuckWheelPower = (x || b) ? Math.min((System.nanoTime() - DuckWheelStarted) / 1000000000.0, 1) * 0.7 : 0.0; //Makes it a gradient

        //Linear Slide
        armPower = left_stick_y > 0.1 ? 0.5 : left_stick_y < -0.1 ? -0.13 : 0;
        desiredArmPos = armPower != 0 ? robot.dc_motor_list[dc_motor_names.indexOf("arm")].getCurrentPosition() : desiredArmPos;

        //Servo
        servoPos += dpad_up ? 1 : dpad_down ? -1 : right_bumper ? 2 : left_bumper ? -2: 0;
        servoPos = Math.max(Math.min(servoPos, 2), 0);

        //Powering all the motors
        robot.dc_motor_list[dc_motor_names.indexOf("intake")].setPower(IntakePower);
        robot.dc_motor_list[dc_motor_names.indexOf("duckWheel")].setPower(DuckWheelPower * DuckWheelDirection);
        robot.dc_motor_list[dc_motor_names.indexOf("arm")].setPower(armPower != 0 ? armPower : robot.dc_motor_list[dc_motor_names.indexOf("arm")].getCurrentPosition() > desiredArmPos ? -0.05 : 0.05);
        robot.servo_list[servo_names.indexOf("right")].setPosition(servoPositions[servoPos]);
    }

    public void update_motors_driver(boolean a, boolean b, boolean x, boolean y, boolean dpad_up,
                                     boolean dpad_down, boolean dpad_left, boolean dpad_right,
                                     boolean left_bumper, boolean right_bumper, double right_stick_y,
                                     double left_trigger_depth) {
        //note there's no left_stick_x, left_stick_y, right_stick_x, or right_trigger
        //there is right_stick_y though
        //code would be the same as if it were the operator doing it;
        //in theory we could have this whole robot on one controller lol
    }

    public void execute_non_driver_controlled() {
        //this will have the telemetry, LEDs, etc.

        //Telemetry
        robot.telemetry.addData("Arm Position: ", robot.dc_motor_list[dc_motor_names.indexOf("arm")].getCurrentPosition());
        robot.telemetry.update();

        /* EXAMPLE - set LED color if distance sensor detects something
        if (r.getDistInch("dSensor") < 4) r.set_led_color("led", "Blue");
        else r.set_led_color("led", "Green");
        */
    }




    //Autonomous Functions

    public void SetArm(String position){

        robot.dc_motor_list[dc_motor_names.indexOf("arm")].setTargetPosition(armPositions[armPositionNames.indexOf(position)]);
        robot.dc_motor_list[dc_motor_names.indexOf("arm")].setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.dc_motor_list[dc_motor_names.indexOf("arm")].setPower(0.4);
        while(robot.dc_motor_list[dc_motor_names.indexOf("arm")].isBusy()) {
        }
        robot.dc_motor_list[dc_motor_names.indexOf("arm")].setPower(0);

    }

    public void SetServo(String position) {
        robot.servo_list[servo_names.indexOf("right")].setPosition(servoPositions[servoPositionNames.indexOf(position)]);
        pause(100);
        /* This *could* be a solution to the issue - let's see
        for (int i = 0; i < 100; i++) {
            r.servo_list[servo_names.indexOf("BucketServo")].setPosition(servoPositions[servoPositionNames.indexOf(position)]);
            r.servo_list[servo_names.indexOf("BucketServo")].setPosition(servoPositions[servoPositionNames.indexOf(position)]+0.001);
        }
        */
    }

    public boolean checkPos() {
        return (robot.getDistInch("sensor_distance") < 4);
    }




    //DO NOT CHANGE

    //PID Stuff

    public int speedFactor = 1;

    public double heading = 0; // Current angle of robot
    public double desiredHeading = 0; // Angle that the robot wants to be at

    public double correction = 0; // Calculated amount that the robot has to correct

    public double current_error; // The difference between the heading and the desired heading
    public double previous_error;

    public long current_time;
    public long previous_time;

    public double p_weight = 0.025;
    public double d_weight = 0.85;

    //Basic Autonomous Functions

    public void pause(double milliseconds) {
        long t = System.nanoTime();
        while (System.nanoTime() - t <= milliseconds * 1000000) {

        }
    }

    public void ExecuteEncoders(double Speed) {
        robot.SpeedSet(Speed);
        while (robot.MotorsBusy()) {
        }
        robot.SpeedSet(0.2);
        pause(100);
        robot.SpeedSet(0);
        pause(200);
    }

    public void ExecuteEncoders() {
        ExecuteEncoders(0.7);
    }

    public void Drive(double Dist){
        robot.DriveDistance(-Dist);
        ExecuteEncoders();
    }

    public void Drive(double Dist, String Direction){
        robot.DriveDistance(Dist, Direction);
        ExecuteEncoders();
    }

    public void Drive(double Dist, String Direction, double Speed){
        robot.DriveDistance(Dist, Direction);
        ExecuteEncoders(Speed);
    }

    public void Turn(int Degrees, String Direction){
        if(Direction.equals("Right")) {
            robot.turnWithEncoders(Degrees);
        } else if(Direction.equals("Left")) {
            robot.turnWithEncoders(-Degrees);
        }
        ExecuteEncoders();
    }

    public void SetPower(String name, double power) {
        robot.dc_motor_list[dc_motor_names.indexOf(name)].setPower(power);
    }

    public void ResetEncoder(String name) {
        robot.dc_motor_list[dc_motor_names.indexOf(name)].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}