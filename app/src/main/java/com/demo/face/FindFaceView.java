package com.demo.face;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Paint.Style;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FindFaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private int mWidth;
    private int mHeight;
    private float eyesDistance;

    public FindFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSPARENT);
        this.setZOrderOnTop(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void drawRect(FaceDetector.Face[] faces, int numberOfFaceDetected) {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            Paint clipPaint = new Paint();
            clipPaint.setAntiAlias(true);
            clipPaint.setStyle(Paint.Style.STROKE);
            clipPaint
                    .setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(clipPaint);
            canvas.drawColor(getResources().getColor(color.transparent));
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.GREEN);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(5.0f);
            for (int i = 0; i < numberOfFaceDetected; i++) {
                Face face = faces[i];
                PointF midPoint = new PointF();
                // 获得两眼之间的中间点
                face.getMidPoint(midPoint);
                // 获得两眼之间的距离
                eyesDistance = face.eyesDistance();
                // 换算出预览图片和屏幕显示区域的比例参数
                float scale_x = mWidth / 500;
                float scale_y = mHeight / 600;
                Log.e("eyesDistance=", eyesDistance + "");
                Log.e("midPoint.x=", midPoint.x + "");
                Log.e("midPoint.y=", midPoint.y + "");
                // 因为拍摄的相片跟实际显示的图像是镜像关系，所以在图片上获取的两眼中间点跟手机上显示的是相反方向
                canvas.drawRect((int) (240 - midPoint.x - eyesDistance)
                                * scale_x, (int) (midPoint.y * scale_y),
                        (int) (240 - midPoint.x + eyesDistance) * scale_x,
                        (int) (midPoint.y + 3 * eyesDistance) * scale_y, paint);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }
}
