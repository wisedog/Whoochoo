package net.wisedog.android.whooing.views;

import java.util.ArrayList;
import java.util.List;

import net.wisedog.android.whooing.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;

public class WhooingGraph {
    /*
    public void showGraph(){
        MountainGraph gv = new MountainGraph(this);
        gv.initAndDraw(new float[]{4444444.0f,5555555.0f,6666666.0f}, 
                new float[]{333333.0f,222222.0f,123456.0f});
        LinearLayout llBody = (LinearLayout) findViewById(R.id.test_layout);
        llBody.addView(gv);
    }
    
    public void showGraph2(){
        String[] titles = new String[] { "Air temperature" };
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
          x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
        }
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,
            13.9 });
        int[] colors = new int[] { Color.BLUE, Color.YELLOW };
        PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT };
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
        setRenderer(renderer, colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
          XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
          r.setLineWidth(3f);
        }
        setChartSettings(renderer, "Average temperature", "Month", "Temperature", 0.5, 12.5, 0, 32,
            Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
        renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
        renderer.setZoomRate(1.05f);

        renderer.setLabelsColor(Color.WHITE);
        renderer.setXLabelsColor(Color.GREEN);
        renderer.setYLabelsColor(0, colors[0]);
        renderer.setYLabelsColor(1, colors[1]);

        renderer.setYTitle("Hours", 1);
        renderer.setYAxisAlign(Align.RIGHT, 1);
        renderer.setYLabelsAlign(Align.LEFT, 1);

        renderer.addYTextLabel(20, "Test", 0);
        renderer.addYTextLabel(10, "New Test", 1);

        XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
        values.clear();
        values.add(new double[] { 4.3, 4.9, 5.9, 8.8, 10.8, 11.9, 13.6, 12.8, 11.4, 9.5, 7.5, 5.5 });
        addXYSeries(dataset, new String[] { "Sunshine hours" }, x, values, 1);
        //Intent intent = ChartFactory.getCubicLineChartIntent(this, dataset, renderer, 0.3f,"Average temperature");
        GraphicalView gv = ChartFactory.getCubeLineChartView(this, dataset, renderer, (float) 0.5);
                // 그래프를 LinearLayout에 추가
                LinearLayout llBody = (LinearLayout) findViewById(R.id.test_layout);
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
          */
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
