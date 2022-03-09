package org.firstinspires.ftc.teamcode.Templates.DoNotChange;

import com.qualcomm.robotcore.hardware.Gamepad;

public class DriverController extends PID_Variables {
    Robot robot;

    public DriverController(Robot r) {
        robot = r;
    }

    public void execute(Gamepad gamepad) {

        speedFactor = (gamepad.right_trigger > 0.1) ? 2 : 1;

        double LX = gamepad.left_stick_x;
        double LY = -gamepad.left_stick_y;
        double RX = -gamepad.right_stick_x;

        desiredHeading = heading; //(aka previous heading)
        heading = robot.getAngle();
        if (LX != 0 || LY != 0 || RX != 0) desiredHeading = heading;

        correction = getPIDSteer();

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

        double p = k_p * current_error;
        //ex. 2°
        double d = k_d * (current_error - previous_error) / (current_time - previous_time);
        //d = how fast is the error changing - ex. 0.3°/tick

        previous_error = current_error;
        previous_time = current_time;

        if (Math.abs(current_error) <= tThreshold) return d;
        else return p+d;
    }
}