package org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Autonomous_Stuff;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;

public class threaded_motor extends Thread {

    Robot robot;
    Logic logic;
    int motor_index;
    int target_position;
    public boolean isBusy = false;

    public threaded_motor(Robot r, Logic l, String motor_name) {
        robot = r;
        logic = l;
        motor_index = logic.dc_motor_names.indexOf(motor_name);
    }

    public boolean should_be_running = true;

    public void set_position(int p) {
        target_position = p;
    }

    public void reset() {
        robot.dc_motor_list[motor_index].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void run() {
        reset();
        while (should_be_running) {
            isBusy = (Math.abs(target_position - robot.dc_motor_list[motor_index].getCurrentPosition()) < 5);
            robot.dc_motor_list[motor_index].setPower(Math.max(-0.5, Math.min(0.5, 0.05 *
                    (target_position - robot.dc_motor_list[motor_index].getCurrentPosition())
            )));
        }
    }

}