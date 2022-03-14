package org.firstinspires.ftc.teamcode.Templates.Template_V1.DoNotChange;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Templates.Template_V1.DoChange.Values;

public abstract class Autonomous_Functions extends LinearOpMode implements Values {

    public void ExecuteEncoders(Robot r, double Speed) {
        r.SpeedSet(Speed);
        while (r.MotorsBusy() && opModeIsActive()) {
            idle();
        }
        r.SpeedSet(0.2);
        sleep(100);
        r.SpeedSet(0);
        sleep(200);
    }

    public void ExecuteEncoders(Robot r) {
        ExecuteEncoders(r, 0.7);
    }

    public void Drive(Robot r, double Dist){
        r.DriveDistance(-Dist);
        ExecuteEncoders(r);
    }

    public void Drive(Robot r, double Dist, String Direction){
        r.DriveDistance(Dist, Direction);
        ExecuteEncoders(r);
    }
    public void Drive(Robot r, double Dist, String Direction, double Speed){
        r.DriveDistance(Dist, Direction);
        ExecuteEncoders(r, Speed);
    }

    public void Turn(Robot r, int Degrees, String Direction){
        if(Direction.equals("Right")) {
            r.turnWithEncoders(Degrees);
        } else if(Direction.equals("Left")) {
            r.turnWithEncoders(-Degrees);
        }
        ExecuteEncoders(r);
    }

    public void SetPower(Robot r, String name, double power) {
        r.dc_motor_list[dc_motor_names.indexOf(name)].setPower(power);
    }

    public void ResetEncoder(Robot r, String name) {
        r.dc_motor_list[dc_motor_names.indexOf(name)].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

}
