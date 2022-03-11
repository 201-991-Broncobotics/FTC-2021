package org.firstinspires.ftc.teamcode.Templates.DoNotChange;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Templates.DoChange.TeleOpLogic;

public class OperatorController extends TeleOpLogic {
    Robot robot;

    public OperatorController(Robot r) { robot = r; }

    public int[] toggle_values = new int[operator_toggles.size()];
    public int[] button_values = new int[operator_buttons.size()];

    public boolean button_active(boolean button_pressed, String button_name) {
        boolean button_active;
        if (operator_toggles.contains(button_name)) {

            toggle_values[operator_toggles.indexOf(button_name)] += (button_pressed == (toggle_values[operator_toggles.indexOf(button_name)] % 2 == 0)) ? 1 : 0;
            button_active = (toggle_values[operator_toggles.indexOf(button_name)] % 4 != 0);

        } else if (operator_buttons.contains(button_name)) {

            button_active = (button_values[operator_buttons.indexOf(button_name)] % 2 == 0) && (button_pressed);
            button_values[operator_buttons.indexOf(button_name)] += (button_pressed == (button_values[operator_buttons.indexOf(button_name)] % 2 == 0)) ? 1 : 0;

        } else button_active = button_pressed;

        return button_active;
    }

    public void execute(Gamepad gamepad) {

        update_motors_operator(robot, button_active(gamepad.a, "a"), button_active(gamepad.b, "b"),
                button_active(gamepad.x, "x"), button_active(gamepad.y, "y"),
                button_active(gamepad.dpad_up, "dpad_up"),
                button_active(gamepad.dpad_down, "dpad_down"),
                button_active(gamepad.dpad_left, "dpad_left"),
                button_active(gamepad.dpad_right, "dpad_right"),
                button_active(gamepad.left_bumper, "left_bumper"),
                button_active(gamepad.right_bumper, "right_bumper"),
                gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x,
                gamepad.right_stick_y, gamepad.left_trigger, gamepad.right_trigger);

    }

}
