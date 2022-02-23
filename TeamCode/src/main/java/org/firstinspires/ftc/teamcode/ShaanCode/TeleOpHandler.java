package org.firstinspires.ftc.teamcode.ShaanCode;

public class TeleOpHandler {

    public double speed = 1.0;

    public double maxPower;

    public boolean a_button = false, a_toggle = false; //intake button
    public boolean y_button = false, y_toggle = false; //outtake button
    public double intakePower = 0.0;
    public boolean redDuckWheel = false; //x button
    public boolean blueDuckWheel = false; //b button
    public double duckWheelPower = 0.0;
    public int duckWheelDirection = 1;

    public double operatorLeftY = 0.0;
    public double armPower = 0.0;
    public int desiredArmPosition = 0; //left stick
    public int desiredServoPosition = 3; //dpad, bumpers?
}
