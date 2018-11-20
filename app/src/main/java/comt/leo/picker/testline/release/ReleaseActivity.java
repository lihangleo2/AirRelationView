package comt.leo.picker.testline.release;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;

import java.util.ArrayList;

import comt.leo.picker.testline.GetJsonDataUtil;
import comt.leo.picker.testline.HVScrollView;
import comt.leo.picker.testline.R;
import comt.leo.picker.testline.UIUtil;
import comt.leo.picker.testline.release.bean.AtmanRelation;
import comt.leo.picker.testline.release.bean.ZhengjuBean;
import comt.leo.picker.testline.release.view.StarAirLayout;


/**
 * 需求：
 * 1、项目里将有关系的人，用星空图谱的关系连接起来。其中包括有6度关系。
 * 2、点击任何一个人头像，展示“被点击人”当前的一度关系 高亮。点击其他位置复原，继续点击此头像进个人中心
 * 3、高亮展示人与人之前的“证据”，并且可点击
 * 4、根据此人关系的多小，在星空图上展示“不同大小”
 * 5、手指可放大缩小星空图，且随着展示关系的滑动，滑动区域改变
 * 6、判断放大缩小范围，优先展示关系多的，头像较大的人
 * */

public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener {
    private ScaleGestureDetector mScaleGestureDetector = null;
    StarAirLayout shaderImage;
    HVScrollView hvScrollView;
    private ArrayList<AtmanRelation> sourceList = new ArrayList<>();
    TextView text_show_how;
    RelativeLayout relative_seekbar;
    SeekBar seekBar;

    private boolean moreFinger = false;//多指操作
    private int showWhat = 3;//当前显示的几度关系

    /*
     * 记录暂时按下的位置
     * */
    int fingerX = 0;
    int fingerY = 0;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    hvScrollView.scrollTo(UIUtil.dip2px(ReleaseActivity.this, 2500) - UIUtil.getWidth() / 2, UIUtil.dip2px(ReleaseActivity.this, 2500) - UIUtil.getHeight() / 2);
                    break;
                case 12:
                    if (scale < 0.7) {
                        tryScale(scale, 0.7f);
                    } else if (scale > 1.3) {
                        tryScale(scale, 1.3f);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starair_release);
        initView();
        initListener();

        mScaleGestureDetector = new ScaleGestureDetector(ReleaseActivity.this,
                new ScaleGestureListener());

        mhandler.sendEmptyMessageDelayed(11, 1000);
        initJsonData();
    }

    private void initJsonData() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "releation.json");//获取assets目录下的json文件数据
        sourceList = parseData(JsonData);//用Gson 转成实体
        Log.e("现在是否解析成了", sourceList.size() + "===================");
        shaderImage.setSourceList(sourceList);
        seekBar.setProgress(2);
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


    private void initListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 0) {
                    hvScrollView.smoothScrollTo(UIUtil.dip2px(ReleaseActivity.this, 2500) - UIUtil.getWidth() / 2, UIUtil.dip2px(ReleaseActivity.this, 2500) - UIUtil.getHeight() / 2);
                }
                clickAtmanRelation = null;
                showWhat = i + 1;
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
                        fingerX = (int) motionEvent.getX();
                        fingerY = (int) motionEvent.getY();
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
                        if (!moreFinger && clickAtmanRelation != null) {//单指 并且是点击状态
                            int x_cha = (int) (fingerX - motionEvent.getX());
                            int y_cha = (int) (fingerY - motionEvent.getY());
                            if ((Math.abs(x_cha) + Math.abs(y_cha)) < 10) {
                                shaderImage.setShowCount(showWhat);
                                clickAtmanRelation = null;
                            }
                        }
                        moreFinger = false;
                        Log.e("我看看那这里的操作哈", "手指移动upupupupupup");
                        mhandler.sendEmptyMessage(12);//手指放开来个回弹效果
                        break;
                }
                Log.e("这个判断一点没用？", moreFinger + "==============");
                return moreFinger;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hvScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (shaderImage.getCanScrollBean() != null) {
                        int left_x = shaderImage.getCanScrollBean().getLeft_x().getX();
                        int lefy_y = shaderImage.getCanScrollBean().getLeft_top_y().getY();
                        int right_x = shaderImage.getCanScrollBean().getRigth_x().getX();
                        int right_y = shaderImage.getCanScrollBean().getRight_bottom_y().getY();
                        int adddisX_left = 0;
                        int adddisX_right = 0;
                        int adddisY_left = 0;
                        int adddisY_right = 0;
                        if (scale >= 1) {
                            if (scale <= 1.3) {
                                adddisX_left = (int) (left_x - (left_x * scale - left_x) / 4) - UIUtil.getWidth() / 2;
                                adddisX_right = (int) (right_x + (right_x * scale - right_x) / 4) + UIUtil.getWidth() / 2;
                                adddisY_left = (int) (lefy_y - (lefy_y * scale - lefy_y) / 4) - UIUtil.getHeight() / 2;
                                adddisY_right = (int) (right_y + (right_y * scale - right_y) / 4) + UIUtil.getHeight() / 2;
                            } else {
                                adddisX_left = (int) (left_x - (left_x * 1.3 - left_x) / 4) - UIUtil.getWidth() / 2;
                                adddisX_right = (int) (right_x + (right_x * 1.3 - right_x) / 4) + UIUtil.getWidth() / 2;
                                adddisY_left = (int) (lefy_y - (lefy_y * 1.3 - lefy_y) / 4) - UIUtil.getHeight() / 2;
                                adddisY_right = (int) (right_y + (right_y * 1.3 - right_y) / 4) + UIUtil.getHeight() / 2;
                            }

                        } else {
                            if (scale >= 0.7) {
                                adddisX_left = (int) (left_x + (left_x - left_x * scale) / 4.5) - UIUtil.getWidth() / 2;
                                adddisX_right = (int) (right_x - (right_x - right_x * scale) / 4.5) + UIUtil.getWidth() / 2;
                                adddisY_left = (int) (lefy_y + (lefy_y - lefy_y * scale) / 4.5) - UIUtil.getHeight() / 2;
                                adddisY_right = (int) (right_y - (right_y - right_y * scale) / 4.5) + UIUtil.getHeight() / 2;
                            } else {
                                adddisX_left = (int) (left_x + (left_x - left_x * 0.7) / 4.5) - UIUtil.getWidth() / 2;
                                adddisX_right = (int) (right_x - (right_x - right_x * 0.7) / 4.5) + UIUtil.getWidth() / 2;
                                adddisY_left = (int) (lefy_y + (lefy_y - lefy_y * 0.7) / 4.5) - UIUtil.getHeight() / 2;
                                adddisY_right = (int) (right_y - (right_y - right_y * 0.7) / 4.5) + UIUtil.getHeight() / 2;
                            }

                        }


                        int minX_left = UIUtil.dip2px(ReleaseActivity.this, 2500) - UIUtil.getWidth() / 2;
                        int minY_left = UIUtil.dip2px(ReleaseActivity.this, 2500) - UIUtil.getHeight() / 2;

                        int minX_right = UIUtil.dip2px(ReleaseActivity.this, 2500) + UIUtil.getWidth() / 2;
                        int minY_right = UIUtil.dip2px(ReleaseActivity.this, 2500) + UIUtil.getHeight() / 2;


                        if (adddisX_left > minX_left) {
                            adddisX_left = minX_left;
                        }
                        if (adddisY_left > minY_left) {
                            adddisY_left = minY_left;
                        }

                        if (adddisX_right < minX_right) {
                            adddisX_right = minX_right;
                        }

                        if (adddisY_right < minY_right) {
                            adddisY_right = minY_right;
                        }


                        if (scrollX <= adddisX_left && scrollY <= adddisY_left) {
                            hvScrollView.smoothScrollTo(adddisX_left, adddisY_left);
                        } else if (scrollX <= adddisX_left && scrollY >= (adddisY_right - UIUtil.getHeight())) {
                            hvScrollView.smoothScrollTo(adddisX_left, adddisY_right - UIUtil.getHeight());
                        } else if (scrollX <= adddisX_left && adddisY_left < scrollY && scrollY < (adddisY_right - UIUtil.getHeight())) {
                            hvScrollView.smoothScrollTo(adddisX_left, scrollY);
                        } else if (scrollX >= (adddisX_right - UIUtil.getWidth()) && scrollY <= adddisY_left) {
                            hvScrollView.smoothScrollTo(adddisX_right - UIUtil.getWidth(), adddisY_left);
                        } else if (scrollX >= (adddisX_right - UIUtil.getWidth()) && scrollY >= (adddisY_right - UIUtil.getHeight())) {
                            hvScrollView.smoothScrollTo(adddisX_right - UIUtil.getWidth(), adddisY_right - UIUtil.getHeight());
                        } else if (scrollX >= (adddisX_right - UIUtil.getWidth()) && adddisY_left < scrollY && scrollY < (adddisY_right - UIUtil.getHeight())) {
                            hvScrollView.smoothScrollTo(adddisX_right - UIUtil.getWidth(), scrollY);
                        } else if (scrollY <= adddisY_left && adddisX_left < scrollX && scrollX < (adddisX_right - UIUtil.getWidth())) {
                            hvScrollView.smoothScrollTo(scrollX, adddisY_left);
                        } else if (scrollY >= (adddisY_right - UIUtil.getHeight()) && adddisX_left < scrollX && scrollX < (adddisX_right - UIUtil.getWidth())) {
                            hvScrollView.smoothScrollTo(scrollX, adddisY_right - UIUtil.getHeight());
                        }
                    } else {
                        //这里是只有一个头像的情况

                    }


                }
            });
        }
    }

    private void initView() {
        shaderImage = findViewById(R.id.shaderImage);
        shaderImage.setListener(this);
        hvScrollView = findViewById(R.id.hvScrollView);
        text_show_how = findViewById(R.id.text_show_how);
        relative_seekbar = findViewById(R.id.relative_seekbar);
        seekBar = findViewById(R.id.seekBar);
    }

    private AtmanRelation clickAtmanRelation;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_theOne:
                if (sourceList != null && sourceList.size() > 0) {
                    AtmanRelation atmanRelation = (AtmanRelation) view.getTag(R.id.image_theOne);
                    if (clickAtmanRelation != null) {
                        if (clickAtmanRelation.getGroup() == atmanRelation.getGroup()) {
                            Toast.makeText(ReleaseActivity.this, "点击头像", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    clickAtmanRelation = atmanRelation;
                    //得到当前所点击的item,拿出他的 子空间和父控件。
                    ArrayList<AtmanRelation> clickList = new ArrayList<>();
                    clickList.add(atmanRelation);
                    for (int i = 0; i < sourceList.size(); i++) {
                        for (int v = 0; v < sourceList.get(i).getParentGroups().size(); v++) {
                            if (atmanRelation.getGroup() == sourceList.get(i).getParentGroups().get(v).getGroup()) {
                                clickList.add(sourceList.get(i));
                            }
                        }

                        for (int j = 0; j < atmanRelation.getParentGroups().size(); j++) {
                            if (atmanRelation.getParentGroups().get(j).getGroup() == sourceList.get(i).getGroup()) {
                                clickList.add(sourceList.get(i));
                            }
                        }
                    }

                    shaderImage.setClickList(clickList, atmanRelation.getDegree(), atmanRelation.getGroup(), atmanRelation);
                    Animation animation = AnimationUtils.loadAnimation(ReleaseActivity.this, R.anim.scale_tip_point);
                    view.startAnimation(animation);
                }
                break;

            case R.id.zhengju_image:
                ZhengjuBean zhengjuBean = (ZhengjuBean) view.getTag();
                Toast.makeText(ReleaseActivity.this, "点击证据:：：GroupId1=======" + zhengjuBean.getGroupId1() + "------>" + "GroupId2=======" + zhengjuBean.getGroupId2(), Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private float scale = 1;
    private float preScale = 1;// 默认前一次缩放比例为1

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
            int thisb = 40;
            if (scale >= 1.3) {
                thisb = 25;
            } else if (scale < 1.3 && scale >= 1.2) {
                thisb = 30;
            } else if (scale < 1.2 && scale >= 1.1) {
                thisb = 35;
            } else if (scale < 1.1 && scale >= 1.0) {
                thisb = 40;
            } else if (scale < 1.0 && scale >= 0.9) {
                thisb = 45;
            } else if (scale < 0.9 && scale >= 0.8) {
                thisb = 50;
            } else if (scale < 0.8 && scale >= 0.7) {
                thisb = 55;
            } else {
                thisb = 55;
            }
            if (clickAtmanRelation != null) {
                shaderImage.setHaveClickPic(thisb, false);
                //得到当前所点击的item,拿出他的 子空间和父控件。
                ArrayList<AtmanRelation> clickList = new ArrayList<>();
                clickList.add(clickAtmanRelation);
                for (int i = 0; i < sourceList.size(); i++) {
                    for (int v = 0; v < sourceList.get(i).getParentGroups().size(); v++) {
                        if (clickAtmanRelation.getGroup() == sourceList.get(i).getParentGroups().get(v).getGroup()) {
                            clickList.add(sourceList.get(i));
                        }
                    }

                    for (int j = 0; j < clickAtmanRelation.getParentGroups().size(); j++) {
                        if (clickAtmanRelation.getParentGroups().get(j).getGroup() == sourceList.get(i).getGroup()) {
                            clickList.add(sourceList.get(i));
                        }
                    }
                }

                shaderImage.setClickListScale(clickList, clickAtmanRelation.getDegree(), clickAtmanRelation.getGroup(), clickAtmanRelation);
            } else {
                shaderImage.setHavePic(thisb);
            }

        }
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
}
