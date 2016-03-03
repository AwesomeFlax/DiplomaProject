package diploma.college.diplomaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class LoginActivity extends Activity implements OnClickListener
{
    ImageButton login;
    ImageButton regist;

    TextView autorize;
    TextView enter;
    TextView toreg;

    EditText id;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Typeface bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        Typeface medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        setContentView(R.layout.login);

        login = (ImageButton) findViewById (R.id.login);
        regist = (ImageButton) findViewById (R.id.registration);

        id = (EditText) findViewById (R.id.name);
        pass = (EditText) findViewById (R.id.pass);

        enter = (TextView) findViewById (R.id.enter);
        autorize = (TextView) findViewById (R.id.autorize);
        toreg = (TextView) findViewById (R.id.toreg);

        login.setOnClickListener(this);
        regist.setOnClickListener(this);

        id.setTypeface(medium);
        pass.setTypeface(medium);
        enter.setTypeface(medium);
        autorize.setTypeface(bold);
        toreg.setTypeface(medium);
    }

    class task extends AsyncTask <String, String, Void>
    {
        private ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        InputStream is = null ;
        String result = "";

        protected void onPreExecute()
        {
            progressDialog.setMessage(getResources().getString(R.string.wait));
            progressDialog.show();
            progressDialog.setOnCancelListener
                    (new OnCancelListener()
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
                Log.e("log_tag", "Connection error "+e.toString());
            }
            try
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null)
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
                for(int i = 0; i < Jarray.length(); i++)
                {
                    JSONObject Jasonobject;
                    Jasonobject = Jarray.getJSONObject(i);

                    //get an output on the screen
                    String name = Jasonobject.getString("login");
                    String db_detail;

                    if(id.getText().toString().equalsIgnoreCase(name))
                    {
                        String password = pass.getText().toString();

                        db_detail = Jasonobject.getString("password");

                        String datainfo = db_detail;

                        if (password.equals(datainfo))
                        {
                            Intent home = new Intent(LoginActivity.this, MainActivity.class);
                            home.putExtra("name", id.getText().toString());
                            startActivity(home);
                        }
                        break;
                    }
                }
                this.progressDialog.dismiss();

            }
            catch (Exception e)
            {
                // TODO: handle exception
                Log.e("log_tag", "Error! "+e.toString());
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub

        switch(v.getId())
        {
            case R.id.login:
            {
                new task().execute();
            }
            break;

            case R.id.registration:
            {
                Intent registration = new Intent(this, RegistrationActivity.class);
                startActivity(registration);
            }
            break;
        }
    }
}
