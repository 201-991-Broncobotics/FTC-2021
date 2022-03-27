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

    //dc motors
    //default (button): 2 inputs: mode (0 for gradient, 1 for normal; always completes gradient in 0.75 seconds) and power
    //toggle: 2 inputs: same as above
    //button: 2 inputs: how much do we increase/decrease index, which list to follow
    //default: mode (gradient or maximum), maximum value
    //default (trigger): 2 inputs: value going up, value going down; cannot be a gradient - that would be stupid

    //servos
    //basically same as dc motors, except:
    //no gradients - there's only one input for toggle/default: speed (in % of servo per second - ex. 0.5 -> 1/4 rotation/s)

    ArrayList<String> operator_keys = new ArrayList<>(Arrays.asList(    "a",        "b",        "x",        "y",        "dpad_up",  "dpad_down",    "dpad_left",    "dpad_right",   "left_bumper",  "right_bumper", "left_stick_x", "left_stick_y", "right_stick_x",    "right_stick_y",    "left_trigger",     "right_trigger"));
    String[] operator_key_types = {                                     "unused",   "unused",   "unused",   "unused",   "unused",   "unused",       "unused",       "unused",       "unused",       "unused",       "unused",       "unused",      "unused",           "unused",           "unused",           "unused"};
    String[] operator_key_binds = {                                     "unused",   "unused",   "unused",   "unused",   "unused",    "unused",      "unused",       "unused",       "unused",       "unused",        "unused",      "unused",      "unused",           "unused",           "unused",           "unused"};
    Object[] operator_key_binds1 = {                                    1,          0,          0,          1,          1,          -1,             0,              0,              -2,             2,              0,              0.5,            0,                  0,                  0,                  0};
    Object[] operator_key_binds2 = {                                    0.3,        0.7,        -0.7,       -0.3,       servoPositions, servoPositions, 0,          0,              servoPositions, servoPositions, 0,              0.13,          0,                  0,                  0,                  0};


    ArrayList<String> driver_keys = new ArrayList<>(Arrays.asList(    "a",        "b",        "x",        "y",        "dpad_up",  "dpad_down",    "dpad_left",    "dpad_right",   "left_bumper",  "right_bumper", "right_stick_y","left_trigger"));
    String[] driver_key_types = {                                     "toggle",   "toggle",   "toggle",   "toggle",   "button",   "button",       "button",       "button",       "button",       "button",       "default",      "unused"};
    String[] driver_key_binds = {                                     "intake",   "duckWheel","duckWheel","intake",   "right",    "right",        "arm",          "arm",       "right",        "right",        "arm",          "unused"};
    Object[] driver_key_binds1 = {                                    1,          0,          0,          1,          1,          -1,             -1,              1,              -2,             2,              0.5,            0};
    Object[] driver_key_binds2 = {                                    0.3,        0.7,        -0.7,       -0.3,       servoPositions, servoPositions, armPositions,          armPositions,              servoPositions, servoPositions, 0.13,           0};

    //What do I want to test: make servo based on right stick (would have to move arm to dpad left and right)
    /* FEATURES
        * buttons can be on either controller, but DO NOT have 2 controllers controlling the same motor
        *
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
        *
        * MAYBE - make it so both controllers can access the same motor; will be very tough though - probably even pointless; definitely check to make sure all of this works first though
     */
}
