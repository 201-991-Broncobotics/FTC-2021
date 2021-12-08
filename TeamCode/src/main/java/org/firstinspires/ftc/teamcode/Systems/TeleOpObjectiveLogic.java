package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

public class TeleOpObjectiveLogic {


    RobotHardware robot;
    ObjectiveController objective;

    //Variables
        private boolean aPrev = false;
        private boolean bPrev = false;
        private boolean xPrev = false;

        private boolean aMotor = false;
        private boolean bMotor = false;
        private boolean xMotor = false;

        private boolean ArmActive = false;

        private double positionA = (RobotHardware.MAX_POS - RobotHardware.MIN_POS) / 2;
        private double positionB = (RobotHardware.MAX_POS - RobotHardware.MIN_POS) / 2;
    //Variables




    public TeleOpObjectiveLogic(RobotHardware r, ObjectiveController o){robot = r; objective = o;robot.telemetry.addData("here", "Here2");robot.telemetry.update();}

    public void aButton(){
        if(!aPrev){
            aMotor = !aMotor;
        }
    }

    public void bButton(){
        if(!bPrev){
            bMotor = !bMotor;
        }
    }

    public void xButton(){
        if(!xPrev){
            xMotor = !xMotor;
        }
    }

    public void yButton(){

    }

    public void LY_Up(){
        ArmActive = true;
        robot.Arm.setPower(0.5);
    }

    public void LY_Down(){
        ArmActive = true;
        robot.Arm.setPower(-0.5);
    }

    public void RY_Down(){

        if(positionA < RobotHardware.MAX_POS){
            positionA += 0.05;
        }
    }

    public void RY_Up(){
        if(positionA > RobotHardware.MIN_POS){
            positionA -= 0.05;
        }
    }

    public void setStates(Gamepad g){
        aPrev = g.a;
        bPrev = g.b;
        xPrev = g.x;
        if(g.left_stick_y > -0.1 && g.left_stick_y < 0.1){ArmActive=false;}

    }

    public void updateMotors(){
        if(!ArmActive){
            if(robot.Arm != null) {
                robot.Arm.setPower(0);
            }
        }

        if(bMotor){
            robot.Duck.setPower(0.5);
        }else if(xMotor){
            robot.Duck.setPower(-0.5);
        }else{
            robot.Duck.setPower(0);
        }

        if(aMotor){
            robot.IN.setPower(0.5);
        }else{
            robot.IN.setPower(0);
        }

        robot.servo.setPosition(positionA);
        robot.telemetry.addData("Position: ", positionA);
        robot.telemetry.update();


    }

}
