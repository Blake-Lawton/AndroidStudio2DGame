package com.example.andriodstudio2dgame;

public class Utils {
    public static double getDistanceBetweenPoints(int p1x, int p1y, double p2x, double p2y)
    {
        return  Math.sqrt(
                Math.pow(p1x - p2x, 2) + Math.pow(p1y - p2y , 2));
    }
}
