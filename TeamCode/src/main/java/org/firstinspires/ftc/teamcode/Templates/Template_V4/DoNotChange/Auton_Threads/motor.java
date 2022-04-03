package org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Auton_Threads;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;

public class motor extends Thread {

    Robot robot;
    Logic logic;
    int motor_index;
    double power;
    int target_position;
    boolean control_power;

    public motor(Robot r, Logic l, String motor_name, String type) {
        robot = r;
        logic = l;
        motor_index = logic.dc_motor_names.indexOf(motor_name);
        control_power = type.equals("power");
    }

    public boolean should_be_running = true;

    public void set_power(double p) {
        power = Math.max(-1.0, Math.min(1.0, p));
    }
    public void set_position(int p) {
        target_position = p;
    }

    public void run() {
        while (should_be_running) {
            if (control_power) {
                robot.dc_motor_list[motor_index].setPower(power);
            } else {
                robot.dc_motor_list[motor_index].setPower(Math.max(-0.5, Math.min(0.5, 0.05 *
                        (target_position - robot.dc_motor_list[motor_index].getCurrentPosition())
                )));
            }
        }
    }

}