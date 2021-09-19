package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH;

@TeleOp(name="MainV55", group="Iterative Opmode")
//@Disabled

public class TeleOpCodeV5 extends LinearOpMode
{

    //Declare Servos and Associated Variables
    public boolean fix = false;
    //Hardware
    Servo armServo, gripServo;

    public double Powershotangle = 0;
    //Positioning Variables
    double  armPosition, gripPosition;
    double  MIN_POSITION = 0, MAX_POSITION = 1;


    boolean PastStateLT = false;

    public int TIME = 260;
//Declare Distance Sensors and Associated Variables

    //Hardware
    private DistanceSensor FTSensor;
    private DistanceSensor distSide = null;

    //Variables
    public double sideDistGoal = 0;
    public double SideDistCurrent = 0;
    public double FTdistance = 0;
    public double SetDistance = 0;


    //Declare System Variables
    double voltage = 1;
    private ElapsedTime runtime = new ElapsedTime();


//Declare Motors and Associated Variables

    //Encoder Variables
    public double EncoderDistance = 0;
    public int Ticks = 0;

    //Drive Train Hardware
    public DcMotor RF = null;
    public DcMotor RB = null;
    public DcMotor LF = null;
    public DcMotor LB = null;

    //Intake and Conveyor Hardware
    public DcMotor Intake = null;
    public DcMotor StorageMotor = null;

    //Shooter Hardware
    private DcMotorEx motorA = null;
    private DcMotorEx motorB = null;

    //Intake Related Variables
    public boolean lbPast = false;
    public boolean rbPast = false;

    public boolean lb2past = false;
    public boolean rb2past = false;

    public boolean IntakeOff = false;

    public double intakespeed = 0;

    //Shooter Variables
    public double outtakeVariant = 0;
    double outtakeConstant = 0;
    double outtakespeed = 0;

    //Conveyor Variables
    public double storagespeed = 0;

//PID Driving Hardware and Associated Variables

    //Initialize IMU
    BNO055IMU imu;

    //PID Driving Variables
    double heading; // Angle that the robot is facing
    double desiredHeading; // Angle that the robot wants to go

    double tThreshold = 1; // Angle that the robot tries to correct to within
    double correction; // Amount that the robot is correction for the error

    double current_error; // The difference between the heading and the desired heading
    double previous_error;

    long current_time;
    long previous_time;

    boolean adjusting_p = true;
    boolean adjusting_d = false;

    long endTime = 0;

    //PID Weights
    double k_p = 0.05;
    double k_d = 1.9;
    double k_i = 0.01;

    //Sameer Bug
    boolean removeSameerBug = true;

