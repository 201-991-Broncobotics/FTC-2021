package org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.Robot_Logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Logic_Base extends Robot_Logic {

    public HashMap<String, String> button_types = new HashMap<>();
    String[] temporary = new String[28];

    public Robot robot;

    public double[] times_started = new double[dc_motor_names.size() + servo_names.size()]; //in seconds
    public double[] target_positions = new double[dc_motor_names.size() + servo_names.size()];
    public double[] starting_positions = new double[dc_motor_names.size() + servo_names.size()]; //never use for dc_motors

    ArrayList<Object> object_keys;
    int number_of_keys;
    boolean object_is_active;

    int temp_0;
    int temp_1;
    int temp_2;

    boolean increasing;
    int temp_3;

    public Logic_Base(Robot r) {
        robot = r;
        set_keybinds();
        for (Map.Entry<String, ArrayList<Object>> element : keybinds.entrySet()) { //for every entry in keybinds...
            for (int i = 0; i < (element.getValue()).size(); i += 4) {
                if (temporary[keys.indexOf((String) element.getValue().get(i))] == null) {
                    temporary[keys.indexOf((String) element.getValue().get(i))] = (String) element.getValue().get(i+1);
                } else if (!((temporary[keys.indexOf((String) element.getValue().get(i))]).equals((String) element.getValue().get(i+1)))) {
                    throw new IllegalArgumentException("A button cannot have 2 types; however, you are setting \"" + element.getValue().get(i) +
                            "\" to be both a " + temporary[keys.indexOf((String) element.getValue().get(i))] + " and a " + element.getValue().get(i+1) + ". ");
                }
            }
        }
        for (int i = 0; i < 28; i++) {
            button_types.put(keys.get(i), temporary[i]);
        }
    }

    public void update_robot(boolean operator_a, boolean operator_b, boolean operator_x, boolean operator_y, boolean operator_dpad_up,
                             boolean operator_dpad_down, boolean operator_dpad_left, boolean operator_dpad_right, boolean operator_left_bumper,
                             boolean operator_right_bumper, double operator_left_stick_x, double operator_left_stick_y, double operator_right_stick_x,
                             double operator_right_stick_y, double operator_left_trigger_depth, double operator_right_trigger_depth,
                             boolean driver_a, boolean driver_b, boolean driver_x, boolean driver_y, boolean driver_dpad_up, boolean driver_dpad_down,
                             boolean driver_dpad_left, boolean driver_dpad_right, boolean driver_left_bumper, boolean driver_right_bumper,
                             double driver_right_stick_y, double driver_left_trigger_depth) {

        boolean[] buttons = {operator_a, operator_b, operator_x, operator_y, operator_dpad_up, operator_dpad_down, operator_dpad_left, operator_dpad_right, operator_left_bumper, operator_right_bumper, driver_a, driver_b, driver_x, driver_y, driver_dpad_up, driver_dpad_down, driver_dpad_left, driver_dpad_right, driver_left_bumper, driver_right_bumper};
        double[] axes = {operator_left_stick_x, operator_right_stick_x, operator_left_stick_y, operator_right_stick_y, driver_right_stick_y, operator_left_trigger_depth, operator_right_trigger_depth, driver_left_trigger_depth};

        for (Map.Entry<String, ArrayList<Object>> element : keybinds.entrySet()) { //for every element in keybinds

            object_keys = element.getValue(); //object_keys = what the motor maps to
            number_of_keys = object_keys.size() / 4; //number of keys that map to the motor
            object_is_active = false; //object is active iff at least one key that maps to it is activated

            for (int i = 0; i < number_of_keys; i++) { //for every key that maps to the button
                temp_0 = keys.indexOf((String) object_keys.get(4 * i));
                if (temp_0 < 20) { //if its a button
                    object_is_active = (object_is_active || buttons[temp_0]);
                } else { //if its an axis
                    object_is_active = (object_is_active || Math.abs(axes[temp_0 - 20]) > 0.1);
                }
            }

            if (dc_motor_names.contains(element.getKey())) { //if it's a dc motor

                temp_1 = dc_motor_names.indexOf(element.getKey()); //temp_1 = where the index is everywhere - it's all on the same naming system

                if (!object_is_active) { //if we aren't pressing any relevant buttons
                    times_started[temp_1] = -10.0; //reset it and make sure its staying where we want it to
                    robot.dc_motor_list[temp_1].setPower(Math.max(-0.25, Math.min((target_positions[temp_1] - robot.dc_motor_list[temp_1].getCurrentPosition()) * 0.005, 0.25)));

                } else {
                    if (times_started[temp_1] < 0) //if we're on and it's reset, un-reset it
                        times_started[temp_1] = (double) System.nanoTime() / 1000000000.0;
                    target_positions[temp_1] = robot.dc_motor_list[temp_1].getCurrentPosition(); //only update target position if we're moving - don't update if we are not
                            //has to be before for dc motors
                    for (int i = 0; i < number_of_keys; i++) { //4 * i: button name; +1: button/default/toggle;
                                    //+2: normal/gradient /  how much do we increase / power on way up
                                    //+3: maximum power / position list / power on way down

                        temp_0 = keys.indexOf((String) object_keys.get(4 * i)); //where button is in list of keys; < 20 -> button, >= 20 -> axis

                        if (temp_0 < 20) { //if its a button
                            if (buttons[temp_0]) {
                                if ((((String) object_keys.get(4 * i + 1)).equals("button")) || (((String) object_keys.get(4 * i + 1)).equals("cycle"))) { //4 * i + 2: what we change by; 4 * i + 3: positions
                                    if (((int[]) object_keys.get(4 * i + 3)).length == 1) {
                                        target_positions[temp_1] = ((int[]) object_keys.get(4 * i + 3))[0];
                                    } else {
                                        increasing = (((int[]) object_keys.get(4 * i + 3))[1] > ((int[]) object_keys.get(4 * i + 3))[0]);
                                        temp_3 = 0;
                                        while ((temp_3 < ((int[]) object_keys.get(4 * i + 3)).length) && ((((int[]) object_keys.get(4 * i + 3))[temp_3] < target_positions[temp_1]) || (!increasing)) && ((((int[]) object_keys.get(4 * i + 3))[temp_3] > target_positions[temp_1]) || (increasing))) {
                                            temp_3 += 1; //note it stops perfectly if it's equal, it lands one past if it skips over value
                                            //if increasing, then increase while index is less
                                        }
                                        if (((int) object_keys.get(4 * i + 2) > 0) && ((((int[]) object_keys.get(4 * i + 3))[temp_3] != target_positions[temp_1]))) {
                                            temp_3 -= 1; //subtract one if we're going up
                                        }
                                        if (((String) object_keys.get(4 * i + 1)).equals("cycle")) {
                                            if ((temp_3 + 2 > ((int[]) object_keys.get(4 * i + 3)).length) && (increasing == ((int) object_keys.get(4 * i + 2) > 0))) {
                                                temp_3 = 0;
                                            } else if ((temp_3 < 1) && (increasing == ((int) object_keys.get(4 * i + 2) < 0))) {
                                                temp_3 = ((int[]) object_keys.get(4 * i + 3)).length - 1;
                                            } else {
                                                temp_3 = Math.max(0, Math.min(temp_3 + (int) object_keys.get(4 * i + 2), ((int[]) object_keys.get(4 * i + 3)).length - 1));
                                            }
                                        } else {
                                            temp_3 = Math.max(0, Math.min(temp_3 + (int) object_keys.get(4 * i + 2), ((int[]) object_keys.get(4 * i + 3)).length - 1));
                                        }
                                        target_positions[temp_1] = ((int[]) object_keys.get(4 * i + 3))[temp_3]; //change the target position
                                    }
                                } else { //toggle or default
                                    robot.dc_motor_list[temp_1].setPower(((double) object_keys.get(4 * i + 3)) * (
                                            ((String) object_keys.get(4 * i + 2)).equals("normal") ? 1 : Math.min(1, ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1]) / 0.75)
                                    ));
                                }
                            }
                        } else { //if its an axis
                            if ((((String) object_keys.get(4 * i + 1)).equals("button")) || (((String) object_keys.get(4 * i + 1)).equals("cycle"))) {
                                if (axes[temp_0 - 20] == 1) { //4 * i + 2: what we change by; 4 * i + 3: positions
                                    if (((int[]) object_keys.get(4 * i + 3)).length == 1) {
                                        target_positions[temp_1] = ((int[]) object_keys.get(4 * i + 3))[0];
                                    } else {
                                        increasing = (((int[]) object_keys.get(4 * i + 3))[1] > ((int[]) object_keys.get(4 * i + 3))[0]);
                                        temp_3 = 0;
                                        while ((temp_3 < ((int[]) object_keys.get(4 * i + 3)).length) && ((((int[]) object_keys.get(4 * i + 3))[temp_3] < target_positions[temp_1]) || (!increasing)) && ((((int[]) object_keys.get(4 * i + 3))[temp_3] > target_positions[temp_1]) || (increasing))) {
                                            temp_3 += 1; //note it stops perfectly if it's equal, it lands one past if it skips over value
                                            //if increasing, then increase while index is less
                                        }
                                        if (((int) object_keys.get(4 * i + 2) > 0) && ((((int[]) object_keys.get(4 * i + 3))[temp_3] != target_positions[temp_1]))) {
                                            temp_3 -= 1; //subtract one if we're going up
                                        }
                                        if (((String) object_keys.get(4 * i + 1)).equals("cycle")) {
                                            if ((temp_3 + 2 > ((int[]) object_keys.get(4 * i + 3)).length) && (increasing == ((int) object_keys.get(4 * i + 2) > 0))) {
                                                temp_3 = 0;
                                            } else if ((temp_3 < 1) && (increasing == ((int) object_keys.get(4 * i + 2) < 0))) {
                                                temp_3 = ((int[]) object_keys.get(4 * i + 3)).length - 1;
                                            } else {
                                                temp_3 = Math.max(0, Math.min(temp_3 + (int) object_keys.get(4 * i + 2), ((int[]) object_keys.get(4 * i + 3)).length - 1));
                                            }
                                        } else {
                                            temp_3 = Math.max(0, Math.min(temp_3 + (int) object_keys.get(4 * i + 2), ((int[]) object_keys.get(4 * i + 3)).length - 1));
                                        }
                                        target_positions[temp_1] = ((int[]) object_keys.get(4 * i + 3))[temp_3]; //change the target position
                                    }
                                }
                            } else if (((String) object_keys.get(4 * i + 1)).equals("toggle")) {
                                if (axes[temp_0 - 20] == 1) { //same code as for button toggles
                                    robot.dc_motor_list[temp_1].setPower(((double) object_keys.get(4 * i + 3)) * (
                                            ((String) object_keys.get(4 * i + 2)).equals("normal") ? 1 : Math.min(1, ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1]) / 0.75)));
                                }
                            } else if (Math.abs(axes[temp_0 - 20]) > 0.1) {
                                robot.dc_motor_list[temp_1].setPower(axes[temp_0 - 20] * ( //similar to button defaults, except no gradient option
                                            (temp_0 > 24) ? (double) object_keys.get(4 * i + 2) : //if it's a trigger, then set it to the first val
                                            (temp_0 < 22) ? (axes[temp_0 - 20] < 0 ? (double) object_keys.get(4 * i + 2) : (double) object_keys.get(4 * i + 3)) : //it's x
                                            -1 * (axes[temp_0 - 20] > 0 ? (double) object_keys.get(4 * i + 2) : (double) object_keys.get(4 * i + 3)) //else it's a y
                                ));
                            }
                        }
                    }
                }

            } else { //servo

                temp_1 = servo_names.indexOf(element.getKey()) + dc_motor_names.size();
                temp_2 = servo_names.indexOf(element.getKey()); //for servos, theres 2 different things:
                    //temp_2 used for getting/setting position of servo, temp_1 for everything else

                if (!object_is_active) {
                    times_started[temp_1] = -10.0;
                    robot.servo_list[temp_2].setPosition(target_positions[temp_1]);
                    //make sure its staying where we want it to, and update the starting position (don't update starting position if the servo should be moving, obviously)
                    starting_positions[temp_1] = target_positions[temp_1];
                } else {

                    if (times_started[temp_1] < 0) { //un-reset it
                        times_started[temp_1] = (double) System.nanoTime() / 1000000000.0;
                    }

                    for (int i = 0; i < number_of_keys; i++) { //for each one in the map

                        temp_0 = keys.indexOf((String) object_keys.get(4 * i));

                        if (temp_0 < 20) { //if its a button
                            if (buttons[temp_0]) { //if it's on
                                if ((((String) object_keys.get(4 * i + 1)).equals("button")) || (((String) object_keys.get(4 * i + 1)).equals("cycle"))) { //4 * i + 2: what we change by; 4 * i + 3: positions
                                    if (((double[]) object_keys.get(4 * i + 3)).length == 1) {
                                        robot.servo_list[temp_2].setPosition(((double[]) object_keys.get(4 * i + 3))[0]);
                                    } else {
                                        increasing = (((double[]) object_keys.get(4 * i + 3))[1] > ((double[]) object_keys.get(4 * i + 3))[0]);
                                        temp_3 = 0;
                                        while ((temp_3 < ((double[]) object_keys.get(4 * i + 3)).length) && ((((double[]) object_keys.get(4 * i + 3))[temp_3] < target_positions[temp_1]) || (!increasing)) && ((((double[]) object_keys.get(4 * i + 3))[temp_3] > target_positions[temp_1]) || (increasing))) {
                                            temp_3 += 1; //note it stops perfectly if it's equal, it lands one past if it skips over value
                                            //if increasing, then increase while index is less
                                        }
                                        if (((int) object_keys.get(4 * i + 2) > 0) && ((((double[]) object_keys.get(4 * i + 3))[temp_3] != target_positions[temp_1]))) {
                                            temp_3 -= 1; //subtract one if we're going up
                                        }
                                        if (((String) object_keys.get(4 * i + 1)).equals("cycle")) {
                                            if ((temp_3 + 2 > ((double[]) object_keys.get(4 * i + 3)).length) && (increasing == ((int) object_keys.get(4 * i + 2) > 0))) {
                                                temp_3 = 0;
                                            } else if ((temp_3 < 1) && (increasing == ((int) object_keys.get(4 * i + 2) < 0))) {
                                                temp_3 = ((double[]) object_keys.get(4 * i + 3)).length - 1;
                                            } else {
                                                temp_3 = Math.max(0, Math.min(temp_3 + (int) object_keys.get(4 * i + 2), ((double[]) object_keys.get(4 * i + 3)).length - 1));
                                            }
                                        } else {
                                            temp_3 = Math.max(0, Math.min(temp_3 + (int) object_keys.get(4 * i + 2), ((double[]) object_keys.get(4 * i + 3)).length - 1));
                                        }
                                        robot.servo_list[temp_2].setPosition(((double[]) object_keys.get(4 * i + 3))[temp_3]); //change the target position
                                    }
                                } else { //toggle or default
                                    robot.servo_list[temp_2].setPosition(Math.max(0, Math.min(1, //we don't have gradients because that's pointless to add; we have to assign position
                                            starting_positions[temp_1] + (double) object_keys.get(4 * i + 2) * ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1])
                                    )));                   //position: initial position (remember, it stopped updating when we started moving) + speed * time
                                }
                            }
                        } else { //if its an axis
                            if ((((String) object_keys.get(4 * i + 1)).equals("button")) || (((String) object_keys.get(4 * i + 1)).equals("cycle"))) {
                                if (axes[temp_0 - 20] == 1) { //4 * i + 2: what we change by; 4 * i + 3: positions
                                    if (((double[]) object_keys.get(4 * i + 3)).length == 1) {
                                        robot.servo_list[temp_2].setPosition(((double[]) object_keys.get(4 * i + 3))[0]);
                                    } else {
                                        increasing = (((double[]) object_keys.get(4 * i + 3))[1] > ((double[]) object_keys.get(4 * i + 3))[0]);
                                        temp_3 = 0;
                                        while ((temp_3 < ((double[]) object_keys.get(4 * i + 3)).length) && ((((double[]) object_keys.get(4 * i + 3))[temp_3] < target_positions[temp_1]) || (!increasing)) && ((((double[]) object_keys.get(4 * i + 3))[temp_3] > target_positions[temp_1]) || (increasing))) {
                                            temp_3 += 1; //note it stops perfectly if it's equal, it lands one past if it skips over value
                                            //if increasing, then increase while index is less
                                        }
                                        if (((int) object_keys.get(4 * i + 2) > 0) && ((((double[]) object_keys.get(4 * i + 3))[temp_3] != target_positions[temp_1]))) {
                                            temp_3 -= 1; //subtract one if we're going up
                                        }
                                        if (((String) object_keys.get(4 * i + 1)).equals("cycle")) {
                                            if ((temp_3 + 2 > ((double[]) object_keys.get(4 * i + 3)).length) && (increasing == ((int) object_keys.get(4 * i + 2) > 0))) {
                                                temp_3 = 0;
                                            } else if ((temp_3 < 1) && (increasing == ((int) object_keys.get(4 * i + 2) < 0))) {
                                                temp_3 = ((double[]) object_keys.get(4 * i + 3)).length - 1;
                                            } else {
                                                temp_3 = Math.max(0, Math.min(temp_3 + (int) object_keys.get(4 * i + 2), ((double[]) object_keys.get(4 * i + 3)).length - 1));
                                            }
                                        } else {
                                            temp_3 = Math.max(0, Math.min(temp_3 + (int) object_keys.get(4 * i + 2), ((double[]) object_keys.get(4 * i + 3)).length - 1));
                                        }
                                        robot.servo_list[temp_2].setPosition(((double[]) object_keys.get(4 * i + 3))[temp_3]); //change the target position
                                    }
                                }
                            } else if (((String) object_keys.get(4 * i + 1)).equals("toggle")) {
                                if (axes[temp_0 - 20] == 1) {
                                    robot.servo_list[temp_2].setPosition(Math.max(0, Math.min(1, //we don't have gradients because that's pointless to add; we have to assign position
                                            starting_positions[temp_1] + (double) object_keys.get(4 * i + 2) * ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1])
                                    ))); //same as above
                                }
                            } else if (Math.abs(axes[temp_0 - 20]) > 0.1) {//if there's an active axis, set the power to what we want the depth to be if positive/negative * trigger depth
                                robot.servo_list[temp_2].setPosition(Math.max(0, Math.min(1,
                                        robot.servo_list[temp_2].getPosition() + //the expression below is seconds/tick, basically; current pos + seconds/tick * depth * angles/second
                                                ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1]) * axes[temp_0 - 20] * (
                                                        (temp_0 > 24) ? (double) object_keys.get(4 * i + 2) : //if it's a trigger, then set it to the first val
                                                        (temp_0 < 22) ? (axes[temp_0 - 20] < 0 ? (double) object_keys.get(4 * i + 2) : (double) object_keys.get(4 * i + 3)) : //it's x
                                                        -1 * (axes[temp_0 - 20] > 0 ? (double) object_keys.get(4 * i + 2) : (double) object_keys.get(4 * i + 3)) //else it's a y
                                ))));
                                times_started[temp_1] = (double) System.nanoTime() / 1000000000.0;
                                //we have to use calculus to know where our next target position should be - unreliable, unfortunately, but best thing we have
                            }
                        }
                    }
                    target_positions[temp_1] = robot.servo_list[temp_2].getPosition(); //has to be after for servos
                }
            }
        }
    }

    public int[] key_values = new int[28];
    public double speedFactor;
    public double heading = 0;
    public double desiredHeading = 0;
    public double correction = 0;
    public double current_error;
    public double previous_error;
    public long current_time;
    public long previous_time;
    public double p_weight = 0.025;
    public double d_weight = 0.85;

    public void pause(double milliseconds) {
        long t = System.nanoTime();
        while (System.nanoTime() - t <= milliseconds * 1000000) {

        }
    }

    public void ExecuteEncoders(double Speed) {
        robot.SpeedSet(Speed);
        while (robot.MotorsBusy()) {
        }
        robot.SpeedSet(0.2);
        pause(100);
        robot.SpeedSet(0);
        pause(200);
    }

    public void Drive(double Dist){
        robot.DriveDistance(-Dist);
        ExecuteEncoders(0.7);
    }

    public void Drive(double Dist, String Direction){
        robot.DriveDistance(Dist, Direction);
        ExecuteEncoders(0.7);
    }

    public void Drive(double Dist, String Direction, double Speed){
        robot.DriveDistance(Dist, Direction);
        ExecuteEncoders(Speed);
    }

    public void Turn(int Degrees, String Direction){
        if(Direction.equals("Right")) {
            robot.turnWithEncoders(Degrees);
        } else if(Direction.equals("Left")) {
            robot.turnWithEncoders(-Degrees);
        }
        ExecuteEncoders(0.7);
    }

    public void SetPower(String name, double power) {
        robot.dc_motor_list[dc_motor_names.indexOf(name)].setPower(power);
    }

    public void ResetEncoder(String name) {
        robot.dc_motor_list[dc_motor_names.indexOf(name)].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public boolean button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        int temp = keys.indexOf(button_name);
        if (("toggle").equals(button_types.get(button_name))) {

            key_values[temp] += (button_pressed == (key_values[temp] % 2 == 0)) ? 1 : 0;
            button_active = (key_values[temp] % 4 != 0);

        } else if (("default").equals(button_types.get(button_name))) {

            button_active = button_pressed;

        } else {

            button_active = (key_values[temp] % 2 == 0) && (button_pressed);
            key_values[temp] += (button_pressed == (key_values[temp] % 2 == 0)) ? 1 : 0;

        }

        return button_active;
    }

    public double axis(double axis, String axis_name) {
        double axis_value;
        int temp = keys.indexOf(axis_name);
        if (("toggle").equals(button_types.get(axis_name))) {

            key_values[temp] += ((Math.abs(axis) > 0.1) == (key_values[temp] % 2 == 0)) ? 1 : 0;
            axis_value = key_values[temp] % 4 != 0 ? 1 : 0;

        } else if (("default").equals(button_types.get(axis_name))) {

            axis_value = axis;

        } else {

            axis_value = (key_values[temp] % 2 == 0) && (Math.abs(axis) > 0.1) ? 1 : 0;
            key_values[temp] += ((Math.abs(axis) > 0.1) == (key_values[temp] % 2 == 0)) ? 1 : 0;

        }

        return axis_value;
    }

    public void execute_controllers(Gamepad gamepad1, Gamepad gamepad2) {

        drive(gamepad1);
        update_robot(
                button_active(gamepad2.a, "operator a"),
                button_active(gamepad2.b, "operator b"),
                button_active(gamepad2.x, "operator x"),
                button_active(gamepad2.y, "operator y"),
                button_active(gamepad2.dpad_up, "operator dpad_up"),
                button_active(gamepad2.dpad_down, "operator dpad_down"),
                button_active(gamepad2.dpad_left, "operator dpad_left"),
                button_active(gamepad2.dpad_right, "operator dpad_right"),
                button_active(gamepad2.left_bumper, "operator left_bumper"),
                button_active(gamepad2.right_bumper, "operator right_bumper"),
                axis(gamepad2.left_stick_x, "operator left_stick_x"),
                axis(gamepad2.left_stick_y, "operator left_stick_y"),
                axis(gamepad2.right_stick_x, "operator right_stick_x"),
                axis(gamepad2.right_stick_y, "operator right_stick_y"),
                axis(gamepad2.left_trigger, "operator left_trigger"),
                axis(gamepad2.right_trigger, "operator right_trigger"),
                button_active(gamepad1.a, "driver a"),
                button_active(gamepad1.b, "driver b"),
                button_active(gamepad1.x, "driver x"),
                button_active(gamepad1.y, "driver y"),
                button_active(gamepad1.dpad_up, "driver dpad_up"),
                button_active(gamepad1.dpad_down, "driver dpad_down"),
                button_active(gamepad1.dpad_left, "driver dpad_left"),
                button_active(gamepad1.dpad_right, "driver dpad_right"),
                button_active(gamepad1.left_bumper, "driver left_bumper"),
                button_active(gamepad1.right_bumper, "driver right_bumper"),
                axis(gamepad1.right_stick_y, "driver right_stick_y"),
                axis(gamepad1.left_trigger, "driver left_trigger")
        );
    }

    public void drive(Gamepad gamepad) {

        speedFactor = 1 + 2 * (double) gamepad.right_trigger;

        double LX = gamepad.left_stick_x;
        double LY = -gamepad.left_stick_y;
        double RX = -gamepad.right_stick_x;

        if (usePID) {

            heading = robot.getAngle();

            if (LX != 0 || LY != 0 || RX != 0) desiredHeading = heading;
            //the desired heading remains constant if we aren't moving manually

            correction = getPIDSteer();
        }

        // Calculates the value to put each motor to; RF, RB, LB, LF
        double[] power = {(LY - LX - correction - RX), (LY + LX - correction - RX), (LY - LX + correction + RX), (LY + LX + correction + RX)};

        // Makes sure that the power values are not truncated
        double maximum = Math.max(1, Math.max(Math.max(Math.abs(power[0]), Math.abs(power[1])), Math.max(Math.abs(power[2]), Math.abs(power[3]))));

        for (int i = 0; i < power.length; i++) {
            power[i] /= maximum;
            power[i] /= speedFactor;
            robot.wheel_list[i].setPower(-power[i]);
        }
    }

    public double getError() {
        double diff = desiredHeading - heading;

        while (diff > 180)  diff -= 360;
        while (diff <= -180) diff += 360;

        return diff;
    }

    public double getPIDSteer() {

        current_time = System.currentTimeMillis();
        current_error = getError();

        double p = current_error;
        double d = (current_error - previous_error) / (current_time - previous_time);

        //basically... p is y value, d is slope
        //difference in one tick: (assume tick lengths are roughly the same): p + d
        // correct by p * p_weight + d * d_weight

        previous_error = current_error;
        previous_time = current_time;

        return p_weight * p + d_weight * d;
    }

}
