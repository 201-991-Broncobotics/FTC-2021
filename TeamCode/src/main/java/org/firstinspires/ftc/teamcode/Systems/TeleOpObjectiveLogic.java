package org.firstinspires.ftc.teamcode.Systems;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

public class TeleOpObjectiveLogic {
    double LeftBeam = 0.3;
    double RightBeam = 0.6;

    RobotHardware robot;
    ObjectiveController objective;
    public TeleOpObjectiveLogic(RobotHardware r, ObjectiveController o){robot = r; objective = o;}
    public void aButton(){
        objective.tf.printData();
    }
    public void bButton(){
        List<Recognition> recs = objective.tf.getRawData();
        float pos = recs.get(0).getLeft();
        if(pos<LeftBeam){

        }else if(pos<RightBeam){

        }else{

        }
    }
}
