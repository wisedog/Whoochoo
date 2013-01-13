package net.wisedog.android.whooing.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.wisedog.android.whooing.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;

public class WhooingGraph {
	protected Date[] mMtDateValues = null;
	protected double mMtMinY = 0.0f;
	protected double mMtMaxY = 0.0f;
	List<Double> mValue1 = new ArrayList<Double>();
	List<Double> mValue2 = new ArrayList<Double>();
	List<Double> mValue3 = new ArrayList<Double>();

	public boolean decodeMountainData(JSONArray data) {
		mMtDateValues =  new Date[data.length()];
		Calendar cal = Calendar.getInstance();
		int todayDay = cal.get(Calendar.DATE);
		for (int i = 0; i < data.length(); i++) {
			try {
				JSONObject obj = (JSONObject) data.get(i);
				double value = obj.getDouble("liabilities");
				double value1 = obj.getDouble("capital");
				double value2 = obj.getDouble("assets");
				mMtMaxY = getMax(mMtMaxY, value, value1, value2);
				mMtMinY = getMin(mMtMinY, value, value1, value2);
				mValue1.add(value);
				mValue2.add(value1);
				mValue3.add(value2);
				int dateInt = obj.getInt("date");
				if(i == data.length()-1){
					cal.set(dateInt / 100, dateInt % 100, todayDay);
				}else{
					cal.set(dateInt / 100, dateInt % 100, Calendar.DAY_OF_MONTH);
				}
				
				Date date = cal.getTime();
				mMtDateValues[i] = date;
				

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * @param mMtMaxY2
	 * @param value
	 * @param value1
	 * @param value2
	 * @return
	 */
	private double getMin(double minY, double value, double value1,
			double value2) {
		double min = Double.MAX_VALUE;
		if (value <= min) {
			min = value;
		}
		if (value1 <= min) {
			min = value1;
		}
		if (value2 <= min) {
			min = value2;
		}
		if (minY <= min) {
			min = minY;
		}
		return min;
	}

	/**
	 * @param mMtMaxY2
	 * @param value
	 * @param value1
	 * @param value2
	 * @return
	 */
	private double getMax(double maxY, double value, double value1,
			double value2) {
		double max = Double.MIN_VALUE;
		if (value >= max) {
			max = value;
		}
		if (value1 >= max) {
			max = value1;
		}
		if (value2 >= max) {
			max = value2;
		}
		if (maxY >= max) {
			max = maxY;
		}
		return max;
	}

	public double[] toPrimitive(List<Double> list) {
		double[] result = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	/**
	 * Draw graphs with given data. Data is like below:
	 * "results":{"rows_type":"month"
	 * ,"aggregate":{"capital":11593688,"liabilities"
	 * :814335,"assets":12408023,"goal":0},
	 * "rows":[{"liabilities":0,"assets":0,"date"
	 * :201206,"capital":0,"goal":null},
	 * {"liabilities":0,"assets":0,"date":201207,"capital":0,"goal":null},
	 * {"liabilities":0,"assets":0,"date":201208,"capital":0,"goal":null},
	 * {"liabilities":0,"assets":0,"date":201209,"capital":0,"goal":null},
	 * {"liabilities"
	 * :567890,"assets":12395678,"date":201210,"capital":11827788,"goal":null},
	 * {"liabilities":694335,"assets":12408023,"date":201211,"capital":11713688,
	 * "goal":null},
	 * {"liabilities":814335,"assets":12408023,"date":201212,"capital"
	 * :11593688,"goal":null}]}
	 * 
	 * @param activity
	 *            Activity
	 * @param data
	 *            Graph data
	 * */
	public void showGraph(Activity activity, LinearLayout insertLayout,
			JSONArray data) {
		String[] titles = new String[] { "liabilities", "capital" };
		this.decodeMountainData(data);
		List<Date[]> dates = new ArrayList<Date[]>();
		List<double[]> values = new ArrayList<double[]>();
		
		dates.add(mMtDateValues);
		dates.add(mMtDateValues);
		

		values.add(toPrimitive(mValue1));
		values.add(toPrimitive(mValue2));
		int[] colors = new int[] { Color.RED, Color.BLUE };
		PointStyle[] styles = new PointStyle[] { PointStyle.SQUARE,
				PointStyle.DIAMOND };
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
		setRenderer(renderer, colors, styles);
		setChartSettings(renderer, "Mountain", "Date", "",
				mMtDateValues[0].getTime(),
				mMtDateValues[mMtDateValues.length - 1].getTime(), getMtMinY(),
				getMtMaxY(), Color.GRAY, Color.LTGRAY);
		renderer.setYLabels(6);
		renderer.setXLabels(6);
		renderer.setShowGrid(true);
		renderer.setInScroll(true);
		renderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer
					.getSeriesRendererAt(i);
			seriesRenderer.setDisplayChartValues(true);
			seriesRenderer.setLineWidth(3f);
			seriesRenderer.setFillPoints(true);
		}

		GraphicalView gv = ChartFactory.getTimeChartView(activity,
				buildDateDataset(titles, dates, values), renderer, "yyyy.MMM");
		insertLayout.addView(gv);
	}

	protected XYMultipleSeriesDataset buildDateDataset(String[] titles,
			List<Date[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	public void showGraph2(Activity activity) {
		String[] titles = new String[] { "Air temperature", "Sunshine hours" };
		List<double[]> x = new ArrayList<double[]>();
		for (int i = 0; i < titles.length; i++) {
			x.add(new double[] { 2, 4, 6, 8, 10, 12 });
		}
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4 });
		values.add(new double[] { 4.3, 4.9, 5.9, 8.8, 10.8, 11.9 });
		int[] colors = new int[] { Color.BLUE, Color.RED };
		PointStyle[] styles = new PointStyle[] { PointStyle.SQUARE,
				PointStyle.DIAMOND, };
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
		setRenderer(renderer, colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = (XYSeriesRenderer) renderer
					.getSeriesRendererAt(i);
			r.setLineWidth(3f);
		}
		setChartSettings(renderer, "Average temperature", "Month",
				"Temperature", 0.5, 12.5, 0, 32, Color.LTGRAY, Color.LTGRAY);
		renderer.setXLabels(6);
		renderer.setYLabels(6);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(false);
		renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		renderer.setZoomRate(1.05f);
		renderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
		renderer.setInScroll(true);

		renderer.setLabelsColor(Color.BLACK);
		renderer.setXLabelsColor(Color.DKGRAY);
		renderer.setYLabelsColor(0, Color.DKGRAY);
		// renderer.setYLabelsColor(1, colors[1]);

		renderer.setYTitle("Hours", 1);
		renderer.setYAxisAlign(Align.RIGHT, 1);
		renderer.setYLabelsAlign(Align.LEFT, 1);

		renderer.addYTextLabel(20, "Test", 0);
		// renderer.addYTextLabel(10, "New Test", 1);

		XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
		/*
		 * values.clear(); values.add(new double[] { 4.3, 4.9, 5.9, 8.8, 10.8,
		 * 11.9 }); addXYSeries(dataset, new String[] { "Sunshine hours" }, x,
		 * values, 1);
		 */
		// Intent intent = ChartFactory.getCubicLineChartIntent(this, dataset,
		// renderer, 0.3f,"Average temperature");
		GraphicalView gv = ChartFactory.getLineChartView(activity, dataset,
				renderer);
		// 그래프를 LinearLayout에 추가
		LinearLayout llBody = (LinearLayout) activity.findViewById(R.id.test1);
		llBody.addView(gv);
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}

	/**
	 * Builds an XY multiple dataset using the provided values.
	 * 
	 * @param titles
	 *            the series titles
	 * @param xValues
	 *            the values for the X axis
	 * @param yValues
	 *            the values for the Y axis
	 * @return the XY multiple dataset
	 */
	protected XYMultipleSeriesDataset buildDataset(String[] titles,
			List<double[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		addXYSeries(dataset, titles, xValues, yValues, 0);
		return dataset;
	}

	public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
			List<double[]> xValues, List<double[]> yValues, int scale) {
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i], scale);
			double[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
	}

	public double getMtMaxY() {
		return mMtMaxY;
	}

	public double getMtMinY() {
		return mMtMinY;
	}
}
