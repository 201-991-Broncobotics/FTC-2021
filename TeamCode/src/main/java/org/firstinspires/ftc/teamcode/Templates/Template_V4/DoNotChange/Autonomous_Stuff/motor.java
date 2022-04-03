package org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Autonomous_Stuff;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;

public class motor {

    Robot robot;
    Logic logic;
    int motor_index;

    public motor(Robot r, Logic l, String motor_name) {
        robot = r;
        logic = l;
        motor_index = logic.dc_motor_names.indexOf(motor_name);
    }

    public void set_power(double p) {
        robot.dc_motor_list[motor_index].setPower(Math.max(-1.0, Math.min(1.0, p)));
    }

}