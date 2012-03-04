package net.androg.samplewebapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SampleWebAPIActivity extends Activity {
    /** Called when the activity is first created. */

    private final String apiKey = (new Setting()).getApiKey();
    private String mJson;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        testCallService();
        printContent(mJson);
    }

    public void testCallService(){
        try{
            //GoogleMaps API
//          URI uri = new URI("http://maps.google.com/maps/api/geocode/json?address="
//               .concat(URLEncoder.encode("神戸")).concat("&sensor=false"));

            //Google+ API
            // https://developers.google.com/+/api/ 参照
            URI uri = new URI("https://www.googleapis.com/plus/v1/people/103136005429296369032/activities/public?key="
                    .concat(apiKey)
                    .concat("&maxResults=2&fields=kind,items/id,items(id,object/content,verb)"));    //Google+API１の仕様を参照

            HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection(); //生成・オープン
            InputStream inputStream = urlConnection.getInputStream(); //URLConnectionに対して、読めるデータに変換する
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder json = new StringBuilder();
            String line;
            while( (line = reader.readLine()) != null ){
                Log.d("",line);
                json.append(line);
            }
            mJson = json.toString();
            inputStream.close();
            reader.close();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void printContent(String str){
        try {
            JSONObject jsonObject = new JSONObject(str); //取得したJSON文字列を渡すと、各属性にアクセスできるオブジェクトを生成します。
            JSONArray items = jsonObject.getJSONArray("items"); // Google+のitemsは配列なので、JSONArrayで生成する
            int count = items.length();
            Log.d("pringContent", "items.length()=" + count);
            for ( int i = 0; i < count; i++ ){
                JSONObject item = items.getJSONObject(i);
                JSONObject object = item.getJSONObject("object");
                String content = object.getString("content");
                Log.d("",content);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}