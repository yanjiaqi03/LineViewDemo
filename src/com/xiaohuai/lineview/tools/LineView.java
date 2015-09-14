package com.xiaohuai.lineview.tools;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;

public class LineView extends CoordinateBase {
	/** 样式相关参数,可修改 **/
	private final int maxValue = 300;
	private final int minValue = 0;
	private float areasHeight;
	private float textSize;
	private int[] lineColors = null;

	/** 其余参数,请勿修改 **/
	private final Boolean isLog = false;
	private Context context;
	private ArrayList<ArrayList<CoorPoint>> data = null;
	private DataEnterListener mDataListener = null;
	private SelectListener mSelectListener = null;

	private Paint coorPaint = null, textPaint = null, linePaint = null, pointPaint = null;

	public LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		/** 坐标轴高度 坐标文字大小 **/
		this.areasHeight = 50 * density;
		this.textSize = 11 * density;
		initPaint();
	}

	private void initPaint() {
		this.coorPaint = new Paint();
		this.coorPaint.setAntiAlias(true);
		this.coorPaint.setColor(0xff999999);
		this.coorPaint.setStrokeWidth(0.5f * density);
		this.coorPaint.setAlpha(255);
		this.coorPaint.setStyle(Paint.Style.FILL_AND_STROKE);

		this.textPaint = new Paint();
		this.textPaint.setAntiAlias(true);
		this.textPaint.setColor(0xff999999);
		this.textPaint.setAlpha(255);
		this.textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		this.textPaint.setTypeface(Typeface.DEFAULT);
		this.textPaint.setTextSize(textSize);
		this.textPaint.setTextAlign(Align.CENTER);

		this.linePaint = new Paint();
		this.linePaint.setAntiAlias(true);
		this.linePaint.setColor(0xff999999);
		this.linePaint.setStrokeWidth(1f * density);
		this.linePaint.setAlpha(255);
		this.linePaint.setStyle(Paint.Style.FILL_AND_STROKE);

		this.pointPaint = new Paint();
		this.pointPaint.setAntiAlias(true);
		this.pointPaint.setColor(0xff999999);
		this.pointPaint.setStrokeWidth(0f);
		this.pointPaint.setAlpha(255);
		this.pointPaint.setStyle(Paint.Style.FILL);
	}

	public void setPaintColor(int coorColor, int[] lineColors) {
		this.coorPaint.setColor(coorColor);
		this.textPaint.setColor(coorColor);
		this.lineColors = lineColors;

		invalidate();
	}

	@Override
	protected int[] extremeSetting() {
		int[] extreme = new int[2];
		extreme[0] = minValue;// minValue
		extreme[1] = maxValue;// maxValue

		return extreme;
	}

	@Override
	protected float xAreaHeightSetting() {
		return areasHeight;
	}

	@Override
	protected float marginLengthSetting() {
		return 20 * density;
	}

	@Override
	protected ArrayList<ArrayList<CoorPoint>> dataInput() {
		data = null;
		if (mDataListener != null) {
			data = mDataListener.setData();
		}
		return data;
	}

	@Override
	protected void drawLine(Canvas canvas, float ratioX, float ratioY) {
		if (data != null) {
			for (int index = 0; index < data.size(); index++) {
				ArrayList<CoorPoint> dataItem = data.get(index);
				if (lineColors != null && index < lineColors.length) {
					linePaint.setColor(lineColors[index]);
				}
				for (int i = 0; i < dataItem.size() - 1; i++) {
					/** drawPoint **/
					if (dataItem.get(i).getyValue() == Integer.MAX_VALUE) {
						continue;
					}
					pointPaint.setColor(dataItem.get(i).getPointColor());
					canvas.drawCircle(dataItem.get(i).getX(), dataItem.get(i).getY(), dataItem.get(i).getSize(),
							pointPaint);
					/** drawLine **/
					if (dataItem.get(i).getyValue() == Integer.MAX_VALUE||dataItem.get(i+1).getyValue() == Integer.MAX_VALUE) {
						continue;
					}
					canvas.drawLine(dataItem.get(i).getX(), dataItem.get(i).getY(), dataItem.get(i + 1).getX(),
							dataItem.get(i + 1).getY(), linePaint);
				}
				/** the last one point **/
				if (dataItem.size() - 1 >= 0) {
					if (dataItem.get(dataItem.size() - 1).getyValue() !=  Integer.MAX_VALUE) {
						pointPaint.setColor(dataItem.get(dataItem.size() - 1).getPointColor());
						canvas.drawCircle(dataItem.get(dataItem.size() - 1).getX(),
								dataItem.get(dataItem.size() - 1).getY(), dataItem.get(dataItem.size() - 1).getSize(),
								pointPaint);
					}
				}
			}
		}
	}

	@Override
	protected void drawAreas(Canvas canvas, float ratioX, float ratioY) {
		if (areasHeight > 0) {
			if (isLog) {
				Log.i("Jiaqi", "LineView->mRect.width = " + mRect.width());
				Log.i("Jiaqi", "LineView->mRect.height = " + mRect.height());
				Log.i("Jiaqi", "LineView->ratioX = " + ratioX);
				Log.i("Jiaqi", "LineView->ratioY = " + ratioY);
			}
			canvas.drawLine(0f, mRect.height() * ratioY - areasHeight, mRect.width() * ratioX,
					mRect.height() * ratioY - areasHeight, coorPaint);
			if (data.size() > 0) {
				ArrayList<CoorPoint> dataItem = data.get(0);
				if (dataItem != null) {
					for (int i = 0; i < dataItem.size(); i++) {
						canvas.drawText(dataItem.get(i).getxValue(), dataItem.get(i).getX(),
								mRect.height() * ratioY - areasHeight + textSize * 3 / 2, textPaint);
					}
				}
			}
		}
	}

	@Override
	protected void drawSelectLine(Canvas canvas, float ratioX, float ratioY, int selectPosition) {
		if (selectPosition >= 0) {
			canvas.drawLine(data.get(0).get(selectPosition).getX(), 0, data.get(0).get(selectPosition).getX(),
					1280 * ratioY - areasHeight, coorPaint);
		}

		if (mSelectListener != null) {
			mSelectListener.getPosition(selectPosition);
		}
	}

	/** 接口 **/
	public interface DataEnterListener {
		public ArrayList<ArrayList<CoorPoint>> setData();
	}

	public void setDataEnterListener(DataEnterListener mListener) {
		this.mDataListener = mListener;
	}

	public interface SelectListener {
		public void getPosition(int position);
	}

	public void setSelectListener(SelectListener mListener) {
		this.mSelectListener = mListener;
	}
}
