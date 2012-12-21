package net.wisedog.android.whooing.views;

import java.util.ArrayList;
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

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;

public class WhooingGraph {
    public void showGraph(Activity activity){
        String[] titles = new String[] { "Sales growth" , "Fixed tickets" };
        List<Date[]> dates = new ArrayList<Date[]>();
        List<double[]> values = new ArrayList<double[]>();
        Date[] dateValues = new Date[] {
            new Date(99, 9, 1), new Date(100, 0, 1), new Date(100, 3, 1), new Date(100, 6, 1),
            new Date(100, 9, 1), new Date(100, 11, 1) };
        dates.add(dateValues);
        dates.add(dateValues);

        values.add(new double[] { 0.4, 4.5, 3.4, 4.5, 4.3, 4 });
        values.add(new double[] { 2.4, 7.5, 1.4, 2.5, 3.3, 6 });
        int[] colors = new int[] { Color.RED, Color.BLUE };
        PointStyle[] styles = new PointStyle[] { PointStyle.SQUARE, PointStyle.DIAMOND };
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
        setRenderer(renderer, colors, styles);
        setChartSettings(renderer, "Sales growth", "Date", "%", dateValues[0].getTime(),
            dateValues[dateValues.length - 1].getTime(), -4, 11, Color.GRAY, Color.LTGRAY);
        renderer.setYLabels(6);
        renderer.setXLabels(6);
        renderer.setShowGrid(true);
        renderer.setInScroll(true);
        renderer.setMarginsColor(Color.argb(0x00, 0x01,     0x01, 0x01));
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
          seriesRenderer.setDisplayChartValues(true);
          seriesRenderer.setLineWidth(3f);
          seriesRenderer.setFillPoints(true);
        }
        
        GraphicalView gv = ChartFactory.getTimeChartView(activity, buildDateDataset(titles, dates, values),
                renderer, "yyyy.MMM");
        // 그래프를 LinearLayout에 추가
        LinearLayout llBody = (LinearLayout) activity.findViewById(R.id.test1);
        llBody.addView(gv);
    }
    
    protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
            List<double[]> yValues) {
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
    
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
      }
    
    public void showGraph2(Activity activity){
        String[] titles = new String[] { "Air temperature" , "Sunshine hours"};
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
          x.add(new double[] { 2, 4, 6, 8, 10, 12 });
        }
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4});
        values.add(new double[] { 4.3, 4.9, 5.9, 8.8, 10.8, 11.9});
        int[] colors = new int[] { Color.BLUE, Color.RED };
        PointStyle[] styles = new PointStyle[] { PointStyle.SQUARE, PointStyle.DIAMOND,
 };
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
        setRenderer(renderer, colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
          XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
          r.setLineWidth(3f);
        }
        setChartSettings(renderer, "Average temperature", "Month", "Temperature", 0.5, 12.5, 0, 32,
            Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(6);
        renderer.setYLabels(6);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setZoomButtonsVisible(false);
        renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
        renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
        renderer.setZoomRate(1.05f);
        renderer.setMarginsColor(Color.argb(0x00, 0x01, 	0x01, 0x01));
        renderer.setInScroll(true);

        renderer.setLabelsColor(Color.BLACK);
        renderer.setXLabelsColor(Color.DKGRAY);
        renderer.setYLabelsColor(0, Color.DKGRAY);
        //renderer.setYLabelsColor(1, colors[1]);

        renderer.setYTitle("Hours", 1);
        renderer.setYAxisAlign(Align.RIGHT, 1);
        renderer.setYLabelsAlign(Align.LEFT, 1);

        renderer.addYTextLabel(20, "Test", 0);
        //renderer.addYTextLabel(10, "New Test", 1);

        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
/*        values.clear();
        values.add(new double[] { 4.3, 4.9, 5.9, 8.8, 10.8, 11.9 });
        addXYSeries(dataset, new String[] { "Sunshine hours" }, x, values, 1);*/
        //Intent intent = ChartFactory.getCubicLineChartIntent(this, dataset, renderer, 0.3f,"Average temperature");
        GraphicalView gv = ChartFactory.getLineChartView(activity, dataset, renderer);
                // 그래프를 LinearLayout에 추가
                LinearLayout llBody = (LinearLayout) activity.findViewById(R.id.test1);
                llBody.addView(gv);
    }
     
     protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
              String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
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
     
     protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
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
       * @param titles the series titles
       * @param xValues the values for the X axis
       * @param yValues the values for the Y axis
       * @return the XY multiple dataset
       */
      protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
          List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
      }

      public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
          List<double[]> yValues, int scale) {
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
}
