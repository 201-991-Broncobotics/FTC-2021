package org.firstinspires.ftc.teamcode.Templates.Template_V3;

import java.util.ArrayList;
import java.util.Arrays;

public interface Robot_Logic {

    //Game Variables
    double[] servoPositions = {1.0, 0.8, 0.6};
    ArrayList<String> servoPositionNames = new ArrayList<>(Arrays.asList("Dump", "Mid", "Bottom"));

    int[] armPositions = {200, 850, 950, 1300};
    ArrayList<String> armPositionNames = new ArrayList<>(Arrays.asList("Reset_Arm", "Low_Goal", "Middle_Goal", "High_Goal"));


    boolean usePID = false;
    double inchPer90 = 13.2;

    ArrayList<String> distance_sensor_names = new ArrayList<>(Arrays.asList("sensor_distance"));

    ArrayList<String> led_names = new ArrayList<>(Arrays.asList());

    ArrayList<String> dc_motor_names = new ArrayList<>(Arrays.asList("arm", "intake", "duckWheel"));
    int[] dc_motor_directions = {0, 1, 1}; //0 for forward, 1 for reverse

    ArrayList<String> servo_names = new ArrayList<>(Arrays.asList("right"));

    //No need to ever change this
    ArrayList<String> wheel_names = new ArrayList<>(Arrays.asList("rightFront", "rightBack", "leftBack", "leftFront"));

    //Key Binds
    //default: hold to power it
    //toggle needs 2 inputs: mode (0 for gradient, 1 for normal), maximum value (can be negative ofc); 0 for gradient, 1 for normal
    //for gradient, will linearly increase to maximum in 0.75 seconds
    //button needs 2: how much do we increase/decrease, which list to follow, OR 69420 to mean that we keep it active for X amount of time
    //default: mode (gradient or maximum), maximum value

    //left stick, right stick, trigger: cross bridge when we get there
    //can also be button / toggle, but we need a minimum threshold for them to activate
    ArrayList<String> operator_keys = new ArrayList<>(Arrays.asList(    "a",        "b",        "x",        "y",        "dpad_up",  "dpad_down",    "dpad_left",    "dpad_right",   "left_bumper",  "right_bumper", "left_stick_x", "left_stick_y", "right_stick_x",    "right_stick_y",    "left_trigger",     "right_trigger"));
    String[] operator_key_types = {                                     "toggle",   "toggle",   "toggle",   "toggle",   "button",   "button",       "unused",       "button",       "button",       "button",       "unused",       "default",      "unused",           "unused",           "unused",           "unused"};
    String[] operator_key_binds = {                                     "intake",   "duckWheel","duckWheel","intake",   "right",    "right",        "unused",       "unused",       "right",        "right",        "unused",       "arm",          "unused",           "unused",           "unused",           "unused"};
    Object[] operator_key_binds1 = {                                    1,          0,          0,          1,          1,          -1,             0,              0,              -2,             2,              0,              0.5,            0,                  0,                  0,                  0};
    Object[] operator_key_binds2 = {                                    0.3,        0.7,        -0.7,       -0.3,       servoPositions, servoPositions, 0,          0,              servoPositions, servoPositions, 0,              -0.13,          0,                  0,                  0,                  0};


    ArrayList<String> driver_keys = new ArrayList<>(Arrays.asList(    "a",        "b",        "x",        "y",        "dpad_up",  "dpad_down",    "dpad_left",    "dpad_right",   "left_bumper",  "right_bumper", "right_stick_y",    "left_trigger"));
    String[] driver_key_types = {                                     "toggle",   "toggle",   "toggle",   "toggle",   "button",   "button",       "unused",       "button",       "button",       "button",       "unused",       "default",      "unused",           "unused",           "unused",           "unused"};
    String[] driver_key_binds = {                                     "intake",   "duckWheel","duckWheel","intake",   "right",    "right",        "unused",       "unused",       "right",        "right",        "unused",       "arm",          "unused",           "unused",           "unused",           "unused"};
    Object[] driver_key_binds1 = {                                    1,          0,          0,          1,          1,          -1,             0,              0,              -2,             2,              0,              0.5,            0,                  0,                  0,                  0};
    Object[] driver_key_binds2 = {                                    0.3,        0.7,        -0.7,       -0.3,       servoPositions, servoPositions, 0,          0,              servoPositions, servoPositions, 0,              -0.13,          0,                  0,                  0,                  0};

}
