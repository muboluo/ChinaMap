package com.example.chinamap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

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
            paint.clearShadowLayer();
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawColor);
            canvas.drawPath(path, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(strokeColor);
            canvas.drawPath(path, paint);
        } else {

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
    }

}
