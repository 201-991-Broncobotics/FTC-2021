package org.firstinspires.ftc.teamcode.Templates.Template_V4.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Auton_Threads.*;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.*;

import java.lang.Thread;

@Autonomous(name = "Autonomous Example")
public class Autonomous_Example extends LinearOpMode {

    Robot r = new Robot();
    Logic logic = new Logic(r);
    servo right;
    motor slide;
    motor duck;
    motor intake;
    position_handler robot;
    telem telemetree;

    @Override
    public void runOpMode() throws InterruptedException {
        r.init(hardwareMap, telemetry);
        right = new servo(r, logic, "right");
        slide = new motor(r, logic, "arm", "stop stalking me");
        duck = new motor(r, logic, "duckWheel", "power");
        intake = new motor(r, logic, "intake", "power");
        telemetree = new telem(r);

        waitForStart();

        update(true);

        right.start();
        slide.start();
        duck.start();
        intake.start();
        telemetree.start();

        while (opModeIsActive()) {

            logic.ResetEncoder("LinearSlide");
            setArm("Reset_Arm");
            //Check where duck is and go to set position
            robot.move(7.5);
            robot.set_position(7.5, 0.0);

            if(logic.checkPos()) { //if we have the thingy at the third square
                logic.elementPosition = "High_Goal";
            } else {
                robot.set_position(7.5, -5.5);
                logic.elementPosition = logic.checkPos() ? "Middle_Goal" : "Low_Goal";
            }

            telemetree.add_data("Barcode: ", logic.elementPosition);

            robot.set_position(7.5, -19.0);


            //drop block in tower
            robot.set_position(11.0, -19.0);

            intake.set_power(0.4);
            setArm("Reset_Arm");
            setArm("Middle_Goal");

            intake.set_power(0.0);

            logic.pause(100); //can also be sleep(100);
                        //difference: sleep suspends all activity, pause suspends reading of code

            setArm(logic.elementPosition);

            setServo("Dump");

            sleep(2000);

            //reset arm
            setServo("Bottom");

            setArm("Reset_Arm");

            sleep(1000);

            //go over to duck wheel and spin it
            robot.set_position(3.9, 6.5);

            //this is a rip :(
            robot.turn(50.0); //positive = right
            robot.move(5.2, 90);

            logic.pause(1000);

            duck.set_power(-logic.DuckWheelPowerA);
            sleep(3000);
            duck.set_power(-logic.DuckWheelPowerB);
            sleep(1000);
            duck.set_power(0);

            robot.move(2, -90);

            //park
            robot.turn(-50);
            robot.move(11);
            robot.move(3, 90.0);

            update(false);

            stop();

        }

        update(false);
    }

    public void update(boolean running) {
        right.should_be_running = running;
        slide.should_be_running = running;
        robot.should_be_running = running;
        duck.should_be_running = running;
        intake.should_be_running = running;
        telemetree.should_be_running = running;
    }

    public void setArm(String position) {
        slide.set_position(logic.armPositions[logic.armPositionNames.indexOf(position)]);
    }

    public void setServo(String position) {
        right.set_position(logic.servoPositions[logic.servoPositionNames.indexOf(position)]);
    }

}