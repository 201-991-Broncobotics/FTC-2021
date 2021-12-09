package org.firstinspires.ftc.teamcode.Systems;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

public class TeleOpDriverLogic {
    double LeftBeam = 0.3;
    double RightBeam = 0.6;

    RobotHardware robot;
    DriverController driver;

    public TeleOpDriverLogic(RobotHardware r, DriverController d){robot = r; driver = d;}
    public void aButton(){
        robot.tf.printData();
    }
    public void bButton(){
        List<Recognition> recs = robot.tf.getRawData();
        float pos = recs.get(0).getLeft();
        if(pos<LeftBeam){

        }else if(pos<RightBeam){

        }else{

        }
    }
}
