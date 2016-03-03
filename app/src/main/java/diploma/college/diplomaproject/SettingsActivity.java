package diploma.college.diplomaproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity
{
    //checkbox для работы с WiFi
    CheckBox workWiFi;
    CheckBox lunchWiFi;
    CheckBox meetingWiFi;
    CheckBox relaxWiFi;
    CheckBox studyWiFi;

    //checkbox для работы с Bluetooth
    CheckBox workBT;
    CheckBox lunchBT;
    CheckBox meetingBT;
    CheckBox relaxBT;
    CheckBox studyBT;

    //checkbox для работы со звуком
    CheckBox workSound;
    CheckBox lunchSound;
    CheckBox meetingSound;
    CheckBox relaxSound;
    CheckBox studySound;

    Typeface bold;
    Typeface regular;
    Typeface medium;

    public void getSettings (String user)
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
                for(int i = 0; i < Jarray.length(); i++)
                {
                    JSONObject Jasonobject;
                    Jasonobject = Jarray.getJSONObject(i);

                    String name = Jasonobject.getString("name");
                    String activity = Jasonobject.getString("activity");
                    String wifi = Jasonobject.getString("timer");
                    String bluetooth = Jasonobject.getString("bluetooth");
                    String sound = Jasonobject.getString("sound");

                    if (name.equals(user))
                    {
                        if (activity.equals("Relax"))
                        {
                            if (wifi.equals("1")) relaxWiFi.setChecked(true);
                            if (bluetooth.equals("1")) relaxBT.setChecked(true);
                            if (sound.equals("1")) relaxSound.setChecked(true);
                        }

                        if (activity.equals("Work"))
                        {
                            if (wifi.equals("1")) workWiFi.setChecked(true);
                            if (bluetooth.equals("1")) workBT.setChecked(true);
                            if (sound.equals("1")) workSound.setChecked(true);
                        }

                        if (activity.equals("Study"))
                        {
                            if (wifi.equals("1")) studyWiFi.setChecked(true);
                            if (bluetooth.equals("1")) studyBT.setChecked(true);
                            if (sound.equals("1")) studySound.setChecked(true);
                        }

                        if (activity.equals("Meeting"))
                        {
                            if (wifi.equals("1")) meetingWiFi.setChecked(true);
                            if (bluetooth.equals("1")) meetingBT.setChecked(true);
                            if (sound.equals("1")) meetingSound.setChecked(true);
                        }

                        if (activity.equals("Lunch"))
                        {
                            if (wifi.equals("1")) lunchWiFi.setChecked(true);
                            if (bluetooth.equals("1")) lunchBT.setChecked(true);
                            if (sound.equals("1")) lunchSound.setChecked(true);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                // TODO: handle exception
                Log.e("log_tag", "Error! "+e.toString());
            }
    }

    public void SaveSettings(final String sett_user, final String sett_activity)
    {
        int wifi = 0;
        int bt = 0;
        int sound = 0;

        switch (sett_activity)
        {
            case "Relax":
                if (relaxWiFi.isChecked()) wifi = 1;
                if (relaxBT.isChecked()) bt = 1;
                if (relaxSound.isChecked()) sound = 1;
                break;

            case "Work":
            {
                if (workWiFi.isChecked()) wifi = 1;
                if (workBT.isChecked()) bt = 1;
                if (workSound.isChecked()) sound = 1;
            }
            break;

            case "Study":
                if (studyWiFi.isChecked()) wifi = 1;
                if (studyBT.isChecked()) bt = 1;
                if (studySound.isChecked()) sound = 1;
                break;

            case "Meeting":
                if (meetingWiFi.isChecked()) wifi = 1;
                if (meetingBT.isChecked()) bt = 1;
                if (meetingSound.isChecked()) sound = 1;
                break;

            case "Lunch":
                if (lunchWiFi.isChecked()) wifi = 1;
                if (lunchBT.isChecked()) bt = 1;
                if (lunchSound.isChecked()) sound = 1;
                break;
        }

        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("name", sett_user));
        nameValuePairs.add(new BasicNameValuePair("activity", sett_activity));
        nameValuePairs.add(new BasicNameValuePair("timer", String.valueOf(wifi)));
        nameValuePairs.add(new BasicNameValuePair("bluetooth", String.valueOf(bt)));
        nameValuePairs.add(new BasicNameValuePair("sound", String.valueOf(sound)));

        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://iskusstvo-mama.kh.ua/denisdiploma/settings.php");
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        TextView header = (TextView) findViewById(R.id.textView12);
        header.setTypeface(bold);

        TextView qweqwe = (TextView) findViewById(R.id.textView13);
        qweqwe.setTypeface(bold);

        TextView textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(medium);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setTypeface(medium);
        TextView textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setTypeface(medium);
        TextView textView6 = (TextView) findViewById(R.id.textView6);
        textView6.setTypeface(medium);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        textView7.setTypeface(medium);
        TextView textView8 = (TextView) findViewById(R.id.textView8);
        textView8.setTypeface(medium);
        TextView textView9 = (TextView) findViewById(R.id.textView9);
        textView9.setTypeface(medium);
        TextView textView10 = (TextView) findViewById(R.id.textView10);
        textView10.setTypeface(medium);

        workWiFi = (CheckBox) findViewById(R.id.workWiFi);
        lunchWiFi = (CheckBox) findViewById(R.id.lunchWiFi);
        meetingWiFi = (CheckBox) findViewById(R.id.meetingWiFi);
        relaxWiFi = (CheckBox) findViewById(R.id.relaxWiFi);
        studyWiFi = (CheckBox) findViewById(R.id.studyWiFi);

        workBT = (CheckBox) findViewById(R.id.workBT);
        lunchBT = (CheckBox) findViewById(R.id.lunchBT);
        meetingBT = (CheckBox) findViewById(R.id.meetingBT);
        relaxBT = (CheckBox) findViewById(R.id.relaxBT);
        studyBT = (CheckBox) findViewById(R.id.studyBT);

        workSound = (CheckBox) findViewById(R.id.workSound);
        lunchSound = (CheckBox) findViewById(R.id.lunchSound);
        meetingSound = (CheckBox) findViewById(R.id.meetingSound);
        relaxSound = (CheckBox) findViewById(R.id.relaxSound);
        studySound = (CheckBox) findViewById(R.id.studySound);

        final ImageButton settings = (ImageButton) findViewById(R.id.apply);

        //возьмем имя
        Intent intent = getIntent();
        final String CurrentUser = intent.getStringExtra("username");

        //заигрузим настройки
        getSettings(CurrentUser);

        //запись в базу данных
        View.OnClickListener setting_settings = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SettingsActivity.this.SaveSettings(CurrentUser, "Relax");
                SettingsActivity.this.SaveSettings(CurrentUser, "Work");
                SettingsActivity.this.SaveSettings(CurrentUser, "Lunch");
                SettingsActivity.this.SaveSettings(CurrentUser, "Meeting");
                SettingsActivity.this.SaveSettings(CurrentUser, "Study");

                String msg = SettingsActivity.this.getResources().getString(R.string.settigs_set);
                Toast.makeText(SettingsActivity.this.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        };

        settings.setOnClickListener(setting_settings);
    }
}