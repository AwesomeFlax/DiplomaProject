package diploma.college.diplomaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

public class CalendarActivity extends Activity
{
    //данные за день
    int work_per_day;
    int relax_per_day;
    int study_per_day;
    int meeting_per_day;
    int lunch_per_day;

    int quantity = 0;

    ProgressBar p_work;
    ProgressBar p_relax;
    ProgressBar p_study;
    ProgressBar p_meeting;
    ProgressBar p_lunch;

    String selected_day = "0";
    String selected_month = "0";
    String selected_year = "0";

    String[] data;

    String CurrentUser;
    Spinner spinner;

    Typeface bold;
    Typeface regular;
    Typeface medium;

    public void getProjectList (String user)
    {
        int k = 0;
        InputStream is = null ;
        String result = "";

        String url_select = "http://iskusstvo-mama.kh.ua/denisdiploma/get_projects.php";

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
            String line;

            while((line = br.readLine())!= null)
                sb.append(line).append("\n");

            is.close();
            result = sb.toString();

        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e("log_tag", "Error parsing data "+e.toString());
        }

        try
        {
            JSONArray Jarray = new JSONArray(result);
            for (int i = 0; i < Jarray.length(); i++)
            {
                JSONObject Jasonobject;
                Jasonobject = Jarray.getJSONObject(i);

                String name = Jasonobject.getString("name");

                if (name.equals(user))
                {
                    data[k++] = Jasonobject.getString("project");
                }

            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e("log_tag", "Error! " + e.toString());
        }
    }

    public void getProjectCount (String user)
    {
        InputStream is = null ;
        String result = "";

        String url_select = "http://iskusstvo-mama.kh.ua/denisdiploma/get_projects.php";

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
            String line;

            while((line = br.readLine())!= null)
                sb.append(line).append("\n");

            is.close();
            result = sb.toString();

        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e("log_tag", "Error parsing data "+e.toString());
        }

        try
        {
            JSONArray Jarray = new JSONArray(result);
            for (int i = 0; i < Jarray.length(); i++)
            {
                JSONObject Jasonobject;
                Jasonobject = Jarray.getJSONObject(i);

                String name = Jasonobject.getString("name");

                if (name.equals(user))
                    quantity++;

            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e("log_tag", "Error! " + e.toString());
        }
    }

