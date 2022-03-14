package org.firstinspires.ftc.teamcode.Templates.Template_V2;

import com.qualcomm.robotcore.hardware.Gamepad;

public class TeleOp extends Logic {

    Robot robot = new Robot();

    public int[] operator_toggle_values = new int[operator_toggles.size()];
    public int[] operator_button_values = new int[operator_buttons.size()];

    public int[] driver_toggle_values = new int[driver_toggles.size()];
    public int[] driver_button_values = new int[driver_buttons.size()];

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap, telemetry);

        waitForStart();

        while(opModeIsActive()){

            execute_driver(robot, gamepad1);
            execute_operator(robot, gamepad2);
            execute_non_driver_controlled(robot);

        }
    }

    //Trigger/Button Logic

    public boolean driver_button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        if (driver_toggles.contains(button_name)) {

            driver_toggle_values[driver_toggles.indexOf(button_name)] += (button_pressed == (driver_toggle_values[driver_toggles.indexOf(button_name)] % 2 == 0)) ? 1 : 0;
            button_active = (driver_toggle_values[driver_toggles.indexOf(button_name)] % 4 != 0);

        } else if (driver_buttons.contains(button_name)) {

            button_active = (driver_button_values[driver_buttons.indexOf(button_name)] % 2 == 0) && (button_pressed);
            driver_button_values[driver_buttons.indexOf(button_name)] += (button_pressed == (driver_button_values[driver_buttons.indexOf(button_name)] % 2 == 0)) ? 1 : 0;

        } else button_active = button_pressed;

        return button_active;
    }

    public boolean operator_button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        if (operator_toggles.contains(button_name)) {

            operator_toggle_values[operator_toggles.indexOf(button_name)] += (button_pressed == (operator_toggle_values[operator_toggles.indexOf(button_name)] % 2 == 0)) ? 1 : 0;
            button_active = (operator_toggle_values[operator_toggles.indexOf(button_name)] % 4 != 0);

        } else if (operator_buttons.contains(button_name)) {

            button_active = (operator_button_values[operator_buttons.indexOf(button_name)] % 2 == 0) && (button_pressed);
            operator_button_values[operator_buttons.indexOf(button_name)] += (button_pressed == (operator_button_values[operator_buttons.indexOf(button_name)] % 2 == 0)) ? 1 : 0;

        } else button_active = button_pressed;

        return button_active;
    }

    //Driver

    public void execute_driver(Robot r, Gamepad gamepad) {

        drive(r, gamepad);
        update_motors_driver(r, driver_button_active(gamepad.a, "a"),
                driver_button_active(gamepad.b, "b"),
                driver_button_active(gamepad.x, "x"),
                driver_button_active(gamepad.y, "y"),
                driver_button_active(gamepad.dpad_up, "dpad_up"),
                driver_button_active(gamepad.dpad_down, "dpad_down"),
                driver_button_active(gamepad.dpad_left, "dpad_left"),
                driver_button_active(gamepad.dpad_right, "dpad_right"),
                driver_button_active(gamepad.left_bumper, "left_bumper"),
                driver_button_active(gamepad.right_bumper, "right_bumper"),
                gamepad.right_stick_y, gamepad.left_trigger);

    }

    public void drive(Robot r, Gamepad gamepad) {

        speedFactor = (gamepad.right_trigger > 0.1) ? 2 : 1;

        double LX = gamepad.left_stick_x;
        double LY = -gamepad.left_stick_y;
        double RX = -gamepad.right_stick_x;

        if (usePID) {

            heading = r.getAngle();

            if (LX != 0 || LY != 0 || RX != 0) desiredHeading = heading;
            //the desired heading remains constant if we aren't moving manually

            correction = getPIDSteer();
        }

        // Calculates the value to put each motor to; RF, RB, LB, LF
        double[] power = {(LY - LX - correction - RX), (LY + LX - correction - RX), (LY - LX + correction + RX), (LY + LX + correction + RX)};

        // Makes sure that the power values are not truncated
        double maximum = Math.max(1, Math.max(Math.max(Math.abs(power[0]), Math.abs(power[1])), Math.max(Math.abs(power[2]), Math.abs(power[3]))));

        for (int i = 0; i < power.length; i++) power[i] /= maximum;

        // Set the power of the drive train
        for (int i = 0; i < power.length; i++) r.wheel_list[i].setPower(-power[i]/speedFactor);
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

    //Operator

    public void execute_operator(Robot r, Gamepad gamepad) {

        update_motors_operator(r, operator_button_active(gamepad.a, "a"),
                operator_button_active(gamepad.b, "b"),
                operator_button_active(gamepad.x, "x"),
                operator_button_active(gamepad.y, "y"),
                operator_button_active(gamepad.dpad_up, "dpad_up"),
                operator_button_active(gamepad.dpad_down, "dpad_down"),
                operator_button_active(gamepad.dpad_left, "dpad_left"),
                operator_button_active(gamepad.dpad_right, "dpad_right"),
                operator_button_active(gamepad.left_bumper, "left_bumper"),
                operator_button_active(gamepad.right_bumper, "right_bumper"),
                gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x,
                gamepad.right_stick_y, gamepad.left_trigger, gamepad.right_trigger);
    }

}
