package org.firstinspires.ftc.teamcode.Templates.DoNotChange;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Templates.DoNotChange.DriverController;
import org.firstinspires.ftc.teamcode.Templates.DoNotChange.OperatorController;
import org.firstinspires.ftc.teamcode.Templates.DoNotChange.Robot;

@TeleOp(name="MainV1", group="Iterative Opmode")
@Disabled

public class TeleOp_Template extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);

        waitForStart();
        DriverController driver = new DriverController(robot);
        OperatorController operator = new OperatorController(robot);

        while(opModeIsActive()){
            driver.execute(gamepad1);
            operator.execute(gamepad2);
            robot.telemetry.addData("PID :", driver.getPIDSteer());
            //robot.telemetry.addData("Arm Position: ", robot.dc_motor_list[dc_motor_names.indexOf("Arm")].getCurrentPosition());
            robot.telemetry.update();
        }
    }
}
