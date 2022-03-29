package org.firstinspires.ftc.teamcode.Templates.Template_V4;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Robot extends Robot_Logic {

    public BNO055IMU imu;

    public HardwareMap map;
    public Telemetry telemetry;

    public DistanceSensor[] distance_sensor_list = new DistanceSensor[distance_sensor_names.size()];
    public RevBlinkinLedDriver[] led_list = new RevBlinkinLedDriver[led_names.size()];
    public DcMotor[] wheel_list = new DcMotor[wheel_names.size()];
    public DcMotor[] dc_motor_list = new DcMotor[dc_motor_names.size()];
    public Servo[] servo_list = new Servo[servo_names.size()];

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        for (int i = 0; i < wheel_list.length; i++) {
            wheel_list[i] = hardwareMap.get(DcMotor.class, wheel_names.get(i));
            wheel_list[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            wheel_list[i].setDirection(i > 1 ? DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE);
        }

        for (int i = 0; i < distance_sensor_list.length; i++)
            distance_sensor_list[i] = hardwareMap.get(DistanceSensor.class, distance_sensor_names.get(i));

        for (int i = 0; i < led_list.length; i++)
            led_list[i] = hardwareMap.get(RevBlinkinLedDriver.class, led_names.get(i));

        for (int i = 0; i < dc_motor_list.length; i++) {
            dc_motor_list[i] = hardwareMap.get(DcMotor.class, dc_motor_names.get(i));
            dc_motor_list[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            dc_motor_list[i].setDirection(dc_motor_directions[i] == 0 ? DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE);
        }

        for (int i = 0; i < servo_list.length; i++)
            servo_list[i] = hardwareMap.get(Servo.class, servo_names.get(i));

        telemetry.addData("Status: ", "Robot Hardware Initialized");
        telemetry.update();

        this.map = hardwareMap;
    }

    //IMU Stuff
    public double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YZX, AngleUnit.DEGREES);
        return angles.firstAngle;
    }

    public void turnDegree(double degrees) {
        double startingAngle = getAngle();

        double targetHeading = startingAngle + degrees;
        while (targetHeading < 0 || targetHeading >= 360) {
            targetHeading += 360 * (targetHeading < 0 ? 1 : -1);
        }

        int factor = (((0 < targetHeading - startingAngle) && (targetHeading - startingAngle < 180)) || (targetHeading - startingAngle < -180) ? 1 : -1);
        for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setPower((i < 2 ? -0.5 : 0.5) * factor);

        while ((getAngle() - targetHeading) * (getAngle() - targetHeading) < 1) {
            //wait
        }
        SpeedSet(0);
    }

    public void SpeedSet(double speed) {
        for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setPower(speed);
    }

    //Driving Functions
    public void ResetEncoders() {
        for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void DriveWithEncoders() {
        for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void DriveDistance(double inches) {
        int Ticks = (int) Math.round(inches / ((3 * Math.PI) / 767));
        ResetEncoders();
        for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setTargetPosition(Ticks);
        DriveWithEncoders();
    }

    public void DriveDistance(double inches, String Direction) {
        int Ticks = (int) Math.round(inches / ((3 * Math.PI) / 767));
        ResetEncoders();
        switch(Direction){
            case "Right":
                for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setTargetPosition(i % 2 == 0 ? Ticks : -Ticks);
                break;
            case "Left":
                for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setTargetPosition(i % 2 == 1 ? Ticks : -Ticks);
                break;
            case "Backward":
                for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setTargetPosition(Ticks);
                break;
            case "Forward":
                for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setTargetPosition(-Ticks);
                break;
        }
        DriveWithEncoders();
    }

    public void turnEncoders(double inches) {
        int Ticks = (int) Math.round(inches / ((3 * Math.PI) / 767));
        ResetEncoders();
        for (int i = 0; i < wheel_list.length; i++) wheel_list[i].setTargetPosition(i < 2 ? Ticks : -Ticks);
        DriveWithEncoders();
    }

    public void turnWithEncoders(double degrees) {
        turnEncoders((int) (degrees * inchPer90 / 90) );
    }

    public boolean MotorsBusy() {
        return wheel_list[0].isBusy() && wheel_list[1].isBusy() && wheel_list[2].isBusy() && wheel_list[3].isBusy();
    }

    //Voltage
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

    //Distance Sensor
    public double getDistInch(String distanceSensor){
        return distance_sensor_list[distance_sensor_names.indexOf(distanceSensor)].getDistance(DistanceUnit.INCH);
    }

    //LED
    public void set_led_color(String led, String pattern) {
        RevBlinkinLedDriver.BlinkinPattern convertedPattern = RevBlinkinLedDriver.BlinkinPattern.WHITE;
        switch(pattern){
            case "Rainbow":
                convertedPattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
                break;
            case "Red":
                convertedPattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                break;
            case "Orange":
                convertedPattern = RevBlinkinLedDriver.BlinkinPattern.ORANGE;
                break;
            case "Yellow":
                convertedPattern = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
                break;
            case "Green":
                convertedPattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
                break;
            case "Blue":
                convertedPattern = RevBlinkinLedDriver.BlinkinPattern.BLUE;
                break;
            case "Violet":
                convertedPattern = RevBlinkinLedDriver.BlinkinPattern.VIOLET;
                break;
            case "White":
                convertedPattern = RevBlinkinLedDriver.BlinkinPattern.WHITE;
                break;
            case "Black":
                convertedPattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
                break;
        }
        led_list[led_names.indexOf(led)].setPattern(convertedPattern);
    }

}
