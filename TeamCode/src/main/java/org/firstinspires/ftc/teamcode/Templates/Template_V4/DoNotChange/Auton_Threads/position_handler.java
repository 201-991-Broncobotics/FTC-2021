package org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Auton_Threads;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;

public class position_handler extends Thread {

    double x = 0.0;
    double y = 0.0;
    double target_x = 0.0;
    double target_y = 0.0;
    double angle = 0.0;
    double target_angle = 0.0;
    boolean keep_angle;
    Robot robot;
    Logic logic;

    public position_handler(Robot r, Logic l) {
        robot = r;
        logic = l;
    }

    public boolean should_be_running = true;

    public void move(double distance) {
        target_x = x + distance * Math.cos(angle);
        target_y = y + distance * Math.sin(angle);
        keep_angle = true;
    }

    public void move(double distance, double target) {
        target_x = x + distance * Math.cos(target + angle);
        target_y = y + distance * Math.sin(target + angle);
        target_angle = mod360(target);
        keep_angle = true;
    }

    public void move(double distance, double target, boolean keep_current_angle) {
        target_x = x + distance * Math.cos(target + angle);
        target_y = y + distance * Math.sin(target + angle);
        target_angle = mod360(target);
        keep_angle = keep_current_angle;
    }

    public void set_position(double tx, double ty) {
        target_x = tx;
        target_y = ty;
        target_angle = (target_x > x) ? Math.atan(((float) target_y - (float) y)/((float) target_x - (float) x)) :
                (target_x < x) ? Math.PI + Math.atan(((float) target_y - (float) y)/((float) target_x - (float) x)) :
                (target_y > y) ? 90 : -90;
        target_angle = convert_to_degrees(target_angle);
        set_angle(target_angle);
        keep_angle = true;
    }
    public void set_position(double tx, double ty, boolean keep_current_angle) {
        target_x = tx;
        target_y = ty;
        target_angle = (target_x > x) ? Math.atan(((float) target_y - (float) y)/((float) target_x - (float) x)) :
                (target_x < x) ? Math.PI + Math.atan(((float) target_y - (float) y)/((float) target_x - (float) x)) :
                        (target_y > y) ? 90 : -90;
        target_angle = convert_to_degrees(target_angle);
        set_angle(target_angle);
        keep_angle = keep_current_angle;
    }

    public void turn(double rotate_by) { //Positive means turn right, negative means turn left
        target_angle = angle + rotate_by;
    }

    public void set_angle(double target) {
        target_angle = target;
    }

    public double mod360(double a) {
        double t = a;
        while (t < 0) {
            t += 360;
        }
        while (t >= 360) {
            t -= 360;
        }
        return t;

    }

    public double convert_to_degrees(double a) {
        double t = a * 360.0 / Math.PI;
        return mod360(t);
    }

    public void run() {
        while (should_be_running) {
            if ((target_x != x) || (target_y != y)) {
                if ((Math.abs(mod360(target_angle - angle) - 0) < 0.5) && keep_angle) {
                    logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)), "Forward");
                } else if ((Math.abs(mod360(target_angle - angle) - 90) < 0.5) && keep_angle) {
                    logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)), "Right");
                } else if ((Math.abs(mod360(target_angle - angle) - 180) < 0.5) && keep_angle) {
                    logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)), "Backward");
                } else if ((Math.abs(mod360(target_angle - angle) - 270) < 0.5) && keep_angle) {
                    logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)), "Left");
                } else {
                    logic.Turn(target_angle - angle);
                    logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)));
                    x = target_x;
                    y = target_y;
                    if (keep_angle) {
                        logic.Turn(angle - target_angle);
                        target_angle = angle;
                    } else {
                        angle = target_angle;
                    }
                }
            } else if (target_angle != angle) {
                logic.Turn(target_angle - angle);
                target_angle = mod360(target_angle);
                angle = target_angle;
            }
            logic.pause(100); //so we don't update all the time - don't actually need this ;)
        }
    }

}
