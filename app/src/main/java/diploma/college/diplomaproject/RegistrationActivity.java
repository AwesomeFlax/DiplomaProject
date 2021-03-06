package diploma.college.diplomaproject;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends Activity
{
    public boolean checkNameAvailable (String user)
    {
        InputStream is = null ;
        String result = "";
        boolean matches = false;

        String url_select = "http://iskusstvo-mama.kh.ua/denisdiploma/login.php";

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

                String name = Jasonobject.getString("login");

                if (name.equals(user))
                    matches = true;
            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e("log_tag", "Error! "+e.toString());
        }

        return matches;
    }


    ImageButton register;

    EditText login;
    EditText password1;
    EditText password2;

    TextView login_note;
    TextView reg_note;
    TextView pass_twice;
    TextView reg_mess;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Typeface bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        Typeface medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        setContentView(R.layout.registration);

        register = (ImageButton) findViewById(R.id.registrr);
        login = (EditText) findViewById(R.id.name);
        password1 = (EditText) findViewById(R.id.pass1);
        password2 = (EditText) findViewById(R.id.pass2);
        login_note = (TextView) findViewById(R.id.login_note);
        reg_note = (TextView) findViewById(R.id.reg_note);
        pass_twice = (TextView) findViewById(R.id.pass_twice);
        reg_mess =  (TextView) findViewById(R.id.reg_mess);

        login.setTypeface(regular);
        reg_mess.setTypeface(bold);
        password1.setTypeface(regular);
        password2.setTypeface(regular);
        login_note.setTypeface(medium);
        reg_note.setTypeface(bold);
        pass_twice.setTypeface(medium);

        register.setOnClickListener(new View.OnClickListener()
        {
            InputStream is = null;

            @Override
            public void onClick(View arg0)
            {
                String name = "" + login.getText().toString();
                String pass = "" + password1.getText().toString();
                String conf = "" + password2.getText().toString();

                boolean used_name = checkNameAvailable(name);

                if (pass.equals(conf) && !used_name)
                {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("name", name));
                    nameValuePairs.add(new BasicNameValuePair("pass", pass));

                    try
                    {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://iskusstvo-mama.kh.ua/denisdiploma/registration.php");
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpClient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();

                        String msg = getResources().getString(R.string.reg_success);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    if (used_name)
                    {
                        String msg = getResources().getString(R.string.used_name);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String msg = getResources().getString(R.string.reg_alert);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
