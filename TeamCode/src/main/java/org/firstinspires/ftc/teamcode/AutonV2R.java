package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Systems.RobotHardware;
@Disabled
@Autonomous(name = "AutonV2 (Red)")
public class AutonV2R extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private RobotHardware robot = new RobotHardware();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {

            robot.DriveDistance(3, "Left");
            ExecuteEncoders();
            sleep(500);
            robot.DriveDistance(-10);
            ExecuteEncoders();




            stop();

        }
    }

    private void ExecuteEncoders() {
        robot.SpeedSet(0.75);
        while (robot.MotorsBusy() && opModeIsActive()) {
            idle();
        }
        robot.SpeedSet(0);
    }
}
