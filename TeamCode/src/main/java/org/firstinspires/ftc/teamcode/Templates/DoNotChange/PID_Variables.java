package org.firstinspires.ftc.teamcode.Templates.DoNotChange;

import org.firstinspires.ftc.teamcode.Templates.DoChange.TeleOpLogic;

public class PID_Variables extends TeleOpLogic {

    int speedFactor = 1;

    double heading = 0; // Current angle of robot
    double desiredHeading = 0; // Angle that the robot wants to be at

    double correction = 0; // Calculated amount that the robot has to correct

    double current_error; // The difference between the heading and the desired heading
    double previous_error;

    long current_time;
    long previous_time;

    //PID Weights
    double p_weight = 0.025;
    double d_weight = 0.85;

}
