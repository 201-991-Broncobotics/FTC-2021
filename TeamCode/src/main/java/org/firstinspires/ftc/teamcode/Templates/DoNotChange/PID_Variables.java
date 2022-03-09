package org.firstinspires.ftc.teamcode.Templates.DoNotChange;

public class PID_Variables {
    int speedFactor = 1;

    public double heading = 0; // Angle that the robot is facing
    public double desiredHeading; // Angle that the robot wants to go

    double tThreshold = 3; // Angle that the robot tries to correct to within
    double correction; // Amount that the robot is correction for the error

    double current_error; // The difference between the heading and the desired heading
    double previous_error;

    long current_time;
    long previous_time;

    //PID Weights
    double k_p = 0.025;
    double k_d = 0.85;

}
