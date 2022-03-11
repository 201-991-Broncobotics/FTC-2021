package org.firstinspires.ftc.teamcode.Templates.DoNotChange;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Template", group="Iterative Opmode")

public class TeleOp_Template extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);

        waitForStart();

        DriverController driver = new DriverController(robot);
        OperatorController operator = new OperatorController(robot);
        NonDriverControlled nonDriverControlled = new NonDriverControlled(robot);

        while(opModeIsActive()){

            driver.execute(gamepad1);
            operator.execute(gamepad2);
            nonDriverControlled.execute(driver);

        }
    }
}