    @Override
    public void runOpMode() throws InterruptedException {

//Map Servos
        armServo = hardwareMap.servo.get("Servo2");
        gripServo = hardwareMap.servo.get("Servo1");

//Set Servo Starting Pos
        armPosition = 0.1;
        gripPosition = 0.6;
        gripServo.setPosition(Range.clip(gripPosition, MIN_POSITION, MAX_POSITION));
        sleep(1000);
        armServo.setPosition(Range.clip(armPosition, MIN_POSITION, MAX_POSITION));


//Map Distance Sensors
        distSide = hardwareMap.get(DistanceSensor.class, "followMeS");
        FTSensor = hardwareMap.get(DistanceSensor.class, "followMeFT");

//Initialize IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        telemetry.addData("Status", "Initialized");

//Map Drive Train
        RF = hardwareMap.get(DcMotor.class, "rightFront");
        RB = hardwareMap.get(DcMotor.class, "rightBack");
        LF = hardwareMap.get(DcMotor.class, "leftFront");
        LB = hardwareMap.get(DcMotor.class, "leftBack");

//Map Shooters, Intake, and Conveyor
        motorA = hardwareMap.get(DcMotorEx.class, "motor_A");
        motorB = hardwareMap.get(DcMotorEx.class, "motor_B");
        Intake = hardwareMap.get(DcMotor.class, "IntakeMotor");
        StorageMotor = hardwareMap.get(DcMotor.class, "StorageMotor");

//Set Direction of Shooters, Intake, and Conveyor
        motorA.setDirection(DcMotor.Direction.FORWARD);
        motorB.setDirection(DcMotor.Direction.FORWARD);
        Intake.setDirection(DcMotor.Direction.FORWARD);
        StorageMotor.setDirection(DcMotor.Direction.REVERSE);

//Set Direction of Drive Train
        RF.setDirection(DcMotor.Direction.REVERSE);
        RB.setDirection(DcMotor.Direction.REVERSE);
        LF.setDirection(DcMotor.Direction.FORWARD);
        LB.setDirection(DcMotor.Direction.FORWARD);

//Add "Initialized" Telemetry
        telemetry.addData("Status", "Initialized");

//Code Loop
        waitForStart();
        while (opModeIsActive()) {



//Telemetry
            telemetry.addData("Side Distance", distSide.getDistance(INCH));
            telemetry.addData("Shooter Variant (-0.25 to 0.25) : ", outtakeVariant);

//Calculate for Change in Voltage
            outtakeConstant = 10 / voltage;

//Get Inputs from Distance Sensor
            FTdistance = FTSensor.getDistance(INCH);
            SideDistCurrent = distSide.getDistance(INCH);

//Set Conveyor Speed to Zero
            storagespeed = 0;

//Toggle Grip Servo
            if(gamepad2.left_trigger > 0.1 && !PastStateLT){
                PastStateLT = true;
                if(gripServo.getPosition() < 0.5){
                    gripPosition = 0.6;
                }else{
                    gripPosition = 0.3;
                }

            }

            //Reset Toggle Variable
            if(gamepad2.left_trigger <= 0.1 && PastStateLT){
                PastStateLT = false;
            }

//Control Arm Servo
            if(gamepad2.left_stick_y > 0.1){

                armPosition = 0.465;

            }else if(gamepad2.left_stick_y < -0.1){

                armPosition = 0.01;

            }else if(gamepad2.left_stick_x < -0.1){
                armPosition = 0.26;
            }

//Move Servos
            armServo.setPosition(Range.clip(armPosition, MIN_POSITION, MAX_POSITION));
            gripServo.setPosition(Range.clip(gripPosition, MIN_POSITION, MAX_POSITION));

//Manual Shooting
            if (gamepad2.right_trigger > 0.1) {

                outtakespeed = 0.8 + outtakeVariant;
                outtakespeed = outtakespeed * outtakeConstant;

                while (gamepad2.right_trigger > 0.1) {

                    storagespeed = 0;

                    if (gamepad2.dpad_up) {

                        storagespeed = 0.8;

                    } else if (gamepad2.dpad_down) {

                        storagespeed = -0.8;

                    }

                    StorageMotor.setPower(storagespeed);
                    motorA.setPower(outtakespeed);
                    motorB.setPower(outtakespeed);

                    //Removes the Sameer bug

                    if (removeSameerBug) {
                        //Gets input from controller for drive train
                        double LX = gamepad1.left_stick_x;  //Left stick left and right
                        double LY = -gamepad1.left_stick_y;  // Left stick up and down
                        double RX = -gamepad1.right_stick_x; // Right stick left and right


                        //Gets current angle of the robot
                        heading = getAngle();


                        //Changes Desired Heading
                        if (Math.abs(gamepad1.right_stick_x) >= 0.05) {

                            desiredHeading = heading;

                            endTime = System.currentTimeMillis();

                        }

                        if ((System.currentTimeMillis() - endTime) < 300) {

                            desiredHeading = heading;

                        }

                        // Controls for gyro turns
                        if (gamepad1.dpad_up) {

                            desiredHeading = 0;

                        } else if (gamepad1.dpad_right) {

                            desiredHeading = -90;

                        } else if (gamepad1.dpad_left) {

                            desiredHeading = 90;

                        } else if (gamepad1.dpad_down) {

                            desiredHeading = 180;

                        }

                        // Changes between adjusting p vaues and d values
                        if (gamepad1.a) {

                            if (adjusting_p == true && adjusting_d == false) {

                                adjusting_p = false;
                                adjusting_d = true;

                            } else {

                                adjusting_p = true;
                                adjusting_d = false;

                            }
                        }

                        if (adjusting_p == true) {

                            telemetry.addLine("Adjusting p");

                            if (gamepad1.right_bumper) {

                                k_p += 0.001;

                            }
                            if (gamepad1.left_bumper) {

                                k_p -= 0.001;

                            }
                        }

                        if (adjusting_d == true) {

                            telemetry.addLine("Adjusting d");

                            if (gamepad1.right_bumper) {

                                k_d += 0.01;

                            }

                            if (gamepad1.left_bumper) {

                                k_d -= 0.01;

                            }
                        }

                        // Gets the PID correction
                        current_error = getError();
                        correction = getPIDSteer();

                        // Calculates the value to put each motor to
                        double powerLF = (LY + LX + correction + RX);
                        double powerLB = (LY - LX + correction + RX);
                        double powerRF = (LY - LX - correction - RX);
                        double powerRB = (LY + LX - correction - RX);

                        // Makes sure that the power values are not truncated
                        if (Math.abs(powerLF) > 1 || Math.abs(powerRF) > 1 || Math.abs(powerLB) > 1 || Math.abs(powerRB) > 1) {

                            // Find the largest power

                            double max = 0;

                            max = Math.max(Math.abs(powerLF), Math.abs(powerLB));
                            max = Math.max(Math.abs(powerRF), max);
                            max = Math.max(Math.abs(powerRB), max);

                            // Divide everything by max (it's positive so we don't need to worry about signs)
                            powerLB /= max;
                            powerLF /= max;
                            powerRB /= max;
                            powerRF /= max;

                        }

                        // Set the power of the drive train
                        RF.setPower(powerRF);
                        RB.setPower(powerRB);
                        LF.setPower(powerLF);
                        LB.setPower(powerLB);
                    }
                }
            }


//Intake Forward, Backward, and Off
            if (IntakeOff) {

                intakespeed = 0;

            } else if (gamepad2.left_bumper) {

                intakespeed = 1.0;

            } else if (gamepad2.right_bumper) {

                intakespeed = -0.75;

            }

            if (lb2past != gamepad2.left_bumper && gamepad2.left_bumper) {

                IntakeOff = !IntakeOff;

            } else if (rb2past != gamepad2.right_bumper && gamepad2.right_bumper) {

                IntakeOff = !IntakeOff;

            }

//Conveyor Control
            if (gamepad2.dpad_up) {

                storagespeed = 0.8;

            } else if (gamepad2.dpad_down) {

                storagespeed = -0.8;

            }

//Shooting
            if (gamepad2.a) { // Low Goal

                SetDistance = 20; //Backs up 20 inches

                outtakespeed = 0.2 + outtakeVariant; //Sets shooter speed

                Shoot(false); //Shoots rings if the robot was initially in front of shooting line

            } else if (gamepad2.b) { // Middle Goal

                SetDistance = 56; //Backs up 56 inches

                outtakespeed = 0.45 + outtakeVariant; //Sets shooter speed

                Shoot(false); //Shoots rings if the robot was initially in front of shooting line

            } else if (gamepad2.x) { // High Goal

                SetDistance = 58; //Backs up 56 inches

                outtakespeed = 0.55 + outtakeVariant; //Sets shooter speed

                Shoot(false); //Shoots rings if the robot was initially in front of shooting line

            } else if (gamepad2.y) { // Powershot
                Powershotangle = getAngle();
                fix = true;
                PowerShot(true);
                sleep(500);
                TIME = 180;

                PowerShot(false);

                sleep(500);
                Intake.setPower(0.8);

                fix = true;
                TIME = 2000;
                PowerShot(false);
                Intake.setPower(0);

            }

//Resets all motors to zero speed
            LB.setPower(0);
            RB.setPower(0);
            LF.setPower(0);
            RF.setPower(0);

            motorA.setPower(0);
            motorB.setPower(0);
            StorageMotor.setPower(0);


//Gets input from controller for drive train
            double LX = gamepad1.left_stick_x;  //Left stick left and right
            double LY = -gamepad1.left_stick_y;  // Left stick up and down
            double RX = -gamepad1.right_stick_x; // Right stick left and right


//Gets current angle of the robot
            heading = getAngle();


// Changes Desired Heading
            if (Math.abs(gamepad1.right_stick_x) >= 0.05) {

                desiredHeading = heading;

                endTime = System.currentTimeMillis();

            }

            if ((System.currentTimeMillis() - endTime) < 300) {

                desiredHeading = heading;

            }

// Controls for gyro turns
            if (gamepad1.dpad_up) {

                desiredHeading = 0;

            } else if (gamepad1.dpad_right) {

                desiredHeading = -90;

            } else if (gamepad1.dpad_left) {

                desiredHeading = 90;

            } else if (gamepad1.dpad_down) {

                desiredHeading = 180;

            }

// Gets the PID correction
            current_error = getError();
            correction = getPIDSteer();

// Calculates the value to put each motor to
            double powerLF = (LY + LX + correction + RX);
            double powerLB = (LY - LX + correction + RX);
            double powerRF = (LY - LX - correction - RX);
            double powerRB = (LY + LX - correction - RX);

// Makes sure that the power values are not truncated
            if (Math.abs(powerLF) > 1 || Math.abs(powerRF) > 1 || Math.abs(powerLB) > 1 || Math.abs(powerRB) > 1) {

                // Find the largest power
                double max = 0;

                max = Math.max(Math.abs(powerLF), Math.abs(powerLB));
                max = Math.max(Math.abs(powerRF), max);
                max = Math.max(Math.abs(powerRB), max);

                // Divide everything by max (it's positive so we don't need to worry about signs)
                powerLB /= max;
                powerLF /= max;
                powerRB /= max;
                powerRF /= max;

            }

// Set the power of the drive train
            RF.setPower(powerRF);
            RB.setPower(powerRB);
            LF.setPower(powerLF);
            LB.setPower(powerLB);

//Set the power of intake and shooter
            Intake.setPower(intakespeed);
            StorageMotor.setPower(storagespeed);

//Set "past" variables to current controller state
            lbPast = gamepad1.left_bumper;
            rbPast = gamepad1.right_bumper;
            lb2past = gamepad2.left_bumper;
            rb2past = gamepad2.right_bumper;

//Get the current battery voltage
            voltage = (double) getBatteryVoltage();

//Set and update telemetries
            if (FTdistance < 50) {

                telemetry.addData("Shooting : ", "Available");

            } else {

                telemetry.addData("Shooting : ", "Not In Range");

            }

            telemetry.addData("distance : ", FTdistance);
            telemetry.addData("Constant:", voltage);

            telemetry.update();
        }
    }


//Outside Functions


