package ru.finik.asyncexample;
/**
 * У вас два проекта: fragmetninteratciondemo и asyncdemo. В первом мы реализовали работу со списоком данных.
 * Во втором - загрузку данных из сети в другом потоке и уведомлением о выполнении.
 * Данные приходят в формате  json и мы его распарсили и сделали список.
 *
 * Надо объединить эти два проекта таким образом чтобы:
 * загружался список валютных пар и отображался на экране.
 * каждый пункт списка должен содержать название валютной пары, последнюю цену и объем торгов
 * * добавить к отображаемым данным зеленую стрелку вверх если цена выросла, и красную стрелку вниз,
 * если цена упала (поле prevDay) в исходном json
 * ** сделать автообновление данных в списке раз в 5-10 секунд (делается с помощью Handler.postDelayed()
 * PS: функциональность нажатий на элемент списка с отображением DetailFragment должна сохраниться
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String MARKETS_URL = "https://api.bittrex.com/api/v1.1/public/getmarketsummaries";
    TextView tv_dataFromMarkets;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String dataMarkets = (String) msg.obj;
            if (tv_dataFromMarkets != null){
                tv_dataFromMarkets.setText(dataMarkets);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_dataFromMarkets = findViewById(R.id.tv_data);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "отложенный запуск", Toast.LENGTH_SHORT).show();
            }
        }, 7000);

    }

    public String getData(String baseURL) throws IOException {
        URL url = new URL(baseURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null){
            builder.append(line);
        }

        return builder.toString();

    }

    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String s = getData(MARKETS_URL);
                    Log.d("tag", "data from server:" + s);
                    Message message = handler.obtainMessage();
                    JSONObject jsonObject = new JSONObject(s);
                    boolean success = jsonObject.getBoolean("success");

                    if (success){
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); ++i){
                            JSONObject pair = jsonArray.getJSONObject(i);
                        }
                    } else {

                    }
                    message.obj = s;
                    handler.sendMessage(message);
//                    tv_dataFromMarkets.setText(s);
                   /* handler.sendMessage()
                    tv_dataFromMarkets.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_dataFromMarkets.setText(s);
                        }
                    });*/
                } catch (IOException e){
                    e.printStackTrace();
//                    tv_dataFromMarkets.setText("error e" + e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    class Pair{
        String title;
        String price;
        String logo;

        public Pair(String title, String price, String logo) {
            this.title = title;
            this.price = price;
            this.logo = logo;
        }
    }
}
