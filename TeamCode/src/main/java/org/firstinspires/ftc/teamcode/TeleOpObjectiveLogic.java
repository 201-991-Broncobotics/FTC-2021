package org.firstinspires.ftc.teamcode;

public class TeleOpObjectiveLogic {
    RobotHardware robot;
    ObjectiveController objective;
    public TeleOpObjectiveLogic(RobotHardware r, ObjectiveController o){robot = r; objective = o;}
    public void aButton(){
        objective.tf.printData();
    }
    public void bButton(){

    }
}
