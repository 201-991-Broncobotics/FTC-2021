package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Systems.RobotHardware;

@Autonomous(name = "Blue Auton Duckwheel (OLD)")
public class AutonV1B extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private RobotHardware robot = new RobotHardware();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {

            robot.DriveDistance(4, "Left");
            ExecuteEncoders();

            sleep(500);

            robot.DriveDistance(-15);
            ExecuteEncoders();

            robot.Duck.setPower(-0.3);
            sleep(5000);
            robot.Duck.setPower(0);

            robot.DriveDistance(12, "Left");
            ExecuteEncoders();

            stop();

        }
    }

    private void ExecuteEncoders() {
        robot.SpeedSet(0.25);
        while (robot.MotorsBusy() && opModeIsActive()) {
            idle();
        }
        robot.SpeedSet(0);
    }
}