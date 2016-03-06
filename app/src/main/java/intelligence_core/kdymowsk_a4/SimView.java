package intelligence_core.kdymowsk_a4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

/**
 * Created by intelligence-core on 3/4/16.
 */
public class SimView extends View {
    private static final String TAG = "SimView";

    /**
     * Ball position and radius (in pixels).
     */
    private PointF mBallPos;
    private int mR;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Constructor for SimView.
    public SimView(Context context)   {
        super(context);

        // Initialize values to something reasonable.
        mBallPos = new PointF(400, 40);
        mR = 30;
    }


    // Android calls this to redraw the view, after invalidate()
    @Override
    protected void onDraw(Canvas canvas)    {
        super.onDraw(canvas);
        //Log.d(TAG, "onDraw(); X = " + mX + " Y = " + mY);
        mPaint.setColor(0xFF00FF00);
        canvas.drawCircle(mBallPos.x, mBallPos.y, mR, mPaint);
    }


    // Setters.

    public void setBallPos(PointF ballPos) {
        mBallPos = ballPos;
    }

    public void setR(int r) {
        mR = r;
    }
}
