package org.firstinspires.ftc.teamcode.Systems;

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

    public DcMotor Duck = null;

    BNO055IMU imu;

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

        Duck = hardwareMap.get(DcMotor.class, "duckMotor");

        RF.setDirection(DcMotor.Direction.REVERSE);
        RB.setDirection(DcMotor.Direction.REVERSE);
        LF.setDirection(DcMotor.Direction.FORWARD);
        LB.setDirection(DcMotor.Direction.FORWARD);

        Duck.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Robot Hardware Initialized");
        this.map = hardwareMap;
    }
    public double getAngle(){

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return angles.firstAngle;
    }
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
