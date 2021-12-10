package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ObjectiveController {
    RobotHardware robot;
    TeleOpObjectiveLogic OL;
    public ObjectiveController(RobotHardware r){robot = r;  OL = new TeleOpObjectiveLogic(robot, this);}


    public void inputs(Gamepad gamepad){
        if(gamepad.a){
            OL.aButton();
        }
        if(gamepad.b){
            OL.bButton();
        }
        if(gamepad.x){
            OL.xButton();
        }
        if(gamepad.y){
           OL.yButton();
        }
        if(gamepad.left_bumper){

        }
        if(gamepad.right_bumper){

        }
        if(gamepad.left_stick_y > 0.1){
            OL.armDown();
        }
        if(gamepad.left_stick_y < -0.1){
            OL.armUp();
        }
        if(gamepad.right_stick_y > 0.1){
            OL.servoBottom();
        }
        if(gamepad.right_stick_y < -0.1){
            OL.servoMid();
        }
        if(gamepad.right_stick_x > 0.1){
            OL.servoMid();
        }
        if(gamepad.right_stick_x < -0.1){
            OL.servoDump();
        }
        OL.setStates(gamepad);
        OL.updateMotors();
    }
}
