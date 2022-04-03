package org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Auton_Threads;

import org.firstinspires.ftc.teamcode.Templates.Template_V4.DoNotChange.Robot;

import java.util.ArrayList;

public class telem extends Thread {

    Robot robot;
    public ArrayList<Object> data = new ArrayList<>();
    public boolean should_be_running = true;
    boolean change = false;

    public telem(Robot r) {
        robot = r;
    }

    public void clear_data() {
        data.clear();
        change = true;
    }
    public void add_data(String s, Object o) {
        data.add(s);
        data.add(o);
        change = true;
    }

    public void run() {
        while(should_be_running) {
            if (change) {
                for (int i = 0; i < data.size(); i += 2) {
                    robot.telemetry.addData((String) data.get(i), data.get(i+1));
                }
                robot.telemetry.update();
                change = false;
            }
        }
    }
}
