package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ObjectiveController {
    RobotHardware robot;
    Tensorflow tf;
    public ObjectiveController(RobotHardware r, Tensorflow t){robot = r; tf = t;}

    TeleOpObjectiveLogic OL = new TeleOpObjectiveLogic(robot, this);
    public void inputs(Gamepad gamepad){
        if(gamepad.a){
            OL.aButton();
        }
        if(gamepad.b){
            OL.bButton();
        }
        if(gamepad.x){
            robot.Duck.setPower(0.2);
        }else{
            robot.Duck.setPower(0);
        }
        if(gamepad.y){
           
        }
        if(gamepad.left_bumper){

        }
        if(gamepad.right_bumper){

        }

    }
}
