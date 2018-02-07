package am.user.json;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import am.user.json.empty.Example;
import am.user.json.empty.Links;
import am.user.json.empty.Rocket;

public class MainActivity extends AppCompatActivity implements DialogInterface {

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new ItemDivider(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new ParseTask().execute();
    }

    @Override
    public void showChooserDialog(final String article, final String video_link) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.choose);
        builder.setCancelable(true);

        builder.setPositiveButton(
                R.string.article,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(android.content.DialogInterface dialog, int id) {
                        dialog.cancel();
                        startViewing(article);
                    }
                });

        builder.setNegativeButton(
                R.string.watch_video,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(android.content.DialogInterface dialog, int id) {
                        dialog.cancel();
                        startViewing(video_link);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startViewing(String article) {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ParseTask extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("https://api.spacexdata.com/v2/launches?launch_year=2017");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                resultJson = stringBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            try {
                ArrayList<Space> mJsonSpaceLists = new ArrayList<>();
                JSONArray dataJsonArray = new JSONArray(strJson);

                List<Example> examples = new ArrayList<>(dataJsonArray.length());
                for (int i = 0; i < dataJsonArray.length(); i++) {
                    Example example = new Example();

                    JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                    example.setLaunchYear(jsonObject.getString("launch_year"));
                    example.setLaunchDateUnix(jsonObject.getInt("launch_date_unix"));
                    example.setDetails(jsonObject.getString("details"));

                    Links links = setLinks(jsonObject);
                    Rocket rocket = setRocket(jsonObject);

                    example.setRocket(rocket);
                    example.setLinks(links);
                    examples.add(example);

                    Space space = new Space(rocket.getRocketName(),
                            example.getLaunchDateUnix(), links.getMissionPatch(),
                            example.getDetails(), links.getArticleLink(), links.getVideoLink());

                    mJsonSpaceLists.add(space);

                }

                initAdapter(mJsonSpaceLists);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private Links setLinks(JSONObject jsonObject) throws JSONException {
            JSONObject jsonObjectLinks = jsonObject.getJSONObject("links");

            Links links = new Links();
            links.setMissionPatch(jsonObjectLinks.getString("mission_patch"));
            links.setArticleLink(jsonObjectLinks.getString("article_link"));
            links.setVideoLink(jsonObjectLinks.getString("video_link"));

            return links;
        }

        private Rocket setRocket(JSONObject jsonObject) throws JSONException {
            JSONObject jsonObjectRocket = jsonObject.getJSONObject("rocket");
            Rocket rocket = new Rocket();
            rocket.setRocketName(jsonObjectRocket.getString("rocket_name"));
            return rocket;
        }
    }

    private void initAdapter(ArrayList<Space> mJsonSpaceLists) {
        Adapter adapter = new Adapter(MainActivity.this, mJsonSpaceLists);
        recyclerView.setAdapter(adapter);
    }
}