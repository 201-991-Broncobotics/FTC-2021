package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

public class TeleOpObjectiveLogic {


    RobotHardware robot;
    ObjectiveController objective;

    //Variables
        private boolean aPrev = false;
        private boolean bPrev = false;

        private boolean aMotor = false;
        private boolean bMotor = false;

        private boolean ArmActive = false;
    //Variables




    public TeleOpObjectiveLogic(RobotHardware r, ObjectiveController o){robot = r; objective = o;}

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
        ArmActive = true;
        robot.Arm.setPower(0.5);
    }

    public void yButton(){
        ArmActive = true;
        robot.Arm.setPower(-0.5);
    }

    public void setStates(Gamepad g){
        aPrev = g.a;
        bPrev = g.b;
        if(!g.x && !g.y){ArmActive=false;}
    }

    public void updateMotors(){
        if(!ArmActive){
            robot.Arm.setPower(0);
        }

        if(bMotor){
            robot.Duck.setPower(1);
        }else{
            robot.Duck.setPower(0);
        }

        if(aMotor){
            robot.IN.setPower(1);
        }else{
            robot.IN.setPower(0);
        }
    }

}
