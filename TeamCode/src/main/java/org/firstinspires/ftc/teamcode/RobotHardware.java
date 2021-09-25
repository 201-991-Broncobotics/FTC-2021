package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class RobotHardware {

    HardwareMap map;

    public DcMotor RF = null;
    public DcMotor RB = null;
    public DcMotor LF = null;
    public DcMotor LB = null;

    BNO055IMU imu;

    double heading; // Angle that the robot is facing
    double desiredHeading; // Angle that the robot wants to go

    double tThreshold = 1; // Angle that the robot tries to correct to within
    double correction; // Amount that the robot is correction for the error

    double current_error; // The difference between the heading and the desired heading
    double previous_error;

    long current_time;
    long previous_time;

    boolean adjusting_p = true;
    boolean adjusting_d = false;

    long endTime = 0;

    //PID Weights
    double k_p = 0.05;
    double k_d = 1.9;
    double k_i = 0.01;

    public void init(HardwareMap hardwareMap, Telemetry telemetry){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        RF = hardwareMap.get(DcMotor.class, "rightFront");
        RB = hardwareMap.get(DcMotor.class, "rightBack");
        LF = hardwareMap.get(DcMotor.class, "leftFront");
        LB = hardwareMap.get(DcMotor.class, "leftBack");

        RF.setDirection(DcMotor.Direction.REVERSE);
        RB.setDirection(DcMotor.Direction.REVERSE);
        LF.setDirection(DcMotor.Direction.FORWARD);
        LB.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Robot Hardware Initialized");
        this.map = hardwareMap;
    }
    public void correctHeading(double targetHeading, ElapsedTime runtime){
        boolean breakout = false;
        double currentTime = runtime.milliseconds();
        double breakoutTime = currentTime + 500;
        heading = getAngle();
        while(currentTime < breakoutTime && !breakout) {
            if (heading > 0.5+targetHeading || heading < -0.5+targetHeading) {
                current_error = getError();
                correction = getPIDSteer();
                currentTime = runtime.milliseconds();
                // Calculates the value to put each motor to
                double powerLF = (correction);
                double powerLB = (correction);
                double powerRF = (-correction);
                double powerRB = (-correction);

                // Makes sure that the power values are not truncated
                if (Math.abs(powerLF) > 1 || Math.abs(powerRF) > 1 || Math.abs(powerLB) > 1 || Math.abs(powerRB) > 1) {
                    // Find the largest power
                    double max = 0;
                    max = Math.max(Math.abs(powerLF), Math.abs(powerLB));
                    max = Math.max(Math.abs(powerRF), max);
                    max = Math.max(Math.abs(powerRB), max);

                    // Divide everything by max (it's positive so we don't need to worry
                    // about signs)
                    powerLB /= max;
                    powerLF /= max;
                    powerRB /= max;
                    powerRF /= max;
                }

                // Set the power of the motors
                RF.setPower(powerRF);
                RB.setPower(powerRB);
                LF.setPower(powerLF);
                LB.setPower(powerLB);
                heading = getAngle();
            }else{
                breakout = true;
            }
            currentTime = runtime.milliseconds();
        }
    }
    public double getAngle(){

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return angles.firstAngle;
    }

    // Gets the difference between the desired heading and the actual heading
    public double getError(){

        double diff = desiredHeading - heading;

        while (diff > 180)  diff -= 360;
        while (diff <= -180) diff += 360;

        return diff;
    }

    // Outputs the amount that the robot should correct based on the error
    public double getPIDSteer(){

        current_time = System.currentTimeMillis();
        current_error = getError();


        double p = k_p * current_error;
        double d = k_d * (current_error - previous_error) / (current_time - previous_time);

        double i = 0;

        i += k_i * (current_error * (current_time - previous_time));

        previous_error = current_error;
        previous_time = current_time;

        //If the error is within the threshold, we correct only on d
        if(Math.abs(current_error) <= tThreshold){

            return p+d;

        } else{

            return p+d;

        }
    }

    //Get the current battery voltage
    public double getBatteryVoltage() {

        double result = Double.POSITIVE_INFINITY;

        for (VoltageSensor sensor : map.voltageSensor) {

            double voltage = sensor.getVoltage();

            if (voltage > 0) {

                result = Math.min(result, voltage);

            }
        }
        return result;
    }
}
