package org.firstinspires.ftc.teamcode.Templates.Template_V4;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="TeleOpFinal", group="Iterative Opmode")
public class Tele_Op_Final extends LinearOpMode {

    Robot robot = new Robot();
    Logic logic = new Logic(robot);

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap, telemetry);
        logic.init();

        waitForStart();

        while(opModeIsActive()) {

            execute_controllers(gamepad1, gamepad2); //driver is gamepad1, operator is gamepad2

            logic.execute_non_driver_controlled();

        }
    }

    public boolean button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        int temp = logic.keys.indexOf(button_name);
        if (("toggle").equals(logic.button_types.get(button_name))) {

            logic.key_values[temp] += (button_pressed == (logic.key_values[temp] % 2 == 0)) ? 1 : 0;
            button_active = (logic.key_values[temp] % 4 != 0);

        } else if (("button").equals(logic.button_types.get(button_name))) {

            button_active = (logic.key_values[temp] % 2 == 0) && (button_pressed);
            logic.key_values[temp] += (button_pressed == (logic.key_values[temp] % 2 == 0)) ? 1 : 0;

        } else button_active = button_pressed;

        return button_active;
    }

    public double axis(double axis, String axis_name) {
        double axis_value;
        int temp = logic.keys.indexOf(axis_name);
        if (("toggle").equals(logic.button_types.get(axis_name))) {

            logic.key_values[temp] += ((Math.abs(axis) > 0.1) == (logic.key_values[temp] % 2 == 0)) ? 1 : 0;
            axis_value = logic.key_values[temp] % 4 != 0 ? 1 : 0;

        } else if (("button").equals(logic.button_types.get(axis_name))) {

            axis_value = (logic.key_values[temp] % 2 == 0) && (Math.abs(axis) > 0.1) ? 1 : 0;
            logic.key_values[temp] += ((Math.abs(axis) > 0.1) == (logic.key_values[temp] % 2 == 0)) ? 1 : 0;

        } else {
            axis_value = axis;
        }

        return axis_value;
    }

    public void execute_controllers(Gamepad gamepad1, Gamepad gamepad2) {

        drive(gamepad1);
        logic.update_robot(
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

}
