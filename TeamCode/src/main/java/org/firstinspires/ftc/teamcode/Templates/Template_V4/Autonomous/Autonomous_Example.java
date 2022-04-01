package org.firstinspires.ftc.teamcode.Templates.Template_V4.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;

import java.lang.Thread;

@Autonomous(name = "Autonomous Example")
public class Autonomous_Example extends LinearOpMode {

    Robot robot = new Robot();
    Logic logic = new Logic(robot);
    servo right;
    arm slide;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, telemetry);
        right = new servo(robot, logic, "right");
        slide = new arm(robot, logic);

        waitForStart();

        Thread servoThread = new Thread(right);
        servoThread.start();
        slide.start();

        while (opModeIsActive()) {

            logic.ResetEncoder("LinearSlide");
            slide.position = "Reset_Arm";
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
            slide.position = "Reset_Arm";
            slide.position = "Middle_Goal";

            logic.SetPower("Intake", 0);
            logic.pause(100); //can also be sleep(100);
                        //difference: sleep suspends all activity, pause suspends reading of code

            slide.position = logic.elementPosition;

            right.position = "Dump";

            sleep(2000);

            //reset arm
            right.position = "Bottom";

            slide.position = "Reset_Arm";

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

            right.should_be_running = false;
            slide.should_be_running = false;
        }

        right.should_be_running = false;
        slide.should_be_running = false;
    }

}

class servo implements Runnable {

    Robot robot;
    int servo_index;
    Logic logic;

    public servo(Robot r, Logic l, String servo_name) {
        robot = r;
        logic = l;
        servo_index = logic.servo_names.indexOf(servo_name);
    }

    boolean should_be_running = true;
    String position = "Mid";

    public void run() {
        while (should_be_running) {
            robot.servo_list[servo_index].setPosition(logic.servoPositions[logic.servoPositionNames.indexOf(position)]);
        }
    }

}

class arm extends Thread {

    Robot robot;
    int arm_index;
    Logic logic;

    public arm(Robot r, Logic l) {
        robot = r;
        logic = l;
        arm_index = logic.dc_motor_names.indexOf("arm");
    }

    boolean should_be_running = true;
    String position = "";

    public void run() {
        while (should_be_running) {
            robot.dc_motor_list[arm_index].setPower(Math.max(-0.5, Math.min(0.5, 0.05 *
                    (logic.armPositions[logic.armPositionNames.indexOf(position) - robot.dc_motor_list[arm_index].getCurrentPosition()])
            )));
        }
    }
}

class handler extends Thread {
    servo se;
    arm ar;
    boolean running = false;
    public handler(servo s, arm a) {
        se = s;
        ar = a;
    }

    public void run() {
        do {
            se.should_be_running = running;
            ar.should_be_running = running;
        } while(running);
    }

}