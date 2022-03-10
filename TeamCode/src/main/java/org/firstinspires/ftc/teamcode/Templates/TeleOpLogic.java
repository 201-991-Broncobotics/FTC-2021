package org.firstinspires.ftc.teamcode.Templates;

import org.firstinspires.ftc.teamcode.Templates.DoNotChange.Robot;

public class TeleOpLogic implements Values {

    double DuckWheelPower = 0.0;
    int DuckWheelDirection = 0;

    double armPower = 0.0;

    int servoPos = 0;
    int pastServoPos = 0;

    double IntakePower = 0.0;

    public void update_motors_operator(Robot r, boolean a, boolean b, boolean x, boolean y, boolean dpad_up,
                              boolean dpad_down, boolean dpad_left, boolean dpad_right,
                              boolean left_bumper, boolean right_bumper, double left_stick_x,
                              double left_stick_y, double right_stick_x, double right_stick_y,
                              double left_trigger_depth, double right_trigger_depth) {

        IntakePower = a ? 0.3 : y ? -0.3 : 0.0;

        DuckWheelDirection = b ? -1 : x ? 1 : 0;
        DuckWheelPower = (x || b) ? Math.min(DuckWheelPower + 0.01, 0.7) : 0.0;
        armPower = left_stick_y > 0.1 ? 0.5 : left_stick_y < -0.1 ? -0.13 : 0;

        pastServoPos = servoPos;
        servoPos += dpad_up ? 1 : dpad_down ? -1 : right_bumper ? 2 : left_bumper ? -2: 0;
        servoPos = Math.max(Math.min(servoPos, 3), 1);
        if (pastServoPos != servoPos) r.servo_list[servo_names.indexOf("BucketServo")].setPosition(servoPositions[servoPos]);

        r.dc_motor_list[dc_motor_names.indexOf("Intake")].setPower(IntakePower);
        r.dc_motor_list[dc_motor_names.indexOf("DuckWheel")].setPower(DuckWheelPower * DuckWheelDirection);
        r.dc_motor_list[dc_motor_names.indexOf("LinearSlide")].setPower(armPower);

    }

    public void update_motors_driver(Robot r, boolean a, boolean b, boolean x, boolean y, boolean dpad_up,
                                       boolean dpad_down, boolean dpad_left, boolean dpad_right,
                                       boolean left_bumper, boolean right_bumper, double right_stick_y,
                                       double left_trigger_depth) {
        //note there's no left_stick_x, left_stick_y, right_stick_x, or right_trigger
        //there is right_stick_y though
    }
}