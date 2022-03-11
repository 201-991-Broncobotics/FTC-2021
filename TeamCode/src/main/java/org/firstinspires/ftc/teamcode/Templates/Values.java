package org.firstinspires.ftc.teamcode.Templates;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Templates.DoNotChange.Robot;

import java.util.ArrayList;
import java.util.Arrays;

public interface Values {

    //game variables
    double[] servoPositions = {0.6, 0.8, 1.0};
    int[] armPositions = {200, 850, 950, 1300};
    boolean usePID = false;
    double distance_between_squares = 5.5;
    double DuckWheelPowerA = 0.3;
    double DuckWheelPowerB = 0.55;

    //Robot motors, etc.

    ArrayList<String> distance_sensor_names = new ArrayList<>(Arrays.asList("dSensor"));

    ArrayList<String> led_names = new ArrayList<>(Arrays.asList("led"));

    ArrayList<String> wheel_names = new ArrayList<>(Arrays.asList("Right_Front", "Right_Back", "Left_Back", "Left_Front"));

    ArrayList<String> dc_motor_names = new ArrayList<>(Arrays.asList("LinearSlide", "Intake", "DuckWheel"));
    int[] dc_motor_directions = {0, 1, 1}; //0 for forward, 1 for reverse

    ArrayList<String> servo_names = new ArrayList<>(Arrays.asList("BucketServo"));

    //Operator Logic
    ArrayList<String> operator_toggles = new ArrayList<>(Arrays.asList("a", "y"));
    //activated from one press to the next release (not the release that immediately follows)
    ArrayList<String> operator_buttons = new ArrayList<>(Arrays.asList("dpad_up", "dpad_down", "right_bumper", "left_bumper"));
    //idk what to call them but basically they're only active for one tick
    //everything else is active while and only while it is held

    //Driver Logic (besides driving)
    ArrayList<String> driver_toggles = new ArrayList<>(Arrays.asList("dpad_up"));
    //activated from one press to the next release (not the release that immediately follows)
    ArrayList<String> driver_buttons = new ArrayList<>(Arrays.asList());
    //idk what to call them but basically they're only active for one tick
    //everything else is active while and only while it is held

    //Encoder Stuff
    double inchPer90 = 13.2;

    //auton functions

    default void sleep(double milliseconds) {
        long t = System.nanoTime();
        while (System.nanoTime() - t <= milliseconds * 1000000) {

        }
    }

    default void SetArm(Robot r, int pos){

        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setTargetPosition(armPositions[pos]);
        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setMode(DcMotor.RunMode.RUN_TO_POSITION);
        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setPower(0.4);
        while(r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].isBusy()) {
        }
        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setPower(0.1);

    }
    default void SetServo(Robot r, int position) {
        double pos = servoPositions[position + 1];
        for (int i = 0; i < 100; i++) {
            r.servo_list[servo_names.indexOf("BucketServo")].setPosition(pos);
            r.servo_list[servo_names.indexOf("BucketServo")].setPosition(pos+0.001);
        }
    }

    default boolean checkPos(Robot r) {
        return (r.getDistInch(r.distance_sensor_list[0]) < 4);
    }

}
