package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Systems.RobotHardware;

import java.util.Set;

//@Disabled
@Autonomous(name = "AutonV2 (Blue)")
public class AutonV2B extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private RobotHardware robot = new RobotHardware();

    private int elementPosition = 0;

    private double distBetweenSquares = 5.5;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {

//======================================================
//---------------Main Code Starts Here------------------
//======================================================

//Read Barcode

            Drive(8);
            checkPos(1);

            if(elementPosition == 0) {

                //Runs if nothing detected at first square

                Drive(distBetweenSquares, "Left");
                checkPos(2);

                if(elementPosition == 0) {

                    //Runs if nothing detected at second square

                    Drive(distBetweenSquares, "Left");
                    checkPos(3);

                }else{

                    //Runs if something is detected at second square

                    Drive(distBetweenSquares, "Left");
                }

            }else{

                //Runs if something is detected at first square

                Drive(distBetweenSquares*2, "Left");

            }

            robot.telemetry.addData("Barcode: ", elementPosition);
            robot.telemetry.update();

//END Read Barcode

//Drop Block in Tower

            Drive(6, "Left");
            Drive(5);

            SetArm(elementPosition);

            robot.rServo.setPosition(0.3);
            robot.lServo.setPosition(0.7);

            sleep(2000);

            Drive(-5);
            Drive(21, "Right");

//END Drop Block in Tower

//Spin Duck Wheel

            Drive(-4);
            Turn(45, "Right");
            Drive(5, "Right", 0.4);

            robot.Duck.setPower(0.3);
            sleep(5000);
            robot.Duck.setPower(0);

            Drive(2, "Left");

//END Spin Duck Wheel

//Park

            Turn(45, "Left");
            Drive(10);
            Drive(3, "Right");

//END Park

//Reset Arm

            robot.rServo.setPosition(1);
            robot.lServo.setPosition(0);
            SetArm(0);
            sleep(500);

//END Reset Arm

            stop();

//======================================================
//-----------------Main Code Ends Here------------------
//======================================================

        }
    }

    private void ExecuteEncoders() {
        robot.SpeedSet(0.75);
        while (robot.MotorsBusy() && opModeIsActive()) {
            idle();
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
        robot.Arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        switch(pos){
            case 1:
                robot.Arm.setTargetPosition(500);
                break;
            case 2:
                robot.Arm.setTargetPosition(900);
                break;
            case 3:
                robot.Arm.setTargetPosition(1300);
                break;
            case 0:
                robot.Arm.setTargetPosition(50);
                break;
        }
        robot.Arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Arm.setPower(0.4);
        while(robot.Arm.isBusy() && opModeIsActive()){
            idle();
        }
        robot.Arm.setPower(0.1);

    }
}
