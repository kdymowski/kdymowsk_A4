package intelligence_core.kdymowsk_a4;

import android.graphics.PointF;

/**
 * Created by intelligence-core on 3/4/16.
 */
public class Simulator {
    /**
     * Width and height of screen.
     */
    private int mWidth, mHeight;

    /**
     * Ball position (in pixels).
     */
    private PointF mBallPos;

    /**
     * Ball radius (in pixels).
     */
    private int mBallRadius;

    /**
     * Ball velocity.
     */
    private PointF mBallVel;

    /**
     * Device acceleration (X,Y only).
     */
    private PointF mDeviceAccel;

    private PointF mBallAccel;

    /**
     * Delta time
     */
    private long newTime = (System.currentTimeMillis());

    /**
     * Reverse variable for bouncing the ball of the wall
     */
    private float reverseY = 1;
    private float reverseX = 1;

    /**
     * scaler to slow down the ball
     */
    private float scaler = 10000;

    // Constructor for Simulator.
    public Simulator(int width, int height)   {
        mWidth = width;
        mHeight = height;

        // Create variables for ball position and velocity.
        mBallVel = new PointF(0, 0);
        mBallPos = new PointF(50, 50);

        // Initialize ball radius to something reasonable.
        mBallRadius = 10;

        // Create variable for device acceleration (X,Y only).
        mDeviceAccel = new PointF(0, 0);

        // Create variable for holding the acceleration
        mBallAccel = new PointF(0,0);
    }

    /**
     * this keeps track fo the ball and wall collisions
     */
    public void runSimulation() {

        mBallAccel.x = -mDeviceAccel.x/scaler;
        mBallAccel.y = mDeviceAccel.y/scaler;
        long dt = System.currentTimeMillis() - newTime;
        newTime = System.currentTimeMillis();


        mBallVel.x = reverseX*(mBallVel.x + mBallAccel.x*dt);
        mBallVel.y = reverseY*(mBallVel.y + mBallAccel.y*dt);


        mBallPos.x = (float) (mBallPos.x + mBallVel.x*dt + .5*mBallAccel.x*(dt*dt));
        mBallPos.y = (float) (mBallPos.y + mBallVel.y*dt + .5*mBallAccel.y*(dt*dt));

        // If ball goes off screen, reposition to opposite side of screen
        if (mBallPos.x > mWidth - mBallRadius){
            reverseX = reverseX*(-1);
        }
        if (mBallPos.y > mHeight - mBallRadius){
            reverseY = reverseY*(-1);
        }
        if (mBallPos.x < 0 + mBallRadius){
            reverseX = reverseX*(-1);
        }
        if (mBallPos.y < 0 + mBallRadius){
            reverseY = reverseY*(-1);
        }
    }

    // Getters and setters.

    public PointF getBallPos() {
        return mBallPos;
    }

    public void setBallPos(PointF ballPos) {
        mBallPos = ballPos;
    }

    public PointF getBallVel() {
        return mBallVel;
    }

    public void setBallVel(PointF ballVel) {
        mBallVel = ballVel;
    }

    public int getBallRadius() {
        return mBallRadius;
    }

    public void setBallRadius(int ballRadius) {
        mBallRadius = ballRadius;
    }

    public PointF getDeviceAccel() {
        return mDeviceAccel;
    }

    public void setDeviceAccel(PointF deviceAccel) {
        mDeviceAccel = deviceAccel;
    }
}
