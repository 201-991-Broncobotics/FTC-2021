package org.firstinspires.ftc.teamcode.Templates.Template_V2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Logic extends LinearOpMode {

    //Game Variables
    public double[] servoPositions = {0.6, 0.8, 1.0};
    public ArrayList<String> servoPositionNames = new ArrayList<>(Arrays.asList("Dump", "Mid", "Bottom"));

    public int[] armPositions = {200, 850, 950, 1300};
    public ArrayList<String> armPositionNames = new ArrayList<>(Arrays.asList("Reset_Arm", "Low_Goal", "Middle_Goal", "High_Goal"));

    public double distance_between_squares = 5.5;
    public double DuckWheelPowerA = 0.3;
    public double DuckWheelPowerB = 0.55;

    //as long as there are no errors, this is the only thing you have to change :) (and auton ofc)

    //TeleOp Logic

    double DuckWheelPower = 0.0;
    int DuckWheelDirection = 0;

    double armPower = 0.0;

    int servoPos = 0;

    double IntakePower = 0.0;

    boolean a_on = false;
    int ticks_left = 0;

    public void update_motors_operator(Robot r, boolean a, boolean b, boolean x, boolean y, boolean dpad_up,
                                       boolean dpad_down, boolean dpad_left, boolean dpad_right,
                                       boolean left_bumper, boolean right_bumper, double left_stick_x,
                                       double left_stick_y, double right_stick_x, double right_stick_y,
                                       double left_trigger_depth, double right_trigger_depth) {

        //Telemetry
        a_on = a;
        ticks_left = dpad_up ? 100 : ticks_left - 1;
        r.telemetry.addData("Dpad Up Button: ", ticks_left > 0 ? "On" : "Off");
        r.telemetry.addData("A toggle: ", a_on ? "On" : "Off");

        //Intake
        IntakePower = a ? 0.3 : y ? -0.3 : 0.0;

        //DuckWheel
        DuckWheelDirection = b ? -1 : x ? 1 : 0;
        DuckWheelPower = (x || b) ? Math.min(DuckWheelPower + 0.01, 0.7) : 0.0; //Makes it a gradient

        //Linear Slide
        armPower = left_stick_y > 0.1 ? 0.5 : left_stick_y < -0.1 ? -0.13 : 0;

        //Servo
        servoPos += dpad_up ? 1 : dpad_down ? -1 : right_bumper ? 2 : left_bumper ? -2: 0;
        servoPos = Math.max(Math.min(servoPos, 3), 1);

        //Powering all the dc motors
        r.dc_motor_list[dc_motor_names.indexOf("Intake")].setPower(IntakePower);
        r.dc_motor_list[dc_motor_names.indexOf("DuckWheel")].setPower(DuckWheelPower * DuckWheelDirection);
        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setPower(armPower);
        r.servo_list[servo_names.indexOf("BucketServo")].setPosition(servoPositions[servoPos]);
    }

    public void update_motors_driver(Robot r, boolean a, boolean b, boolean x, boolean y, boolean dpad_up,
                                     boolean dpad_down, boolean dpad_left, boolean dpad_right,
                                     boolean left_bumper, boolean right_bumper, double right_stick_y,
                                     double left_trigger_depth) {
        //note there's no left_stick_x, left_stick_y, right_stick_x, or right_trigger
        //there is right_stick_y though
        //code would be the same as if it were the operator doing it;
        //in theory we could have this whole robot on one controller lol
    }

    public void execute_non_driver_controlled(Robot r) {
        //this will have the telemetry, LEDs, etc.

        //Telemetry

        r.telemetry.addData("Arm Position: ", r.dc_motor_list[dc_motor_names.indexOf("Arm")].getCurrentPosition());
        r.telemetry.update();

        /* EXAMPLE - set LED color if distance sensor detects something
        if (r.getDistInch(r.distance_sensor_list[0]) < 4) r.set_led_color(r.led_list[0], "Blue");
        else r.set_led_color(r.led_list[0], "Green");
        */
    }




    //Robot Logic
    public boolean usePID = false;

    //Robot motors, etc.

    public ArrayList<String> distance_sensor_names = new ArrayList<>(Arrays.asList("dSensor"));

    public ArrayList<String> led_names = new ArrayList<>(Arrays.asList("led"));

    public ArrayList<String> dc_motor_names = new ArrayList<>(Arrays.asList("LinearSlide", "Intake", "DuckWheel"));
    public int[] dc_motor_directions = {0, 1, 1}; //0 for forward, 1 for reverse

    public ArrayList<String> servo_names = new ArrayList<>(Arrays.asList("BucketServo"));

    //No need to ever change this
    public ArrayList<String> wheel_names = new ArrayList<>(Arrays.asList("Right_Front", "Right_Back", "Left_Back", "Left_Front"));

    //Operator Logic
    public ArrayList<String> operator_toggles = new ArrayList<>(Arrays.asList("a", "y", "x", "b"));
    //activated from one press to the next release (not the release that immediately follows)
    public ArrayList<String> operator_buttons = new ArrayList<>(Arrays.asList("dpad_up", "dpad_down", "right_bumper", "left_bumper"));
    //idk what to call them but basically they're only active for one tick
    //everything else is active while and only while it is held

    //Driver Logic (besides driving)
    public ArrayList<String> driver_toggles = new ArrayList<>(Arrays.asList());
    //activated from one press to the next release (not the release that immediately follows)
    public ArrayList<String> driver_buttons = new ArrayList<>(Arrays.asList());

    //idk what to call them but basically they're only active for one tick
    //everything else is active while and only while it is held

    //names: a, b, x, y, dpad_up, dpad_down, dpad_left, dpad_right, left_bumper, right_bumper




    //Autonomous Functions

    public void SetArm(Robot r, String position){

        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setTargetPosition(armPositions[armPositionNames.indexOf(position)]);
        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setMode(DcMotor.RunMode.RUN_TO_POSITION);
        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setPower(0.4);
        while(r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].isBusy()) {
        }
        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setPower(0);

    }

    public void SetServo(Robot r, String position) {
        r.servo_list[servo_names.indexOf("BucketServo")].setPosition(servoPositions[servoPositionNames.indexOf(position)]);
        pause(100);
        /* This *could* be a solution to the issue - let's see
        for (int i = 0; i < 100; i++) {
            r.servo_list[servo_names.indexOf("BucketServo")].setPosition(servoPositions[servoPositionNames.indexOf(position)]);
            r.servo_list[servo_names.indexOf("BucketServo")].setPosition(servoPositions[servoPositionNames.indexOf(position)]+0.001);
        }
        */
    }

    public boolean checkPos(Robot r) {
        return (r.getDistInch(r.distance_sensor_list[0]) < 4);
    }








    //DO NOT CHANGE

    //PID Stuff

    public int speedFactor = 1;
    public double inchPer90 = 13.2;

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

    public void ExecuteEncoders(Robot r, double Speed) {
        r.SpeedSet(Speed);
        while (r.MotorsBusy() && opModeIsActive()) {
            idle();
        }
        r.SpeedSet(0.2);
        sleep(100);
        r.SpeedSet(0);
        sleep(200);
    }

    public void ExecuteEncoders(Robot r) {
        ExecuteEncoders(r, 0.7);
    }

    public void Drive(Robot r, double Dist){
        r.DriveDistance(-Dist);
        ExecuteEncoders(r);
    }

    public void Drive(Robot r, double Dist, String Direction){
        r.DriveDistance(Dist, Direction);
        ExecuteEncoders(r);
    }

    public void Drive(Robot r, double Dist, String Direction, double Speed){
        r.DriveDistance(Dist, Direction);
        ExecuteEncoders(r, Speed);
    }

    public void Turn(Robot r, int Degrees, String Direction){
        if(Direction.equals("Right")) {
            r.turnWithEncoders(Degrees);
        } else if(Direction.equals("Left")) {
            r.turnWithEncoders(-Degrees);
        }
        ExecuteEncoders(r);
    }

    public void SetPower(Robot r, String name, double power) {
        r.dc_motor_list[dc_motor_names.indexOf(name)].setPower(power);
    }

    public void ResetEncoder(Robot r, String name) {
        r.dc_motor_list[dc_motor_names.indexOf(name)].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

}