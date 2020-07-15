package com.example.chinamap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

public class ProvinceItem {

    public Path path;
    private int drawColor;

    private int strokeColor = 0xFFD0E8F4;

    public ProvinceItem(Path path) {
        this.path = path;
    }

    public void setDrawColor(int color) {
        this.drawColor = color;
    }

    public void drawItem(Canvas canvas, Paint paint, boolean isSelect) {

        if (isSelect) {
            drawSelectItem(canvas, paint);
        } else {

            drawUnSelectItem(canvas, paint);
        }
    }

    private void drawSelectItem(Canvas canvas, Paint paint) {
        paint.clearShadowLayer();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(drawColor);
        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        canvas.drawPath(path, paint);
    }

    private void drawUnSelectItem(Canvas canvas, Paint paint) {
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(8, 0, 0, 0xffffff);
        canvas.drawPath(path, paint);
        //填充背景色
        paint.clearShadowLayer();
        paint.setColor(drawColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        canvas.drawPath(path, paint);
    }

    // 判断当前点击区域是否在map中。使用region来判断。
    public boolean isTouch(float x, float y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region region = new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
        region.setPath(path, region);

        return region.contains((int) x, (int) y);
    }

}
