package diploma.college.diplomaproject;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StatisticsActivity extends Activity
{
    int[] pieChartValues = new int [5];
    String CurrentUser = "";

    String[] piesNames = new String [5];

    TextView label_work;
    TextView label_relax;
    TextView label_lunch;
    TextView label_meeting;
    TextView label_study;

    ImageButton extend;

    Typeface bold;
    Typeface regular;
    Typeface medium;

    public static final String TYPE = "type";
    private static int[] COLORS = new int[] { Color.argb(180, 125, 38, 205), Color.argb(180, 255, 99, 71), Color.argb(180, 74, 140, 255), Color.argb(180, 118, 212, 0), Color.argb(180, 255, 215, 0) };
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;

    class task extends AsyncTask<String, String, Void>
    {
        private ProgressDialog progressDialog = new ProgressDialog(StatisticsActivity.this);
        InputStream is = null ;
        String result = "";

        protected void onPreExecute()
        {
            progressDialog.setMessage(getResources().getString(R.string.wait));
            progressDialog.show();
            progressDialog.setOnCancelListener
                    (new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel(DialogInterface arg0)
                        {
                            task.this.cancel(true);
                        }
                    }
                    );
        }

        @Override
        protected Void doInBackground(String... params)
        {
            String url_select = "http://iskusstvo-mama.kh.ua/denisdiploma/get_spent.php";

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);

            ArrayList<NameValuePair> param = new ArrayList<>();

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is =  httpEntity.getContent();

            }
            catch (Exception e)
            {
                Log.e("log_tag", "Connection error " + e.toString());
            }
            try
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line=br.readLine())!=null)
                {
                    sb.append(line).append("\n");
                }
                is.close();
                result = sb.toString();

            } catch (Exception e)
            {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data "+e.toString());
            }

            return null;
        }

        protected void onPostExecute(Void v)
        {
            try
            {
                JSONArray Jarray = new JSONArray(result);
                for (int i = 0; i < Jarray.length(); i++)
                {
                    JSONObject Jasonobject;
                    Jasonobject = Jarray.getJSONObject(i);

                    //get an output on the screen
                    String name = Jasonobject.getString("name");

                    if (name.equals(CurrentUser))
                    {
                        pieChartValues[0] = Jasonobject.getInt("Work");
                        pieChartValues[1] = Jasonobject.getInt("Relax");
                        pieChartValues[2] = Jasonobject.getInt("Lunch");
                        pieChartValues[3] = Jasonobject.getInt("Meeting");
                        pieChartValues[4] = Jasonobject.getInt("Study");

                        piesNames[0] = getResources().getString(R.string.work);
                        piesNames[1] = getResources().getString(R.string.relax);
                        piesNames[2] = getResources().getString(R.string.lunch);
                        piesNames[3] = getResources().getString(R.string.meeting);
                        piesNames[4] = getResources().getString(R.string.study);

                        fillPieChart();
                        mChartView.repaint();

                        for (int k = 0; k < 5; k++)
                            LoadSpentTimeInEveryActivity(k);
                    }
                }
                this.progressDialog.dismiss();

            }
            catch (Exception e)
            {
                // TODO: handle exception
                Log.e("log_tag", "Error! " + e.toString());
            }
        }
    }

    void LoadSpentTimeInEveryActivity (int i)
    {
        String temp = String.valueOf(pieChartValues[i]);

        //работаем с work
        if (i == 0)
        {
            String newline = label_work.getText().toString();
            newline += ": " + temp;
            label_work.setText(newline);
        }

        //работаем с relax
        if (i == 1)
        {
            String newline = label_relax.getText().toString();
            newline += ": " + temp;
            label_relax.setText(newline);
        }

        //работаем с lunch
        if (i == 2)
        {
            String newline = label_lunch.getText().toString();
            newline += ": " + temp;
            label_lunch.setText(newline);
        }

        //работаем с meeting
        if (i == 3)
        {
            String newline = label_meeting.getText().toString();
            newline += ": " + temp;
            label_meeting.setText(newline);
        }

        //работаем с study
        if (i == 4)
        {
            String newline = label_study.getText().toString();
            newline += ": " + temp;
            label_study.setText(newline);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        extend = (ImageButton) findViewById(R.id.extended);
        extend.setOnClickListener(openCalendar);

        bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        TextView extended = (TextView) findViewById(R.id.textView);
        extended.setTypeface(bold);

        TextView wqeqwe = (TextView) findViewById(R.id.stat_header);
        wqeqwe.setTypeface(bold);

        label_work = (TextView) findViewById(R.id.textView15);
        label_relax = (TextView) findViewById(R.id.textView16);
        label_lunch = (TextView) findViewById(R.id.textView17);
        label_meeting = (TextView) findViewById(R.id.textView18);
        label_study = (TextView) findViewById(R.id.textView19);

        label_work.setTypeface(medium);
        label_relax.setTypeface(medium);
        label_lunch.setTypeface(medium);
        label_meeting.setTypeface(medium);
        label_study.setTypeface(medium);

        //возьмем имя
        Intent intent = getIntent();
        CurrentUser = intent.getStringExtra("username");

        for (int i = 0; i < 5; i++)
            pieChartValues[i] = 1;

        new task().execute();

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setLegendHeight(200);
        mRenderer.setBackgroundColor(Color.argb(100, 238, 238, 238));

        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setShowLegend(false);

        mRenderer.setChartTitleTextSize(35);
        mRenderer.setLabelsTextSize(35);
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setStartAngle (120);

        if (mChartView == null)
        {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);
            layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        }
        else
        {
            mChartView.repaint();
        }
    }

    View.OnClickListener openCalendar = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            Intent stats = new Intent(StatisticsActivity.this, CalendarActivity.class);
            stats.putExtra("username", CurrentUser);
            StatisticsActivity.this.startActivity(stats);
        }
    };

    public void fillPieChart()
    {
        for (int i = 0; i < pieChartValues.length; i++)
        {
            mSeries.add(piesNames[i], pieChartValues[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
            if (mChartView != null)
                mChartView.repaint();
        }
    }
}
