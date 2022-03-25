package org.firstinspires.ftc.teamcode.Templates.Template_V2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="Skeolhgvkhgvkhgv", group="Iterative Opmode")
public class TeleOp_Temp extends LinearOpMode {

    Robot robot = new Robot();
    Logic logic = new Logic(robot);

    public int[] operator_toggle_values = new int[logic.operator_toggles.size()];
    public int[] operator_button_values = new int[logic.operator_buttons.size()];

    public int[] driver_toggle_values = new int[logic.driver_toggles.size()];
    public int[] driver_button_values = new int[logic.driver_buttons.size()];

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap, telemetry);

        waitForStart();

        while(opModeIsActive()) {

            execute_driver(gamepad1);
            execute_operator(gamepad2);
            logic.execute_non_driver_controlled();

        }
    }

    //Trigger/Button Logic

    public boolean driver_button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        if (logic.driver_toggles.contains(button_name)) {

            driver_toggle_values[logic.driver_toggles.indexOf(button_name)] += (button_pressed == (driver_toggle_values[logic.driver_toggles.indexOf(button_name)] % 2 == 0)) ? 1 : 0;
            button_active = (driver_toggle_values[logic.driver_toggles.indexOf(button_name)] % 4 != 0);

        } else if (logic.driver_buttons.contains(button_name)) {

            button_active = (driver_button_values[logic.driver_buttons.indexOf(button_name)] % 2 == 0) && (button_pressed);
            driver_button_values[logic.driver_buttons.indexOf(button_name)] += (button_pressed == (driver_button_values[logic.driver_buttons.indexOf(button_name)] % 2 == 0)) ? 1 : 0;

        } else button_active = button_pressed;

        return button_active;
    }

    public boolean operator_button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        if (logic.operator_toggles.contains(button_name)) {

            operator_toggle_values[logic.operator_toggles.indexOf(button_name)] += (button_pressed == (operator_toggle_values[logic.operator_toggles.indexOf(button_name)] % 2 == 0)) ? 1 : 0;
            button_active = (operator_toggle_values[logic.operator_toggles.indexOf(button_name)] % 4 != 0);

        } else if (logic.operator_buttons.contains(button_name)) {

            button_active = (operator_button_values[logic.operator_buttons.indexOf(button_name)] % 2 == 0) && (button_pressed);
            operator_button_values[logic.operator_buttons.indexOf(button_name)] += (button_pressed == (operator_button_values[logic.operator_buttons.indexOf(button_name)] % 2 == 0)) ? 1 : 0;

        } else button_active = button_pressed;

        return button_active;
    }

    //Driver

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
                gamepad.right_stick_y, gamepad.left_trigger);

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
                gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x,
                gamepad.right_stick_y, gamepad.left_trigger, gamepad.right_trigger);
    }

}
