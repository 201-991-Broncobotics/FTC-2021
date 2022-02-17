package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Systems.*;


@TeleOp(name="MainV1", group="Iterative Opmode")
//@Disabled

public class TeleOpV1 extends LinearOpMode {

    RobotHardware robot = new RobotHardware();



    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap, telemetry);

        waitForStart();
        DriverController driver = new DriverController(robot);
        ObjectiveController objective = new ObjectiveController(robot);
        while(opModeIsActive()){

            driver.drive(gamepad1);
            driver.inputs(gamepad1);
            objective.inputs(gamepad2);

            robot.telemetry.addData("Arm Position: ", robot.Arm.getCurrentPosition());
            robot.telemetry.update();


        }
    }
}
