package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="MainV1", group="Iterative Opmode")
//@Disabled

public class TeleOpV1 extends LinearOpMode {

    RobotHardware robot = new RobotHardware();
    DriverController driver = new DriverController(robot);
    ObjectiveController objective = new ObjectiveController(robot);
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap, telemetry);

        waitForStart();
        while(opModeIsActive()){

            driver.drive(gamepad1);
            driver.inputs(gamepad1);
            objective.inputs(gamepad2);


        }
    }
}
