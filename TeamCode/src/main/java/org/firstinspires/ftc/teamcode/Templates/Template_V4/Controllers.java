package org.firstinspires.ftc.teamcode.Templates.Template_V4;

import java.util.ArrayList;
import java.util.Map;

public class Controllers extends Robot_Logic {

    Robot robot;

    public double[] times_started = new double[dc_motor_names.size() + servo_names.size()]; //in seconds
    public double[] target_positions = new double[dc_motor_names.size() + servo_names.size()];
    public int[] target_indices = new int[dc_motor_names.size() + servo_names.size()];
    public double[] starting_positions = new double[dc_motor_names.size() + servo_names.size()]; //never use for dc_motors

    ArrayList<Object> object_keys;
    int number_of_keys;
    boolean object_is_active;

    int temp_0;
    int temp_1;
    int temp_2;

    public Controllers(Robot r) {
        robot = r;
    }

    public void update_robot(boolean operator_a, boolean operator_b, boolean operator_x, boolean operator_y, boolean operator_dpad_up,
                             boolean operator_dpad_down, boolean operator_dpad_left, boolean operator_dpad_right, boolean operator_left_bumper,
                             boolean operator_right_bumper, double operator_left_stick_x, double operator_left_stick_y, double operator_right_stick_x,
                             double operator_right_stick_y, double operator_left_trigger_depth, double operator_right_trigger_depth,
                             boolean driver_a, boolean driver_b, boolean driver_x, boolean driver_y, boolean driver_dpad_up, boolean driver_dpad_down,
                             boolean driver_dpad_left, boolean driver_dpad_right, boolean driver_left_bumper, boolean driver_right_bumper,
                             double driver_right_stick_y, double driver_left_trigger_depth) {

        boolean[] buttons = {operator_a, operator_b, operator_x, operator_y, operator_dpad_up, operator_dpad_down, operator_dpad_left, operator_dpad_right, operator_left_bumper, operator_right_bumper, driver_a, driver_b, driver_x, driver_y, driver_dpad_up, driver_dpad_down, driver_dpad_left, driver_dpad_right, driver_left_bumper, driver_right_bumper};
        double[] axes = {operator_left_stick_x, operator_left_stick_y, operator_right_stick_x, operator_right_stick_y, operator_left_trigger_depth, operator_right_trigger_depth, driver_right_stick_y, driver_left_trigger_depth};

        for (Map.Entry<String, ArrayList<Object>> element : keybinds.entrySet()) { //for every element in keybinds

            object_keys = element.getValue();
            number_of_keys = object_keys.size() / 4;
            object_is_active = false;

            for (int i = 0; i < number_of_keys; i++) { //for every key that maps to the button
                temp_0 = keys.indexOf((String) object_keys.get(4 * i));
                if (temp_0 < 20) { //if its a button
                    object_is_active = (object_is_active || buttons[temp_0]);
                } else { //if its an axis
                    object_is_active = (object_is_active || Math.abs(axes[temp_0 - 20]) > 0.1);
                }
            }

            if (dc_motor_names.contains(element.getKey())) { //if it's a dc motor

                temp_1 = dc_motor_names.indexOf(element.getKey());

                if (!object_is_active) { //if we aren't pressing any relevant buttons
                    times_started[temp_1] = -10.0; //reset it and make sure its staying where we want it to
                    robot.dc_motor_list[temp_1].setPower(Math.max(-0.25, Math.min((target_positions[temp_1] - robot.dc_motor_list[temp_1].getCurrentPosition()) * 0.005, 0.25)));

                } else {
                    if (times_started[temp_1] < 0) //if we're on and it's reset, un-reset it
                        times_started[temp_1] = (double) System.nanoTime() / 1000000000.0;
                    target_positions[temp_1] = robot.dc_motor_list[temp_1].getCurrentPosition(); //only update target position if we're moving - don't update if we are not

                    for (int i = 0; i < number_of_keys; i++) { //4 * i: button name; +1: button/default/toggle;
                                    //+2: normal/gradient /  how much do we increase / power on way up
                                    //+3: maximum power / position list / power on way down

                        temp_0 = keys.indexOf((String) object_keys.get(4 * i));

                        if (temp_0 < 20) { //if its a button
                            if (buttons[temp_0]) {
                                if (((String) object_keys.get(4 * i + 1)).equals("button")) { //4 * i + 2: what we change by; 4 * i + 3: positions
                                    target_indices[temp_1] += (int) object_keys.get(4 * i + 2); //mlt for dc motor = index of the list we are at
                                    target_indices[temp_1] = Math.max(0, Math.min(target_indices[temp_1], ((int[]) object_keys.get(4 * i + 3)).length - 1)); //make sure it won't throw an error
                                    target_positions[temp_1] = ((int[]) object_keys.get(4 * i + 3))[target_indices[temp_1]]; //change the target position
                                } else { //toggle or default
                                    robot.dc_motor_list[temp_1].setPower(((double) object_keys.get(4 * i + 3)) * (
                                            ((String) object_keys.get(4 * i + 2)).equals("normal") ? 1 : Math.min(1, ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1]) / 0.75)
                                    ));
                                }
                            }
                        } else { //if its an axis
                            if (((String) object_keys.get(4 * i + 1)).equals("button")) {
                                if (axes[temp_0 - 20] == 1) {
                                    target_indices[temp_1] += (int) object_keys.get(4 * i + 2); //mlt for dc motor = index of the list we are at
                                    target_indices[temp_1] = Math.max(0, Math.min(target_indices[temp_1], ((int[]) object_keys.get(4 * i + 3)).length - 1));
                                    target_positions[temp_1] = ((int[]) object_keys.get(4 * i + 3))[target_indices[temp_1]]; //change the target position
                                }
                            } else if (((String) object_keys.get(4 * i + 1)).equals("toggle")) {
                                if (axes[temp_0 - 20] == 1) {
                                    robot.dc_motor_list[temp_1].setPower(((double) object_keys.get(4 * i + 3)) * (
                                            ((String) object_keys.get(4 * i + 2)).equals("normal") ? 1 : Math.min(1, ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1]) / 0.75)));
                                }
                            } else if (Math.abs(axes[temp_0 - 20]) > 0.1) {
                                robot.dc_motor_list[temp_1].setPower(axes[i] *
                                        (axes[i] > 0 ? (double) object_keys.get(4 * i + 2) : (double) object_keys.get(4 * i + 3)));
                            }
                        }
                    }
                }

            } else { //servo

                temp_1 = servo_names.indexOf(element.getKey()) + dc_motor_names.size();
                temp_2 = servo_names.indexOf(element.getKey());

                if (!object_is_active) {
                    times_started[temp_1] = -10.0;
                    robot.servo_list[temp_2].setPosition(target_positions[temp_1]);
                    //make sure its staying where we want it to, and update the starting position (don't update starting position if the servo should be moving, obviously)
                    starting_positions[temp_1] = target_positions[temp_1];
                } else {

                    if (times_started[temp_1] < 0) { //un-reset it
                        times_started[temp_1] = (double) System.nanoTime() / 1000000000.0;
                    }

                    for (int i = 0; i < number_of_keys; i++) {
                        temp_0 = keys.indexOf((String) object_keys.get(4 * i));

                        if (temp_0 < 20) { //if its a button
                            if (buttons[temp_0]) {
                                if (((String) object_keys.get(4 * i + 1)).equals("button")) {
                                    target_indices[temp_1] += (int) object_keys.get(4 * i + 2);
                                    target_indices[temp_1] = Math.max(0, Math.min(target_indices[temp_1], ((double[]) object_keys.get(4 * i + 3)).length - 1));
                                    robot.servo_list[temp_2].setPosition(((double[]) object_keys.get(4 * i + 3))[target_indices[temp_1]]); //change the target position
                                } else { //toggle or default
                                    robot.servo_list[temp_2].setPosition(Math.max(0, Math.min(1, //we don't have gradients because that's pointless to add; we have to assign position
                                            starting_positions[temp_1] + (double) object_keys.get(4 * i + 2) * ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1])
                                    )));                   //position: initial position (remember, it stopped updating when we started moving) + speed * time
                                }
                            }
                        } else { //if its an axis
                            if (((String) object_keys.get(4 * i + 1)).equals("button")) {
                                if (axes[temp_0 - 20] == 1) {
                                    target_indices[temp_1] += (int) object_keys.get(4 * i + 2);
                                    target_indices[temp_1] = Math.max(0, Math.min(target_indices[temp_1], ((double[]) object_keys.get(4 * i + 3)).length - 1));
                                    robot.servo_list[temp_2].setPosition(((double[]) object_keys.get(4 * i + 3))[target_indices[temp_1]]); //change the target position
                                }
                            } else if (((String) object_keys.get(4 * i + 1)).equals("toggle")) {
                                if (axes[temp_0 - 20] == 1) {
                                    robot.servo_list[temp_2].setPosition(Math.max(0, Math.min(1, //we don't have gradients because that's pointless to add; we have to assign position
                                            starting_positions[temp_1] + (double) object_keys.get(4 * i + 2) * ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1])
                                    )));
                                }
                            } else if (Math.abs(axes[temp_0 - 20]) > 0.1) {//if there's an active axis, set the power to what we want the depth to be if positive/negative * trigger depth
                                robot.servo_list[temp_2].setPosition(Math.max(0, Math.min(1,  //unfortunately, this isn't as perfect as I wanted, but it's the best I can do
                                        robot.servo_list[temp_2].getPosition() +
                                                ((double) System.nanoTime() / 1000000000.0 - times_started[temp_1]) * axes[i] * (axes[i] > 0 ? (double) object_keys.get(4 * i + 2) : (double) object_keys.get(4 * i + 3)) //how much we increase - might have to do something based on current time - previous time
                                )));
                                times_started[temp_1] = (double) System.nanoTime() / 1000000000.0;
                            }
                        }
                    }
                }
            }
        }
    }
}
