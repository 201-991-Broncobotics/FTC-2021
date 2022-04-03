package org.firstinspires.ftc.teamcode.Templates.Template_V4.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Autonomous_Stuff.*;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.*;

@Autonomous(name = "Autonomous Example")
public class Autonomous_Example extends LinearOpMode {

    Robot r = new Robot();
    Logic logic = new Logic(r);
    threaded_servo right;
    threaded_motor slide;
    position_handler robot;

    @Override
    public void runOpMode() throws InterruptedException {
        r.init(hardwareMap, telemetry);
        right = new threaded_servo(r, logic, "right");
        slide = new threaded_motor(r, logic, "arm");
        robot = new position_handler(r, logic);

        waitForStart();

        update(true);

        right.start();
        slide.start();

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

            r.telemetry.addData("Barcode: ", logic.elementPosition);
            r.telemetry.update();

            //drop block in tower
            robot.set_position(11.0, -19.0);

            logic.SetPower("intake", 0.4);
            setArm("Reset_Arm");
            setArm("Middle_Goal");

            logic.SetPower("intake", 0.0);

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

            robot.turn(50.0); //positive = right
            robot.move(5.2, 90);

            logic.pause(1000);

            logic.SetPower("duck", -logic.DuckWheelPowerA);
            sleep(3000);
            logic.SetPower("duck", -logic.DuckWheelPowerB);
            sleep(1000);
            logic.SetPower("duck", 0);

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
    }

    public void setArm(String position) {
        slide.set_position(logic.armPositions[logic.armPositionNames.indexOf(position)]);
        espera();
    }

    public void setServo(String position) {
        right.set_position(logic.servoPositions[logic.servoPositionNames.indexOf(position)]);
    }

    public void espera() {
        while (slide.isBusy) {
            idle();
        }
    }

}