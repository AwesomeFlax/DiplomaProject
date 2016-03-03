package diploma.college.diplomaproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity
{
    int timespent = 1;
    String previous = "";
    int quantity = 0;
    String[] data;
    Spinner spinner;
    String CurrentUser = "";
    AudioManager am;

    private RadioButton Work;
    private RadioButton Study;
    private RadioButton Relax;
    private RadioButton Meeting;
    private RadioButton Lunch;

    boolean set_bluetooth = false;
    boolean set_sound = false;
    boolean set_timer = false;

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

    public void AddNoteInDB (String user, String state, int TimeSpent, String prev, String project)
    {
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("name", user));
        nameValuePairs.add(new BasicNameValuePair("activity", state));
        nameValuePairs.add(new BasicNameValuePair("TimeSpent", String.valueOf(TimeSpent)));
        nameValuePairs.add(new BasicNameValuePair("previous", prev));
        nameValuePairs.add(new BasicNameValuePair("project", project));

        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://iskusstvo-mama.kh.ua/denisdiploma/activity_change.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            entity.getContent();
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);
                ((TextView) parent.getChildAt(0)).setTypeface(medium);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //действие
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        spinner = (Spinner) findViewById(R.id.spinner);
        createSpisochek(CurrentUser);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        //получение имени
        Intent intent = getIntent();
        CurrentUser = intent.getStringExtra("name");

        spinner = (Spinner) findViewById(R.id.spinner);
        createSpisochek(CurrentUser);

        //управление устройствами
        final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        final AudioManager sound = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        //для простых холопов красивые кнопки
        final ImageButton imgRelax = (ImageButton) findViewById(R.id.imageRelax);
        final ImageButton imgWork = (ImageButton) findViewById(R.id.imageWork);
        final ImageButton imgStudy = (ImageButton) findViewById(R.id.imageStudy);
        final ImageButton imgMeeting = (ImageButton) findViewById(R.id.imageMeeting);
        final ImageButton imgLunch = (ImageButton) findViewById(R.id.imageLunch);
        final ImageButton imgSet = (ImageButton) findViewById(R.id.settings);
        final ImageButton Exit = (ImageButton) findViewById(R.id.exit);
        final ImageButton Project = (ImageButton) findViewById(R.id.project);

        TextView selectproj = (TextView) findViewById(R.id.selectproject);
        selectproj.setTypeface(medium);

        //невидимые глазу обычного смертного переключатели режимов
        Work = (RadioButton) findViewById(R.id.Work);
        Relax = (RadioButton) findViewById(R.id.Relax);
        Study = (RadioButton) findViewById(R.id.Study);
        Meeting = (RadioButton) findViewById(R.id.Meeting);
        Lunch = (RadioButton) findViewById(R.id.Lunch);

        final ImageButton statistics = (ImageButton) findViewById(R.id.statistics);

        final Chronometer mChronometer = (Chronometer) findViewById(R.id.chrono);

        mChronometer.start();

        mChronometer.setOnChronometerTickListener
                (new Chronometer.OnChronometerTickListener()
                 {
                     @Override
                     public void onChronometerTick(Chronometer chronometer)
                     {
                         if (Work.isChecked()) timespent++;

                         if (Relax.isChecked()) timespent++;

                         if (Study.isChecked()) timespent++;

                         if (Meeting.isChecked()) timespent++;

                         if (Lunch.isChecked()) timespent++;
                     }
                 }
                );


        View.OnClickListener openSettings = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                settings.putExtra("username", CurrentUser);
                MainActivity.this.startActivity(settings);
            }
        };

        View.OnClickListener openStatistics = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent stats = new Intent(MainActivity.this, StatisticsActivity.class);
                stats.putExtra("username", CurrentUser);
                MainActivity.this.startActivity(stats);
            }
        };

        View.OnClickListener projectList = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent project = new Intent(MainActivity.this, ProjectActivity.class);
                project.putExtra("username", CurrentUser);
                MainActivity.this.startActivity(project);
            }
        };

        View.OnClickListener ExitApp = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (previous)
                {
                    case "Relax":
                        imgRelax.setImageResource(R.drawable.tr_relax);
                        break;
                    case "Lunch":
                        imgLunch.setImageResource(R.drawable.tr_lunch);
                        break;
                    case "Study":
                        imgStudy.setImageResource(R.drawable.tr_study);
                        break;
                    case "Work":
                        imgWork.setImageResource(R.drawable.tr_work);
                        break;
                    case "Meeting":
                        imgMeeting.setImageResource(R.drawable.tr_meet);
                        break;
                }

                MainActivity.this.AddNoteInDB(CurrentUser, "Exit", timespent, previous, spinner.getSelectedItem().toString());
                timespent = 0;
                previous = "Exit";

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        };

        View.OnClickListener ImageButtons = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //смена картинок в зависимости от состояния
                switch (v.getId())
                {
                    case R.id.imageRelax:
                        Relax.setChecked(true);

                        switch (previous)
                        {
                            case "Lunch": imgLunch.setImageResource(R.drawable.tr_lunch); break;
                            case "Study": imgStudy.setImageResource(R.drawable.tr_study); break;
                            case "Work": imgWork.setImageResource(R.drawable.tr_work); break;
                            case "Meeting": imgMeeting.setImageResource(R.drawable.tr_meet); break;
                        }

                        imgRelax.setImageResource(R.drawable.gr_relax);

                        if (!previous.equals("Relax"))
                        {
                            MainActivity.this.AddNoteInDB(CurrentUser, "Relax", timespent, previous, spinner.getSelectedItem().toString());
                            timespent = 0;
                            previous = "Relax";
                        }

                        set_bluetooth = false;
                        set_sound = false;
                        set_timer = false;

                        getSettings(CurrentUser, previous);

                        break;

                    case R.id.imageLunch:
                        Lunch.setChecked(true);

                        switch (previous)
                        {
                            case "Relax": imgRelax.setImageResource(R.drawable.tr_relax); break;
                            case "Study": imgStudy.setImageResource(R.drawable.tr_study); break;
                            case "Work": imgWork.setImageResource(R.drawable.tr_work); break;
                            case "Meeting": imgMeeting.setImageResource(R.drawable.tr_meet); break;
                        }

                        imgLunch.setImageResource(R.drawable.gr_lunch);

                        if (!previous.equals("Lunch"))
                        {
                            MainActivity.this.AddNoteInDB(CurrentUser, "Lunch", timespent, previous, spinner.getSelectedItem().toString());
                            timespent = 0;
                            previous = "Lunch";
                        }

                        set_bluetooth = false;
                        set_sound = false;
                        set_timer = false;

                        getSettings(CurrentUser, previous);

                        break;

                    case R.id.imageMeeting:
                        Meeting.setChecked(true);

                        switch (previous)
                        {
                            case "Relax": imgRelax.setImageResource(R.drawable.tr_relax); break;
                            case "Study": imgStudy.setImageResource(R.drawable.tr_study); break;
                            case "Work": imgWork.setImageResource(R.drawable.tr_work); break;
                            case "Lunch": imgLunch.setImageResource(R.drawable.tr_lunch); break;
                        }

                        imgMeeting.setImageResource(R.drawable.gr_meet);

                        if (!previous.equals("Meeting"))
                        {
                            MainActivity.this.AddNoteInDB(CurrentUser, "Meeting", timespent, previous, spinner.getSelectedItem().toString());
                            timespent = 0;
                            previous = "Meeting";
                        }

                        set_bluetooth = false;
                        set_sound = false;
                        set_timer = false;

                        getSettings(CurrentUser, previous);

                        break;

                    case R.id.imageWork:
                        Work.setChecked(true);

                        switch (previous)
                        {
                            case "Relax": imgRelax.setImageResource(R.drawable.tr_relax); break;
                            case "Study": imgStudy.setImageResource(R.drawable.tr_study); break;
                            case "Meeting": imgMeeting.setImageResource(R.drawable.tr_meet); break;
                            case "Lunch": imgLunch.setImageResource(R.drawable.tr_lunch); break;
                        }

                        imgWork.setImageResource(R.drawable.gr_work);

                        if (!previous.equals("Work"))
                        {
                            MainActivity.this.AddNoteInDB(CurrentUser, "Work", timespent, previous, spinner.getSelectedItem().toString());
                            timespent = 0;
                            previous = "Work";
                        }

                        set_bluetooth = false;
                        set_sound = false;
                        set_timer = false;

                        getSettings(CurrentUser, previous);

                        break;

                    case R.id.imageStudy:
                        Study.setChecked(true);

                        switch (previous)
                        {
                            case "Relax": imgRelax.setImageResource(R.drawable.tr_relax); break;
                            case "Work": imgWork.setImageResource(R.drawable.tr_work); break;
                            case "Meeting": imgMeeting.setImageResource(R.drawable.tr_meet); break;
                            case "Lunch": imgLunch.setImageResource(R.drawable.tr_lunch); break;
                        }

                        imgStudy.setImageResource(R.drawable.gr_study);

                        if (!previous.equals("Study"))
                        {
                            MainActivity.this.AddNoteInDB(CurrentUser, "Study", timespent, previous, spinner.getSelectedItem().toString());
                            timespent = 0;
                            previous = "Study";
                        }

                        set_bluetooth = false;
                        set_sound = false;
                        set_timer = false;

                        getSettings(CurrentUser, previous);

                        break;
                }
            }
        };

        //присвоение обработчиков
        imgRelax.setOnClickListener(ImageButtons);
        imgLunch.setOnClickListener(ImageButtons);
        imgMeeting.setOnClickListener(ImageButtons);
        imgStudy.setOnClickListener(ImageButtons);
        imgWork.setOnClickListener(ImageButtons);
        imgSet.setOnClickListener(openSettings);
        statistics.setOnClickListener(openStatistics);
        Exit.setOnClickListener(ExitApp);
        Project.setOnClickListener(projectList);
    }

    public void getSettings(String user, String activity)
    {
        InputStream is = null ;
        String result = "";

        String url_select = "http://iskusstvo-mama.kh.ua/denisdiploma/get_settings.php";

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
                String current_activity = Jasonobject.getString("activity");

                if (name.equals(user) && activity.equals(current_activity))
                {
                    //работа с данными
                    int bt = Jasonobject.getInt("bluetooth");
                    int sound = Jasonobject.getInt("sound");
                    int timer = Jasonobject.getInt("timer");

                    if (bt == 1)
                        set_bluetooth = true;
                    if (sound == 1)
                        set_sound = true;
                    if (timer == 1)
                        set_timer = true;

                    turnOffSoundPls();
                    setOneHourTimer();
                    turnOffBTPls();
                }

            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e("log_tag", "Error! " + e.toString());
        }
    }

    public void turnOffBTPls()
    {
        if (set_bluetooth)
        {
            BluetoothAdapter BA;
            BA = BluetoothAdapter.getDefaultAdapter();
            BA.disable();
        }
    }

    public void turnOffSoundPls()
    {
        if (set_sound)
            am.setRingerMode(1);
        else
            am.setRingerMode(2);
    }

    public void setOneHourTimer()
    {
        String msg0 = MainActivity.this.getResources().getString(R.string.timer_seted);

        if (set_timer)
        {
            Toast.makeText(MainActivity.this.getApplicationContext(), msg0, Toast.LENGTH_LONG).show();
            startService(new Intent(this, NotifyService.class));
        }
    }
}

