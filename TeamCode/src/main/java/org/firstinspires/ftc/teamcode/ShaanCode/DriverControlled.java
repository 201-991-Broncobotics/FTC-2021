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

    boolean a_button = false, a_toggle = false; //intake button
    boolean y_button = false, y_toggle; //outtake button
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
            speed = gamepad1.right_trigger > 0.1 ? 0.8 : 0.4;

            //get info on operator inputs
            if ((gamepad2.a = !a_button) && (gamepad2.a)) a_toggle = !a_toggle;
            if ((gamepad2.y = !y_button) && (gamepad2.y)) y_toggle = !y_toggle;

            a_button = gamepad2.a;
            y_button = gamepad2.y;
            redDuckWheel = gamepad2.x; blueDuckWheel = gamepad2.b; operatorLeftY = gamepad2.left_stick_y;
            operatorServo[0] = gamepad2.dpad_up; operatorServo[1] = gamepad2.dpad_down; operatorServo[2] = gamepad2.dpad_left; operatorServo[3] = gamepad2.dpad_right; operatorServo[4] = gamepad2.left_bumper; operatorServo[5] = gamepad2.right_bumper;

            //intakePower = 0.0;
            //if (intake) {intakePower = 0.3;} else if (outtake) {intakePower = -0.3;}
            intakePower = a_toggle ? 0.3 : y_toggle ? -0.3 : 0;

            if (redDuckWheel || blueDuckWheel) duckWheelDirection = redDuckWheel ? 1 : -1;
            duckWheelPower = (redDuckWheel || blueDuckWheel) ? Math.min(duckWheelPower+0.01, 0.7) : 0.0;

            updateMotors();

        }

    }

    public void updateMotors() {
        Gary_II.IN.setPower(intakePower);
        Gary_II.Duck.setPower(duckWheelPower * duckWheelDirection);

        Gary_II.RF.setPower((gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x)*speed);
        Gary_II.RB.setPower((gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x)*speed);
        Gary_II.LF.setPower((gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x)*speed);
        Gary_II.LB.setPower((gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x)*speed);

        if (changeDesiredServo) setServo(desiredServoPosition);
        if (changeDesiredArm) setArm(desiredArmPosition);

        if (!Gary_II.Arm.isBusy()) Gary_II.Arm.setPower(0);

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
        int multiplier = motor.getCurrentPosition() > targetPosition ? -1 : 1;
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.3 * multiplier);
    }
}
