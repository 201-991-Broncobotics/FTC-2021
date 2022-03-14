package org.firstinspires.ftc.teamcode.Templates.Template_V1.DoNotChange;

import org.firstinspires.ftc.teamcode.Templates.Template_V1.DoChange.TeleOpLogic;
import org.firstinspires.ftc.teamcode.Templates.Template_V1.DoChange.Values;

public class NonDriverControlled extends TeleOpLogic implements Values {

    Robot robot;

    public NonDriverControlled(Robot r) {
        robot = r;
    }

    public void execute(DriverController d) {

        update_motors_non_driver_controlled(robot, d);

    }
}
