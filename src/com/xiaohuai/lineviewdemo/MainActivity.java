package com.xiaohuai.lineviewdemo;

import java.util.ArrayList;

import com.xiaohuai.lineview.tools.CoorPoint;
import com.xiaohuai.lineview.tools.LineView;
import com.xiaohuai.lineview.tools.LineView.DataEnterListener;
import com.xiaohuai.lineview.tools.LineView.SelectListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class MainActivity extends Activity {
	private ArrayList<ArrayList<CoorPoint>> data = new ArrayList<ArrayList<CoorPoint>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		InitViews();
	}

	private void InitViews() {
		
		/** 每个dataItem中的CoorPoint数量一定要一样!!!**/
		ArrayList<CoorPoint> dataItem = new ArrayList<CoorPoint>();
		for (int i = 0; i < 20; i++) {
			dataItem.add(new CoorPoint(8, i + "", (int) (100 + 50 * Math
					.random()), 0xffff0000, true));
		}
		data.add(dataItem);

		dataItem = new ArrayList<CoorPoint>();
		for (int i = 0; i < 18; i++) {
			dataItem.add(new CoorPoint(8, i + "", (int) (200 + 50 * Math
					.random()), 0xff0000ff, true));
		}
		/*if yvalue == Integer.MAX_VALUE, this point and line(s)(left or right or both) will be not shown*/
		dataItem.add(new CoorPoint(8, 19 + "", Integer.MAX_VALUE, 0xff0000ff, true));
		dataItem.add(new CoorPoint(8, 20 + "", (int) (200 + 50 * Math
				.random()), 0xff0000ff, true));
		data.add(dataItem);

		LineView line = (LineView) findViewById(R.id.lineview);
		line.setPaintColor(0xff999999, new int[] { 0xffff0000, 0xff0000ff });
		line.setDataEnterListener(new DataEnterListener() {

			@Override
			public ArrayList<ArrayList<CoorPoint>> setData() {

				return data;
			}
		});
		line.setSelectListener(new SelectListener() {

			@Override
			public void getPosition(int position) {
				//if position < 0, it means touch up. else touch down
				Log.i("XiaoHuai", "selectPosition = " + position);
			}
		});
	}
}
