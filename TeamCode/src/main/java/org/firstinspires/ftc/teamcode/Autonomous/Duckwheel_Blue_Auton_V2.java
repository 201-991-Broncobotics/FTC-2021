package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Systems.RobotHardware;

@Autonomous(name = "Blue Duckwheel Auton")
public class Duckwheel_Blue_Auton_V2 extends LinearOpMode implements Auton_Values{

    private ElapsedTime runtime = new ElapsedTime();
    private RobotHardware robot = new RobotHardware();

    private int elementPosition = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {

            //checking position and go to set position by first square
            Drive(7.5);
            checkPos(3);

            if(elementPosition == 3) { //if we have the thingy at the third square
                Drive(distance_between_squares*2, "Left");
            } else {
                Drive(distance_between_squares, "Left");
                checkPos(2);
                Drive(distance_between_squares, "Left");
            } //elementPosition = element position; ends up by square
            robot.telemetry.addData("Barcode: ", elementPosition);
            robot.telemetry.update();

            //drop block in tower
            Drive(8, "Left");
            Drive(3.8);

            robot.IN.setPower(0.4);
            robot.Arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            SetArm(0);
            SetServo(2);

            robot.IN.setPower(0);
            sleep(100);
            SetArm(elementPosition);

            sleep(100);
            for(int i = 0; i<50; i++){
                robot.rServo.setPosition(servoD);
                robot.rServo.setPosition(servoD+0.001);
            }
            robot.telemetry.addData("CheckPoint: ",1);
            robot.telemetry.update();
            sleep(500);
            sleep(500);
            robot.Duck.setPower(0.2);
            sleep(500);
            robot.Duck.setPower(0);
            sleep(500);
            robot.telemetry.addData("CheckPoint: ",2);
            robot.telemetry.update();
            //reset arm
            SetServo(1);

            SetArm(0);
            sleep(1000);
            robot.telemetry.addData("CheckPoint: ",3);
            robot.telemetry.update();

            //go over to duck wheel and spin it
            Drive(-3.1);
            Drive(25.5, "Right");

            Drive(-4);
            Turn(50, "Right");
            Drive(5.2, "Right", 0.4);

            robot.Duck.setPower(-duckMotorPowerA);
            sleep(3000);
            robot.Duck.setPower(-duckMotorPowerB);
            sleep(1000);
            robot.Duck.setPower(0);

            Drive(2, "Left");

            //park
            Turn(50, "Left");
            Drive(11);
            Drive(3, "Right");

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


        double current_time = runtime.milliseconds();
//&& runtime.milliseconds() < current_time + 4000
        int iterations = 0;
        while((Math.abs(pos-robot.rServo.getPosition()) > 0.08) && (opModeIsActive()) && (iterations < 100) ){
            iterations++;
            if (robot.rServo.getPosition() > pos) {
                robot.rServo.setPosition(robot.rServo.getPosition()-0.01);
            } else {
                robot.rServo.setPosition(robot.rServo.getPosition()+0.01);
            }
            telemetry.addData("in the", "loop");
            telemetry.update();
        //    sleep(10);
        }

    }
}