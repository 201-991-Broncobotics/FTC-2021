package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Systems.RobotHardware;

@Autonomous(name = "AutonV1")
public class AutonV1 extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private RobotHardware robot = new RobotHardware();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {

            robot.DriveDistance(2);
            ExecuteEncoders();

            robot.DriveDistance(1, "Left");
            ExecuteEncoders();

            robot.Duck.setPower(0.5);

            stop();

        }
    }

    private void ExecuteEncoders(){
        robot.SpeedSet(0.75);
        while(robot.MotorsBusy() && opModeIsActive()){
            idle();
        }
        robot.SpeedSet(0);
    }
}
