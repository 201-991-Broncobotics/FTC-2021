package org.firstinspires.ftc.teamcode.ShaanCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Shaan Code", group = "Iterative Opmode")
public class DriverControlled extends LinearOpMode implements Driver_Controlled_Values {

    Robot Gary_II = new Robot();
    TeleOpHandler handler = new TeleOpHandler();

    @Override
    public void runOpMode() throws InterruptedException {
        Gary_II.init(hardwareMap, telemetry);
        waitForStart();

        while(opModeIsActive()) {

            //driving
            handler.speed = gamepad1.right_trigger > 0.1 ? 0.5 : 1.0;

            handler.maxPower = Math.max(Math.max(Math.abs(gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x), Math.abs(gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x)), Math.max(Math.abs(gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x), Math.abs(gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x)));

            Gary_II.Right_Front_Wheel.setPower((gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x)*handler.speed/handler.maxPower);
            Gary_II.Right_Back_Wheel.setPower((gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x)*handler.speed/handler.maxPower);
            Gary_II.Left_Front_Wheel.setPower((gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x)*handler.speed/handler.maxPower);
            Gary_II.Left_Back_Wheel.setPower((gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x)*handler.speed/handler.maxPower);

            //intake
            if ((gamepad2.a = !handler.a_button) && (gamepad2.a)) handler.a_toggle = !handler.a_toggle;
            if ((gamepad2.y = !handler.y_button) && (gamepad2.y)) handler.y_toggle = !handler.y_toggle;
            handler.a_button = gamepad2.a;
            handler.y_button = gamepad2.y;
            handler.intakePower = handler.a_toggle ? 0.3 : handler.y_toggle ? -0.3 : 0;
            Gary_II.Intake.setPower(handler.intakePower);

            //servo
            handler.desiredServoPosition = gamepad2.dpad_up ? Math.min(handler.desiredServoPosition + 1, 3) : gamepad2.dpad_down ? Math.max(handler.desiredServoPosition - 1, 1) : gamepad2.right_bumper ? 3 : gamepad2.left_bumper ? 1 : handler.desiredServoPosition;
            if (Gary_II.Bucket_Servo.getPosition() != servoPositions[handler.desiredServoPosition-1]) setServo(handler.desiredServoPosition);

            //duck wheel
            if (gamepad2.x || gamepad2.b) handler.duckWheelDirection = gamepad2.x ? 1 : -1;
            handler.duckWheelPower = (gamepad2.x || gamepad2.b) ? Math.min(handler.duckWheelPower + 0.01, 0.7) : 0.0;
            Gary_II.Duck_Wheel.setPower(handler.duckWheelPower * handler.duckWheelDirection);

            //linear slide
            handler.armPower = gamepad2.left_stick_y > 0.1 ? 0.5 : gamepad2.left_stick_y < -0.1 ? -0.13 : 0;
            Gary_II.Linear_Slide.setPower(handler.armPower);
        }

    }

    public void setArm(int desiredArmPosition) { goToPosition(Gary_II.Linear_Slide, armPositions[desiredArmPosition-1]); }

    public void setServo(int desiredServoPosition) {
        for (int i = 0; i < 250; i++) {
            Gary_II.Bucket_Servo.setPosition(servoPositions[desiredServoPosition-1]+0.001);
            Gary_II.Bucket_Servo.setPosition(servoPositions[desiredServoPosition-1]);
        }

    }

    public void goToPosition(DcMotor motor, int targetPosition) {
        int multiplier = motor.getCurrentPosition() > targetPosition ? -1 : 1;
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.3 * multiplier);
    }
}
