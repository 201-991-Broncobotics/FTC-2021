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
    //toggle needs 2 inputs: mode (0 for gradient, 1 for normal), final value (can be negative); 0 for gradient, 1 for normal
    //for gradient, will linearly increase to maximum in 0.75 seconds
    //button needs 2: how much do we increase/decrease, which list to follow
    //default: mode (gradient or maximum), maximum value

    //left stick, right stick, trigger: cross bridge when we get there
    //can also be button / toggle, but we need a minimum threshold for them to activate
    ArrayList<String> operator_keys = new ArrayList<>(Arrays.asList(    "a",        "b",        "x",        "y",        "dpad_up",  "dpad_down",    "dpad_left",    "dpad_right",   "left_bumper",  "right_bumper", "left_stick_x", "left_stick_y", "right_stick_x",    "right_stick_y",    "left_trigger",     "right_trigger"));
    String[] operator_key_types = {                                     "unused",   "unused",   "unused",   "unused",   "unused",   "unused",       "unused",       "unused",       "unused",       "unused",       "unused",       "unused",      "unused",           "unused",           "unused",           "unused"};
    String[] operator_key_binds = {                                     "unused",   "unused",   "unused",   "unused",   "unused",    "unused",      "unused",       "unused",       "unused",       "unused",        "unused",      "unused",      "unused",           "unused",           "unused",           "unused"};
    Object[] operator_key_binds1 = {                                    1,          0,          0,          1,          1,          -1,             0,              0,              -2,             2,              0,              0.5,            0,                  0,                  0,                  0};
    Object[] operator_key_binds2 = {                                    0.3,        0.7,        -0.7,       -0.3,       servoPositions, servoPositions, 0,          0,              servoPositions, servoPositions, 0,              -0.13,          0,                  0,                  0,                  0};


    ArrayList<String> driver_keys = new ArrayList<>(Arrays.asList(    "a",        "b",        "x",        "y",        "dpad_up",  "dpad_down",    "dpad_left",    "dpad_right",   "left_bumper",  "right_bumper", "right_stick_y","left_trigger"));
    String[] driver_key_types = {                                     "toggle",   "toggle",   "toggle",   "toggle",   "button",   "button",       "unused",       "button",       "button",       "button",       "unused",       "default"};
    String[] driver_key_binds = {                                     "intake",   "duckWheel","duckWheel","intake",   "right",    "right",        "unused",       "unused",       "right",        "right",        "unused",       "arm"};
    Object[] driver_key_binds1 = {                                    1,          0,          0,          1,          1,          -1,             0,              0,              -2,             2,              0,              0.5};
    Object[] driver_key_binds2 = {                                    0.3,        0.7,        -0.7,       -0.3,       servoPositions, servoPositions, 0,          0,              servoPositions, servoPositions, 0,              -0.13};


    /* FEATURES
        * can change button types freely - toggle, button, default
            * to test: easy - make sure you have all 3 types
        *
        * not all buttons have to be used - if unused, you don't really have to do anything, just make sure it's not connected to a motor. You can still have it be a toggle/button/etc.
            * to testL easy - make sure at least one button is not used
        *
        * can change which motor the buttons go to freely
            * to test: switch the roles of buttons a and b
        *
        * gradient/non-gradient modes can be switched easily
            * to test: switch a from non-gradient to gradient
        *
        * toggle: acts as a normal toggle
        * default: active iff the button is held down
            * for both of these:
            * gradient/non-gradient options
            * choose the final value of the motor (ex. you want it to go to -0.3 without a gradient, 0.15 with a gradient, etc.)
                * to test, change the mode and final value to be (including sign)
        *
        * button: active only when it is pressed
            * choose value for motor/servo position on a list
            * choose how many spaces to move up/down on that list
            * to test, change how many spaces we skip or change the values of the list
        *
        * axes also have 3 modes: button, toggle are same as buttons
            * to test, give an axis the role of button a
            * default mode:
                * same as button, but with a multiplier based on trigger depth
                * also, you can choose the value based on if the trigger is positive or negative (only for left/right sticks) (ex. 0.5 power going up, 0.15 power going down)

       HOW TO IMPROVE:
       * make toggle/default/etc. based on the motor, not based on the button
     */
}