    public void createSpisochek(String CurrentUser)
    {
        quantity = 0;
        getProjectCount(CurrentUser);
        data = new String[quantity];
        getProjectList(CurrentUser);

        //выпадающий список
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // выделяем элемент
        spinner.setSelection(0);

        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // показываем позиция нажатого элемента
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                //действие
            }
        });
    }


    class task extends AsyncTask<String, String, Void>
    {
        private ProgressDialog progressDialog = new ProgressDialog(CalendarActivity.this);
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
            });
        }

        @Override
        protected Void doInBackground(String... params)
        {
            String url_select = "http://iskusstvo-mama.kh.ua/denisdiploma/get_stats_per_day.php";

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
                String line;
                while((line = br.readLine())!=null)
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

                p_lunch.setProgress(0);
                p_relax.setProgress(0);
                p_study.setProgress(0);
                p_work.setProgress(0);
                p_meeting.setProgress(0);

                for (int i = 0; i < Jarray.length(); i++)
                {
                    JSONObject Jasonobject;
                    Jasonobject = Jarray.getJSONObject(i);

                    //get an output on the screen
                    String name = Jasonobject.getString("name");
                    String rec_day = Jasonobject.getString("day");
                    String rec_month = Jasonobject.getString("month");
                    String rec_year = Jasonobject.getString("year");
                    String project = Jasonobject.getString("project");

                    if (name.equals(CurrentUser) && rec_day.equals(selected_day) && rec_month.equals(selected_month) &&  rec_year.equals(selected_year) && project.equals(spinner.getSelectedItem().toString()))
                    {
                        //получаем инфу из запроса
                        work_per_day = Jasonobject.getInt("Work");
                        relax_per_day = Jasonobject.getInt("Relax");
                        study_per_day = Jasonobject.getInt("Study");
                        meeting_per_day = Jasonobject.getInt("Meeting");
                        lunch_per_day = Jasonobject.getInt("Lunch");

                        int alltime = work_per_day + relax_per_day + study_per_day + meeting_per_day + lunch_per_day;

                        double work_progress = 100*work_per_day/alltime;
                        double relax_progress = 100*relax_per_day/alltime;
                        double study_progress = 100*study_per_day/alltime;
                        double meeting_progress = 100*meeting_per_day/alltime;
                        double lunch_progress = 100*lunch_per_day/alltime;

                        double maximum = 0;
                        if (work_progress > maximum) maximum = work_progress;
                        if (relax_progress > maximum) maximum = relax_progress;
                        if (study_progress > maximum) maximum = study_progress;
                        if (meeting_progress > maximum) maximum = meeting_progress;
                        if (lunch_progress > maximum) maximum = lunch_progress;

                        p_lunch.setMax((int) maximum);
                        p_relax.setMax((int) maximum);
                        p_study.setMax((int) maximum);
                        p_work.setMax((int) maximum);
                        p_meeting.setMax((int) maximum);

                        p_lunch.setProgress((int) lunch_progress);
                        p_relax.setProgress((int) relax_progress);
                        p_study.setProgress((int) study_progress);
                        p_work.setProgress((int) work_progress);
                        p_meeting.setProgress((int) meeting_progress);
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        //возьмем имя
        Intent intent = getIntent();
        CurrentUser = intent.getStringExtra("username");

        bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        TextView header = (TextView) findViewById(R.id.header);
        header.setTypeface(bold);

        TextView selectme = (TextView) findViewById(R.id.selectme);
        selectme.setTypeface(medium);

        //названия деятельностей шрифтуем
        TextView textView22 = (TextView) findViewById(R.id.textView22);
        textView22.setTypeface(medium);

        TextView textView23 = (TextView) findViewById(R.id.textView23);
        textView23.setTypeface(medium);

        TextView textView24 = (TextView) findViewById(R.id.textView24);
        textView24.setTypeface(medium);

        TextView textView25 = (TextView) findViewById(R.id.textView25);
        textView25.setTypeface(medium);

        TextView textView26 = (TextView) findViewById(R.id.textView26);
        textView26.setTypeface(medium);

        spinner = (Spinner) findViewById(R.id.spinner1);
        createSpisochek(CurrentUser);

        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener()
                 {
                     @Override
                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                     {
                         // показываем позиция нажатого элемента
                         new task().execute();

                         ((TextView) parent.getChildAt(0)).setTextSize(18);
                         ((TextView) parent.getChildAt(0)).setTypeface(medium);
                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> arg0) { }
                 }
                );

        //ловим объекты для анализа
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        p_work = (ProgressBar) findViewById(R.id.progressBar1);
        p_relax = (ProgressBar) findViewById(R.id.progressBar2);
        p_meeting = (ProgressBar) findViewById(R.id.progressBar3);
        p_study = (ProgressBar) findViewById(R.id.progressBar4);
        p_lunch = (ProgressBar) findViewById(R.id.progressBar5);

        CalendarView.OnDateChangeListener get_selected_date = new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                selected_day = String.valueOf(dayOfMonth);
                selected_month = String.valueOf(month + 1);
                selected_year = String.valueOf(year);

                new task().execute();
            }
        };

        calendarView.setOnDateChangeListener(get_selected_date);

        /*Calendar c = Calendar.getInstance();

        selected_day = String.valueOf(c.get(YEAR));
        selected_month = String.valueOf(c.get(MONTH) + 1);
        selected_year = String.valueOf(c.get(DAY_OF_MONTH));*/
    }
}
