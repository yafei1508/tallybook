package com.lyf.tallybook.acticity;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lyf.tallybook.MyApplication;
import com.lyf.tallybook.R;
import com.lyf.tallybook.model.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class StatisticsActivity extends AppCompatActivity {

    private String[] categorysName;
    private double[] categorysInNum;
    private double[] categorysOutNum;
    private double sumIn = 0;
    private double sumOut = 0;
    private MyApplication application;
    private List<Record> records;

    private PieChart inPieChart;
    private PieChart outPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("账目统计");
        application = (MyApplication) getApplication();
        categorysName = getResources().getStringArray(R.array.category);
        categorysInNum = new double[categorysName.length];
        for (int i = 0; i < categorysName.length; i++) {
            categorysInNum[i] = 0;
        }
        categorysOutNum = new double[categorysName.length];
        for (int i = 0; i < categorysName.length; i++) {
            categorysOutNum[i] = 0;
        }
        BmobQuery<Record> query = new BmobQuery<>();
        query.addWhereEqualTo("userName", application.getUser().getUserName());
        query.findObjects(new FindListener<Record>() {
            @Override
            public void done(List<Record> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSign()) {
                            categorysOutNum[list.get(i).getCategory()] += list.get(i).getMoney();
                            sumOut += list.get(i).getMoney();
                            Log.d("categorysOutNum", String.valueOf(categorysOutNum[list.get(i).getCategory()]));
                        } else {
                            categorysInNum[list.get(i).getCategory()] += list.get(i).getMoney();
                            sumIn += list.get(i).getMoney();
                        }
                    }
                    initPieChart();
                } else {
                    Toast.makeText(getApplicationContext(), "数据获取出错", Toast.LENGTH_SHORT).show();
                    Log.i("Bmob", e.getMessage() + e.getErrorCode());
                }
            }
        });


//
//        List<PieEntry> strings = new ArrayList<>();
//        strings.add(new PieEntry(30f,"aaa"));
//        strings.add(new PieEntry(70f,"bbb"));
//
//        PieDataSet dataSet = new PieDataSet(strings,"Label");
//
//        PieData pieData = new PieData(dataSet);
//        pieData.setDrawValues(true);
//
//        inPieChart.setData(pieData);
//        inPieChart.invalidate();


    }

    public void initPieChart() {
        inPieChart = findViewById(R.id.in_piechart);
        outPieChart = findViewById(R.id.out_piechart);

        ArrayList<PieEntry> inPieChartList = new ArrayList<>();
        ArrayList<PieEntry> outPieChartList = new ArrayList<>();
        for (int i = 0; i < categorysName.length; i++) {
            Log.i("testttttin", String.valueOf(categorysInNum[i]));
            Log.i("testttttout", String.valueOf(categorysOutNum[i]));
            inPieChartList.add(new PieEntry((float) categorysInNum[i], categorysName[i]));
            outPieChartList.add(new PieEntry((float) categorysOutNum[i], categorysName[i]));
        }

        PieDataSet inPieDataSet = new PieDataSet(inPieChartList, "inPieData");
        PieDataSet outPieDataSet = new PieDataSet(outPieChartList, "outPieData");

        PieData inPieData = new PieData(inPieDataSet);
        PieData outPieData = new PieData(outPieDataSet);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        inPieDataSet.setColors(colors);
        outPieDataSet.setColors(colors);
        inPieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        outPieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        inPieDataSet.setSliceSpace(5f);
        outPieDataSet.setSliceSpace(5f);
        inPieChart.setCenterText("总收入\n" + String.valueOf(sumIn));
        outPieChart.setCenterText("总支出\n" + String.valueOf(sumIn));
        inPieChart.setDrawEntryLabels(false);
        outPieChart.setDrawEntryLabels(false);
        inPieChart.setData(inPieData);
        outPieChart.setData(outPieData);
        Legend inLegend = inPieChart.getLegend();
        inLegend.setWordWrapEnabled(true);
        inLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_INSIDE);

        Legend outLegend = outPieChart.getLegend();
        outLegend.setWordWrapEnabled(true);
        outLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_INSIDE);
        inPieChart.invalidate();
        outPieChart.invalidate();

    }
}

