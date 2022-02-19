package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Systems.RobotHardware;

import java.util.Set;

@Autonomous(name = "Red Warehouse Auton")
public class Red_Warehouse extends LinearOpMode implements Auton_Values{

    private ElapsedTime runtime = new ElapsedTime();
    private RobotHardware robot = new RobotHardware();

    private int elementPosition = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {

            Drive(8, "Right");
            Drive(15);

            stop();
        }
    }

    private void ExecuteEncoders() {
        robot.SpeedSet(0.7);
        while (robot.MotorsBusy() && opModeIsActive()) {
            robot.telemetry.addData("Encoder RF: ", robot.RF.getCurrentPosition());
            robot.telemetry.addData("Encoder LF: ", robot.LF.getCurrentPosition());
            robot.telemetry.addData("Encoder RB: ", robot.RB.getCurrentPosition());
            robot.telemetry.addData("Encoder LB: ", robot.LB.getCurrentPosition());

            // in theory these should be the same while it's going forward

            robot.telemetry.update();
        }
        robot.SpeedSet(0.2);
        sleep(100);
        robot.SpeedSet(0);
        sleep(200);
    }
    private void ExecuteEncoders(double Speed) {
        robot.SpeedSet(Speed);
        while (robot.MotorsBusy() && opModeIsActive()) {
            idle();
        }
        robot.SpeedSet(0.2);
        sleep(100);
        robot.SpeedSet(0);
        sleep(200);
    }

    private void checkPos(int position){
        //robot.getGreen() > 100 &&
        if(robot.getDistInch() < 4){
            elementPosition = position;
            robot.telemetry.addData("Barcode: ", position);
            robot.telemetry.update();
        }
    }

    private void Drive(double Dist){
        robot.DriveDistance(-Dist);
        ExecuteEncoders();
    }

    private void Drive(double Dist, String Direction){
        robot.DriveDistance(Dist, Direction);
        ExecuteEncoders();
    }
    private void Drive(double Dist, String Direction, double Speed){
        robot.DriveDistance(Dist, Direction);
        ExecuteEncoders(Speed);
    }

    private void Turn(int Degrees, String Direction){
        if(Direction.equals("Right")){
            robot.turnEncoderDegree(Degrees);
        }else if(Direction.equals("Left")){
            robot.turnEncoderDegree(-Degrees);
        }
        ExecuteEncoders();
    }
    private void SetArm(int pos){

        switch(pos){
            case 1:

                robot.Arm.setTargetPosition(low_goal);
                break;
            case 2:

                robot.Arm.setTargetPosition(mid_goal);
                break;
            case 3:

                robot.Arm.setTargetPosition(high_goal);
                break;
            case 0:
                robot.Arm.setTargetPosition(reset_arm);
                break;
        }
        robot.Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Arm.setPower(armSpeed);
        while(robot.Arm.isBusy() && opModeIsActive()){
            idle();
        }
        robot.Arm.setPower(0.1);

    }
    private void SetServo(int position) throws InterruptedException{
        double pos = servoB;
        switch(position) {
            case 1:
                pos = servoB;
            case 2:
                pos = servoM;
            case 3:
                pos = servoD;
        }
        for(int i = 0; i<100; i++){
            robot.rServo.setPosition(pos);
            robot.rServo.setPosition(pos+0.001);
        }
    }
}