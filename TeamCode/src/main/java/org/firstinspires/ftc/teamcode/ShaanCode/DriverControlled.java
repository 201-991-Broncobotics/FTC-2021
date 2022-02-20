package org.firstinspires.ftc.teamcode.ShaanCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Systems.RobotHardware;

@TeleOp(name = "Shaan Code", group = "Iterative Opmode")
public class DriverControlled extends LinearOpMode {

    RobotHardware Gary_II = new RobotHardware();
    ElapsedTime runTime = new ElapsedTime();

    double speed = 1;
    double angle = 0;

    double leftX;
    double leftY;
    double rightX;
    double rightY;

    boolean intake = false; //a button
    boolean outtake = false; //y button
    double intakePower = 0.0;
    boolean redDuckWheel = false; //x button
    boolean blueDuckWheel = false; //b button
    double duckWheelPower = 0.0;
    int duckWheelDirection = 1;

    double operatorLeftY = 0.0;
    boolean[] operatorServo = {false, false, false, false, false, false};
    boolean changeDesiredServo = false;
    boolean changeDesiredArm = false;
    int desiredArmPosition = 0; //left stick
    int desiredServoPosition = 0; //dpad, bumpers?

    @Override
    public void runOpMode() throws InterruptedException {
        Gary_II.init(hardwareMap, telemetry);
        waitForStart();

        while(opModeIsActive()) {

            //get info on driver inputs
            if (gamepad1.right_trigger > 0.1) {speed = 2;} else {speed = 1;}
            leftX = gamepad1.left_stick_x; leftY = gamepad1.left_stick_y; rightX = gamepad1.right_stick_x; rightY = gamepad1.right_stick_y;

            //get info on operator inputs
            intake = gamepad2.a; outtake = gamepad2.y; redDuckWheel = gamepad2.x; blueDuckWheel = gamepad2.b; operatorLeftY = gamepad2.left_stick_y;
            operatorServo[0] = gamepad2.dpad_up; operatorServo[1] = gamepad2.dpad_down; operatorServo[2] = gamepad2.dpad_left; operatorServo[3] = gamepad2.dpad_right; operatorServo[4] = gamepad2.left_bumper; operatorServo[5] = gamepad2.right_bumper;

            intakePower = 0.0;
            if (intake) {intakePower = 0.3;} else if (outtake) {intakePower = -0.3;}

            if (redDuckWheel || blueDuckWheel) {
                duckWheelDirection = 1;
                if (blueDuckWheel) {duckWheelDirection = -1;}
                duckWheelPower += 0.01; duckWheelPower = Math.min(duckWheelPower, 0.7);
            } else {
                duckWheelPower = 0.0;
            }
            updateMotors();

        }

    }

    public void updateMotors() {
        Gary_II.IN.setPower(intakePower);
        Gary_II.Duck.setPower(duckWheelPower * duckWheelDirection);
        if (changeDesiredServo) {
            setServo(desiredServoPosition);
        }
        if (changeDesiredArm) {
            setArm(desiredArmPosition);
        }

        if (!Gary_II.Arm.isBusy()) {
            Gary_II.Arm.setPower(0);
        }

    }

    public void setArm(int desiredArmPosition) {
        int convertedPosition = 0;
        switch(desiredArmPosition) {
            case 1:
                convertedPosition = 200;
            case 2:
                convertedPosition = 800;
            case 3:
                convertedPosition = 1700;
        }

        goToPosition(Gary_II.Arm, convertedPosition);

    }

    public void setServo(int desiredServoPosition) {
        double convertedPosition = 0.0;
        switch(desiredServoPosition) {
            case 1:
                convertedPosition = 1;
            case 2:
                convertedPosition = 0.8;
            case 3:
                convertedPosition = 0.6;
        for (int i = 0; i < 250; i++) {
            Gary_II.rServo.setPosition(convertedPosition);
            Gary_II.rServo.setPosition(convertedPosition+0.001);
        }
        }

    }

    public void goToPosition(DcMotor motor, int targetPosition) {
        int multiplier = 1;
        if (motor.getCurrentPosition() > targetPosition) {
            multiplier = -1;
        }
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.3 * multiplier);
    }
}
