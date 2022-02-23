package org.firstinspires.ftc.teamcode.ShaanCode;

import java.util.ArrayList;
import java.util.Arrays;

public interface Driver_Controlled_Values {
    ArrayList<String> armPositionsNames = new ArrayList<>(Arrays.asList("Bottom", "Middle", "Top")); //only for Autonomous
    int[] armPositions = {200, 600, 1700};
    double[] servoPositions = {0.6, 0.8, 1.0};
}
