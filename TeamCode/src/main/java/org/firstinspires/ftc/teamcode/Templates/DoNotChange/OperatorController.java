package org.firstinspires.ftc.teamcode.Templates.DoNotChange;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Templates.TeleOpLogic;
import org.firstinspires.ftc.teamcode.Templates.Variables;

public class OperatorController extends TeleOpLogic implements Variables {
    Robot robot;

    public OperatorController(Robot r) {
        robot = r;
    }

    int[] toggle_values = new int[toggles.size()];
    int[] button_values = new int[buttons.size()];

    boolean a_on = false;
    boolean b_on = false;
    boolean x_on = false;
    boolean y_on = false;
    boolean dpad_up_on = false;
    boolean dpad_down_on = false;
    boolean dpad_left_on = false;
    boolean dpad_right_on = false;
    boolean left_bumper_on = false;
    boolean right_bumper_on = false;

    public void a(boolean a_pressed) {
        if (toggles.contains("a")) {
            toggle_values[toggles.indexOf("a")] += (a_pressed == (toggle_values[toggles.indexOf("a")] % 2 == 0)) ? 1 : 0;
            a_on = toggle_values[toggles.indexOf("a")] % 4 != 0;
        } else if (buttons.contains("a")) {
            a_on = a_pressed && button_values[buttons.indexOf("a")] % 2 == 0;
            button_values[buttons.indexOf("a")] += (a_pressed == (button_values[buttons.indexOf("a")] % 2 == 0)) ? 1 : 0;
        } else {
            a_on = a_pressed;
        }
    }
    public void b(boolean b_pressed) {
        if (toggles.contains("b")) {
            toggle_values[toggles.indexOf("b")] += (b_pressed == (toggle_values[toggles.indexOf("b")] % 2 == 0)) ? 1 : 0;
            b_on = toggle_values[toggles.indexOf("b")] % 4 != 0;
        } else if (buttons.contains("b")) {
            b_on = b_pressed && button_values[buttons.indexOf("b")] % 2 == 0;
            button_values[buttons.indexOf("b")] += (b_pressed == (button_values[buttons.indexOf("b")] % 2 == 0)) ? 1 : 0;
        } else {
            b_on = b_pressed;
        }
    }
    public void x(boolean x_pressed) {
        if (toggles.contains("x")) {
            toggle_values[toggles.indexOf("x")] += (x_pressed == (toggle_values[toggles.indexOf("x")] % 2 == 0)) ? 1 : 0;
            x_on = toggle_values[toggles.indexOf("x")] % 4 != 0;
        } else if (buttons.contains("x")) {
            x_on = x_pressed && button_values[buttons.indexOf("x")] % 2 == 0;
            button_values[buttons.indexOf("x")] += (x_pressed == (button_values[buttons.indexOf("x")] % 2 == 0)) ? 1 : 0;
        } else {
            x_on = x_pressed;
        }
    }
    public void y(boolean y_pressed) {
        if (toggles.contains("y")) {
            toggle_values[toggles.indexOf("y")] += (y_pressed == (toggle_values[toggles.indexOf("y")] % 2 == 0)) ? 1 : 0;
            y_on = toggle_values[toggles.indexOf("y")] % 4 != 0;
        } else if (buttons.contains("y")) {
            y_on = y_pressed && button_values[buttons.indexOf("y")] % 2 == 0;
            button_values[buttons.indexOf("y")] += (y_pressed == (button_values[buttons.indexOf("y")] % 2 == 0)) ? 1 : 0;
        } else {
            y_on = y_pressed;
        }
    }

