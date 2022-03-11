package org.firstinspires.ftc.teamcode.Templates.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Templates.DoNotChange.Autonomous_Functions;
import org.firstinspires.ftc.teamcode.Templates.DoNotChange.Robot;

@Autonomous(name = "Autonomous Template")

public class Auton_Example extends Autonomous_Functions {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);

        int elementPosition;

        waitForStart();
        while (opModeIsActive()) {
            //Code for autonomous goes here, non-standard functions for autonomous go in Values.java

            //Initialize Arm, Servo Positions
            SetArm(robot, 2);
            SetServo(robot, 2);

            //Check where duck is and go to set position
            Drive(robot, 7.5);
            if(checkPos(robot)) { //if we have the thingy at the third square
                Drive(robot, distance_between_squares*2, "Left");
                elementPosition = 3;
            } else {
                Drive(robot, distance_between_squares, "Left");
                elementPosition = checkPos(robot) ? 2 : 1;
                Drive(robot, distance_between_squares, "Left");
            }
            robot.telemetry.addData("Barcode: ", elementPosition);
            robot.telemetry.update();

            //drop block in tower
            Drive(robot, 8, "Left");
            Drive(robot, 3.5);

            SetPower(robot, "Intake", 0.4);
            ResetEncoder(robot, "LinearSlide");
            SetArm(robot, 0);
            SetArm(robot, 2);

            SetPower(robot, "Intake", 0);
            sleep(100);
            SetArm(robot, elementPosition);
            SetServo(robot, 0);

            sleep(2000);

            //reset arm
            SetServo(robot, 2);

            SetArm(robot, 0);
            sleep(1000);

            //go over to duck wheel and spin it
            Drive(robot, -3.1);
            Drive(robot, 25.5, "Right");

            Drive(robot, -4);
            Turn(robot, 50, "Right");
            Drive(robot, 5.2, "Right", 0.4);

            SetPower(robot, "DuckWheel", -DuckWheelPowerA);
            sleep(3000);
            SetPower(robot, "DuckWheel", -DuckWheelPowerB);
            sleep(1000);
            SetPower(robot, "DuckWheel", 0);

            Drive(robot, 2, "Left");

            //park
            Turn(robot, 50, "Left");
            Drive(robot, 11);
            Drive(robot, 3, "Right");

            stop();
        }
    }

}
