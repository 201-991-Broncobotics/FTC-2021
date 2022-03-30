package org.firstinspires.ftc.teamcode.Templates.Template_V4.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;

@Autonomous(name = "Autonomous Example")
public class Autonomous_Example extends LinearOpMode {

    Robot robot = new Robot();
    Logic logic = new Logic(robot);

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            //Code for autonomous goes here, non-standard functions for autonomous go in Values.java

            //Initialize Arm, Servo Positions
            logic.SetArm("Reset_Arm");
            logic.SetServo("Mid");

            //Check where duck is and go to set position
            logic.Drive(7.5);
            if(logic.checkPos()) { //if we have the thingy at the third square
                logic.Drive(logic.distance_between_squares*2, "Left");
                logic.elementPosition = "High_Goal";
            } else {
                logic.Drive(logic.distance_between_squares, "Left");
                logic.elementPosition = logic.checkPos() ? "Middle_Goal" : "Low_Goal";
                logic.Drive(logic.distance_between_squares, "Left");
            }
            robot.telemetry.addData("Barcode: ", logic.elementPosition);
            robot.telemetry.update();

            //drop block in tower
            logic.Drive(8, "Left");
            logic.Drive(3.5);

            logic.SetPower("Intake", 0.4);
            logic.ResetEncoder("LinearSlide");
            logic.SetArm("Reset_Arm");
            logic.SetArm("Middle_Goal");

            logic.SetPower("Intake", 0);
            logic.pause(100); //can also be sleep(100);
                        //difference: sleep suspends all activity, pause suspends reading of code
            logic.SetArm(logic.elementPosition);
            logic.SetServo("Dump");

            sleep(2000);

            //reset arm
            logic.SetServo("Bottom");

            logic.SetArm("Reset_Arm");
            sleep(1000);

            //go over to duck wheel and spin it
            logic.Drive(-3.1);
            logic.Drive(25.5, "Right");

            logic.Drive(-4);
            logic.Turn(50, "Right");
            logic.Drive(5.2, "Right", 0.4);

            logic.SetPower("DuckWheel", -logic.DuckWheelPowerA);
            sleep(3000);
            logic.SetPower("DuckWheel", -logic.DuckWheelPowerB);
            sleep(1000);
            logic.SetPower("DuckWheel", 0);

            logic.Drive(2, "Left");

            //park
            logic.Turn(50, "Left");
            logic.Drive(11);
            logic.Drive(3, "Right");

            stop();
        }
    }

}