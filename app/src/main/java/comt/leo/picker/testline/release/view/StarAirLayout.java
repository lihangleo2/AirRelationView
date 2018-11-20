package comt.leo.picker.testline.release.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.ArrayList;

import comt.leo.picker.testline.CircleImageView;
import comt.leo.picker.testline.R;
import comt.leo.picker.testline.UIUtil;
import comt.leo.picker.testline.release.bean.AtmanRelation;
import comt.leo.picker.testline.release.bean.CanScrollBean;
import comt.leo.picker.testline.release.bean.PointLeo;
import comt.leo.picker.testline.release.bean.PointScroll;
import comt.leo.picker.testline.release.bean.RectPoint;
import comt.leo.picker.testline.release.bean.RelationBean;
import comt.leo.picker.testline.release.bean.RelationParent;
import comt.leo.picker.testline.release.bean.ZhengjuBean;


public class StarAirLayout extends FrameLayout {
    private Paint mPaint;
    FrameLayout layouPoints;
    WaveView waveView;
    private LineConcentView shaderImageView;
    private OnClickListener onClickListener;

    private CircleImageView image_theOne;
    private ArrayList<AtmanRelation> sourceList;
    private ArrayList<PointLeo> points = new ArrayList<>();//用于连线的。
    private ArrayList<RectPoint> rects = new ArrayList<>();//用于判断重叠的
    private ArrayList<RelationBean> reList_2 = new ArrayList<>();//第二度关系的集合，目的是 为了绘制完一度后再绘制二度
    private ArrayList<RelationBean> reList_3 = new ArrayList<>();//第三度关系的集合，目的是 绘制完二度再开始 三度
    private ArrayList<RelationBean> reList_4 = new ArrayList<>();
    private ArrayList<RelationBean> reList_5 = new ArrayList<>();
    private ArrayList<RelationBean> reList_6 = new ArrayList<>();
    private ArrayList<AtmanRelation> clickList;//当前点击需要高亮的集合

    private ArrayList<RectPoint> rectsZhengj = new ArrayList<>();//用于判断重叠的(只限制于证据里)

    /**
     * 这里好坑啊好坑啊
     */
    private ArrayList<AtmanRelation> otherLinePdList = new ArrayList<>();


    private ArrayList<AtmanRelation> relation_source_2;
    private ArrayList<AtmanRelation> relation_source_3;
    private ArrayList<AtmanRelation> relation_source_4;
    private ArrayList<AtmanRelation> relation_source_5;
    private ArrayList<AtmanRelation> relation_source_6;

    /***/


    private boolean isShow2 = true;//是否显示二度关系
    private boolean isShow3 = true;
    private boolean isShow4 = false;
    private boolean isShow5 = false;
    private boolean isShow6 = false;

    private CanScrollBean canScrollBean;//用于判断可滑动的区域
    private int HavePic = 40;
    private boolean isShowClickAnim = false;


    public void setHavePic(int havePic) {
        this.HavePic = havePic;
        if (sourceList != null && sourceList.size() > 0) {

        } else {
            return;
        }
        addPoint();
    }

    public void setHaveClickPic(int havePic, boolean isShowClickAnim) {
        this.isShowClickAnim = isShowClickAnim;
        this.HavePic = havePic;
        if (sourceList != null && sourceList.size() > 0) {

        } else {
            return;
        }
    }

    public void setShowCount(int count) {
        if (count == 1) {
            isShow2 = false;//是否显示二度关系
            isShow3 = false;
            isShow4 = false;
            isShow5 = false;
            isShow6 = false;
        } else if (count == 2) {
            isShow2 = true;
            isShow3 = false;
            isShow4 = false;
            isShow5 = false;
            isShow6 = false;
        } else if (count == 3) {
            isShow2 = true;
            isShow3 = true;
            isShow4 = false;
            isShow5 = false;
            isShow6 = false;
        } else if (count == 4) {
            isShow2 = true;
            isShow3 = true;
            isShow4 = true;
            isShow5 = false;
            isShow6 = false;
        } else if (count == 5) {
            isShow2 = true;
            isShow3 = true;
            isShow4 = true;
            isShow5 = true;
            isShow6 = false;
        } else if (count == 6) {
            isShow2 = true;
            isShow3 = true;
            isShow4 = true;
            isShow5 = true;
            isShow6 = true;
        }


        if (clickList != null) {
            //滑动了进度条，将点击状态取消
            clickList.clear();
            clickList = null;
            degreeRelation = -1;
            clickNode = -1;
            nowClickAtmanRelation = null;
        }
        Log.e("我去总有这么多问题", isShow2 + "===========");
        if (sourceList != null && sourceList.size() > 0) {
            addPoint();
        }

    }


    public StarAirLayout(Context context) {
        this(context, null);
    }

    public StarAirLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarAirLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initView(context, attrs);

    }


    AtmanRelation myselfAtman;

    /*
     * 登录情况下，传入
     * */
