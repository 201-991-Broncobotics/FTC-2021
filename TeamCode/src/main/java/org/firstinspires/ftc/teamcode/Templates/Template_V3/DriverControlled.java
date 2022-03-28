package org.firstinspires.ftc.teamcode.Templates.Template_V3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="OP Tele", group="Iterative Opmode")
public class DriverControlled extends LinearOpMode implements Robot_Logic {

    Robot robot = new Robot();
    Logic logic = new Logic(robot);

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap, telemetry);
        logic.init();

        waitForStart();

        while(opModeIsActive()) {

            execute_driver(gamepad1);
            execute_operator(gamepad2);
            logic.execute_non_driver_controlled();

        }
    }

    public boolean driver_button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        if (driver_keys.get(driver_keys.indexOf(button_name)).equals("toggle")) {

            logic.driver_values[driver_keys.indexOf(button_name)] += (button_pressed == (logic.driver_values[driver_keys.indexOf(button_name)] % 2 == 0)) ? 1 : 0;
            button_active = (logic.driver_values[driver_keys.indexOf(button_name)] % 4 != 0);

        } else if (driver_keys.get(driver_keys.indexOf(button_name)).equals("button")) {

            button_active = (logic.driver_values[driver_keys.indexOf(button_name)] % 2 == 0) && (button_pressed);
            logic.driver_values[driver_keys.indexOf(button_name)] += (button_pressed == (logic.driver_values[driver_keys.indexOf(button_name)] % 2 == 0)) ? 1 : 0;

        } else button_active = button_pressed;

        return button_active;
    }

    public double driver_axis(double axis, String axis_name) {
        double axis_value;
        if (driver_keys.get(driver_keys.indexOf(axis_name)).equals("toggle")) {

            logic.driver_values[driver_keys.indexOf(axis_name)] += ((Math.abs(axis) > 0.1) == (logic.driver_values[driver_keys.indexOf(axis_name)] % 2 == 0)) ? 1 : 0;
            axis_value = logic.driver_values[driver_keys.indexOf(axis_name)] % 4 != 0 ? 1 : 0;

        } else if (driver_keys.get(driver_keys.indexOf(axis_name)).equals("button")) {

            axis_value = (logic.driver_values[driver_keys.indexOf(axis_name)] % 2 == 0) && (Math.abs(axis) > 0.1) ? 1 : 0;
            logic.driver_values[driver_keys.indexOf(axis_name)] += ((Math.abs(axis) > 0.1) == (logic.driver_values[driver_keys.indexOf(axis_name)] % 2 == 0)) ? 1 : 0;

        } else {
            axis_value = axis;
        }

        return axis_value;
    }

    public boolean operator_button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        if (operator_keys.get(operator_keys.indexOf(button_name)).equals("toggle")) {

            logic.operator_values[operator_keys.indexOf(button_name)] += (button_pressed == (logic.operator_values[operator_keys.indexOf(button_name)] % 2 == 0)) ? 1 : 0;
            button_active = (logic.operator_values[operator_keys.indexOf(button_name)] % 4 != 0);

        } else if (operator_keys.get(operator_keys.indexOf(button_name)).equals("button")) {

            button_active = (logic.operator_values[operator_keys.indexOf(button_name)] % 2 == 0) && (button_pressed);
            logic.operator_values[operator_keys.indexOf(button_name)] += (button_pressed == (logic.operator_values[operator_keys.indexOf(button_name)] % 2 == 0)) ? 1 : 0;

        } else button_active = button_pressed;

        return button_active;
    }

    public double operator_axis(double axis, String axis_name) {
        double axis_value;
        if (operator_keys.get(operator_keys.indexOf(axis_name)).equals("toggle")) {

            logic.operator_values[operator_keys.indexOf(axis_name)] += ((Math.abs(axis) > 0.1) == (logic.operator_values[operator_keys.indexOf(axis_name)] % 2 == 0)) ? 1 : 0;
            axis_value = logic.operator_values[operator_keys.indexOf(axis_name)] % 4 != 0 ? 1 : 0;

        } else if (operator_keys.get(operator_keys.indexOf(axis_name)).equals("button")) {

            axis_value = (logic.operator_values[operator_keys.indexOf(axis_name)] % 2 == 0) && (Math.abs(axis) > 0.1) ? 1 : 0;
            logic.operator_values[operator_keys.indexOf(axis_name)] += ((Math.abs(axis) > 0.1) == (logic.operator_values[operator_keys.indexOf(axis_name)] % 2 == 0)) ? 1 : 0;

        } else {
            axis_value = axis;
        }

        return axis_value;
    }

    public void execute_driver(Gamepad gamepad) {

        drive(gamepad);
        logic.update_motors_driver(driver_button_active(gamepad.a, "a"),
                driver_button_active(gamepad.b, "b"),
                driver_button_active(gamepad.x, "x"),
                driver_button_active(gamepad.y, "y"),
                driver_button_active(gamepad.dpad_up, "dpad_up"),
                driver_button_active(gamepad.dpad_down, "dpad_down"),
                driver_button_active(gamepad.dpad_left, "dpad_left"),
                driver_button_active(gamepad.dpad_right, "dpad_right"),
                driver_button_active(gamepad.left_bumper, "left_bumper"),
                driver_button_active(gamepad.right_bumper, "right_bumper"),
                driver_axis(gamepad.right_stick_y, "right_stick_y"),
                driver_axis(gamepad.left_trigger, "left_trigger"));

    }

    public void drive(Gamepad gamepad) {

        logic.speedFactor = (gamepad.right_trigger > 0.1) ? 2 : 1;

        double LX = gamepad.left_stick_x;
        double LY = -gamepad.left_stick_y;
        double RX = -gamepad.right_stick_x;

        if (logic.usePID) {

            logic.heading = robot.getAngle();

            if (LX != 0 || LY != 0 || RX != 0) logic.desiredHeading = logic.heading;
            //the desired heading remains constant if we aren't moving manually

            logic.correction = getPIDSteer();
        }

        // Calculates the value to put each motor to; RF, RB, LB, LF
        double[] power = {(LY - LX - logic.correction - RX), (LY + LX - logic.correction - RX), (LY - LX + logic.correction + RX), (LY + LX + logic.correction + RX)};

        // Makes sure that the power values are not truncated
        double maximum = Math.max(1, Math.max(Math.max(Math.abs(power[0]), Math.abs(power[1])), Math.max(Math.abs(power[2]), Math.abs(power[3]))));

        for (int i = 0; i < power.length; i++) power[i] /= maximum;

        // Set the power of the drive train
        for (int i = 0; i < power.length; i++) robot.wheel_list[i].setPower(-power[i]/logic.speedFactor);
    }

    public double getError() {
        double diff = logic.desiredHeading - logic.heading;

        while (diff > 180)  diff -= 360;
        while (diff <= -180) diff += 360;

        return diff;
    }

    public double getPIDSteer() {

        logic.current_time = System.currentTimeMillis();
        logic.current_error = getError();

        double p = logic.current_error;
        double d = (logic.current_error - logic.previous_error) / (logic.current_time - logic.previous_time);

        //basically... p is y value, d is slope
        //difference in one tick: (assume tick lengths are roughly the same): p + d
        // correct by p * p_weight + d * d_weight

        logic.previous_error = logic.current_error;
        logic.previous_time = logic.current_time;

        return logic.p_weight * p + logic.d_weight * d;
    }

    //Operator

    public void execute_operator(Gamepad gamepad) {

        logic.update_motors_operator(operator_button_active(gamepad.a, "a"),
                operator_button_active(gamepad.b, "b"),
                operator_button_active(gamepad.x, "x"),
                operator_button_active(gamepad.y, "y"),
                operator_button_active(gamepad.dpad_up, "dpad_up"),
                operator_button_active(gamepad.dpad_down, "dpad_down"),
                operator_button_active(gamepad.dpad_left, "dpad_left"),
                operator_button_active(gamepad.dpad_right, "dpad_right"),
                operator_button_active(gamepad.left_bumper, "left_bumper"),
                operator_button_active(gamepad.right_bumper, "right_bumper"),
                operator_axis(gamepad.left_stick_x, "left_stick_x"),
                operator_axis(gamepad.left_stick_y, "left_stick_y"),
                operator_axis(gamepad.right_stick_x, "right_stick_x"),
                operator_axis(gamepad.right_stick_y, "right_stick_y"),
                operator_axis(gamepad.left_trigger, "left_trigger"),
                operator_axis(gamepad.right_trigger, "right_trigger"));
    }

}
