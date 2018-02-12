package com.sang.peoplan;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AlarmActivity extends AppCompatActivity {
    Button stop_alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        stop_alarm = findViewById(R.id.stop_alarm_bt);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.stopAlarm();
                new Thread(new Runnable() {//AlarmActivity에 이식해야함.
                    public void run() {
                        File file = getSpeakFile();
                        try {
                            speakDaySchedule(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                finish();
            }
        });
    }

    public File getSpeakFile() {
        String clientId = "sr30v8cCW4IQChqsjebJ";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "RMDr06KYzA";//애플리케이션 클라이언트 시크릿값";
        File f = null;
        try {
            String text = URLEncoder.encode("현재 시간은 오후 10시 31분 입니다. 오늘의 일정은 피플랜 회의, 맥주집 아르바이트가 있습니다.", "UTF-8"); // 13자
            String apiURL = "https://openapi.naver.com/v1/voice/tts.bin";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "speaker=mijin&speed=0&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                // 랜덤한 이름으로 mp3 파일 생성
                String tempname =getApplicationContext().getFilesDir().getPath().toString() + "/file.txt";
                f = new File(tempname + ".mp3");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while ((read =is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                is.close();

            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                Log.d("error", response.toString());
            }
        } catch (Exception e) {
            Log.d("error==", e.toString());
        }
        return f;
    }

    public void speakDaySchedule(final File file) throws IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(file.getPath());
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("delete success==", String.valueOf(file.delete()));
            }
        });
        try{
            mp.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
