package org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;

@TeleOp(name="TeleOpFinal", group="Iterative Opmode")
public class Tele_Op_Final extends LinearOpMode {

    Robot robot = new Robot();
    Logic logic = new Logic(robot);

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap, telemetry);
        logic.init();

        waitForStart();

        while(opModeIsActive()) {

            logic.execute_controllers(gamepad1, gamepad2); //driver is gamepad1, operator is gamepad2
            logic.execute_non_driver_controlled();

        }
    }

}
