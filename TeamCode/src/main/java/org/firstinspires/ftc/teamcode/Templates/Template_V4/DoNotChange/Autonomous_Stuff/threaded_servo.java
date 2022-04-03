package org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Autonomous_Stuff;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;

public class threaded_servo extends Thread {

    Robot robot;
    int servo_index;
    Logic logic;
    double target_position;

    public threaded_servo(Robot r, Logic l, String servo_name) {
        robot = r;
        logic = l;
        servo_index = logic.servo_names.indexOf(servo_name);
    }

    public void set_position(double p) {
        target_position = p;
    }

    public boolean should_be_running = true;

    public void run() {
        while (should_be_running) {
            robot.servo_list[servo_index].setPosition(target_position);
        }
    }

}
