package ch.epfl.sweng.groupup;

public class TestHelper {
    public static boolean reasonablyEqual(double x, double y){
        return Math.abs(y - x) < 0.00001;
    }
}
