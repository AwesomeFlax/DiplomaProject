package diploma.college.diplomaproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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


public class ProjectActivity extends Activity  implements View.OnClickListener
{
    int quantity = 0;
    boolean edit = false;
    String[] project_names = new String [5];
    String CurrentUser;
    String oldData = "";

    Typeface bold;
    Typeface regular;
    Typeface medium;

    //поле ввода
    EditText input;
    ImageButton ok;

    //первая строчка
    TextView num_1;
    TextView name_1;
    ImageButton ed_1;
    ImageButton del_1;

    //вторая строчка
    TextView num_2;
    TextView name_2;
    ImageButton ed_2;
    ImageButton del_2;

    //третья строчка
    TextView num_3;
    TextView name_3;
    ImageButton ed_3;
    ImageButton del_3;

    //четверая строчка
    TextView num_4;
    TextView name_4;
    ImageButton ed_4;
    ImageButton del_4;

    //пятая строчка
    TextView num_5;
    TextView name_5;
    ImageButton ed_5;
    ImageButton del_5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects);

        //шрифты
        bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        TextView head = (TextView) findViewById(R.id.textView2);
        head.setTypeface(bold);

        TextView qweqw = (TextView) findViewById(R.id.textView28);
        qweqw.setTypeface(medium);

        //поле ввода
        input = (EditText) findViewById(R.id.input);
        input.setTypeface(medium);
        ok = (ImageButton) findViewById(R.id.ok);

        //первая строчка
        num_1 = (TextView) findViewById(R.id.num_1);
        num_1.setTypeface(bold);
        name_1 = (TextView) findViewById(R.id.name_1);
        name_1.setTypeface(medium);
        ed_1 = (ImageButton) findViewById(R.id.ed_1);
        del_1 = (ImageButton) findViewById(R.id.del_1);

        //вторая строчка
        num_2 = (TextView) findViewById(R.id.num_2);
        num_2.setTypeface(bold);
        name_2 = (TextView) findViewById(R.id.name_2);
        name_2.setTypeface(medium);
        ed_2 = (ImageButton) findViewById(R.id.ed_2);
        del_2 = (ImageButton) findViewById(R.id.del_2);

        //третья строчка
        num_3 = (TextView) findViewById(R.id.num_3);
        num_3.setTypeface(bold);
        name_3 = (TextView) findViewById(R.id.name_3);
        name_3.setTypeface(medium);
        ed_3 = (ImageButton) findViewById(R.id.ed_3);
        del_3 = (ImageButton) findViewById(R.id.del_3);

        //четверая строчка
        num_4 = (TextView) findViewById(R.id.num_4);
        num_3.setTypeface(bold);
        name_4 = (TextView) findViewById(R.id.name_4);
        name_4.setTypeface(medium);
        ed_4 = (ImageButton) findViewById(R.id.ed_4);
        del_4 = (ImageButton) findViewById(R.id.del_4);

        //пятая строчка
        num_5 = (TextView) findViewById(R.id.num_5);
        num_3.setTypeface(bold);
        name_5 = (TextView) findViewById(R.id.name_5);
        name_5.setTypeface(medium);
        ed_5 = (ImageButton) findViewById(R.id.ed_5);
        del_5 = (ImageButton) findViewById(R.id.del_5);

        //возьмем имя
        Intent intent = getIntent();
        CurrentUser = intent.getStringExtra("username");

        refresh();

        ok.setOnClickListener(this);
        del_1.setOnClickListener(this);
        del_2.setOnClickListener(this);
        del_3.setOnClickListener(this);
        del_4.setOnClickListener(this);
        del_5.setOnClickListener(this);
        ed_1.setOnClickListener(this);
        ed_2.setOnClickListener(this);
        ed_3.setOnClickListener(this);
        ed_4.setOnClickListener(this);
        ed_5.setOnClickListener(this);
    }

    public void addNewProject(final String user_name, final String project)
    {
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("name", user_name));
        nameValuePairs.add(new BasicNameValuePair("project", project));

        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://iskusstvo-mama.kh.ua/denisdiploma/add_project.php");
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

    public void makeThenAllInvisible()
    {
        num_1.setVisibility(View.INVISIBLE);
        name_1.setVisibility(View.INVISIBLE);
        ed_1.setVisibility(View.INVISIBLE);
        del_1.setVisibility(View.INVISIBLE);

        num_2.setVisibility(View.INVISIBLE);
        name_2.setVisibility(View.INVISIBLE);
        ed_2.setVisibility(View.INVISIBLE);
        del_2.setVisibility(View.INVISIBLE);

        num_3.setVisibility(View.INVISIBLE);
        name_3.setVisibility(View.INVISIBLE);
        ed_3.setVisibility(View.INVISIBLE);
        del_3.setVisibility(View.INVISIBLE);

        num_4.setVisibility(View.INVISIBLE);
        name_4.setVisibility(View.INVISIBLE);
        ed_4.setVisibility(View.INVISIBLE);
        del_4.setVisibility(View.INVISIBLE);

        num_5.setVisibility(View.INVISIBLE);
        name_5.setVisibility(View.INVISIBLE);
        ed_5.setVisibility(View.INVISIBLE);
        del_5.setVisibility(View.INVISIBLE);
    }

    public void delete (final String usern_name, final String project)
    {
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("name", usern_name));
        nameValuePairs.add(new BasicNameValuePair("project", project));

        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://iskusstvo-mama.kh.ua/denisdiploma/delete_project.php");
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

    public void changeData (int pressed, String qwe)
    {
        edit = true;
        oldData = qwe;

        switch (pressed)
        {
            case 1: input.setText(name_1.getText()); break;
            case 2: input.setText(name_2.getText()); break;
            case 3: input.setText(name_3.getText()); break;
            case 4: input.setText(name_4.getText()); break;
            case 5: input.setText(name_5.getText()); break;
        }
    }

    public void sendNewData(final String user_name, final String project, String data)
    {
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("name", user_name));
        nameValuePairs.add(new BasicNameValuePair("project", project));
        nameValuePairs.add(new BasicNameValuePair("oldData", data));

        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://iskusstvo-mama.kh.ua/denisdiploma/rename_project.php");
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

    public void getProjectList (String user)
    {
        quantity = 0;
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
                String proj = Jasonobject.getString("project");

                if (name.equals(user))
                {
                    project_names[quantity] = proj;
                    ++quantity;
                }

            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
            Log.e("log_tag", "Error! " + e.toString());
        }
    }

    public void refresh ()
    {
        getProjectList(CurrentUser);

        switch (quantity)
        {
            case 1:
                name_1.setText(project_names[0]);

                num_1.setVisibility(View.VISIBLE);
                name_1.setVisibility(View.VISIBLE);
                ed_1.setVisibility(View.VISIBLE);
                del_1.setVisibility(View.VISIBLE);

                break;

            case 2:
                name_1.setText(project_names[0]);
                name_2.setText(project_names[1]);

                num_1.setVisibility(View.VISIBLE);
                name_1.setVisibility(View.VISIBLE);
                ed_1.setVisibility(View.VISIBLE);
                del_1.setVisibility(View.VISIBLE);

                num_2.setVisibility(View.VISIBLE);
                name_2.setVisibility(View.VISIBLE);
                ed_2.setVisibility(View.VISIBLE);
                del_2.setVisibility(View.VISIBLE);

                break;

            case 3:
                name_1.setText(project_names[0]);
                name_2.setText(project_names[1]);
                name_3.setText(project_names[2]);

                num_1.setVisibility(View.VISIBLE);
                name_1.setVisibility(View.VISIBLE);
                ed_1.setVisibility(View.VISIBLE);
                del_1.setVisibility(View.VISIBLE);

                num_2.setVisibility(View.VISIBLE);
                name_2.setVisibility(View.VISIBLE);
                ed_2.setVisibility(View.VISIBLE);
                del_2.setVisibility(View.VISIBLE);

                num_3.setVisibility(View.VISIBLE);
                name_3.setVisibility(View.VISIBLE);
                ed_3.setVisibility(View.VISIBLE);
                del_3.setVisibility(View.VISIBLE);

                break;

            case 4:
                name_1.setText(project_names[0]);
                name_2.setText(project_names[1]);
                name_3.setText(project_names[2]);
                name_4.setText(project_names[3]);

                num_1.setVisibility(View.VISIBLE);
                name_1.setVisibility(View.VISIBLE);
                ed_1.setVisibility(View.VISIBLE);
                del_1.setVisibility(View.VISIBLE);

                num_2.setVisibility(View.VISIBLE);
                name_2.setVisibility(View.VISIBLE);
                ed_2.setVisibility(View.VISIBLE);
                del_2.setVisibility(View.VISIBLE);

                num_3.setVisibility(View.VISIBLE);
                name_3.setVisibility(View.VISIBLE);
                ed_3.setVisibility(View.VISIBLE);
                del_3.setVisibility(View.VISIBLE);

                num_4.setVisibility(View.VISIBLE);
                name_4.setVisibility(View.VISIBLE);
                ed_4.setVisibility(View.VISIBLE);
                del_4.setVisibility(View.VISIBLE);

                break;

            case 5:
                name_1.setText(project_names[0]);
                name_2.setText(project_names[1]);
                name_3.setText(project_names[2]);
                name_4.setText(project_names[3]);
                name_5.setText(project_names[4]);

                num_1.setVisibility(View.VISIBLE);
                name_1.setVisibility(View.VISIBLE);
                ed_1.setVisibility(View.VISIBLE);
                del_1.setVisibility(View.VISIBLE);

                num_2.setVisibility(View.VISIBLE);
                name_2.setVisibility(View.VISIBLE);
                ed_2.setVisibility(View.VISIBLE);
                del_2.setVisibility(View.VISIBLE);

                num_3.setVisibility(View.VISIBLE);
                name_3.setVisibility(View.VISIBLE);
                ed_3.setVisibility(View.VISIBLE);
                del_3.setVisibility(View.VISIBLE);

                num_4.setVisibility(View.VISIBLE);
                name_4.setVisibility(View.VISIBLE);
                ed_4.setVisibility(View.VISIBLE);
                del_4.setVisibility(View.VISIBLE);

                num_5.setVisibility(View.VISIBLE);
                name_5.setVisibility(View.VISIBLE);
                ed_5.setVisibility(View.VISIBLE);
                del_5.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        String msg0 = ProjectActivity.this.getResources().getString(R.string.deleted);
        String msg1 = ProjectActivity.this.getResources().getString(R.string.newprojectsuccess);

        switch(v.getId())
        {
            case R.id.del_1:

                delete(CurrentUser, name_1.getText().toString());
                makeThenAllInvisible();
                refresh();
                Toast.makeText(ProjectActivity.this.getApplicationContext(), msg0, Toast.LENGTH_LONG).show();

                break;

            case R.id.del_2:

                delete(CurrentUser, name_2.getText().toString());
                makeThenAllInvisible();
                refresh();
                Toast.makeText(ProjectActivity.this.getApplicationContext(), msg0, Toast.LENGTH_LONG).show();

                break;

            case R.id.del_3:

                delete(CurrentUser, name_3.getText().toString());
                makeThenAllInvisible();
                refresh();
                Toast.makeText(ProjectActivity.this.getApplicationContext(), msg0, Toast.LENGTH_LONG).show();

                break;

            case R.id.del_4:

                delete(CurrentUser, name_4.getText().toString());
                makeThenAllInvisible();
                refresh();
                Toast.makeText(ProjectActivity.this.getApplicationContext(), msg0, Toast.LENGTH_LONG).show();

                break;

            case R.id.del_5:

                delete(CurrentUser, name_5.getText().toString());
                makeThenAllInvisible();
                refresh();
                Toast.makeText(ProjectActivity.this.getApplicationContext(), msg0, Toast.LENGTH_LONG).show();

                break;

            case R.id.ok:

                if (!edit)
                {
                    ProjectActivity.this.addNewProject(CurrentUser, input.getText().toString());
                    refresh();
                    Toast.makeText(ProjectActivity.this.getApplicationContext(), msg1, Toast.LENGTH_LONG).show();
                }
                else
                {
                    edit = false;
                    sendNewData(CurrentUser, input.getText().toString(), oldData);
                    refresh();
                }

                break;

            case R.id.ed_1:

                changeData(1, name_1.getText().toString());
                break;

            case R.id.ed_2:

                changeData(2, name_2.getText().toString());
                break;

            case R.id.ed_3:

                changeData(3, name_3.getText().toString());
                break;

            case R.id.ed_4:

                changeData(4, name_4.getText().toString());
                break;

            case R.id.ed_5:

                changeData(5, name_5.getText().toString());
                break;
        }
    }
}
