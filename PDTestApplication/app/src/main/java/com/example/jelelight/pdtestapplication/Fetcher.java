package com.example.jelelight.pdtestapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Fetcher extends AsyncTask<Void,Void,Void> {

    boolean isFetched = false;

    String status;

    String data = "";
    String dataParsed = "";
    String singleParsed = "";

    List<String> typeList = new ArrayList<>();
    List<Actor> actors = new ArrayList<>();
    List<Repo> repos = new ArrayList<>();
    List<Event> events = new ArrayList<>();

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.github.com/events");
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray jsonArray = new JSONArray(data);

            if(!typeList.isEmpty()) {
                typeList.clear();
                actors.clear();
                repos.clear();
                events.clear();
            }

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                // Get Actor to List //
                String actorString = jsonObject.get("actor").toString();
                JSONObject jsonActor = new JSONObject(actorString);
                Actor actor = new Actor();
                actor.setId(jsonActor.getString("id"));
                actor.setLogin(jsonActor.getString("login"));
                actor.setDisplay_login(jsonActor.getString("display_login"));
                actor.setGravatar_id(jsonActor.getString("gravatar_id"));
                actor.setUrl(jsonActor.getString("url"));
                actor.setAvatar_url(jsonActor.getString("avatar_url"));
                actors.add(actor);
                // Get Repo to List //
                String repoString = jsonObject.get("repo").toString();
                JSONObject jsonRepo = new JSONObject(repoString);
                Repo repo = new Repo(jsonRepo.getString("id"),
                                        jsonRepo.getString("name"),
                                        jsonRepo.getString("url"));
                repos.add(repo);
                // Get Event to List //
                Event event = new Event();
                event.setId(jsonObject.getString("id"));
                event.setType(jsonObject.getString("type"));
                event.setActor(actor);
                event.setRepo(repo);
                event.setCreated(jsonObject.getString("created_at"));
                event.setPublic(jsonObject.getBoolean("public"));
                events.add(event);
                // Set Text //
                singleParsed = "ID : " + jsonObject.get("id") + "\n" +
                                "Type : " + jsonObject.get("type") + "\n" +
                                "UserID : " + jsonActor.get("id") + "\n";
                dataParsed = dataParsed + singleParsed;
                // Check Type //
                if(!typeList.contains(jsonObject.get("type"))){
                    typeList.add(jsonObject.get("type").toString());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        isFetched = true;
        //CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,events);
        //MainActivity.mResult.setText(data);
        //MainActivity.mResult.setText(dataParsed);
        //Log.d("Actors", actors.toString());

        Log.d("ActionType", typeList.toString());
    }

    public List<Event> getEvents(){
        while (events.isEmpty()){
            status = "waiting";
        }
        return events;
    }

}
