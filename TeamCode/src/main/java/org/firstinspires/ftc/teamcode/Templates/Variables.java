package org.firstinspires.ftc.teamcode.Templates;

import java.util.ArrayList;
import java.util.Arrays;

public interface Variables {

    //game variables
    double[] servoPositions = {0.6, 0.8, 1.0};

    //Encoder stuff
    double inchPer90 = 13.2;

    //Robot motors, etc.

    ArrayList<String> distance_sensor_names = new ArrayList<>(Arrays.asList("dSensor"));

    ArrayList<String> led_names = new ArrayList<>(Arrays.asList("led"));

    ArrayList<String> wheel_names = new ArrayList<>(Arrays.asList("Right_Front", "Right_Back", "Left_Back", "Left_Front"));

    ArrayList<String> dc_motor_names = new ArrayList<>(Arrays.asList("LinearSlide", "Intake", "DuckWheel"));
    int[] dc_motor_directions = {0, 1, 1}; //0 for forward, 1 for reverse

    ArrayList<String> servo_names = new ArrayList<>(Arrays.asList("BucketServo"));

    //Operator Logic
    ArrayList<String> toggles = new ArrayList<>(Arrays.asList("a", "y"));
        //activated from one press to the next release (not the release that immediately follows)
    ArrayList<String> buttons = new ArrayList<>(Arrays.asList("dpad_up", "dpad_down", "right_bumper", "left_bumper"));
        //idk what to call them but basically they're only active for one tick
        //everything else is active while and only while it is held
}
