package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.chart.BarChart.Type;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.views.WhooingGraph;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public final class TestFragment2 extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    public static TestFragment2 newInstance(String content) {
        TestFragment2 fragment = new TestFragment2();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            builder.append(content).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();

        return fragment;
    }

    private String mContent = "???";
	private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.activity_sample2, null);
    	
        return view;
    }
    
    

    @Override
	public void onResume() {
    	LinearLayout llBody = (LinearLayout) (mActivity.findViewById(R.id.test1));
    	/*WhooingGraph wg = new WhooingGraph();
    	wg.showGraph2(mActivity);*/
    	showGraph(llBody);
		super.onResume();
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
    
    @Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}
    
    public void showGraph2(){
    	
    }

	public void showGraph(LinearLayout llBody) {
		// 표시할 수치값
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200,
				22030, 21200, 19500, 15500, 12600, 14000 });
		values.add(new double[] { 1423, 1230, 1240, 1544, 1500, 1900,
				2230, 2100, 1900, 1500, 1200, 1400 });

		/** 그래프 출력을 위한 그래픽 속성 지정객체 */
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2	);

		// 상단 표시 제목과 글자 크기
		renderer.setChartTitle("2011년도 판매량");
		renderer.setChartTitleTextSize(20);

		// 분류에 대한 이름
		String[] titles = new String[] { "월별 판매량", "aaa" };

		// 항목을 표시하는데 사용될 색상값
		int[] colors = new int[] { Color.BLACK , Color.RED};

		// 분류명 글자 크기 및 각 색상 지정
		renderer.setLegendTextSize(15);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}

		// X,Y축 항목이름과 글자 크기
		renderer.setXTitle("월");
		renderer.setYTitle("판매량");
		renderer.setAxisTitleTextSize(12);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setMarginsColor(Color.argb(0x00, 0x01, 	0x01, 0x01));

		// 수치값 글자 크기 / X축 최소,최대값 / Y축 최소,최대값
		renderer.setLabelsTextSize(10);
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(12.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(24000);

		// X,Y축 라인 색상
		renderer.setAxesColor(Color.RED);
		// 상단제목, X,Y축 제목, 수치값의 글자 색상
		renderer.setLabelsColor(Color.CYAN);

		// X축의 표시 간격
		renderer.setXLabels(12);
		// Y축의 표시 간격
		renderer.setYLabels(5);

		// X,Y축 정렬방향
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		// X,Y축 스크롤 여부 ON/OFF
		renderer.setPanEnabled(false, false);
		// ZOOM기능 ON/OFF
		renderer.setZoomEnabled(false, false);
		// ZOOM 비율
		renderer.setZoomRate(1.0f);
		// 막대간 간격
		renderer.setBarSpacing(0.5f);
		renderer.setInScroll(true);

		// 설정 정보 설정
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for (int i = 0; i < titles.length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}

		// 그래프 객체 생성
		GraphicalView gv = ChartFactory.getBarChartView(mActivity, dataset,
				renderer, Type.STACKED);
//		GraphicalView gv = ChartFactory.getLineChartView(mActivity, dataset, renderer);
		//gv = ChartFactory.getLineChartIntent(mActivity, dataset, renderer, "");

//		gv.setBackgroundColor(Color.WHITE);
		// 그래프를 LinearLayout에 추가
		// LinearLayout llBody = (LinearLayout)
		// mActivity.findViewById(R.id.testLayout);
		llBody.addView(gv);
	}

}
