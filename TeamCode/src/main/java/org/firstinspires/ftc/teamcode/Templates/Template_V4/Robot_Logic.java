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

    public ArrayList<String> distance_sensor_names = new ArrayList<>(Arrays.asList("sensor_distance"));

    public ArrayList<String> led_names = new ArrayList<>(Arrays.asList());

    public ArrayList<String> dc_motor_names = new ArrayList<>(Arrays.asList("arm", "intake", "duckWheel"));
    public int[] dc_motor_directions = {0, 1, 1}; //0 for forward, 1 for reverse

    public ArrayList<String> servo_names = new ArrayList<>(Arrays.asList("right"));

    public void set_keybinds() {

        //arm

        /* Testing
        temp.add("driver right_stick_y"); //SHOULD throw an error
        temp.add("default"); //SHOULD throw an error
        temp.add(0.13); //SHOUlD throw an error (should be 0.5)
        temp.add(0.5); //negative means stick is pointing up
        */

        temp.add("driver dpad_left");
        temp.add("button");
        temp.add(-1);
        temp.add(armPositions);

        temp.add("driver dpad_right");
        temp.add("button");
        temp.add(1);
        temp.add(armPositions);

        update("arm");

        //intake

        temp.add("driver a");
        temp.add("toggle");
        temp.add("normal");
        temp.add(0.3);

        temp.add("driver y");
        temp.add("toggle");
        temp.add("normal");
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

        //right

        temp.add("driver right_stick_y");
        temp.add("default");
        temp.add(0.13);
        temp.add(0.5);

        temp.add("driver dpad_down");
        temp.add("button");
        temp.add(-1);
        temp.add(servoPositions2);

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

    /*
      KEY BINDS INSTRUCTIONS
        * We can have the same button control 2 different things, but it has to have the same type (button/toggle/default) each time
        * Button name has to be same as it appears in keys
        *
        * DC MOTORS
            * default (button) - 2 inputs; just set mode as "default", program will know if its a button or axis
                * mode ("gradient" or "normal" - gradient will always complete in 0.75 seconds)
                * power (double between -1.0, 1.0)
                    * if you put -1, 0, or 1 it will throw an error - has to be -1.0, 0.0, 1.0
            * default (axis) - 2 inputs
                * power if axis is positive
                * power if axis is negative
                    * NOTE: for left, right stick y, "positive" is down; for left, right stick x, "positive" is right;
                            for triggers, it is always positive, but you still have to put a double for negative.
                * same requirements as power in default buttons
            * toggle - 2 inputs
                * inputs are identical as default (button)
            * button - 2 inputs
                * how much we increase/decrease index on the list from where we are now
                * which list do we follow. Note, it must be an int[] array
        * SERVOS - same as DC motors except for the following:
            * for power, it's not servo power but instead half-revolutions/sec. (I think) Can be negative or positive; must be a double (i.e. not 2, -4).
            * only 1 input for default (button) or toggle: power. Cannot be a gradient so mode is useless
        *
        * For any error, there will be an IllegalArgumentException thrown showing you where the error is.

      FEATURES
        * we can have multiple buttons, from either controller, access the same motor in
                different ways (i.e. they don't have to have the same parameters)
        * we can have the same button control 2 different motors
        * can change button functions freely - toggle, button, default and all their parameters
        * not all buttons have to be used
     */




































    //No need to ever change this
    public double inchPer90 = 13.2;
    
    public ArrayList<String> wheel_names = new ArrayList<>(Arrays.asList("rightFront", "rightBack", "leftBack", "leftFront"));

    public ArrayList<String> keys = new ArrayList<>(Arrays.asList(
            "operator a", "operator b", "operator x", "operator y", "operator dpad_up", "operator dpad_down",
            "operator dpad_left", "operator dpad_right", "operator left_bumper", "operator right_bumper",
            "driver a", "driver b", "driver x", "driver y", "driver dpad_up", "driver dpad_down",
            "driver dpad_left", "driver dpad_right", "driver left_bumper", "driver right_bumper",
            "operator left_stick_x", "operator right_stick_x", "operator left_stick_y", "operator right_stick_y",
            "driver right_stick_y", "operator left_trigger", "operator right_trigger", "driver left_trigger"
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
