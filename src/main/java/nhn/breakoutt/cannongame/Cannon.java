package nhn.breakoutt.cannongame;

public class Cannon {
    private double x;
    private double y;
    private double angle;
    private int power;
    private int barrelLength;
    private int barrelWidth;
    private boolean isCharging;
    private long chargeStartTime;


    public void startCharging(){}

    public void stopCharging(){}

    public double getChargeLevel(){
        return 1;
    }

    public void fire(){}

    public void adjustAngle(double delta){}

    public void setPower(double power){}
}