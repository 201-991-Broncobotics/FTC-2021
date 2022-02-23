package org.firstinspires.ftc.teamcode.ShaanCode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
    public HardwareMap map;
    public Telemetry telemetry;

    public DcMotor Right_Front_Wheel = null;
    public DcMotor Right_Back_Wheel = null;
    public DcMotor Left_Front_Wheel = null;
    public DcMotor Left_Back_Wheel = null;

    public DcMotor Intake = null;
    public DcMotor Duck_Wheel = null;
    public DcMotor Linear_Slide = null;

    public Servo Bucket_Servo = null;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        Right_Front_Wheel = hardwareMap.get(DcMotor.class, "right_front_wheel");
        Right_Back_Wheel = hardwareMap.get(DcMotor.class, "right_back_wheel");
        Left_Front_Wheel = hardwareMap.get(DcMotor.class, "left_front_wheel");
        Left_Back_Wheel = hardwareMap.get(DcMotor.class, "left_back_wheel");
        Intake = hardwareMap.get(DcMotor.class, "intake");
        Duck_Wheel = hardwareMap.get(DcMotor.class, "duck_wheel");
        Linear_Slide = hardwareMap.get(DcMotor.class, "linear_slide");
        Bucket_Servo = hardwareMap.get(Servo.class, "bucket_servo");

        Right_Front_Wheel.setDirection(DcMotor.Direction.REVERSE);
        Right_Back_Wheel.setDirection(DcMotor.Direction.REVERSE);
        Left_Front_Wheel.setDirection(DcMotor.Direction.FORWARD);
        Left_Back_Wheel.setDirection(DcMotor.Direction.FORWARD);
        Intake.setDirection(DcMotor.Direction.FORWARD);
        Duck_Wheel.setDirection(DcMotor.Direction.REVERSE);
        Linear_Slide.setDirection(DcMotorSimple.Direction.REVERSE);

        Right_Front_Wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Right_Back_Wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Left_Front_Wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Left_Back_Wheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Linear_Slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Right_Front_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Right_Back_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Left_Front_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Left_Back_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Duck_Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addData("Status", "Robot Hardware Initialized");
        telemetry.update();
        this.map = hardwareMap;
    }

}