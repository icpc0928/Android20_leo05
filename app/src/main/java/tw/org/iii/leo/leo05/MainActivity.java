package tw.org.iii.leo.leo05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private boolean isRunning;
    private Button leftBtn,rightBtn;
    private Timer timer = new Timer();
    private int hs ;
    private Counter counter; //每次都是一個週期任務
    private TextView clock;
    private UIHander uiHander = new UIHander();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        clock = findViewById(R.id.clock);
        leftBtn = findViewById(R.id.leftBtn);
        rightBtn = findViewById(R.id.rightBtn);
        changeDisplay();
        clock.setText(parseHS(hs));

        initLap();

    }

    private void initLap(){

    }

    private void changeDisplay(){
        rightBtn.setText(isRunning?"STOP":"START");
        leftBtn.setText(isRunning?"LAP":"RESET");

    }

    public void doLeft(View view) {
        if(isRunning){
            //LAP
            doLap();
        }else{
            //RESET
            doReset();
        }
    }

    private void doReset(){
        hs = 0;
        clock.setText(parseHS(hs)); //上面的畫面清空
    }

    private void doLap(){

    }




    public void doRight(View view) {
        //切換狀態
        isRunning = !isRunning;
        changeDisplay();

        if (isRunning){
            counter = new Counter(); // 新任務
            timer.schedule(counter,1*10,10);
        }else{
            counter.cancel();
            counter = null;
        }

    }
    //  時間到要做的事情  Counter繼承
    private class Counter extends TimerTask{
        @Override
        public void run() {
            hs++;
            uiHander.sendEmptyMessage(0);
        }
    }

    private class UIHander extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            clock.setText(parseHS(hs));//上面的畫面清空
        }
    }

    private static String parseHS(int hs){
        int phs = hs % 100; //小數點後
        int ts = hs / 100;  //總秒數（不含小數點)
        int hh = ts / (60*60);
        int mm = (ts-hh*60*60)/60;
        int ss = ts % 60;

        return String.format("%d:%s:%s.%s",hh,(mm<10?"0"+mm:mm),(ss<10?"0"+ss:ss),(phs<10?"0"+phs:phs));
    }


    @Override
    public void finish() {
        if(timer != null){
            timer.cancel(); //任務取消
            timer.purge();  //清除
            timer = null;   //去死
        }
        super.finish();
    }
}

//碼錶有兩個狀態 進行跟停止

