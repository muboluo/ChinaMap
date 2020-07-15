package com.example.chinamap.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chinamap.PathParser;
import com.example.chinamap.ProvinceItem;
import com.example.chinamap.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapView extends View {

    ArrayList<ProvinceItem> provinceList = new ArrayList();
    private Context context;
    private RectF totalRect;
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFF90CBB1};
    private float scale = 1.0f;
    private Paint paint;
    private ProvinceItem selectItem;

    public static final String TAG = MapView.class.getSimpleName();

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        loadResourcesThread.start();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (totalRect != null) {

            scale = (float) width / totalRect.width();

        }
        Log.e(TAG, "on measure scale" + scale);

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (provinceList == null) {
            return;
        }

        canvas.save();
        Log.e(TAG, "on draw scale" + scale);
        canvas.scale(scale, scale);
        for (ProvinceItem item :
                provinceList) {
            if (item != selectItem) {
                item.drawItem(canvas, paint, false);
            }
        }
        if (selectItem != null) {
            selectItem.drawItem(canvas, paint, true);
        }
    }

    Thread loadResourcesThread = new Thread() {

        @Override
        public void run() {
            if (context == null) {
                return;
            }
            final InputStream inputStream = context.getResources().openRawResource(R.raw.chinahigh);
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder;
            try {
                documentBuilder = documentBuilderFactory.newDocumentBuilder();

                final Document doc = documentBuilder.parse(inputStream);
                final Element rootElement = doc.getDocumentElement();
                final NodeList pathItems = rootElement.getElementsByTagName("path");

                float left = -1, right = -1, top = -1, bottom = -1;
                for (int i = 0; i < pathItems.getLength(); ++i) {
                    Element element = (Element) pathItems.item(i);
                    String pathData = element.getAttribute("android:pathData");
                    final Path path = PathParser.createPathFromPathData(pathData);
                    ProvinceItem item = new ProvinceItem(path);
                    int color = getProvinceDrawColor(i);
                    item.setDrawColor(color);

                    provinceList.add(item);

                    RectF rect = new RectF();

                    path.computeBounds(rect, true);

                    left = left == -1 ? rect.left : Math.min(left, rect.left);
                    top = top == -1 ? rect.top : Math.min(top, rect.top);
                    right = right == -1 ? rect.right : Math.max(right, rect.right);
                    bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);

                }
                totalRect = new RectF(left, top, right, bottom);
                handler.sendEmptyMessage(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private int getProvinceDrawColor(int i) {
        int color = Color.WHITE;
        int flag = i % 4;
        switch (flag) {
            case 1:
                color = colorArray[0];
                break;
            case 2:
                color = colorArray[1];
                break;
            case 3:
                color = colorArray[2];
                break;
            default:
                color = Color.CYAN;
                break;

        }
        return color;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {

            if (provinceList == null) {
                return;
            }
            Log.e(TAG, "handle message ,postinvalidate");
            requestLayout();
            postInvalidate();

        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        handleTouch(event);
        Log.e(TAG, "handle event" + event.getAction());
        return super.onTouchEvent(event);
    }

    private void handleTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e(TAG, "handle event" + event.getAction());

            for (ProvinceItem item :
                    provinceList) {
                if (item.isTouch(event.getX() / scale, event.getY() / scale)) {
                    selectItem = item;
                    break;
                }
            }
            if (selectItem != null) {
                postInvalidate();
            }
        }
    }

}