    public void dpad_up(boolean dpad_up_pressed) {
        if (toggles.contains("dpad_up")) {
            toggle_values[toggles.indexOf("dpad_up")] += (dpad_up_pressed == (toggle_values[toggles.indexOf("dpad_up")] % 2 == 0)) ? 1 : 0;
            dpad_up_on = toggle_values[toggles.indexOf("dpad_up")] % 4 != 0;
        } else if (buttons.contains("dpad_up")) {
            dpad_up_on = dpad_up_pressed && button_values[buttons.indexOf("dpad_up")] % 2 == 0;
            button_values[buttons.indexOf("dpad_up")] += (dpad_up_pressed == (button_values[buttons.indexOf("dpad_up")] % 2 == 0)) ? 1 : 0;
        } else {
            dpad_up_on = dpad_up_pressed;
        }
    }
    public void dpad_down(boolean dpad_down_pressed) {
        if (toggles.contains("dpad_down")) {
            toggle_values[toggles.indexOf("dpad_down")] += (dpad_down_pressed == (toggle_values[toggles.indexOf("dpad_down")] % 2 == 0)) ? 1 : 0;
            dpad_down_on = toggle_values[toggles.indexOf("dpad_down")] % 4 != 0;
        } else if (buttons.contains("dpad_down")) {
            dpad_down_on = dpad_down_pressed && button_values[buttons.indexOf("dpad_down")] % 2 == 0;
            button_values[buttons.indexOf("dpad_down")] += (dpad_down_pressed == (button_values[buttons.indexOf("dpad_down")] % 2 == 0)) ? 1 : 0;
        } else {
            dpad_down_on = dpad_down_pressed;
        }
    }
    public void dpad_left(boolean dpad_left_pressed) {
        if (toggles.contains("dpad_left")) {
            toggle_values[toggles.indexOf("dpad_left")] += (dpad_left_pressed == (toggle_values[toggles.indexOf("dpad_left")] % 2 == 0)) ? 1 : 0;
            dpad_left_on = toggle_values[toggles.indexOf("dpad_left")] % 4 != 0;
        } else if (buttons.contains("dpad_left")) {
            dpad_left_on = dpad_left_pressed && button_values[buttons.indexOf("dpad_left")] % 2 == 0;
            button_values[buttons.indexOf("dpad_left")] += (dpad_left_pressed == (button_values[buttons.indexOf("dpad_left")] % 2 == 0)) ? 1 : 0;
        } else {
            dpad_left_on = dpad_left_pressed;
        }
    }
    public void dpad_right(boolean dpad_right_pressed) {
        if (toggles.contains("dpad_right")) {
            toggle_values[toggles.indexOf("dpad_right")] += (dpad_right_pressed == (toggle_values[toggles.indexOf("dpad_right")] % 2 == 0)) ? 1 : 0;
            dpad_right_on = toggle_values[toggles.indexOf("dpad_right")] % 4 != 0;
        } else if (buttons.contains("dpad_right")) {
            dpad_right_on = dpad_right_pressed && button_values[buttons.indexOf("dpad_right")] % 2 == 0;
            button_values[buttons.indexOf("dpad_right")] += (dpad_right_pressed == (button_values[buttons.indexOf("dpad_right")] % 2 == 0)) ? 1 : 0;
        } else {
            dpad_right_on = dpad_right_pressed;
        }
    }

    public void left_bumper(boolean left_bumper_pressed) {
        if (toggles.contains("left_bumper")) {
            toggle_values[toggles.indexOf("left_bumper")] += (left_bumper_pressed == (toggle_values[toggles.indexOf("left_bumper")] % 2 == 0)) ? 1 : 0;
            left_bumper_on = toggle_values[toggles.indexOf("left_bumper")] % 4 != 0;
        } else if (buttons.contains("left_bumper")) {
            left_bumper_on = left_bumper_pressed && button_values[buttons.indexOf("left_bumper")] % 2 == 0;
            button_values[buttons.indexOf("left_bumper")] += (left_bumper_pressed == (button_values[buttons.indexOf("left_bumper")] % 2 == 0)) ? 1 : 0;
        } else {
            left_bumper_on = left_bumper_pressed;
        }
    }
    public void right_bumper(boolean right_bumper_pressed) {
        if (toggles.contains("right_bumper")) {
            toggle_values[toggles.indexOf("right_bumper")] += (right_bumper_pressed == (toggle_values[toggles.indexOf("right_bumper")] % 2 == 0)) ? 1 : 0;
            right_bumper_on = toggle_values[toggles.indexOf("right_bumper")] % 4 != 0;
        } else if (buttons.contains("right_bumper")) {
            right_bumper_on = right_bumper_pressed && button_values[buttons.indexOf("right_bumper")] % 2 == 0;
            button_values[buttons.indexOf("right_bumper")] += (right_bumper_pressed == (button_values[buttons.indexOf("right_bumper")] % 2 == 0)) ? 1 : 0;
        } else {
            right_bumper_on = right_bumper_pressed;
        }
    }

    public void execute(Gamepad gamepad) {

        a(gamepad.a);
        b(gamepad.b);
        x(gamepad.x);
        y(gamepad.y);

        dpad_up(gamepad.dpad_up);
        dpad_down(gamepad.dpad_down);
        dpad_left(gamepad.dpad_left);
        dpad_right(gamepad.dpad_right);

        left_bumper(gamepad.left_bumper);
        right_bumper(gamepad.right_bumper);

        update_motors(robot, a_on, b_on, x_on, y_on, dpad_up_on, dpad_down_on, dpad_left_on,
                dpad_right_on, left_bumper_on, right_bumper_on, gamepad.left_stick_x,
                gamepad.left_stick_y, gamepad.right_stick_x, gamepad.right_stick_y,
                gamepad.left_trigger, gamepad.right_trigger);
    }

}
