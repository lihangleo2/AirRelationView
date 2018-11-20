package comt.leo.picker.testline;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.UUID;

import comt.leo.picker.testline.bean.AtmanRelation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //    private PowerfulLayout powerfulLayout;
    private Button button1, button2, button3, button4, button5, button6;
    private ScaleGestureDetector mScaleGestureDetector = null;
    private ImageLayout shaderImage;
    private HVScrollView hvScrollView;
    private ArrayList<AtmanRelation> sourceList;

    private TextView text_show_how;
    private SeekBar seekBar;
    private TextView text_renzheng;


    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    hvScrollView.scrollTo(UIUtil.dip2px(MainActivity.this, 2500) - UIUtil.getWidth() / 2, UIUtil.dip2px(MainActivity.this, 2500) - UIUtil.getHeight() / 2);
                    break;
                case 12:
                    if (scale < 0.7) {
                        tryScale(scale, 0.7f);
                    }else if (scale>1.3){
                        tryScale(scale, 1.3f);
                    }
                    break;
            }
        }
    };

    private boolean moreFinger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shaderImage = findViewById(R.id.shaderImage);
        shaderImage.setListener(this);
        hvScrollView = findViewById(R.id.hvScrollView);
        mScaleGestureDetector = new ScaleGestureDetector(this,
                new ScaleGestureListener());
        initView();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tryScale(3.0f);//1.8f
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                tryScale(1.6f);//0.8f
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tryScale(1.4f);//1.2f
            }
        });


        mhandler.sendEmptyMessageDelayed(11, 1000);
        initJsonData();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                shaderImage.setShowCount(i + 1);
                text_show_how.setText("已显示" + (i + 1) + "°关系以内关系");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        hvScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("这里该怎么解决", "=====2");
                if (moreFinger) {
                    mScaleGestureDetector.onTouchEvent(motionEvent);
                }
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        //第一个手指按下事件
                        Log.e("我看看那这里的操作哈", "第一个手指按下");
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        //第二个手指按下事件
                        Log.e("我看看那这里的操作哈", "第二个手指按下=====");

                        moreFinger = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e("我看看那这里的操作哈", "手指移动1111");
                        break;
                    case MotionEvent.ACTION_UP:
                        moreFinger = false;
                        Log.e("我看看那这里的操作哈", "手指移动upupupupupup");
                        mhandler.sendEmptyMessage(12);//手指放开来个回弹效果
                        break;
                }
                Log.e("这个判断一点没用？", moreFinger + "==============");
                return moreFinger;
            }
        });


    }


    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        sourceList = parseData(JsonData);//用Gson 转成实体
        Log.e("现在是否解析成了", sourceList.size() + "===================");
        shaderImage.setSourceList(sourceList);
    }


    public ArrayList<AtmanRelation> parseData(String result) {//Gson 解析
        ArrayList<AtmanRelation> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                AtmanRelation entity = gson.fromJson(data.optJSONObject(i).toString(), AtmanRelation.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }


    public void tryScale(float current, float target) {

        ValueAnimator animator_shadow = ValueAnimator.ofFloat(current, target);
        animator_shadow.setDuration(200);
        animator_shadow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                scale = (float) valueAnimator.getAnimatedValue();
                preScale = scale;
                ViewHelper.setScaleX(shaderImage, scale);// x方向上缩放
                ViewHelper.setScaleY(shaderImage, scale);// y方向上缩放
            }
        });
        animator_shadow.start();

    }

    private void initView() {
//        powerfulLayout = findViewById(R.id.powerfulLayout);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        text_show_how = findViewById(R.id.text_show_how);
        seekBar = findViewById(R.id.seekBar);
        text_renzheng = findViewById(R.id.text_renzheng);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 返回给ScaleGestureDetector来处理
        return mScaleGestureDetector.onTouchEvent(event);
    }

    private float scale = 1;
    private float preScale = 1;// 默认前一次缩放比例为1

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_theOne:
                AtmanRelation atmanRelation = (AtmanRelation) view.getTag(R.id.image_theOne);
                //得到当前所点击的item,拿出他的 子空间和父控件。
                ArrayList<AtmanRelation> clickList = new ArrayList<>();
                clickList.add(atmanRelation);
                for (int i = 0; i < sourceList.size(); i++) {
                    if (atmanRelation.getNode() == sourceList.get(i).getNodePar() || atmanRelation.getNodePar() == sourceList.get(i).getNode()) {
                        clickList.add(sourceList.get(i));
                    }
                }

                shaderImage.setClickList(clickList, atmanRelation.getDegree(), atmanRelation.getNode());
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_tip_point);
                view.startAnimation(animation);
                Log.e("走动了么走动了么", "1111111+++");

                break;
        }
    }

    public class ScaleGestureListener implements
            ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float previousSpan = detector.getPreviousSpan();
            float currentSpan = detector.getCurrentSpan();
            if (currentSpan < previousSpan) {
                // 缩小
                // scale = preScale-detector.getScaleFactor()/3;
                scale = preScale - (previousSpan - currentSpan) / 1000;
                if (scale < 0.5) {
                    scale = 0.5f;
                }
            } else {
                // 放大
                // scale = preScale+detector.getScaleFactor()/3;
                scale = preScale + (currentSpan - previousSpan) / 1000;
                if (scale > 1.5) {
                    scale = 1.5f;
                }
            }

            // 缩放view
            ViewHelper.setScaleX(shaderImage, scale);// x方向上缩小
            ViewHelper.setScaleY(shaderImage, scale);// y方向上缩小
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // 一定要返回true才会进入onScale()这个函数
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            preScale = scale;//记录本次缩放比例
        }
    }


}