    // Gets the angle from the imu
    private double getAngle(){

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        return angles.firstAngle;
    }

    // Gets the difference between the desired heading and the actual heading
    private double getError(){

        double diff = desiredHeading - heading;

        while (diff > 180)  diff -= 360;
        while (diff <= -180) diff += 360;

        return diff;
    }

    // Outputs the amount that the robot should correct based on the error
    private double getPIDSteer(){

        current_time = System.currentTimeMillis();
        current_error = getError();


        double p = k_p * current_error;
        double d = k_d * (current_error - previous_error) / (current_time - previous_time);

        double i = 0;

        i += k_i * (current_error * (current_time - previous_time));

        previous_error = current_error;
        previous_time = current_time;

        //If the error is within the threshold, we correct only on d
        if(Math.abs(current_error) <= tThreshold){

            return p+d;

        } else{

            return p+d;

        }
    }

    //Get the current battery voltage
    double getBatteryVoltage() {

        double result = Double.POSITIVE_INFINITY;

        for (VoltageSensor sensor : hardwareMap.voltageSensor) {

            double voltage = sensor.getVoltage();

            if (voltage > 0) {

                result = Math.min(result, voltage);

            }
        }
        return result;
    }
    private void correctHeading(double PowerShotHeading){
        boolean breakout = false;
        double currentTime = runtime.milliseconds();
        double breakoutTime = currentTime + 500;
        heading = getAngle();
        while(currentTime < breakoutTime && !breakout) {
            if (heading > 0.5+PowerShotHeading || heading < -0.5+PowerShotHeading) {
                current_error = getError();
                correction = getPIDSteer();
                currentTime = runtime.milliseconds();
                telemetry.addData("Target:", breakoutTime);
                telemetry.addData("Current:", currentTime);
                telemetry.update();
                // Calculates the value to put each motor to
                double powerLF = (correction);
                double powerLB = (correction);
                double powerRF = (-correction);
                double powerRB = (-correction);

                // Makes sure that the power values are not truncated
                if (Math.abs(powerLF) > 1 || Math.abs(powerRF) > 1 || Math.abs(powerLB) > 1 || Math.abs(powerRB) > 1) {
                    // Find the largest power
                    double max = 0;
                    max = Math.max(Math.abs(powerLF), Math.abs(powerLB));
                    max = Math.max(Math.abs(powerRF), max);
                    max = Math.max(Math.abs(powerRB), max);

                    // Divide everything by max (it's positive so we don't need to worry
                    // about signs)
                    powerLB /= max;
                    powerLF /= max;
                    powerRB /= max;
                    powerRF /= max;
                }

                // Set the power of the motors
                RF.setPower(powerRF);
                RB.setPower(powerRB);
                LF.setPower(powerLF);
                LB.setPower(powerLB);
                heading = getAngle();
            }else{
                breakout = true;
            }
            currentTime = runtime.milliseconds();
        }
    }
    void PowerShot(boolean firstShot) {
        int currentPos = motorB.getCurrentPosition();
        int targetPos = 0;
        double distNeeded = 0;
        if(firstShot){
            StorageMotor.setPower(-0.3);
            sleep(75);
            StorageMotor.setPower(0);
            sleep(100);
            TIME = 200;
            distNeeded = distSide.getDistance(INCH) - 20;
            targetPos = (int) ((distNeeded * 1552)+currentPos);
            telemetry.addData("Distance to Travel:", distNeeded);
            telemetry.addData("targetPos:", targetPos);
        }else{

            targetPos = currentPos - 10919;

        }
        while (currentPos > targetPos) {
            LF.setPower(0.5);
            RB.setPower(0.5);
            LB.setPower(-0.5);
            RF.setPower(-0.5);

            telemetry.addData("Distance to Travel:", distNeeded);
            telemetry.addData("targetPos:", targetPos);
            currentPos = motorB.getCurrentPosition();
            telemetry.addData("Odometry:", currentPos);
            telemetry.update();
        }
        LF.setPower(0);
        RB.setPower(0);
        LB.setPower(0);
        RF.setPower(0);
        if(firstShot) {

            LF.setPower(0);
            RB.setPower(0);
            LB.setPower(0);
            RF.setPower(0);
        }

        SetDistance = 58; //Backs up 58 inches

        outtakespeed = 0.45 + outtakeVariant;
        Shoot(true);

    }

