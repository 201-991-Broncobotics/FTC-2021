package org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Autonomous_Stuff;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;
import org.firstinspires.ftc.teamcode.Templates.Template_V4.Logic;

public class position_handler_open {

    double x = 0.0;
    double y = 0.0;
    double angle = 0.0;
    Robot robot;
    Logic logic;

    public position_handler_open(Robot r, Logic l) {
        robot = r;
        logic = l;
    }

    public void move(double distance) {
        move(distance, 0.0, true);
    }

    public void move(double distance, double target) {
        move(distance, target, true);
    }

    public void move(double distance, double target, boolean keep_current_angle) {
        set_position(x + distance * Math.cos(target + angle), y + distance * Math.sin(target + angle), keep_current_angle);
    }

    public void set_position(double target_x, double target_y) {
        set_position(target_x, target_y, true);
    }

    public void set_position(double target_x, double target_y, boolean keep_current_angle) {
        double target_angle = (target_x > x) ? Math.atan(((float) target_y - (float) y)/((float) target_x - (float) x)) :
                (target_x < x) ? Math.PI + Math.atan(((float) target_y - (float) y)/((float) target_x - (float) x)) :
                        (target_y > y) ? 90 : -90;
        target_angle = convert_to_degrees(target_angle);

        if ((Math.abs(mod360(target_angle - angle) - 0) < 0.5) && keep_current_angle) {
            logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)), "Forward");
        } else if ((Math.abs(mod360(target_angle - angle) - 90) < 0.5) && keep_current_angle) {
            logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)), "Right");
        } else if ((Math.abs(mod360(target_angle - angle) - 180) < 0.5) && keep_current_angle) {
            logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)), "Backward");
        } else if ((Math.abs(mod360(target_angle - angle) - 270) < 0.5) && keep_current_angle) {
            logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)), "Left");
        } else {
            set_angle(target_angle);
            logic.Drive(Math.sqrt((target_x - x) * (target_x - x) + (target_y - y) * (target_y - y)));
            x = target_x;
            y = target_y;
            if (keep_current_angle) {
                set_angle(angle);
            } else {
                angle = target_angle;
            }
        }
    }

    public void turn(double rotate_by) { //Positive means turn right, negative means turn left
        set_angle(angle + rotate_by);
    }

    public void set_angle(double target_angle) {
        logic.Turn(target_angle - angle);
        angle = mod360(target_angle);
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

}
