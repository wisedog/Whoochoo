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
import android.util.Log;
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
				Log.d("wisedog", "Data \n" + data.toString());
				return false;
			}
		}
		return true;
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
	 * @param activity
	 *            Activity
	 * @param data
	 *            Graph data
	 * */
	public boolean showGraph(Activity activity, LinearLayout insertLayout,
			JSONArray data) {
		String[] titles = new String[] { "liabilities", "capital" };
		if(this.decodeMountainData(data) == false){
		    return false;
		}
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
		
		return true;
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
