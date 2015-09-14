package com.xiaohuai.lineview.tools;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class CoordinateBase extends View {
	private final Boolean isLog = false;
	private Context context;
	protected Rect mRect = new Rect(0, 0, 720, 1280);
	private float ratioX, ratioY;
	private ArrayList<ArrayList<CoorPoint>> pointArr = new ArrayList<ArrayList<CoorPoint>>();
	private int maxValue = 250, minValue = 0;
	private float xTemp, yTemp;
	private float xAreaHeight;
	protected float density;
	private float marginLength;
	private Boolean displaySelectLine = false;
	private float xInterval;
	private float pointSize;
	private int lastSelectPosition = -1;

	public CoordinateBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initParams(context);
	}

	public CoordinateBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		initParams(context);
	}

	public CoordinateBase(Context context) {
		super(context);
		initParams(context);
	}

	private void initParams(Context context) {
		this.context = context;
		this.density = context.getResources().getDisplayMetrics().density;
		int[] params = extremeSetting();
		if (params != null && params.length == 2) {
			minValue = params[0];
			maxValue = params[1];
		}
		if (isLog) {
			Log.i("Jiaqi", "minValue = " + minValue);
			Log.i("Jiaqi", "maxValue = " + maxValue);
		}
		this.setClickable(true);
		this.setOnTouchListener(mOnTouchListener);
	}

	protected abstract int[] extremeSetting();

	protected abstract float xAreaHeightSetting();

	protected abstract float marginLengthSetting();

	protected abstract ArrayList<ArrayList<CoorPoint>> dataInput();

	protected abstract void drawLine(Canvas canvas, float ratioX, float ratioY);

	protected abstract void drawAreas(Canvas canvas, float ratioX, float ratioY);
	
	protected abstract void drawSelectLine(Canvas canvas, float ratioX, float ratioY,int selectPosition);

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (isLog) {
			Log.i("Jiaqi", "onMeasure");
		}

		xAreaHeight = xAreaHeightSetting();
		marginLength = marginLengthSetting();
		ratioX = this.getMeasuredWidth() * 1f / 720;
		ratioY = this.getMeasuredHeight() * 1f / 1280;

		pointArr = dataInput();

		if (pointArr != null) {
			for (int i = 0; i < pointArr.size(); i++) {
				if (pointArr.get(i) != null && pointArr.get(i).size() > 0) {
					calculateXY(pointArr.get(i));
				}
			}
		}

		if (isLog) {
			Log.i("Jiaqi", "Calculating Over");
		}
	}

	private void calculateXY(ArrayList<CoorPoint> pointArr) {
		pointSize = pointArr.get(0).getSize();
		xInterval = (720 * ratioX - 2 * pointSize - marginLength * 2) / (pointArr.size() - 1);
		float yInterval = (1280 * ratioY - pointSize - xAreaHeight) / (maxValue - minValue);
		if (isLog) {
			Log.i("Jiaqi", "xAreaHeight = " + xAreaHeight);
		}
		for (int i = 0; i < pointArr.size(); i++) {
			xTemp = marginLength + pointSize + i * xInterval;
			yTemp = 1280 * ratioY  - xAreaHeight - yInterval * pointArr.get(i).getyValue();
			pointArr.get(i).setX(xTemp);
			pointArr.get(i).setY(yTemp);
		}
	}
	
	private void searchSelectPosition(float touchX)
	{
		int position = -1;
		if(pointArr!=null&&pointArr.size()>0){
			position = (int) ((touchX - pointSize - marginLength - xInterval/2)/xInterval);
			for(int i = 0;i<pointArr.size();i++){
				if(position>=pointArr.get(i).size()){
					position = -1;
					break;
				}
			}
		}
		if(position>=0&&lastSelectPosition!=position){
			invalidate();
		}
		lastSelectPosition = position;
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				displaySelectLine = true;
				searchSelectPosition(event.getX());
				break;
			case MotionEvent.ACTION_MOVE:
				searchSelectPosition(event.getX());
				break;
			case MotionEvent.ACTION_UP:
				displaySelectLine = false;
				invalidate();
				break;

			default:
				break;
			}
			return false;
		}
	};

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isLog) {
			Log.i("Jiaqi", "onDraw");
		}

		drawAreas(canvas, ratioX, ratioY);
		drawLine(canvas, ratioX, ratioY);
		if(displaySelectLine){
			drawSelectLine(canvas, ratioX, ratioY, lastSelectPosition);
		}else{
			drawSelectLine(canvas, ratioX, ratioY, Integer.MIN_VALUE);
		}
	}
	
	public void setDisplaySelectLine(Boolean displaySelectLine) {
		this.displaySelectLine = displaySelectLine;
	}
	
	public Boolean getDisplaySelectLine() {
		return displaySelectLine;
	}
}
