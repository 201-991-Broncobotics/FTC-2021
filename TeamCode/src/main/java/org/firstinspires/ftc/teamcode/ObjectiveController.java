package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ObjectiveController {
    RobotHardware robot;
    public ObjectiveController(RobotHardware r){robot = r;}

    TeleOpObjectiveLogic OL = new TeleOpObjectiveLogic(robot, this);
    public void inputs(Gamepad gamepad){
        if(gamepad.a){
            OL.aButton();
        }
        if(gamepad.b){
            OL.bButton();
        }
        if(gamepad.x){

        }
        if(gamepad.y){

        }
        if(gamepad.left_bumper){

        }
        if(gamepad.right_bumper){

        }

    }
}
