package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ObjectiveController {
    RobotHardware robot;
    TeleOpObjectiveLogic OL;
    public ObjectiveController(RobotHardware r){robot = r;  OL = new TeleOpObjectiveLogic(robot, this);}


    public void inputs(Gamepad gamepad){
        if(gamepad.a){ //intake
            OL.aButton();
        }
        if(gamepad.b){
            OL.bButton();
        }
        if(gamepad.x){
            OL.xButton();
        }
        if(gamepad.y){ //outtake
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
        /*
        if(gamepad.right_stick_y > 0.25){
            OL.servoBottom();
        }
        if(gamepad.right_stick_y < -0.25){
            OL.servoMid();
        }
        if(gamepad.right_stick_x > 0.25){
            OL.servoMid();
        }
        if(gamepad.right_stick_x < -0.25){
            OL.servoDump();
        }*/
        if(gamepad.dpad_down) {
            OL.decrementServo();
        }else if(gamepad.dpad_up){
            OL.incrementServo();
        }

        if(gamepad.right_trigger > 0.1){
            OL.speedDuck = 2;
        }else{
            OL.speedDuck = 1;
        }

        OL.setStates(gamepad);
        OL.updateMotors();
    }
}
