package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class RobotHardware {

    public HardwareMap map;
    public Tensorflow tf;
    public Telemetry telemetry;

    public BNO055IMU imu;

    public DcMotor RF = null;
    public DcMotor RB = null;
    public DcMotor LF = null;
    public DcMotor LB = null;

    public DcMotor IN = null;
    public DcMotor Duck = null;
    public DcMotor Arm = null;

    static final double INCREMENT   = 0.01;
    static final int    CYCLE_MS    =   50;
    static final double MAX_POS     =  1.0;
    static final double MIN_POS     =  0.0;

    public Servo rServo = null;
    public Servo lServo = null;


    public void init(HardwareMap hardwareMap, Telemetry telemetry){
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

        RF = hardwareMap.get(DcMotor.class, "rightFront");
        RB = hardwareMap.get(DcMotor.class, "rightBack");
        LF = hardwareMap.get(DcMotor.class, "leftFront");
        LB = hardwareMap.get(DcMotor.class, "leftBack");

        IN = hardwareMap.get(DcMotor.class, "intake");
        Duck = hardwareMap.get(DcMotor.class, "duckWheel");
        Arm = hardwareMap.get(DcMotor.class, "arm");

        lServo = hardwareMap.get(Servo.class, "left");
        rServo = hardwareMap.get(Servo.class, "right");

        RF.setDirection(DcMotor.Direction.REVERSE);
        RB.setDirection(DcMotor.Direction.REVERSE);
        LF.setDirection(DcMotor.Direction.FORWARD);
        LB.setDirection(DcMotor.Direction.FORWARD);

        IN.setDirection(DcMotor.Direction.FORWARD);
        Duck.setDirection(DcMotor.Direction.REVERSE);
        Arm.setDirection(DcMotorSimple.Direction.REVERSE);
        telemetry.addData("Info", Arm);
        telemetry.addData("Status", "Robot Hardware Initialized");
        telemetry.update();
        this.map = hardwareMap;

        tf = new Tensorflow(this, telemetry);
    }
    public double getAngle(){

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YZX, AngleUnit.DEGREES);

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
    public void ResetEncoders(){
        LF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void DriveWithEncoders(){
        LF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void DriveNormally(){
        LF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void DriveDistance(double inches){
        int Ticks = (int) Math.round(inches / ((3 * Math.PI) / 767));
        ResetEncoders();
        LF.setTargetPosition(Ticks);
        LB.setTargetPosition(Ticks);
        RF.setTargetPosition(Ticks);
        RB.setTargetPosition(Ticks);
        DriveWithEncoders();
    }
    public void DriveDistance(double inches, String Direction){
        int Ticks = (int) Math.round(inches / ((3 * Math.PI) / 767));
        ResetEncoders();
        switch(Direction){
            case "Right":
                LF.setTargetPosition(-Ticks);
                LB.setTargetPosition(Ticks);
                RF.setTargetPosition(Ticks);
                RB.setTargetPosition(-Ticks);
                break;
            case "Left":
                LF.setTargetPosition(Ticks);
                LB.setTargetPosition(-Ticks);
                RF.setTargetPosition(-Ticks);
                RB.setTargetPosition(Ticks);
                break;
        }

        DriveWithEncoders();
    }
    public void SpeedSet(double speed){
        LF.setPower(speed);
        LB.setPower(speed);
        RF.setPower(speed);
        RB.setPower(speed);
    }
    public boolean MotorsBusy(){
        return LF.isBusy() && LB.isBusy() && RF.isBusy() && RB.isBusy();
    }

}
