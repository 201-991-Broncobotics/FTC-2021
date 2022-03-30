package org.firstinspires.ftc.teamcode.Templates.Template_V4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Robot_Logic {
    //Game Variables
    public double[] servoPositions = {1.0, 0.8, 0.6};
    public double[] servoPositions2 = {1.0, 0.8, 0.7, 0.6};
    public ArrayList<String> servoPositionNames = new ArrayList<>(Arrays.asList("Dump", "Mid", "Bottom"));

    public int[] armPositions = {200, 850, 950, 1300};
    public ArrayList<String> armPositionNames = new ArrayList<>(Arrays.asList("Reset_Arm", "Low_Goal", "Middle_Goal", "High_Goal"));

    public boolean usePID = false;
    public double inchPer90 = 13.2;

    public ArrayList<String> distance_sensor_names = new ArrayList<>(Arrays.asList("sensor_distance"));

    public ArrayList<String> led_names = new ArrayList<>(Arrays.asList());

    public ArrayList<String> dc_motor_names = new ArrayList<>(Arrays.asList("arm", "intake", "duckWheel"));
    public int[] dc_motor_directions = {0, 1, 1}; //0 for forward, 1 for reverse

    public ArrayList<String> servo_names = new ArrayList<>(Arrays.asList("right"));

    public void set_keybinds() {

        //motors

        //arm

        /* Testing
        temp.add("driver right_stick_y"); //SHOULD throw an error
        temp.add("default"); //SHOULD throw an error
        temp.add(0.13); //SHOUlD throw an error (should be 0.5)
        temp.add(0.5); //negative means stick is pointing up
        */

        temp.add("driver dpad_left");
        temp.add("button");
        temp.add(-1); //SHOULD throw an error (-1)
        temp.add(armPositions); //SHOULD throw an error (armPositions)

        temp.add("driver dpad_right");
        temp.add("button");
        temp.add(1);
        temp.add(armPositions);

        update("arm"); //SHOULD throw an error

        //intake

        temp.add("driver a");
        temp.add("toggle");
        temp.add("normal");
        temp.add(0.3); //SHOULD throw an error

        temp.add("driver y");
        temp.add("toggle");
        temp.add("normal"); //SHOULD throw an error
        temp.add(-0.3);

        update("intake");

        //duckWheel

        temp.add("driver b");
        temp.add("toggle");
        temp.add("gradient");
        temp.add(0.7);

        temp.add("driver x");
        temp.add("toggle");
        temp.add("gradient");
        temp.add(-0.7);

        update("duckWheel");

        //servos - note the code is the same as if it were a dc motor

        //right

        temp.add("driver right_stick_y"); //SHOULD throw an error
        temp.add("default"); //SHOULD throw an error
        temp.add(0.13); //SHOUlD throw an error (should be 0.5)
        temp.add(0.5); //negative means stick is pointing up

        temp.add("driver dpad_down");
        temp.add("button");
        temp.add(-1);
        temp.add(servoPositions2); //SHOULD throw an error; should be servoPositions2

        temp.add("driver dpad_up");
        temp.add("button");
        temp.add(1);
        temp.add(servoPositions2);

        temp.add("driver left_bumper");
        temp.add("button");
        temp.add(-2);
        temp.add(servoPositions);

        temp.add("driver right_bumper");
        temp.add("button");
        temp.add(2);
        temp.add(servoPositions);

        update("right");
    }

    /* KEY BINDS INSTRUCTIONS
        * Note: We can have the same button control 2 different things, but it has to have the same type (button/toggle/default) each time
        * button name has to be same as it appears in keys
     */

    //Key Binds - note we can have the same button control 2 different things - should test this feature as well - not 100% sure
    //button name has to be same as it appears in keys
    //dc motors
    //default (button): 2 inputs: mode ("gradient" or "normal"; always completes gradient in 0.75 seconds) and power (-1.0 to 1.0)
    //default (axis): 2 inputs: power going up, power going down; cannot be a gradient
    //toggle: 2 inputs: same as above
    //button: 2 inputs: how much do we increase/decrease index, which list to follow

    //servos
    //same as dc motors, except:
    //no gradients - there's only one input for toggle/default: speed (in % of servo per second - ex. 0.5 -> 1/4 rotation/s)

    //If a key is a button somewhere, it is a button everywhere

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




































    //No need to ever change this
    public ArrayList<String> wheel_names = new ArrayList<>(Arrays.asList("rightFront", "rightBack", "leftBack", "leftFront"));

    public ArrayList<String> keys = new ArrayList<>(Arrays.asList(
            "operator a", "operator b", "operator x", "operator y", "operator dpad_up", "operator dpad_down",
            "operator dpad_left", "operator dpad_right", "operator left_bumper", "operator right_bumper",
            "driver a", "driver b", "driver x", "driver y", "driver dpad_up", "driver dpad_down",
            "driver dpad_left", "driver dpad_right", "driver left_bumper", "driver right_bumper",
            "operator left_stick_x", "operator left_stick_y", "operator right_stick_x", "operator right_stick_y",
            "operator left_trigger", "operator right_trigger", "driver right_stick_y", "driver left_trigger"
    ));

    public HashMap<String, ArrayList<Object>> keybinds = new HashMap<>();
    public ArrayList<Object> temp = new ArrayList<>();
    public Object temp2;

    public void update(String name) {
        if (!dc_motor_names.contains(name) && !servo_names.contains(name)) {
            throw new IllegalArgumentException("You misspelled " + name + " - make sure its exactly as it's spelled in dc motor list or servo list. Idiot");
        }
        keybinds.put(name, new ArrayList<Object>());
        for (int i = 0; i < temp.size(); i += 4) {
            if (keybinds.get(name).contains(temp.get(i))) {
                throw new IllegalArgumentException("You can't have \"" + temp.get(i) + "\" have 2 different functions. Error is in section " + name + ". ");
            } else if (!keys.contains((String) temp.get(i))) {
                throw new IllegalArgumentException("You misspelled " + temp.get(i) + " in section " + name + "  - make sure its exactly as it's spelled in keys. ");
            } else if (temp.get(i+1).equals("button")) {
                try {
                    temp2 = (int) temp.get(i+2);
                } catch(ClassCastException e) {
                    throw new IllegalArgumentException("Increments have to be by an integer amount. Error was in section " + name + " subsection " + temp.get(i) + ". ");
                }
                if (servo_names.contains(name)) {
                    try {
                        temp2 = (double[]) temp.get(i + 3);
                    } catch(ClassCastException e) {
                        throw new IllegalArgumentException("For servos, the list has to be one of doubles. Error was in section " + name + " subsection " + temp.get(i) + ". ");
                    }
                } else {
                    try {
                        temp2 = (int[]) temp.get(i + 3);
                    } catch(ClassCastException e) {
                        throw new IllegalArgumentException("For dc motors, the list has to be one of integers. Error was in section " + name + " subsection " + temp.get(i) + ". ");
                    }
                }
            } else if (temp.get(i+1).equals("toggle") || temp.get(i+1).equals("default")) {
                if (temp.get(i+1).equals("default") && (keys.indexOf((String) temp.get(i)) > 19)) {
                    try {
                        temp2 = (double) temp.get(i+2);
                        temp2 = (double) temp.get(i+3);
                    } catch(ClassCastException e) {
                        throw new IllegalArgumentException("Power has to be a double. Error was in section " + name + " subsection " + temp.get(i) + ". ");
                    }
                    if ((Math.max(Math.abs((double) temp.get(i+2)), Math.abs((double) temp.get(i+3))) > 1) && dc_motor_names.contains(name)) {
                        throw new IllegalArgumentException("DC Motor Power has to be between -1.0 and 1.0. Error was in section " + name + " subsection " + temp.get(i) + ". ");
                    }
                } else if (servo_names.contains(name)) { //servo, default or toggle
                    try {
                        temp2 = (double) temp.get(i+2);
                    } catch(ClassCastException e) {
                        throw new IllegalArgumentException("Power has to be a double. Error was in section " + name + " subsection " + temp.get(i) + ". ");
                    }
                } else {
                    try {
                        temp2 = (String) temp.get(i+2);
                        if (!((String) temp.get(i+2)).equals("normal") && !((String) temp.get(i+2)).equals("gradient")) {
                            throw new ClassCastException();
                        }
                    } catch(ClassCastException e) {
                        throw new IllegalArgumentException("Button type has to be \"normal\" or \"gradient\". Error was in section " + name + " subsection " + temp.get(i) + ". ");
                    }
                    try {
                        temp2 = (double) temp.get(i+3);
                        if (Math.abs((double) temp.get(i+3)) > 1) {
                            throw new ClassCastException();
                        }
                    } catch(ClassCastException e) {
                        throw new IllegalArgumentException("Power has to be a double between -1.0 and 1.0. Error was in section " + name + " subsection " + temp.get(i) + ". ");
                    }
                }
            } else {
                throw new IllegalArgumentException("You misspelled " + temp.get(i+1) + " in section " + name + " subsection " + temp.get(i) + " - make sure its \"default\", \"button\" or \"toggle\".");
            }
            for (int j = 0; j < 4; j++) keybinds.get(name).add(temp.get(i+j));
        }
        temp.clear();
    }
}
