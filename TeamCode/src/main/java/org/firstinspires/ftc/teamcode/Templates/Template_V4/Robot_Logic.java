package org.firstinspires.ftc.teamcode.Templates.Template_V4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Robot_Logic {
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

    ArrayList<String> keys = new ArrayList<>(Arrays.asList(
            "operator a", "operator b", "operator x", "operator y", "operator dpad_up", "operator dpad_down",
            "operator dpad_left", "operator dpad_right", "operator left_bumper", "operator right_bumper",
            "driver a", "driver b", "driver x", "driver y", "driver dpad_up", "driver dpad_down",
            "driver dpad_left", "driver dpad_right", "driver left_bumper", "driver right_bumper",
            "operator left_stick_x", "operator left_stick_y", "operator right_stick_x", "operator right_stick_y",
            "operator left_trigger", "operator right_trigger", "driver right_stick_y", "driver left_trigger"
            ));

    HashMap<String, ArrayList<Object>> keybinds = new HashMap<>();
    ArrayList<Object> temp = new ArrayList<>();

    public void update(String name) {
        keybinds.put(name, new ArrayList<Object>());
        for (int i = 0; i < temp.size(); i += 1) {
            keybinds.get(name).add(temp.get(i));
        }
        temp.clear();
    }

    public void set_keybinds() {

        //motors

        //arm

        temp.add("driver right_stick_y");
        temp.add("default");
        temp.add(0.5);
        temp.add(0.13);

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

        //servos - note the code is the same as if it were a dc motor

        //duckWheel
        temp.clear();

        temp.add("driver dpad_down");
        temp.add("button");
        temp.add(-1);
        temp.add(servoPositions);

        temp.add("driver dpad_up");
        temp.add("button");
        temp.add(1);
        temp.add(servoPositions);

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

    //Key Binds - note we can have the same button control 2 different things - should test this feature as well - not 100% sure

    //dc motors
    //default (button): 2 inputs: mode ("gradient" or "normal"; always completes gradient in 0.75 seconds) and power (-1.0 to 1.0)
    //default (axis): 2 inputs: power going up, power going down; cannot be a gradient
    //toggle: 2 inputs: same as above
    //button: 2 inputs: how much do we increase/decrease index, which list to follow

    //servos
    //same as dc motors, except:
    //no gradients - there's only one input for toggle/default: speed (in % of servo per second - ex. 0.5 -> 1/4 rotation/s)

    //If a key is a button somewhere, it is a button everywhere
}
