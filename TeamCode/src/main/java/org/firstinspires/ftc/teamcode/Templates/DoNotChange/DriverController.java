package org.firstinspires.ftc.teamcode.Templates.DoNotChange;

import com.qualcomm.robotcore.hardware.Gamepad;

public class DriverController extends PID_Variables {
    Robot robot;

    int[] toggle_values = new int[driver_toggles.size()];
    int[] button_values = new int[driver_buttons.size()];

    public DriverController(Robot r) {
        robot = r;
    }

    public void execute(Gamepad gamepad) {
        drive(gamepad);
        update_motors_driver(robot, button_active(gamepad.a, "a"), button_active(gamepad.b, "b"),
                button_active(gamepad.x, "x"), button_active(gamepad.y, "y"),
                button_active(gamepad.dpad_up, "dpad_up"),
                button_active(gamepad.dpad_down, "dpad_down"),
                button_active(gamepad.dpad_left, "dpad_left"),
                button_active(gamepad.dpad_right, "dpad_right"),
                button_active(gamepad.left_bumper, "left_bumper"),
                button_active(gamepad.right_bumper, "right_bumper"),
                gamepad.right_stick_y, gamepad.left_trigger);
    }

    public boolean button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        if (driver_toggles.contains(button_name)) {

            toggle_values[driver_toggles.indexOf(button_name)] += (button_pressed == (toggle_values[driver_toggles.indexOf(button_name)] % 2 == 0)) ? 1 : 0;
            button_active = (toggle_values[driver_toggles.indexOf(button_name)] % 4 != 0);

        } else if (driver_buttons.contains(button_name)) {

            button_active = (button_values[driver_buttons.indexOf(button_name)] % 2 == 0) && (button_pressed);
            button_values[driver_buttons.indexOf(button_name)] += (button_pressed == (button_values[driver_buttons.indexOf(button_name)] % 2 == 0)) ? 1 : 0;

        } else button_active = button_pressed;

        return button_active;
    }

    public void drive(Gamepad gamepad) {

        speedFactor = (gamepad.right_trigger > 0.1) ? 2 : 1;

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

        for (int i = 0; i < power.length; i++) power[i] /= maximum;

        // Set the power of the drive train
        for (int i = 0; i < power.length; i++) robot.wheel_list[i].setPower(-power[i]/speedFactor);
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