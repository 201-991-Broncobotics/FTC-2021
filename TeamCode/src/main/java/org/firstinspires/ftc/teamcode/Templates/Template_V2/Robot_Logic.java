package org.firstinspires.ftc.teamcode.Templates.Template_V2;

import java.util.ArrayList;
import java.util.Arrays;

public class Robot_Logic {

    boolean usePID = false;
    double inchPer90 = 13.2;

    ArrayList<String> distance_sensor_names = new ArrayList<>(Arrays.asList("sensor_distance"));

    ArrayList<String> led_names = new ArrayList<>(Arrays.asList());

    ArrayList<String> dc_motor_names = new ArrayList<>(Arrays.asList("arm", "intake", "duckWheel"));
    int[] dc_motor_directions = {0, 1, 1}; //0 for forward, 1 for reverse

    ArrayList<String> servo_names = new ArrayList<>(Arrays.asList("right"));

    //No need to ever change this
    ArrayList<String> wheel_names = new ArrayList<>(Arrays.asList("rightFront", "rightBack", "leftBack", "leftFront"));

    //Operator Logic
    ArrayList<String> operator_toggles = new ArrayList<>(Arrays.asList("a", "y", "x", "b"));
    //activated from one press to the next release (not the release that immediately follows)
    ArrayList<String> operator_buttons = new ArrayList<>(Arrays.asList("dpad_up", "dpad_down", "right_bumper", "left_bumper", "dpad_right"));
    //idk what to call them but basically they're only active for one tick

    //everything else is active while and only while it is held
    //names: a, b, x, y, dpad_up, dpad_down, dpad_left, dpad_right, left_bumper, right_bumper

    //Driver Logic (besides driving)
    ArrayList<String> driver_toggles = new ArrayList<>(Arrays.asList());
    ArrayList<String> driver_buttons = new ArrayList<>(Arrays.asList());
}
