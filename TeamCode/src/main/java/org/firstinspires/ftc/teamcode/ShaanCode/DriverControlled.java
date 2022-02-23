package org.firstinspires.ftc.teamcode.ShaanCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

import org.firstinspires.ftc.teamcode.Systems.RobotHardware;

@TeleOp(name = "Shaan Code", group = "Iterative Opmode")
public class DriverControlled extends LinearOpMode implements Driver_Controlled_Values {

    RobotHardware Gary_II = new RobotHardware();

    double speed = 1.0;
    double angle = 0;

    double maxPower;

    boolean a_button = false, a_toggle = false; //intake button
    boolean y_button = false, y_toggle = false; //outtake button
    double intakePower = 0.0;
    boolean redDuckWheel = false; //x button
    boolean blueDuckWheel = false; //b button
    double duckWheelPower = 0.0;
    int duckWheelDirection = 1;

    double operatorLeftY = 0.0;
    double armPower = 0.0;
    int desiredArmPosition = 0; //left stick
    int desiredServoPosition = 3; //dpad, bumpers?

    @Override
    public void runOpMode() throws InterruptedException {
        Gary_II.init(hardwareMap, telemetry);
        waitForStart();

        while(opModeIsActive()) {

            //get info on driver inputs
            speed = gamepad1.right_trigger > 0.1 ? 0.5 : 1.0;

            maxPower = Math.max(Math.max(gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x, gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x), Math.max(gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x, gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x));

            Gary_II.RF.setPower((gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x)*speed/maxPower);
            Gary_II.RB.setPower((gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x)*speed/maxPower);
            Gary_II.LF.setPower((gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x)*speed/maxPower);
            Gary_II.LB.setPower((gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x)*speed/maxPower);

            //get info on operator inputs
            if ((gamepad2.a = !a_button) && (gamepad2.a)) a_toggle = !a_toggle; //intake
            if ((gamepad2.y = !y_button) && (gamepad2.y)) y_toggle = !y_toggle;
            a_button = gamepad2.a;
            y_button = gamepad2.y;
            intakePower = a_toggle ? 0.3 : y_toggle ? -0.3 : 0;
            Gary_II.IN.setPower(intakePower);

            //servo
            desiredServoPosition = gamepad2.dpad_up ? Math.min(desiredServoPosition + 1, 3) : gamepad2.dpad_down ? Math.max(desiredServoPosition - 1, 1) : gamepad2.right_bumper ? 3 : gamepad2.left_bumper ? 1 : desiredServoPosition;
            if (Gary_II.rServo.getPosition() != servoPositions[desiredServoPosition-1]) setServo(desiredServoPosition);

            //duck wheel
            if (gamepad2.x || gamepad2.b) duckWheelDirection = gamepad2.x ? 1 : -1;
            duckWheelPower = (gamepad2.x || gamepad2.b) ? Math.min(duckWheelPower + 0.01, 0.7) : 0.0;
            Gary_II.Duck.setPower(duckWheelPower * duckWheelDirection);

            //linear slide
            armPower = gamepad2.left_stick_y > 0.1 ? 0.5 : gamepad2.left_stick_y < -0.1 ? -0.13 : 0;
            Gary_II.Arm.setPower(armPower);
        }

    }

    public void setArm(int desiredArmPosition) { goToPosition(Gary_II.Arm, armPositions[desiredArmPosition-1]); }

    public void setServo(int desiredServoPosition) {
        for (int i = 0; i < 250; i++) {
            Gary_II.rServo.setPosition(servoPositions[desiredServoPosition-1]+0.001);
            Gary_II.rServo.setPosition(servoPositions[desiredServoPosition-1]);
        }

    }

    public void goToPosition(DcMotor motor, int targetPosition) {
        int multiplier = motor.getCurrentPosition() > targetPosition ? -1 : 1;
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.3 * multiplier);
    }
}
