package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Systems.RobotHardware;

import java.util.Set;

@Autonomous(name = "Red Duckwheel Auton")
public class Duckwheel_Red_Auton extends LinearOpMode implements Auton_Values{

    private ElapsedTime runtime = new ElapsedTime();
    private RobotHardware robot = new RobotHardware();

    private int elementPosition = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {

            //Read Barcode

            Drive(7.5);
            checkPos(1);

            if(elementPosition == 0) {

                //Runs if nothing detected at first square

                Drive(distance_between_squares, "Right");
                checkPos(2);

                if(elementPosition == 0) {

                    //Runs if nothing detected at second square

                    Drive(distance_between_squares, "Right");
                    checkPos(3);

                }else{

                    //Runs if something is detected at second square

                    Drive(distance_between_squares, "Right");
                }

            }else{

                //Runs if something is detected at first square

                Drive(distance_between_squares*2, "Right");

            }

            robot.telemetry.addData("Barcode: ", elementPosition);
            robot.telemetry.update();

//END Read Barcode

//Drop Block in Tower

            Drive(9.5, "Right");
            Drive(3.7);

            robot.IN.setPower(0.4);
            robot.Arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            SetArm(0);
            SetServo(2);
            // robot.lServo.setPosition(1-servoM);
            robot.IN.setPower(0);
            sleep(100);
            SetArm(elementPosition);
            SetServo(1);
            // robot.lServo.setPosition(1-servoBM);
            sleep(100);
            SetServo(3);
            sleep(500);
            sleep(500);
            robot.Duck.setPower(0.2);
            sleep(500);
            robot.Duck.setPower(0);
            sleep(500);
            // robot.lServo.setPosition(1-servoD);



//END Drop Block in Tower

//Reset Arm


          SetServo(1);

//END Reset Arm

//Spin Duck Wheel

            Drive(-3.1);
            Drive(23.5, "Left");

            Drive(-4);
            Turn(145, "Right");
            Drive(5, "Right", 0.4);

            robot.Duck.setPower(duckMotorPowerA);
            sleep(3000);
            robot.Duck.setPower(duckMotorPowerB);
            sleep(1000);
            robot.Duck.setPower(0);

            Drive(2, "Left");

//END Spin Duck Wheel

//Park

            Turn(40, "Right");
            Drive(-11);
            Drive(3, "Right");

//END Park



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