//    public void setMyself(User mUser) {
//        sourceList = null;
//        isShow2 = true;
//        isShow3 = true;
//        isShow4 = false;
//        isShow5 = false;
//        isShow6 = false;
//        canScrollBean = null;
//        if (clickList != null) {
//            //滑动了进度条，将点击状态取消
//            clickList.clear();
//            clickList = null;
//            degreeRelation = -1;
//            clickNode = -1;
//            nowClickAtmanRelation = null;
//        }
//
//        points.clear();
//        rects.clear();
//        reList_2.clear();
//        reList_3.clear();
//        reList_4.clear();
//        reList_5.clear();
//        reList_6.clear();
//        otherLinePdList.clear();
//        layouPoints.removeAllViews();
//
//        image_theOne.setBorderColor(getResources().getColor(R.color.jidu_1));
//        image_theOne.setBorderWidth(UIUtil.dip2px(getContext(), 2));
//        Glide.with(getContext())
//                .load(SystemConst.IMAGE_HEAD + mUser.getIcon())
//                .asBitmap()
//                .dontAnimate()
//                .error(R.mipmap.default_head)
//                .into(image_theOne);
//
//        TextView textView = new TextView(getContext());
//        LayoutParams leoParams_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        leoParams_text.height = UIUtil.dip2px(getContext(), 20);
//        leoParams_text.leftMargin = UIUtil.dip2px(getContext(), 5000) / 2 + UIUtil.dip2px(getContext(), 17);
//        leoParams_text.topMargin = UIUtil.dip2px(getContext(), 5000) / 2 - UIUtil.dip2px(getContext(), 10);
//
//        textView.setTextColor(getResources().getColor(R.color.theme_text_black2d));
//        textView.setTextSize(UIUtil.dip2px(getContext(), 4));
//        textView.setGravity(Gravity.CENTER);
//        if (!TextUtils.isEmpty(mUser.getNickName())) {
//            if (mUser.getNickName().length() > 6) {
//                String newStr = mUser.getNickName().substring(0, 6);
//                textView.setText(newStr + "...");
//            } else {
//                textView.setText(mUser.getNickName());
//            }
//        }
//        layouPoints.addView(textView, leoParams_text);
//
//    }


    public void setSourceList(ArrayList<AtmanRelation> sourceList) {
        waveView.start();
        this.sourceList = sourceList;
        isShow2 = true;
        isShow3 = true;
        isShow4 = false;
        isShow5 = false;
        isShow6 = false;
        if (clickList != null) {
            //滑动了进度条，将点击状态取消
            clickList.clear();
            clickList = null;
            degreeRelation = -1;
            clickNode = -1;
            nowClickAtmanRelation = null;
        }

        if (sourceList != null && sourceList.size() > 0) {

        } else {//没有数据则不进行绘制
            return;
        }

        relation_source_2 = getDeree(2);
        relation_source_3 = getDeree(3);
        relation_source_4 = getDeree(4);
        relation_source_5 = getDeree(5);
        relation_source_6 = getDeree(6);

        /**试试试试**/
        //找到自己这个点。并把中心点带入
        ArrayList<AtmanRelation> myAtmanList = getDeree(0);
        myselfAtman = myAtmanList.get(0);
        myAtmanList.get(0).setY_center(UIUtil.dip2px(getContext(), 5000) / 2);
        myAtmanList.get(0).setX_center(UIUtil.dip2px(getContext(), 5000) / 2);
        myAtmanList.get(0).setRectPoint(new RectPoint(UIUtil.dip2px(getContext(), 5000) / 2 - UIUtil.dip2px(getContext(), 23), UIUtil.dip2px(getContext(), 5000) / 2 - UIUtil.dip2px(getContext(), 23)
                , UIUtil.dip2px(getContext(), 5000) / 2 + UIUtil.dip2px(getContext(), 23), UIUtil.dip2px(getContext(), 5000) / 2 + UIUtil.dip2px(getContext(), 23)));
//        sourceList.remove(myselfAtman);
        //这里接上假数据别面判断 唯一没有ParentGroups 我这个点
        ArrayList<RelationParent> arr = new ArrayList<>();
        RelationParent jia = new RelationParent();
        jia.setGroup(-11);
        arr.add(jia);

        myselfAtman.setParentGroups(arr);
        if (onClickListener != null) {
            image_theOne.setTag(R.id.image_theOne, myselfAtman);
            image_theOne.setOnClickListener(onClickListener);
        }
        addPoint();
    }

    private int degreeRelation = -1;
    private int clickNode = -1;
    private AtmanRelation nowClickAtmanRelation;

    public void setClickList(ArrayList<AtmanRelation> clickList, int degreeRelation, int clickNode, AtmanRelation nowClickAtmanRelation) {
        isShowClickAnim = true;
        this.clickList = clickList;
        this.degreeRelation = degreeRelation;
        this.clickNode = clickNode;
        this.nowClickAtmanRelation = nowClickAtmanRelation;
        rectsZhengj.clear();
        for (int i = 0; i < clickList.size(); i++) {
            rectsZhengj.add(clickList.get(i).getRectPoint());
        }

        addPoint();
    }


    //这是手指放大缩小运行的
    public void setClickListScale(ArrayList<AtmanRelation> clickList, int degreeRelation, int clickNode, AtmanRelation nowClickAtmanRelation) {
        this.clickList = clickList;
        this.degreeRelation = degreeRelation;
        this.clickNode = clickNode;
        this.nowClickAtmanRelation = nowClickAtmanRelation;
        rectsZhengj.clear();
        for (int i = 0; i < clickList.size(); i++) {
            rectsZhengj.add(clickList.get(i).getRectPoint());
        }

        addPoint();
    }


    public void setListener(OnClickListener listener) {
        this.onClickListener = listener;
    }


    private void initView(Context context, AttributeSet attrs) {
        View imgPointLayout = inflate(context, R.layout.layout_starair_, this);
        layouPoints = (FrameLayout) imgPointLayout.findViewById(R.id.layouPoints);
        shaderImageView = (LineConcentView) imgPointLayout.findViewById(R.id.ShaderImageView);
        image_theOne = (CircleImageView) imgPointLayout.findViewById(R.id.image_theOne);
        waveView = imgPointLayout.findViewById(R.id.waveView);
        waveView.setSpeed(1000);
        waveView.setColor(getResources().getColor(R.color.jidu_1));
    }


    public ArrayList<AtmanRelation> getDeree(int Deree) {//获取几度关系
        ArrayList<AtmanRelation> arrayList = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).getDegree() == Deree) {
                arrayList.add(sourceList.get(i));
                if (Deree == 1 || Deree == 0) {
                }
            }
        }
        return arrayList;
    }


    public ArrayList<AtmanRelation> getsonList(AtmanRelation atmanRelation) {//获取这个节点下 所有的集合
        ArrayList<AtmanRelation> arrayList = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).getParentGroups() != null && sourceList.get(i).getParentGroups().size() > 0) {
                if (sourceList.get(i).getParentGroups().get(0).getGroup() == atmanRelation.getGroup()) {
                    arrayList.add(sourceList.get(i));
                }
            } else {
                sourceList.remove(i);
            }
        }

        return arrayList;
    }


    public boolean checkClickIn(int node) {

        if (clickList != null) {
            for (int i = 0; i < clickList.size(); i++) {
                if (clickList.get(i).getGroup() == node) {
                    return true;//在里面的话 那么现实高亮
                }
            }
            return false;//不在里面，其他的显示暗色
        } else {
            return true;//当没有点击的时候 显示高亮
        }

    }


    public void addCanScroll(int x, int y) {
        PointScroll canGo = new PointScroll(x, y);
        if (x < canScrollBean.getLeft_x().getX()) {
            canScrollBean.setLeft_x(canGo);
        } else if (x > canScrollBean.getRigth_x().getX()) {
            canScrollBean.setRigth_x(canGo);
        } else if (y < canScrollBean.getLeft_top_y().getY()) {
            canScrollBean.setLeft_top_y(canGo);
        } else if (y > canScrollBean.getRight_bottom_y().getY()) {
            canScrollBean.setRight_bottom_y(canGo);
        }
    }

    public CanScrollBean getCanScrollBean() {
        return canScrollBean;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void addPoint() {
        points.clear();
        rects.clear();
        reList_2.clear();
        reList_3.clear();
        reList_4.clear();
        reList_5.clear();
        reList_6.clear();
        otherLinePdList.clear();
        layouPoints.removeAllViews();
        canScrollBean = new CanScrollBean();//用于判断可滑动的区域


        int objectMy = 0;//当前控件的大小
        int numMy = myselfAtman.getSonCount();
        objectMy = 25 + numMy - 1;
        if (objectMy >= HavePic) {
            image_theOne.setBorderColor(getResources().getColor(R.color.jidu_1));
            image_theOne.setBorderWidth(UIUtil.dip2px(getContext(), 2));
            image_theOne.setImageResource(setHeadNoNet(myselfAtman.getHeaderUrl()));
        } else {
            image_theOne.setImageResource(R.mipmap.transt_test);
            image_theOne.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_1));
        }

        RelativeLayout.LayoutParams reParams = (RelativeLayout.LayoutParams) image_theOne.getLayoutParams();
        reParams.height = UIUtil.dip2px(getContext(), objectMy);
        reParams.width = UIUtil.dip2px(getContext(), objectMy);

        RelativeLayout.LayoutParams reParamsWave = (RelativeLayout.LayoutParams) waveView.getLayoutParams();
        reParamsWave.height = UIUtil.dip2px(getContext(), objectMy + 45);
        reParamsWave.width = UIUtil.dip2px(getContext(), objectMy + 45);


        //我的在里面，那么久先是高亮
        if (checkClickIn(myselfAtman.getGroup())) {//判断我是不是在里面
            image_theOne.setAlpha(1.0f);
            waveView.start();
            if (clickList != null) {//如果点击集合里不为空才加名字
                TextView textView = new TextView(getContext());
                LayoutParams leoParams_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leoParams_text.height = UIUtil.dip2px(getContext(), 20);
                leoParams_text.leftMargin = UIUtil.dip2px(getContext(), 5000) / 2 + UIUtil.dip2px(getContext(), objectMy / 2);
                leoParams_text.topMargin = UIUtil.dip2px(getContext(), 5000) / 2 - UIUtil.dip2px(getContext(), 10);

                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setTextSize(UIUtil.dip2px(getContext(), 4));
                textView.setGravity(Gravity.CENTER);
                if (!TextUtils.isEmpty(myselfAtman.getNickName())) {
                    if (myselfAtman.getNickName().length() > 6) {
                        String newStr = myselfAtman.getNickName().substring(0, 6);
                        textView.setText(newStr + "...");
                    } else {
                        textView.setText(myselfAtman.getNickName());
                    }
                }
                layouPoints.addView(textView, leoParams_text);
            }


        } else {
            image_theOne.setAlpha(0.2f);
            waveView.stopImmediately();
        }

        /***
         * 如果是一度关系
         */
        int circle = UIUtil.dip2px(getContext(), 130);//当前一度关系的长度
        ArrayList<AtmanRelation> oneAtmanList = getDeree(1);
        otherLinePdList.addAll(oneAtmanList);

        int number = oneAtmanList.size();//当前一度关系的个数
        int x_center = UIUtil.dip2px(getContext(), 5000) / 2;
        int y_center = UIUtil.dip2px(getContext(), 5000) / 2;
        PointScroll canGo = new PointScroll(x_center, y_center);
        canScrollBean.setLeft_top_y(canGo);
        canScrollBean.setLeft_x(canGo);
        canScrollBean.setRight_bottom_y(canGo);
        canScrollBean.setRigth_x(canGo);
        int firstJD = 0;//一度关系开始的角度是从 0 开始的，
        for (int i = 0; i < oneAtmanList.size(); i++) {
            AtmanRelation itemBean = oneAtmanList.get(i);
            //相隔的角度就是

            int jiaod;//这里的操作是，一度关系不能为偶数。因为这里要考虑到连线，避免有直线覆盖原来的线
            if (oneAtmanList.size() % 2 == 0) {
                jiaod = 360 / (oneAtmanList.size() + 1);
            } else {
                jiaod = 360 / oneAtmanList.size();
            }

            int currentJD = 0 + jiaod * (i + 1);

            int X1 = 0;
            int Y1 = 0;

            if (currentJD != 0 && currentJD != 180 && currentJD != 360) {//别面有错误出现
                X1 = (int) (x_center + circle * (Math.cos(Math.PI * currentJD / 180)));
                Y1 = (int) (y_center + circle * (Math.sin(Math.PI * currentJD / 180)));
            } else {
                if (currentJD == 0 || currentJD == 360) {
                    X1 = x_center + circle;
                    Y1 = y_center;
                } else {
                    Y1 = y_center;
                    X1 = x_center - circle;
                }

            }

            int objectBig = 0;//当前控件的大小
            int num = itemBean.getSonCount() + itemBean.getParentGroups().size();
            objectBig = 25 + num - 1;

            int trueX1 = X1 - UIUtil.dip2px(getContext(), objectBig / 2);//减去自身控件的长度
            int trueY1 = Y1 - UIUtil.dip2px(getContext(), objectBig / 2);//减去自身控件的长度


            //当前点的所在区域则是
            RectPoint leoPoint = new RectPoint(trueX1 - UIUtil.dip2px(getContext(), objectBig / 2 + 13), trueY1 - UIUtil.dip2px(getContext(), objectBig / 2 + 13),
                    trueX1 + UIUtil.dip2px(getContext(), objectBig / 2 + 13), trueY1 + UIUtil.dip2px(getContext(), objectBig / 2 + 13));

            if (checkHaveRect(leoPoint)) {//检查是否有重叠，true为有重叠。要增加半径的长度

                repetAdd(x_center, y_center, circle, currentJD, 1, itemBean);

            } else {//false 没有重叠，则直接添加

                CircleImageView imageView_bottom_yy = new CircleImageView(getContext());
                imageView_bottom_yy.setId(R.id.image_theOne);
                LayoutParams leoParams_bottom_yy = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leoParams_bottom_yy.width = UIUtil.dip2px(getContext(), objectBig);
                leoParams_bottom_yy.height = UIUtil.dip2px(getContext(), objectBig);
                leoParams_bottom_yy.leftMargin = trueX1;
                leoParams_bottom_yy.topMargin = trueY1;

                if (objectBig >= HavePic) {
                    if (TextUtils.isEmpty(itemBean.getUserId()) && TextUtils.isEmpty(itemBean.getLable())) {
                        imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.noUser));
                    } else {
                        imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_1));
                    }
                    imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
                    imageView_bottom_yy.setImageResource(setHeadNoNet(itemBean.getHeaderUrl()));
                } else {
                    if (TextUtils.isEmpty(itemBean.getUserId()) && TextUtils.isEmpty(itemBean.getLable())) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_nouser));
                    } else {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_1));
                    }
                }


                layouPoints.addView(imageView_bottom_yy, leoParams_bottom_yy);
                addCanScroll(trueX1, trueY1);


                if (itemBean.getGroup() == clickNode && isShowClickAnim) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_tip_point);
                    imageView_bottom_yy.startAnimation(animation);
                }


                if (checkClickIn(itemBean.getGroup())) {//判断我是不是在里面
                    imageView_bottom_yy.setAlpha(1.0f);
                    Log.i("这里的bug到底咱那里", "3333");
                    if (nowClickAtmanRelation != null) {
                        if (degreeRelation > 1) {
                            points.add(new PointLeo(x_center, y_center, X1, Y1, "暗色"));
                        } else {

                            if (x_center == nowClickAtmanRelation.getX_center() && y_center == nowClickAtmanRelation.getY_center()) {
                                points.add(new PointLeo(x_center, y_center, X1, Y1, "高亮"));
                            } else if (X1 == nowClickAtmanRelation.getX_center() && Y1 == nowClickAtmanRelation.getY_center()) {
                                points.add(new PointLeo(x_center, y_center, X1, Y1, "高亮"));
                            } else {
                                points.add(new PointLeo(x_center, y_center, X1, Y1, "暗色"));
                            }

                        }
                    } else {
                        points.add(new PointLeo(x_center, y_center, X1, Y1, "默认显示"));
                    }

                    if (clickList != null) {
                        TextView textView = new TextView(getContext());
                        LayoutParams leoParams_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        leoParams_text.height = UIUtil.dip2px(getContext(), 20);
                        leoParams_text.leftMargin = X1 + UIUtil.dip2px(getContext(), objectBig / 2);
                        leoParams_text.topMargin = Y1 - UIUtil.dip2px(getContext(), 10);


                        textView.setTextColor(getResources().getColor(R.color.white));
                        textView.setTextSize(UIUtil.dip2px(getContext(), 4));
                        textView.setGravity(Gravity.CENTER);
                        if (!TextUtils.isEmpty(itemBean.getNickName())) {
                            if (itemBean.getNickName().length() > 6) {
                                String newStr = itemBean.getNickName().substring(0, 6);
                                textView.setText(newStr + "...");
                            } else {
                                textView.setText(itemBean.getNickName());
                            }
                        }

                        layouPoints.addView(textView, leoParams_text);
                    }

                } else {
                    imageView_bottom_yy.setAlpha(0.2f);
                    points.add(new PointLeo(x_center, y_center, X1, Y1, "暗色"));
                }

                // 把当前的中心点坐标  和当前点的所在区域存储起来。以便放置证据的时候 不覆盖 判断
                itemBean.setX_center(X1);
                itemBean.setY_center(Y1);
                itemBean.setRectPoint(new RectPoint(trueX1 - UIUtil.dip2px(getContext(), objectBig / 2), trueY1 - UIUtil.dip2px(getContext(), objectBig / 2),
                        trueX1 + UIUtil.dip2px(getContext(), objectBig / 2), trueY1 + UIUtil.dip2px(getContext(), objectBig / 2)));

                rects.add(leoPoint);
                Log.e("我看到底是谁", "+++++++++++++++++1");

                /**
                 * 这里是添加成功关系以后，进行的下一层 关系的绘制
                 * */

                ArrayList<AtmanRelation> sonList = getsonList(itemBean);//一度节点下二度的关系集合
                if (sonList.size() > 0) {
                    int numberNex = sonList.size();//下一层关系有几个。（注意这里下一层的一度关系是不包括父类的。而且算角度的时候要加个1）
                    reList_2.add(new RelationBean(circle, numberNex, X1, Y1, currentJD + firstJD + 180, 2, sonList));//倒数第二个参数，第几度关系
                }

                if (onClickListener != null) {
                    imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                    imageView_bottom_yy.setOnClickListener(onClickListener);
                }


            }

            if (isShow2) {//这里显示二度
                if (i == number - 1) {
                    if (reList_2.size() > 0) {
                        for (int j = 0; j < reList_2.size(); j++) {
                            RelationBean relationBean = reList_2.get(j);
                            if (relationBean.getNumberNex() > 0) {
                                addNexAbout(relationBean.getCircle(), relationBean.getNumberNex(), relationBean.getX1(), relationBean.getY1(), relationBean.getTrueJD(), relationBean.getRelation(), relationBean.getAtmanRelations(), j);
                            }
                        }
                    } else {//这里的判断是。如果没有二度只有一度关系。此时点击也需要展示证据
                        addZhengju(1);
                        addOtherLine();
                    }
                }
            } else {//这里不显示二度，此时有点击clickList集合的时候，看看是否有证据，将证据添加进去。注意一定要是绘制完整个一度时候才添加

                if (i == number - 1) {//代表绘制完整个一度了。拿clickList一点在clickList里找到父类。确定2个点加上证据
                    addZhengju(1);
                    addOtherLine();
                }

            }

        }
        shaderImageView.setLines(points);
    }


    public void addOtherLine() {
        /*************************************************************************************/
        for (int b = 0; b < otherLinePdList.size(); b++) {
            AtmanRelation otherRelation = otherLinePdList.get(b);

            if (nowClickAtmanRelation != null && otherRelation.getGroup() != nowClickAtmanRelation.getGroup()) {
                for (int g = 0; g < nowClickAtmanRelation.getParentGroups().size(); g++) {
                    if (nowClickAtmanRelation.getParentGroups().get(g).getGroup() == otherRelation.getGroup()) {

                        //中心点在
                        int zheng_x_center = otherRelation.getX_center() + (nowClickAtmanRelation.getX_center() - otherRelation.getX_center()) / 2;
                        int zheng_y_center = otherRelation.getY_center() + (nowClickAtmanRelation.getY_center() - otherRelation.getY_center()) / 2;

                        RelativeLayout relativeLayout = (RelativeLayout) inflate(getContext(), R.layout.item_zhengju_release, null);
                        TextView text_zhengjCount = relativeLayout.findViewById(R.id.text_zhengjCount);
                        ImageView zhengju_image = relativeLayout.findViewById(R.id.zhengju_image);
                        ZhengjuBean zhengjuBean = new ZhengjuBean();
                        zhengjuBean.setGroupId1(otherRelation.getGroup() + "");
                        zhengjuBean.setGroupId2(nowClickAtmanRelation.getGroup() + "");
                        zhengjuBean.setPicCount(nowClickAtmanRelation.getParentGroups().get(g).getPictureCount());
                        zhengju_image.setTag(zhengjuBean);
                        zhengju_image.setOnClickListener(onClickListener);
                        if (nowClickAtmanRelation.getParentGroups().get(g).getPictureCount() > 99) {
                            text_zhengjCount.setText("99");
                        } else {
                            text_zhengjCount.setText(nowClickAtmanRelation.getParentGroups().get(g).getPictureCount() + "");
                        }
                        LayoutParams zhengjuLayout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        zhengjuLayout.height = UIUtil.dip2px(getContext(), 25);
                        zhengjuLayout.width = UIUtil.dip2px(getContext(), 25);
                        zhengjuLayout.leftMargin = zheng_x_center - UIUtil.dip2px(getContext(), 12.5f);
                        zhengjuLayout.topMargin = zheng_y_center - UIUtil.dip2px(getContext(), 12.5f);
                        layouPoints.addView(relativeLayout, zhengjuLayout);
                        points.add(new PointLeo(otherRelation.getX_center(), otherRelation.getY_center(), nowClickAtmanRelation.getX_center(), nowClickAtmanRelation.getY_center(), "高亮"));
                    }
                }
            }

            if (otherRelation.getParentGroups().size() > 1) {
                for (int d = 1; d < otherRelation.getParentGroups().size(); d++) {
                    RelationParent woqu = otherRelation.getParentGroups().get(d);
                    for (int e = 0; e < otherLinePdList.size(); e++) {
                        if (woqu.getGroup() == otherLinePdList.get(e).getGroup()) {
                            if (clickNode == -1) {
                                points.add(new PointLeo(otherRelation.getX_center(), otherRelation.getY_center(), otherLinePdList.get(e).getX_center(), otherLinePdList.get(e).getY_center(), "默认显示"));

                            } else {
                                if (woqu.getGroup() == clickNode) {
                                    //中心点在
                                    int zheng_x_center = otherRelation.getX_center() + (otherLinePdList.get(e).getX_center() - otherRelation.getX_center()) / 2;
                                    int zheng_y_center = otherRelation.getY_center() + (otherLinePdList.get(e).getY_center() - otherRelation.getY_center()) / 2;

                                    RelativeLayout relativeLayout = (RelativeLayout) inflate(getContext(), R.layout.item_zhengju_release, null);
                                    TextView text_zhengjCount = relativeLayout.findViewById(R.id.text_zhengjCount);
                                    ImageView zhengju_image = relativeLayout.findViewById(R.id.zhengju_image);
                                    ZhengjuBean zhengjuBean = new ZhengjuBean();
                                    zhengjuBean.setGroupId1(otherRelation.getGroup() + "");
                                    zhengjuBean.setGroupId2(woqu.getGroup() + "");
                                    zhengjuBean.setPicCount(woqu.getPictureCount());
                                    zhengju_image.setTag(zhengjuBean);
                                    zhengju_image.setOnClickListener(onClickListener);

                                    if (woqu.getPictureCount() > 99) {
                                        text_zhengjCount.setText("99");
                                    } else {
                                        text_zhengjCount.setText(woqu.getPictureCount() + "");
                                    }
                                    LayoutParams zhengjuLayout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    zhengjuLayout.height = UIUtil.dip2px(getContext(), 25);
                                    zhengjuLayout.width = UIUtil.dip2px(getContext(), 25);
                                    zhengjuLayout.leftMargin = zheng_x_center - UIUtil.dip2px(getContext(), 12.5f);
                                    zhengjuLayout.topMargin = zheng_y_center - UIUtil.dip2px(getContext(), 12.5f);
                                    layouPoints.addView(relativeLayout, zhengjuLayout);


                                    points.add(new PointLeo(otherRelation.getX_center(), otherRelation.getY_center(), otherLinePdList.get(e).getX_center(), otherLinePdList.get(e).getY_center(), "高亮"));

                                } else {
                                    points.add(new PointLeo(otherRelation.getX_center(), otherRelation.getY_center(), otherLinePdList.get(e).getX_center(), otherLinePdList.get(e).getY_center(), "暗色"));

                                }
                            }


                        }
                    }

                }

            }
        }
        /*************************************************************************************/
    }


    public void addZhengju(int showDegree) {
        if (clickList != null) {
            for (int m = 0; m < clickList.size(); m++) {
                AtmanRelation findPairBean = clickList.get(m);
                if (findPairBean.getDegree() <= showDegree) {
                    if (nowClickAtmanRelation != null && nowClickAtmanRelation.getDegree() != 0) {
                        if (findPairBean.getX_center() == nowClickAtmanRelation.getX_center() && findPairBean.getY_center() == nowClickAtmanRelation.getY_center()) {
                            trueZheng(findPairBean);
                        } else {
                            if (findPairBean.getParentGroups().get(0).getGroup() == nowClickAtmanRelation.getGroup()) {
                                trueZheng(findPairBean);
                            }

                        }
                    } else {
                        trueZheng(findPairBean);
                    }


                }
            }
        }
    }


    public void trueZheng(AtmanRelation findPairBean) {
        for (int n = 0; n < clickList.size(); n++) {
            if (findPairBean.getParentGroups().get(0).getGroup() == clickList.get(n).getGroup()) {//说明找到父类了。那么也确定了2个点。把证据加上去
                //中心点在
                int zheng_x_center = findPairBean.getX_center() + (clickList.get(n).getX_center() - findPairBean.getX_center()) / 2;
                int zheng_y_center = findPairBean.getY_center() + (clickList.get(n).getY_center() - findPairBean.getY_center()) / 2;


                int trueXX1 = zheng_x_center - UIUtil.dip2px(getContext(), 12.5f);
                int trueYY1 = zheng_y_center - UIUtil.dip2px(getContext(), 12.5f);

                RectPoint leoPoint = new RectPoint(trueXX1 - UIUtil.dip2px(getContext(), 12.5f), trueYY1 - UIUtil.dip2px(getContext(), 12.5f),
                        trueXX1 + UIUtil.dip2px(getContext(), 12.5f), trueYY1 + UIUtil.dip2px(getContext(), 12.5f));


                if (findPairBean.getParentGroups().get(0).getPictureCount() > 0) {//证据数量大于0  才能加上去
                    if (checkHaveZhengjuRect(leoPoint)) {
                        Log.e("要死了来一个这个bug", "包含在内了！！！");
                        zhengjuNoG(findPairBean.getX_center(), findPairBean.getY_center(), (clickList.get(n).getX_center() - findPairBean.getX_center()), (clickList.get(n).getY_center() - findPairBean.getY_center()), findPairBean, 1);
                    } else {

                        RelativeLayout relativeLayout = (RelativeLayout) inflate(getContext(), R.layout.item_zhengju_release, null);
                        TextView text_zhengjCount = relativeLayout.findViewById(R.id.text_zhengjCount);
                        ImageView zhengju_image = relativeLayout.findViewById(R.id.zhengju_image);
                        ZhengjuBean zhengjuBean = new ZhengjuBean();
                        zhengjuBean.setGroupId1(findPairBean.getGroup() + "");
                        zhengjuBean.setGroupId2(findPairBean.getParentGroups().get(0).getGroup() + "");
                        zhengjuBean.setPicCount(findPairBean.getParentGroups().get(0).getPictureCount());
                        zhengju_image.setTag(zhengjuBean);
                        zhengju_image.setOnClickListener(onClickListener);
                        if (findPairBean.getParentGroups().get(0).getPictureCount() > 99) {
                            text_zhengjCount.setText("99");
                        } else {
                            text_zhengjCount.setText(findPairBean.getParentGroups().get(0).getPictureCount() + "");
                        }
                        LayoutParams zhengjuLayout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        zhengjuLayout.height = UIUtil.dip2px(getContext(), 25);
                        zhengjuLayout.width = UIUtil.dip2px(getContext(), 25);
                        zhengjuLayout.leftMargin = zheng_x_center - UIUtil.dip2px(getContext(), 12.5f);
                        zhengjuLayout.topMargin = zheng_y_center - UIUtil.dip2px(getContext(), 12.5f);
                        layouPoints.addView(relativeLayout, zhengjuLayout);
                        rectsZhengj.add(leoPoint);

                    }
                }

            }
        }
    }


    public void zhengjuNoG(int startX, int startY, int distanceX, int distanceY, AtmanRelation findPairBean, int numCount) {

        int zheng_x_center = startX + distanceX * numCount / 50;
        int zheng_y_center = startY + distanceY * numCount / 50;

        int trueXX1 = zheng_x_center - UIUtil.dip2px(getContext(), 15);
        int trueYY1 = zheng_y_center - UIUtil.dip2px(getContext(), 15);

        RectPoint leoPoint = new RectPoint(trueXX1 - UIUtil.dip2px(getContext(), 15), trueYY1 - UIUtil.dip2px(getContext(), 15),
                trueXX1 + UIUtil.dip2px(getContext(), 15), trueYY1 + UIUtil.dip2px(getContext(), 15));

        if (checkHaveZhengjuRect(leoPoint)) {
            int thisCount = numCount + 1;
            Log.e("我看我这里出现了多少次", thisCount + "================");
            if (thisCount >= 50) {
                return;
            } else {
                zhengjuNoG(zheng_x_center, zheng_y_center, distanceX, distanceY, findPairBean, thisCount);
            }
        } else {
            Log.e("成功总是困难的啊", zheng_x_center + "==========");
            Log.e("成功总是困难的啊", zheng_y_center + "==========");
            RelativeLayout relativeLayout = (RelativeLayout) inflate(getContext(), R.layout.item_zhengju_release, null);
            TextView text_zhengjCount = relativeLayout.findViewById(R.id.text_zhengjCount);
            ImageView zhengju_image = relativeLayout.findViewById(R.id.zhengju_image);
            ZhengjuBean zhengjuBean = new ZhengjuBean();
            zhengjuBean.setGroupId1(findPairBean.getGroup() + "");
            zhengjuBean.setGroupId2(findPairBean.getParentGroups().get(0).getGroup() + "");
            zhengjuBean.setPicCount(findPairBean.getParentGroups().get(0).getPictureCount());
            zhengju_image.setTag(zhengjuBean);
            zhengju_image.setOnClickListener(onClickListener);
            if (findPairBean.getParentGroups().get(0).getPictureCount() > 99) {
                text_zhengjCount.setText("99");
            } else {
                text_zhengjCount.setText(findPairBean.getParentGroups().get(0).getPictureCount() + "");
            }
            LayoutParams zhengjuLayout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            zhengjuLayout.height = UIUtil.dip2px(getContext(), 30);
            zhengjuLayout.width = UIUtil.dip2px(getContext(), 30);
            zhengjuLayout.leftMargin = zheng_x_center - UIUtil.dip2px(getContext(), 15);
            zhengjuLayout.topMargin = zheng_y_center - UIUtil.dip2px(getContext(), 15);
            layouPoints.addView(relativeLayout, zhengjuLayout);
            rectsZhengj.add(leoPoint);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addNexAbout(int circleNex, int numberNex, int x_centerNex, int y_centerNex, int nowJD, int relation, ArrayList<AtmanRelation> atmanRelations, int index) {

        int circle = circleNex;//半径的长度
        int number = numberNex + 1;//当前关系个数。当然要加上父类那条线
        int x_center = x_centerNex;//目前坐标中心点
        int y_center = y_centerNex;//目前坐标重新点
        int trueJD = nowJD % 360 + 90;
        Log.e("这里为什么会越界啊", numberNex + "==========");
        for (int i = 0; i < numberNex; i++) {
            AtmanRelation itemBean = atmanRelations.get(i);
            //相隔的角度就是
            int jiaod;
            if (number % 2 == 0) {//这里的操作是，一度关系不能为偶数。因为这里要考虑到连线，避免有直线覆盖原来的线
                jiaod = 180 / (number + 1);
            } else {
                jiaod = 180 / number;
            }

            int currentJD = trueJD + jiaod * (i + 1);
            Log.i("为什么二度关系这里有问题了呢", currentJD + "=========");

            int X1 = 0;
            int Y1 = 0;
            if (currentJD != 0 && currentJD != 180 && currentJD != 360) {//别面有错误出现
                X1 = (int) (x_center + circle * (Math.cos(Math.PI * currentJD / 180)));
                Y1 = (int) (y_center + circle * (Math.sin(Math.PI * currentJD / 180)));
            } else {
                if (currentJD == 0 || currentJD == 360) {
                    X1 = x_center + circle;
                    Y1 = y_center;
                } else {
                    Y1 = y_center;
                    X1 = x_center - circle;
                }

            }

            int objectBig = 0;//当前控件的大小
            int num = itemBean.getSonCount() + itemBean.getParentGroups().size();
            objectBig = 25 + num - 1;

            int trueX1 = X1 - UIUtil.dip2px(getContext(), objectBig / 2);//减去自身控件的长度
            int trueY1 = Y1 - UIUtil.dip2px(getContext(), objectBig / 2);//减去自身控件的长度

            //当前点的所在区域则是
            RectPoint leoPoint = new RectPoint(trueX1 - UIUtil.dip2px(getContext(), objectBig / 2 + 13), trueY1 - UIUtil.dip2px(getContext(), objectBig / 2 + 13),
                    trueX1 + UIUtil.dip2px(getContext(), objectBig / 2 + 13), trueY1 + UIUtil.dip2px(getContext(), objectBig / 2 + 13));

            if (checkHaveRect(leoPoint)) {//检查是否有重叠，true为有重叠。要增加半径的长度

                repetAdd(x_center, y_center, circle, currentJD, relation, itemBean);

            } else { //false 没有重叠，则直接添加

                CircleImageView imageView_bottom_yy = new CircleImageView(getContext());
                imageView_bottom_yy.setId(R.id.image_theOne);
                LayoutParams leoParams_bottom_yy = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leoParams_bottom_yy.width = UIUtil.dip2px(getContext(), objectBig);
                leoParams_bottom_yy.height = UIUtil.dip2px(getContext(), objectBig);
                leoParams_bottom_yy.leftMargin = trueX1;
                leoParams_bottom_yy.topMargin = trueY1;


                if (objectBig >= HavePic) {
                    imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
                    if (TextUtils.isEmpty(itemBean.getUserId()) && TextUtils.isEmpty(itemBean.getLable())) {
                        imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.noUser));
                    } else {
                        if (relation == 2) {
                            imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_2));
                        } else if (relation == 3) {
                            imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_3));
                        } else if (relation == 4) {
                            imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_4));
                        } else if (relation == 5) {
                            imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_5));
                        } else if (relation == 6) {
                            imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_6));
                        }
                    }
                    imageView_bottom_yy.setImageResource(setHeadNoNet(itemBean.getHeaderUrl()));
                } else {
                    if (relation == 2) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_2));
                    } else if (relation == 3) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_3));
                    } else if (relation == 4) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_4));
                    } else if (relation == 5) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_5));
                    } else if (relation == 6) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_6));
                    }
                }


                layouPoints.addView(imageView_bottom_yy, leoParams_bottom_yy);
                addCanScroll(trueX1, trueY1);


                if (itemBean.getGroup() == clickNode && isShowClickAnim) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_tip_point);
                    imageView_bottom_yy.startAnimation(animation);
                }

                if (checkClickIn(itemBean.getGroup())) {//判断我是不是在里面
                    imageView_bottom_yy.setAlpha(1.0f);
                    if (nowClickAtmanRelation != null) {
                        if (degreeRelation > relation) {
                            points.add(new PointLeo(x_center, y_center, X1, Y1, "暗色"));
                        } else {

                            if (x_center == nowClickAtmanRelation.getX_center() && y_center == nowClickAtmanRelation.getY_center()) {
                                points.add(new PointLeo(x_center, y_center, X1, Y1, "高亮"));
                            } else if (X1 == nowClickAtmanRelation.getX_center() && Y1 == nowClickAtmanRelation.getY_center()) {
                                points.add(new PointLeo(x_center, y_center, X1, Y1, "高亮"));
                            } else {
                                points.add(new PointLeo(x_center, y_center, X1, Y1, "暗色"));
                            }
                        }
                    } else {
                        points.add(new PointLeo(x_center, y_center, X1, Y1, "默认显示"));
                    }

                    if (clickList != null) {
                        TextView textView = new TextView(getContext());
                        LayoutParams leoParams_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        leoParams_text.height = UIUtil.dip2px(getContext(), 20);
                        leoParams_text.leftMargin = X1 + UIUtil.dip2px(getContext(), objectBig / 2);
                        leoParams_text.topMargin = Y1 - UIUtil.dip2px(getContext(), 10);


                        textView.setTextColor(getResources().getColor(R.color.white));
                        textView.setTextSize(UIUtil.dip2px(getContext(), 4));
                        textView.setGravity(Gravity.CENTER);
                        if (!TextUtils.isEmpty(itemBean.getNickName())) {
                            if (itemBean.getNickName().length() > 6) {
                                String newStr = itemBean.getNickName().substring(0, 6);
                                textView.setText(newStr + "...");
                            } else {
                                textView.setText(itemBean.getNickName());
                            }
                        }
                        layouPoints.addView(textView, leoParams_text);
                    }
                    Log.i("这里的bug到底咱那里", "11111111111");
                } else {
                    imageView_bottom_yy.setAlpha(0.2f);
                    points.add(new PointLeo(x_center, y_center, X1, Y1, "暗色"));
                }

                itemBean.setX_center(X1);
                itemBean.setY_center(Y1);
                itemBean.setRectPoint(new RectPoint(trueX1 - UIUtil.dip2px(getContext(), objectBig / 2), trueY1 - UIUtil.dip2px(getContext(), objectBig / 2),
                        trueX1 + UIUtil.dip2px(getContext(), objectBig / 2), trueY1 + UIUtil.dip2px(getContext(), objectBig / 2)));
                rects.add(leoPoint);

                /**
                 * 这里是添加成功关系以后，进行的下一层 关系的绘制
                 * */
                if (relation == 2) {//保证当前的节点是在2度关系的节点下，才添加3度关系

                    ArrayList<AtmanRelation> sonList = getsonList(itemBean);//一度节点下二度的关系集合
                    if (sonList.size() > 0) {
                        int numbSon = sonList.size();//下一层关系有几个。（注意这里下一层的一度关系是不包括父类的。而且算角度的时候要加个1）
                        reList_3.add(new RelationBean(circle, numbSon, X1, Y1, currentJD + 180, 3, sonList));//倒数第二个参数，第几度关系
                    }


                } else if (relation == 3) {//保证当前的节点在3度下，才能添加4度关系
                    ArrayList<AtmanRelation> sonList = getsonList(itemBean);
                    Log.e("四度关系问题", sonList.size() + "======");
                    if (sonList.size() > 0) {
                        int numbSon = sonList.size();
                        reList_4.add(new RelationBean(circle, numbSon, X1, Y1, currentJD + 180, 4, sonList));//倒数第二个参数，第几度关系
                    }


                } else if (relation == 4) {//当前节点是4 才能添加5度关系
                    ArrayList<AtmanRelation> sonList = getsonList(itemBean);
                    if (sonList.size() > 0) {
                        int numbSon = sonList.size();
                        reList_5.add(new RelationBean(circle, numbSon, X1, Y1, currentJD + 180, 5, sonList));//倒数第二个参数，第几度关系
                    }

                } else if (relation == 5) {//当前节点是5 才能添加6度关系
                    ArrayList<AtmanRelation> sonList = getsonList(itemBean);
                    if (sonList.size() > 0) {
                        int numbSon = sonList.size();
                        reList_6.add(new RelationBean(circle, numbSon, X1, Y1, currentJD + 180, 6, sonList));//倒数第二个参数，第几度关系
                    }
                }

                if (onClickListener != null) {
                    imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                    imageView_bottom_yy.setOnClickListener(onClickListener);
                }


            }
            if (relation == 2) {//这里是启动绘制。将二度全部绘制完成后，才绘制3度
                if (isShow3) {
                    if (index == reList_2.size() - 1 && i == numberNex - 1) {
                        otherLinePdList.addAll(relation_source_2);
                        if (reList_3.size() > 0) {
                            for (int j = 0; j < reList_3.size(); j++) {
                                RelationBean relationBean = reList_3.get(j);
                                if (relationBean.getNumberNex() > 0) {
                                    addNexAbout(relationBean.getCircle(), relationBean.getNumberNex(), relationBean.getX1(), relationBean.getY1(), relationBean.getTrueJD(), relationBean.getRelation(), relationBean.getAtmanRelations(), j);
                                }
                            }
                        } else {
                            addZhengju(2);
                            otherLinePdList.addAll(relation_source_2);
                            addOtherLine();
                        }
                    }
                } else {

                    if (index == reList_2.size() - 1 && i == numberNex - 1) {
                        addZhengju(2);
                        otherLinePdList.addAll(relation_source_2);
                        addOtherLine();
                    }


                }
            } else if (relation == 3) {
                if (isShow4) {
                    if (index == reList_3.size() - 1 && i == numberNex - 1) {
                        otherLinePdList.addAll(relation_source_3);
                        if (reList_4.size() > 0) {
                            for (int j = 0; j < reList_4.size(); j++) {
                                RelationBean relationBean = reList_4.get(j);
                                if (relationBean.getNumberNex() > 0) {
                                    addNexAbout(relationBean.getCircle(), relationBean.getNumberNex(), relationBean.getX1(), relationBean.getY1(), relationBean.getTrueJD(), relationBean.getRelation(), relationBean.getAtmanRelations(), j);
                                }
                            }
                        } else {
                            addZhengju(3);
                            otherLinePdList.addAll(relation_source_3);
                            addOtherLine();
                            Log.i("天呐的小bug", "显示4度，但是4度没有集合");
                        }
                    }
                } else {
                    if (index == reList_3.size() - 1 && i == numberNex - 1) {
                        addZhengju(3);
                        otherLinePdList.addAll(relation_source_3);
                        addOtherLine();
                    }

                }
            } else if (relation == 4) {
                if (isShow5) {
                    if (index == reList_4.size() - 1 && i == numberNex - 1) {
                        otherLinePdList.addAll(relation_source_4);
                        if (reList_5.size() > 0) {
                            for (int j = 0; j < reList_5.size(); j++) {
                                RelationBean relationBean = reList_5.get(j);
                                if (relationBean.getNumberNex() > 0) {
                                    addNexAbout(relationBean.getCircle(), relationBean.getNumberNex(), relationBean.getX1(), relationBean.getY1(), relationBean.getTrueJD(), relationBean.getRelation(), relationBean.getAtmanRelations(), j);
                                }
                            }
                        } else {
                            addZhengju(4);
                            otherLinePdList.addAll(relation_source_4);
                            addOtherLine();
                        }
                    }
                } else {
                    if (index == reList_4.size() - 1 && i == numberNex - 1) {
                        addZhengju(4);
                        otherLinePdList.addAll(relation_source_4);
                        addOtherLine();
                    }


                }
            } else if (relation == 5) {
                if (isShow6) {
                    if (index == reList_5.size() - 1 && i == numberNex - 1) {
                        otherLinePdList.addAll(relation_source_5);
                        if (reList_6.size() > 0) {
                            for (int j = 0; j < reList_6.size(); j++) {
                                RelationBean relationBean = reList_6.get(j);
                                if (relationBean.getNumberNex() > 0) {
                                    addNexAbout(relationBean.getCircle(), relationBean.getNumberNex(), relationBean.getX1(), relationBean.getY1(), relationBean.getTrueJD(), relationBean.getRelation(), relationBean.getAtmanRelations(), j);
                                }
                                if (j == reList_6.size() - 1) {//展示完6度最后一个添加证据
                                    addZhengju(6);
                                    otherLinePdList.addAll(relation_source_6);
                                    addOtherLine();
                                }
                            }
                        } else {
                            addZhengju(5);
                            otherLinePdList.addAll(relation_source_5);
                            addOtherLine();
                            Log.i("天呐的小bug", "显示6度，但是6度没有集合");
                        }
                    }
                } else {

                    if (index == reList_5.size() - 1 && i == numberNex - 1) {
                        addZhengju(5);
                        otherLinePdList.addAll(relation_source_5);
                        addOtherLine();
                    }

                }
            }


        }


    }


    //这里是检查重叠区域，如果有重叠则一直增加半径 直至不重叠位置
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void repetAdd(int x_center, int y_center, int circle, int currentJD, int relation, AtmanRelation itemBean) {
        int circleAdd = circle + UIUtil.dip2px(getContext(), 50);
        int X1 = 0;
        int Y1 = 0;
        if (currentJD != 0 && currentJD != 180 && currentJD != 360) {//别面有错误出现
            X1 = (int) (x_center + circleAdd * (Math.cos(Math.PI * currentJD / 180)));
            Y1 = (int) (y_center + circleAdd * (Math.sin(Math.PI * currentJD / 180)));
        } else {
            if (currentJD == 0 || currentJD == 360) {
                X1 = x_center + circleAdd;
                Y1 = y_center;
            } else {
                Y1 = y_center;
                X1 = x_center - circleAdd;
            }

        }

        int objectBig = 0;//当前控件的大小
        int num = itemBean.getSonCount() + itemBean.getParentGroups().size();
        objectBig = 25 + num - 1;
        int trueX1 = X1 - UIUtil.dip2px(getContext(), objectBig / 2);//减去自身控件的长度
        int trueY1 = Y1 - UIUtil.dip2px(getContext(), objectBig / 2);//减去自身控件的长度

        //当前点的所在区域则是
        RectPoint leoPoint = new RectPoint(trueX1 - UIUtil.dip2px(getContext(), objectBig / 2 + 13), trueY1 - UIUtil.dip2px(getContext(), objectBig / 2 + 13),
                trueX1 + UIUtil.dip2px(getContext(), objectBig / 2 + 13), trueY1 + UIUtil.dip2px(getContext(), objectBig / 2 + 13));


        if (checkHaveRect(leoPoint)) {//检查是否有重叠，true为有重叠。要增加半径的长度
            repetAdd(x_center, y_center, circleAdd, currentJD, relation, itemBean);
        } else {//false 没有重叠，则直接添加

            CircleImageView imageView_bottom_yy = new CircleImageView(getContext());
            imageView_bottom_yy.setId(R.id.image_theOne);
            LayoutParams leoParams_bottom_yy = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            leoParams_bottom_yy.width = UIUtil.dip2px(getContext(), objectBig);
            leoParams_bottom_yy.height = UIUtil.dip2px(getContext(), objectBig);
            leoParams_bottom_yy.leftMargin = trueX1;
            leoParams_bottom_yy.topMargin = trueY1;
            if (onClickListener != null) {
                imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                imageView_bottom_yy.setOnClickListener(onClickListener);
            }

            if (objectBig >= HavePic) {
                imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
                if (TextUtils.isEmpty(itemBean.getUserId()) && TextUtils.isEmpty(itemBean.getLable())) {
                    imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.noUser));
                } else {
                    if (relation == 2) {
                        imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_2));
                    } else if (relation == 3) {
                        imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_3));
                    } else if (relation == 4) {
                        imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_4));
                    } else if (relation == 5) {
                        imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_5));
                    } else if (relation == 6) {
                        imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_6));
                    } else {
                        imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_1));
                    }
                }
                imageView_bottom_yy.setImageResource(setHeadNoNet(itemBean.getHeaderUrl()));

            } else {

                if (TextUtils.isEmpty(itemBean.getUserId()) && TextUtils.isEmpty(itemBean.getLable())) {
                    imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_nouser));
                } else {
                    if (relation == 2) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_2));
                    } else if (relation == 3) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_3));
                    } else if (relation == 4) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_4));
                    } else if (relation == 5) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_5));
                    } else if (relation == 6) {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_6));
                    } else {
                        imageView_bottom_yy.setBackground(getResources().getDrawable(R.drawable.aa_bg_jidu_1));
                    }
                }

            }


            layouPoints.addView(imageView_bottom_yy, leoParams_bottom_yy);
            addCanScroll(trueX1, trueY1);


            if (itemBean.getGroup() == clickNode && isShowClickAnim) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_tip_point);
                imageView_bottom_yy.startAnimation(animation);
            }

            if (checkClickIn(itemBean.getGroup())) {//判断我是不是在里面
                imageView_bottom_yy.setAlpha(1.0f);
                if (nowClickAtmanRelation != null) {
                    if (degreeRelation > relation) {
                        points.add(new PointLeo(x_center, y_center, X1, Y1, "暗色"));
                    } else {

                        if (x_center == nowClickAtmanRelation.getX_center() && y_center == nowClickAtmanRelation.getY_center()) {
                            points.add(new PointLeo(x_center, y_center, X1, Y1, "高亮"));
                        } else if (X1 == nowClickAtmanRelation.getX_center() && Y1 == nowClickAtmanRelation.getY_center()) {
                            points.add(new PointLeo(x_center, y_center, X1, Y1, "高亮"));
                        } else {
                            points.add(new PointLeo(x_center, y_center, X1, Y1, "暗色"));
                        }
                    }
                } else {
                    points.add(new PointLeo(x_center, y_center, X1, Y1, "默认显示"));
                }

                if (clickList != null) {
                    TextView textView = new TextView(getContext());
                    LayoutParams leoParams_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    leoParams_text.height = UIUtil.dip2px(getContext(), 20);
                    leoParams_text.leftMargin = X1 + UIUtil.dip2px(getContext(), objectBig / 2);
                    leoParams_text.topMargin = Y1 - UIUtil.dip2px(getContext(), 10);


                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setTextSize(UIUtil.dip2px(getContext(), 4));
                    textView.setGravity(Gravity.CENTER);
                    if (!TextUtils.isEmpty(itemBean.getNickName())) {
                        if (itemBean.getNickName().length() > 6) {
                            String newStr = itemBean.getNickName().substring(0, 6);
                            textView.setText(newStr + "...");
                        } else {
                            textView.setText(itemBean.getNickName());
                        }
                    }

                    layouPoints.addView(textView, leoParams_text);
                }
                Log.i("这里的bug到底咱那里", "22222");

            } else {
                imageView_bottom_yy.setAlpha(0.2f);
                points.add(new PointLeo(x_center, y_center, X1, Y1, "暗色"));

            }
            itemBean.setX_center(X1);
            itemBean.setY_center(Y1);
            itemBean.setRectPoint(new RectPoint(trueX1 - UIUtil.dip2px(getContext(), objectBig / 2), trueY1 - UIUtil.dip2px(getContext(), objectBig / 2),
                    trueX1 + UIUtil.dip2px(getContext(), objectBig / 2), trueY1 + UIUtil.dip2px(getContext(), objectBig / 2)));
            rects.add(leoPoint);

            if (relation == 1) {//当前是一度关系的节点，才能添加二度
                ArrayList<AtmanRelation> sonList = getsonList(itemBean);//一度节点下二度的关系集合
                if (sonList.size() > 0) {
                    int numberNex = sonList.size();//下一层关系有几个。（注意这里下一层的一度关系是不包括父类的。而且算角度的时候要加个1）
                    reList_2.add(new RelationBean(circle, numberNex, X1, Y1, currentJD + 180, 2, sonList));//倒数第二个参数，第几度关系
                }
                if (onClickListener != null) {
                    imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                    imageView_bottom_yy.setOnClickListener(onClickListener);
                }
            } else if (relation == 2) {
                ArrayList<AtmanRelation> sonList = getsonList(itemBean);//一度节点下二度的关系集合
                if (sonList.size() > 0) {
                    int numberNex = sonList.size();//下一层关系有几个。（注意这里下一层的一度关系是不包括父类的。而且算角度的时候要加个1）
                    reList_3.add(new RelationBean(circle, numberNex, X1, Y1, currentJD + 180, 3, sonList));//倒数第二个参数，第几度关系
                }
                if (onClickListener != null) {
                    imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                    imageView_bottom_yy.setOnClickListener(onClickListener);
                }
            } else if (relation == 3) {

                ArrayList<AtmanRelation> sonList = getsonList(itemBean);//一度节点下二度的关系集合
                Log.e("四度关系问题", sonList.size() + "++++++++++++++++++");
                if (sonList.size() > 0) {
                    int numberNex = sonList.size();//下一层关系有几个。（注意这里下一层的一度关系是不包括父类的。而且算角度的时候要加个1）
                    reList_4.add(new RelationBean(circle, numberNex, X1, Y1, currentJD + 180, 4, sonList));//倒数第二个参数，第几度关系
                }
                if (onClickListener != null) {
                    imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                    imageView_bottom_yy.setOnClickListener(onClickListener);
                }

            } else if (relation == 4) {
                ArrayList<AtmanRelation> sonList = getsonList(itemBean);//一度节点下二度的关系集合
                Log.e("四度关系问题", sonList.size() + "++++++++++++++++++");
                if (sonList.size() > 0) {
                    int numberNex = sonList.size();//下一层关系有几个。（注意这里下一层的一度关系是不包括父类的。而且算角度的时候要加个1）
                    reList_5.add(new RelationBean(circle, numberNex, X1, Y1, currentJD + 180, 5, sonList));//倒数第二个参数，第几度关系
                }
                if (onClickListener != null) {
                    imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                    imageView_bottom_yy.setOnClickListener(onClickListener);
                }
            } else if (relation == 5) {
                ArrayList<AtmanRelation> sonList = getsonList(itemBean);//一度节点下二度的关系集合
                Log.e("四度关系问题", sonList.size() + "++++++++++++++++++");
                if (sonList.size() > 0) {
                    int numberNex = sonList.size();//下一层关系有几个。（注意这里下一层的一度关系是不包括父类的。而且算角度的时候要加个1）
                    reList_6.add(new RelationBean(circle, numberNex, X1, Y1, currentJD + 180, 6, sonList));//倒数第二个参数，第几度关系
                }
                if (onClickListener != null) {
                    imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                    imageView_bottom_yy.setOnClickListener(onClickListener);
                }
            }


        }
    }


    //检查是否有重叠区域
    public boolean checkHaveRect(RectPoint rectPoint) {
        for (int i = 0; i < rects.size(); i++) {
            RectPoint currenRect = rects.get(i);
            if (rectPoint.getLeft_x() > currenRect.getRigth_x()
                    || rectPoint.getLeft_top_y() > currenRect.getRight_bottom_y()
                    || rectPoint.getRight_bottom_y() < currenRect.getLeft_top_y()
                    || rectPoint.getRigth_x() < currenRect.getLeft_x()
                    ) {

            } else {
                return true;//只要不符合上面条件  则有重叠
            }

        }

        return false;//没有包含在内
    }


    //检查是否有重叠区域。只限制在证据和高亮的item里
    public boolean checkHaveZhengjuRect(RectPoint rectPoint) {
        for (int i = 0; i < rectsZhengj.size(); i++) {
            RectPoint currenRect = rectsZhengj.get(i);
            if (currenRect != null) {
                if (rectPoint.getLeft_x() > currenRect.getRigth_x()
                        || rectPoint.getLeft_top_y() > currenRect.getRight_bottom_y()
                        || rectPoint.getRight_bottom_y() < currenRect.getLeft_top_y()
                        || rectPoint.getRigth_x() < currenRect.getLeft_x()
                        ) {

                } else {
                    return true;//只要不符合上面条件  则有重叠
                }
            } else {
                return false;
            }
        }

        return false;//没有包含在内
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    public Integer setHeadNoNet(String url) {
        Log.e("我没有数据吗", url + "====================");
        if (url.equals("starsInTheSky/0/0.png")) {
            Log.e("不是吧", url + "====================");
            return R.mipmap.head_1;
        } else if (url.equals("starsInTheSky/1/1.png")) {
            return R.mipmap.head_2;
        } else if (url.equals("starsInTheSky/1/2.png")) {
            return R.mipmap.head_3;
        } else if (url.equals("starsInTheSky/1/3.png")) {
            return R.mipmap.head_4;
        } else if (url.equals("starsInTheSky/1/4.png")) {
            return R.mipmap.head_5;
        } else if (url.equals("starsInTheSky/2/11.png")) {
            return R.mipmap.head_6;
        } else if (url.equals("starsInTheSky/2/12.png")) {
            return R.mipmap.head_7;
        } else if (url.equals("starsInTheSky/2/21.png")) {
            return R.mipmap.head_8;
        } else if (url.equals("starsInTheSky/2/31.png")) {
            return R.mipmap.head_9;
        } else if (url.equals("starsInTheSky/3/111.png")) {
            return R.mipmap.head_10;
        } else if (url.equals("starsInTheSky/4/1111.png")) {
            return R.mipmap.head_11;
        } else if (url.equals("starsInTheSky/4/1112.png")) {
            return R.mipmap.head_12;
        } else if (url.equals("starsInTheSky/5/11111.png")) {
            return R.mipmap.head_13;
        } else if (url.equals("starsInTheSky/5/11112.png")) {
            return R.mipmap.head_14;
        } else if (url.equals("starsInTheSky/5/11113.png")) {
            return R.mipmap.head_15;
        } else if (url.equals("starsInTheSky/6/111111.png")) {
            return R.mipmap.head_16;
        } else if (url.equals("starsInTheSky/6/111112.png")) {
            return R.mipmap.head_17;
        } else if (url.equals("starsInTheSky/6/111113.png")) {
            return R.mipmap.head_1;
        } else {
            return R.mipmap.default_head;
        }
    }

}