    //Function to automatically back up and shoot rings
    void Shoot(boolean r){

        //Get current distance from front wall and shooter speed
        FTdistance = FTSensor.getDistance(INCH);
        outtakespeed = outtakespeed*outtakeConstant;

        if (FTdistance < 50) {

            //Calculate encoder ticks needed to reach target position
            EncoderDistance = FTdistance - SetDistance;
            Ticks = (int) Math.round(EncoderDistance / ((3 * Math.PI) / 767));

            //Resets motor encoders
            LF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            LB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            RF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            RB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            //Sets the target position for the encoders to the previously specified "Ticks" variable
            LF.setTargetPosition(Ticks);
            LB.setTargetPosition(Ticks);
            RF.setTargetPosition(Ticks);
            RB.setTargetPosition(Ticks);

            //Sets the DC Motor mode to "Run to Position"
            LF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            LB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Turns on the motors to full power
            LB.setPower(1);
            RB.setPower(1);
            LF.setPower(1);
            RF.setPower(1);

            //Warms up shooter motors
            motorA.setPower(outtakespeed + 0.2);
            motorB.setPower(outtakespeed);

            //Pauses the code until the robot has backed up
            while (LB.isBusy() && LF.isBusy() && RF.isBusy() && RB.isBusy() && opModeIsActive()) {
                idle();
            }

            //Slows down then stops the drive train
            LB.setPower(0.2);
            RB.setPower(0.2);
            LF.setPower(0.2);
            RF.setPower(0.2);
            sleep(100);
            LB.setPower(0);
            RB.setPower(0);
            LF.setPower(0);
            RF.setPower(0);

            //Set Motors to Run Normally (With Controller not Encoder)
            //Must do this step otherwise the motors will stop running after the encoders reach the target ticks
            //It functions like a limit on how far the motors will drive and isn't switched off until these lines
            LF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            LB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            RF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            RB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            //Check which mode to shoot in (Backing up or already behind line)
            //Backing Up

        }
        if(fix){
            LF.setPower(0.5);
            RB.setPower(-0.5);
            LB.setPower(0.5);
            RF.setPower(-0.5);
            sleep(200);
            correctHeading(Powershotangle);
            correctHeading(Powershotangle);
            LF.setPower(0);
            RB.setPower(0);
            LB.setPower(0);
            RF.setPower(0);
            fix = false;
        }
        if(!r && FTdistance < 50) {
            LB.setPower(0);
            RB.setPower(0);
            LF.setPower(0);
            RF.setPower(0);
            sleep(500);
            StorageMotor.setPower(0.75 * outtakeConstant);
            sleep(1000);
            Intake.setPower(1);
            sleep(750);
            Intake.setPower(0);
            sleep(1000);

            //Already behind line
        }else if(r){
            LB.setPower(0);
            RB.setPower(0);
            LF.setPower(0);
            RF.setPower(0);
            motorA.setPower(outtakespeed + 0.2+0.05);
            motorB.setPower(outtakespeed+0.05);
            sleep(900);
            StorageMotor.setPower(-0.8);
            sleep(100);
            StorageMotor.setPower(0.75 * outtakeConstant);
            sleep(TIME);
            Intake.setPower(1);
            sleep(340);
            Intake.setPower(0);
            sleep(100);
            r = !r;
        }
        LB.setPower(0);
        RB.setPower(0);
        LF.setPower(0);
        RF.setPower(0);

        motorA.setPower(0);
        motorB.setPower(0);
        StorageMotor.setPower(0);
    }

}