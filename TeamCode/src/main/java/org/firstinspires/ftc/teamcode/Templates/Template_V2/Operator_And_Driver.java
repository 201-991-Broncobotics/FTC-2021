package org.firstinspires.ftc.teamcode.Templates.Template_V2;

import java.util.ArrayList;

public class Operator_And_Driver implements Robot_Logic {

    Robot robot;
    public double[] timesStarted = new double[dc_motor_names.size() + servo_names.size()]; //in seconds
    public int[] targetPositions = new int[dc_motor_names.size()];
    public int[] motor_list_targets = new int[dc_motor_names.size()];
    public double[] servo_targets = new double[servo_names.size()];
    public int[] servo_list_targets = new int[servo_names.size()];
    public double[] starting_positions = new double[servo_names.size()];

    public Operator_And_Driver(Robot r) {
        robot = r;
        for (int i = 0; i < timesStarted.length; i++) timesStarted[i] = -10.0;
    }

    ArrayList<String> temp = new ArrayList<>();
    boolean temp1;
    public void update_motors_operator(boolean a, boolean b, boolean x, boolean y, boolean dpad_up,
                                       boolean dpad_down, boolean dpad_left, boolean dpad_right,
                                       boolean left_bumper, boolean right_bumper, double left_stick_x,
                                       double left_stick_y, double right_stick_x, double right_stick_y,
                                       double left_trigger_depth, double right_trigger_depth) {

        boolean[] buttons = {a, b, x, y, dpad_up, dpad_down, dpad_left, dpad_right, left_bumper, right_bumper};
        double[] axes = {left_stick_x, left_stick_y, right_stick_x, right_stick_y, left_trigger_depth, right_trigger_depth};
        //dc motors
        //operator_keys: names
        //operator_key_binds: what motor it goes to
        //operator_key_binds 1 and 2: params
        //default: mode, maximum value
        //toggle: mode, maximum value
        //button: how much do we increase/decrease, list with index

        //servos
        //operator_keys: names
        //operator_key_binds: what servo it goes to
        //operator_key_binds 1 and 2: params
        //default: speed - no 2nd param
        //toggle: speed - no 2nd param
        //button: how much do we increase/decrease, list with index

        //dc motors

        for (String name : dc_motor_names) { //for name in dc_motor_names

            temp.clear(); //all the buttons that correspond to whatever motor
            for (int i = 0; i < operator_keys.size(); i++) {
                if (operator_key_binds[i].equals(name)) {
                    temp.add(operator_keys.get(i));
                }
            }

            temp1 = false; //should this motor be moving or no
            for (int i = 0; i < 10; i++) { //buttons on
                if (temp.contains(operator_keys.get(i)))
                    temp1 = temp1 || buttons[i];
            }
            for (int i = 0; i < 6; i++) {
                if (temp.contains(operator_keys.get(i + 10)))
                    temp1 = temp1 || Math.abs(axes[i]) > 0.1; //only register as active if its pressed at least 0.1 of the way
            } //STILL HAVE TO ADD THIS TO SERVO PART

            if (!temp1) {
                timesStarted[dc_motor_names.indexOf(name)] = -10.0; //reset it and make sure its staying where we want it to
                robot.dc_motor_list[dc_motor_names.indexOf(name)].setPower(Math.max((targetPositions[dc_motor_names.indexOf(name)] - robot.dc_motor_list[dc_motor_names.indexOf(name)].getCurrentPosition()) * 0.005, 0.25));
            } else {

                if (timesStarted[dc_motor_names.indexOf(name)] < 0) //if we're on and it's reset, un-reset it
                    timesStarted[dc_motor_names.indexOf(name)] = (double) System.nanoTime() / 1000000000.0;

                targetPositions[dc_motor_names.indexOf(name)] = robot.dc_motor_list[dc_motor_names.indexOf(name)].getCurrentPosition(); //only update target position if we're moving - don't update if we are not

                for (int i = 0; i < 10; i++) {
                    if (temp.contains(operator_keys.get(i)) && buttons[i]) { //if it's one of the acceptable buttons and its on
                        if (operator_key_types[operator_keys.indexOf(operator_keys.get(i))].equals("toggle") || operator_key_types[operator_keys.indexOf(operator_keys.get(i))].equals("default")) { //if toggle or default - both have the same exact instructions except for how they're activated
                            if ((int) operator_key_binds1[operator_keys.indexOf(operator_keys.get(i))] == 1) { //if not a gradient, set it to the max power right away
                                robot.dc_motor_list[dc_motor_names.indexOf(name)].setPower((double) operator_key_binds2[operator_keys.indexOf(operator_keys.get(i))]);
                            } else { //otherwise, make it ramp up to it in 0.75 seconds
                                robot.dc_motor_list[dc_motor_names.indexOf(name)].setPower((double) operator_key_binds2[operator_keys.indexOf(operator_keys.get(i))] * Math.min(1, ((double) System.nanoTime() / 1000000000.0 - timesStarted[dc_motor_names.indexOf(name)]) / 0.75));
                            }
                            //temp.clear(); //? - maybe
                        } else if (operator_key_types[operator_keys.indexOf(operator_keys.get(i))].equals("button")) { //activated only once: this means encoders are used: we have a list of targets
                            motor_list_targets[dc_motor_names.indexOf(name)] += (int) operator_key_binds1[i]; //mlt for dc motor = index of the list we are at
                            motor_list_targets[dc_motor_names.indexOf(name)] = Math.max(0, Math.min(motor_list_targets[servo_names.indexOf(name)], ((double [])operator_key_binds2[i]).length - 1)); //make sure it won't throw an error
                            targetPositions[dc_motor_names.indexOf(name)] = ((int [])operator_key_binds2[i])[motor_list_targets[dc_motor_names.indexOf(name)]]; //change the target position
                        }
                    }
                }
                for (int i = 0; i < 6; i++) {
                    if (temp.contains(operator_keys.get(i + 10)) && (Math.abs(axes[i]) > 0.1)) { //if there's an active axis, set the power to what we want the depth to be if positive/negative * trigger depth
                        robot.dc_motor_list[dc_motor_names.indexOf(name)].setPower(Math.abs(axes[i]) * (Math.abs(axes[i]) > 0 ? (double) operator_key_binds1[i + 10] : (double) operator_key_binds2[i + 10]));
                    }
                }
            }
        }

        //servos

        for (String name : servo_names) { //for name in dc_motor_names

            temp.clear();
            for (int i = 0; i < operator_keys.size(); i++) {
                if (operator_key_binds[i].equals(name)) {
                    temp.add(operator_keys.get(i));
                }
            }

            temp1 = false;
            for (int i = 0; i < 10; i++) {
                if (temp.contains(operator_keys.get(i)))
                    temp1 = temp1 || buttons[i];
            }
            for (int i = 0; i < 6; i++) {
                if (temp.contains(operator_keys.get(i + 10)))
                    temp1 = temp1 || Math.abs(axes[i]) > 0.1;
            } //so far, same exact code as dc motors

            if (!temp1) { //reset
                timesStarted[servo_names.indexOf(name) + dc_motor_names.size()] = -10.0;
            } else if (timesStarted[servo_names.indexOf(name) + dc_motor_names.size()] < 0) { //if we're on and it's reset
                timesStarted[servo_names.indexOf(name) + dc_motor_names.size()] = (double) System.nanoTime() / 1000000000.0;
            } //so far, same logic as dc motors

            if (!temp1) {
                robot.servo_list[servo_names.indexOf(name)].setPosition(servo_targets[servo_names.indexOf(name)]);
                //make sure its staying where we want it to, and update the starting position (don't update starting position if the servo should be moving, obviously)
                starting_positions[servo_names.indexOf(name)] = servo_targets[servo_names.indexOf(name)];
            } else {

                for (int i = 0; i < 10; i++) { //for all the buttons
                    if (temp.contains(operator_keys.get(i)) && buttons[i]) { //if it's one of the acceptable buttons and its on
                        if (operator_key_types[operator_keys.indexOf(operator_keys.get(i))].equals("toggle") || operator_key_types[operator_keys.indexOf(operator_keys.get(i))].equals("default")) { //again, toggles/buttons = same logic
                            robot.servo_list[servo_names.indexOf(name)].setPosition(Math.max(-1, Math.min(1, //we don't have gradients because that's pointless to add; we have to assign position
                                    starting_positions[servo_names.indexOf(name)] + (double) operator_key_binds1[i] * ((double) System.nanoTime() / 1000000000.0 - timesStarted[dc_motor_names.size() + servo_names.indexOf(name)])
                            )));                   //position: initial position (remember, it stopped updating when we started moving) + speed * time
                            //temp.clear(); //? - maybe
                        } else if (operator_key_types[operator_keys.indexOf(operator_keys.get(i))].equals("button")) { //again, we have to increase/decrease based on a list of values; same logic
                            servo_list_targets[servo_names.indexOf(name)] += (int) operator_key_binds1[i];
                            servo_list_targets[servo_names.indexOf(name)] = Math.max(0, Math.min(servo_list_targets[servo_names.indexOf(name)], ((double [])operator_key_binds2[i]).length - 1));
                            robot.servo_list[servo_names.indexOf(name)].setPosition(((double [])operator_key_binds2[i])[servo_list_targets[servo_names.indexOf(name)]]); //change the target position
                        }
                    }
                }
                for (int i = 0; i < 6; i++) { //now for the triggers
                    if (temp.contains(operator_keys.get(i + 10)) && (Math.abs(axes[i]) > 0.1)) { //if there's an active axis, set the power to what we want the depth to be if positive/negative * trigger depth
                        robot.servo_list[servo_names.indexOf(name)].setPosition(Math.max(-1, Math.min(1,  //unfortunately, this isn't as perfect as I wanted, but it's the best I can do
                                robot.servo_list[servo_names.indexOf(name)].getPosition() +
                                Math.abs(axes[i]) * (Math.abs(axes[i]) > 0 ? (double) operator_key_binds1[i + 10] : (double) operator_key_binds2[i + 10])
                        )));
                    }
                }
                servo_targets[servo_names.indexOf(name)] = robot.servo_list[servo_names.indexOf(name)].getPosition();
            }
        }
    }
}
