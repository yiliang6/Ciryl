package com.example.albert.ciryl;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Class generates lyrics based on what the user pressed in preceding page
 */

public class LoadLyricPage extends Activity {
    private String track_id = "";
    private static String apiKey = MainActivity.API_KEY;
    private static final String TAG = "Ciryl";
    TextView textView;
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_load_lyric_page);
        track_id = LoadSongList.getTrackId();
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Loading...");
        //textView.setText("Home page" + track_id);

        APICall();
    }
    /**
     * Trim user search parameters to format for search function via API call.
     * @return formatted URL for query
     */
    private String getUrl() {
        String url = "https://api.musixmatch.com/ws/1.1/track.lyrics.get?fo" +
                "rmat=json&callback=callback&track_id=" + track_id + "&apikey=" + apiKey;
        return url;
    }
    /**
     * API call to display lyrics based on the button user presses
     */
    void APICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    getUrl(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {

                            //textView.setText(response.toString());
                            Log.d(TAG, response.toString());
                            try {
                                textView.setText(response.toString());
                                String lyric = response.getJSONObject("message").getJSONObject("body").getJSONObject("lyrics").getString("lyrics_body");

                                textView.setText(lyric+ " \n" + track_id);
                                if (lyric.length() == 0) {
                                    textView.setText("No results found! Please check your search.");
                                }
                            } catch (JSONException e) {
                                textView.setText("Lyrics aren't found! Try a different song");
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("apiKey", "55209121330a9e50d5bc48132c055b8d");
                    Log.d(TAG, params.toString());
                    return params;
                }
            };
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            textView.setText("API doesn't work 2");
        }
    }
}
