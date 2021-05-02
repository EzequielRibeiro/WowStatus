package com.wows.status;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] arrayDates = new String[28];
    private int battles = 0, survived = 0, wins = 0, destroyed = 0;
    private DBAdapter dbAdapter;

    public ProgressFragment() {
        // Required empty public constructor
    }

    public static ProgressFragment newInstance(String param1, String param2) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        dbAdapter = new DBAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        AnyChartView anyChartView = v.findViewById(R.id.any_chart_view);
        // anyChartView.setBackgroundColor(getResources().getColor(R.color.background));
        anyChartView.setProgressBar(v.findViewById(R.id.progress_bar));

        List<DataEntry> seriesData = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final String id = getArguments().getString("id");
        final String country = getArguments().getString("country");
        final String url = "https://api.worldofwarships" + country + "/wows/account/statsbydate/?application_id=4f74e545dc59b664d7ae1f5397eaaf73&account_id=" + id + "&dates=XXX";
        StringBuffer stringBuffer;

        for (int i = 0; i < arrayDates.length; i++) {
            cal.add(Calendar.DATE, -1);
            arrayDates[27 - i] = sdf.format(cal.getTime());
        }

        // 0 until 9
        stringBuffer = new StringBuffer();
        for (int i = 0; i <= 9; i++) {

            stringBuffer.append(arrayDates[i].replace("-", ""));
            if (i < 9)
                stringBuffer.append("%2C");

        }
        String urlReplaced = url.replace("XXX", stringBuffer);
        requestJSON(seriesData, urlReplaced, id, 10, 0);
        Log.e("URL1:", urlReplaced);
        // 10 until 19
        stringBuffer = new StringBuffer();
        for (int i = 10; i <= 19; i++) {

            stringBuffer.append(arrayDates[i].replace("-", ""));
            if (i < 19)
                stringBuffer.append("%2C");

        }
        urlReplaced = url.replace("XXX", stringBuffer);
        requestJSON(seriesData, urlReplaced, id, 20, 10);
        Log.e("URL2:", urlReplaced);
        // 20 until 27
        stringBuffer = new StringBuffer();
        for (int i = 20; i < 28; i++) {
            stringBuffer.append(arrayDates[i].replace("-", ""));
            if (i < 28)
                stringBuffer.append("%2C");

        }
        urlReplaced = url.replace("XXX", stringBuffer);
        requestJSON(seriesData, urlReplaced, id, 28, 20);
        Log.e("URL3:", urlReplaced);
        Cartesian cartesian = AnyChart.line();
        cartesian.dataArea().background().enabled(true);
        cartesian.background().fill("#000000");
        cartesian.credits().text(" ");
        cartesian.credits().enabled(false);

        cartesian.animation(true);
        cartesian.padding(5d, 10d, 2d, 10d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title(dbAdapter.getMensagemTranslated(61));

        cartesian.yAxis(0).title(dbAdapter.getMensagemTranslated(62));
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
        Mapping series4Mapping = set.mapAs("{ x: 'x', value: 'value4' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name(dbAdapter.getMensagemTranslated(21));
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name(dbAdapter.getMensagemTranslated(38));
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name(dbAdapter.getMensagemTranslated(22));
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series4 = cartesian.line(series4Mapping);
        series4.name(dbAdapter.getMensagemTranslated(36));
        series4.hovered().markers().enabled(true);
        series4.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series4.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);


        return v;
    }

    private void requestJSON(List<DataEntry> seriesData, String url, String id, int rangeDateMax, int rangeDateMin) {
        HttpGetRequest getRequest = new HttpGetRequest(getContext());
        String result;


        try {
            result = getRequest.execute(url).get();
            JSONObject jsonObject = new JSONObject(result);
            JSONObject objData = (JSONObject) jsonObject.get("data");
            JSONObject objIdPlayer = (JSONObject) objData.get(id);
            JSONObject objPVP = (JSONObject) objIdPlayer.get("pvp");
            Log.e("result:", result);

            for (int i = rangeDateMin; i < rangeDateMax; i++) {

                if (objPVP.has(arrayDates[i].replace("-", ""))) {
                    JSONObject json = objPVP.getJSONObject(arrayDates[i].replace("-", ""));

                    if (battles == 0) {
                        battles = Integer.valueOf(json.getString("battles"));
                        survived = Integer.valueOf(json.getString("survived_battles"));
                        wins = Integer.valueOf(json.getString("wins"));
                        destroyed = Integer.valueOf(json.getString("frags"));

                    } else {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = dateFormat.parse(arrayDates[i].replace("-", "/"));
                        seriesData.add(new CustomDataEntry(java.text.DateFormat.getDateInstance().format(date), Integer.valueOf(json.getString("battles")) - battles, Integer.valueOf(json.getString("survived_battles")) - survived,
                                Integer.valueOf(json.getString("wins")) - wins, Integer.valueOf(json.getString("frags")) - destroyed));

                        battles = Integer.valueOf(json.getString("battles"));
                        survived = Integer.valueOf(json.getString("survived_battles"));
                        wins = Integer.valueOf(json.getString("wins"));
                        destroyed = Integer.valueOf(json.getString("frags"));
                    }


                }
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            Toast.makeText(getContext(), dbAdapter.getMensagemTranslated(67), Toast.LENGTH_LONG).show();
            seriesData.add(new CustomDataEntry("", 0.0, 0.0,
                    0.0, 0.0));
            e.printStackTrace();
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if(dbAdapter != null)
            dbAdapter.close();
        super.onDestroy();
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3, Number value4) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
            setValue("value4", value4);
        }

    }

}