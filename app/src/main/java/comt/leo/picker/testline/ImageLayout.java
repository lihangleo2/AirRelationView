package comt.leo.picker.testline;

import android.content.Context;
import android.graphics.Paint;
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

import comt.leo.picker.testline.bean.AtmanRelation;
import comt.leo.picker.testline.bean.PointLeo;
import comt.leo.picker.testline.bean.RectPoint;
import comt.leo.picker.testline.bean.RelationBean;


public class ImageLayout extends FrameLayout {
    private Paint mPaint;
    FrameLayout layouPoints;
    private ShaderImageView shaderImageView;
    private OnClickListener onClickListener;

    private ImageView image_theOne;
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

    private boolean isShow2 = false;//是否显示二度关系
    private boolean isShow3 = false;
    private boolean isShow4 = false;
    private boolean isShow5 = false;
    private boolean isShow6 = false;

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
            clickNode= -1;
        }
        Log.e("我去总有这么多问题", isShow2 + "===========");
        addPoint();
    }


    public ImageLayout(Context context) {
        this(context, null);
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initView(context, attrs);

    }


    public void setSourceList(ArrayList<AtmanRelation> sourceList) {
        this.sourceList = sourceList;
        AtmanRelation atmanRelation = null;
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).getNodePar() == 0) {
                atmanRelation = sourceList.get(i);
            }
        }
        if (onClickListener != null) {
            image_theOne.setTag(R.id.image_theOne, atmanRelation);
            image_theOne.setOnClickListener(onClickListener);
        }
        addPoint();
    }

    private int degreeRelation = -1;
    private int clickNode = -1;

    public void setClickList(ArrayList<AtmanRelation> clickList, int degreeRelation,int clickNode) {
        this.clickList = clickList;
        this.degreeRelation = degreeRelation;
        this.clickNode = clickNode;
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
        View imgPointLayout = inflate(context, R.layout.layout_imgview_point, this);
        layouPoints = (FrameLayout) imgPointLayout.findViewById(R.id.layouPoints);
        shaderImageView = (ShaderImageView) imgPointLayout.findViewById(R.id.ShaderImageView);
        image_theOne = (ImageView) imgPointLayout.findViewById(R.id.image_theOne);

    }


    public ArrayList<AtmanRelation> getDeree(int Deree) {//获取几度关系
        ArrayList<AtmanRelation> arrayList = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).getDegree() == Deree) {
                arrayList.add(sourceList.get(i));
            }
        }
        return arrayList;
    }


    public ArrayList<AtmanRelation> getsonList(AtmanRelation atmanRelation) {//获取这个节点下 所有的集合
        ArrayList<AtmanRelation> arrayList = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).getNodePar() == atmanRelation.getNode()) {
                arrayList.add(sourceList.get(i));
                Log.i("这样的数据令我神伤", i + "====");
            }
        }
        return arrayList;
    }


    public boolean checkClickIn(int node) {

        if (clickList != null) {
            for (int i = 0; i < clickList.size(); i++) {
                if (clickList.get(i).getNode() == node) {
                    return true;//在里面的话 那么现实高亮
                }
            }
            return false;//不在里面，其他的显示暗色
        } else {
            return true;//当没有点击的时候 显示亮色
        }

    }


    public void addPoint() {
        points.clear();
        rects.clear();
        reList_2.clear();
        reList_3.clear();
        reList_4.clear();
        reList_5.clear();
        reList_6.clear();
        layouPoints.removeAllViews();

        //找到自己这个点。并把中心点带入
        ArrayList<AtmanRelation> myAtmanList = getDeree(0);
        myAtmanList.get(0).setY_center(UIUtil.dip2px(getContext(), 5000) / 2);
        myAtmanList.get(0).setX_center(UIUtil.dip2px(getContext(), 5000) / 2);
        myAtmanList.get(0).setRectPoint(new RectPoint(UIUtil.dip2px(getContext(), 5000) / 2 - UIUtil.dip2px(getContext(), 23), UIUtil.dip2px(getContext(), 5000) / 2 - UIUtil.dip2px(getContext(), 23)
                , UIUtil.dip2px(getContext(), 5000) / 2 + UIUtil.dip2px(getContext(), 23), UIUtil.dip2px(getContext(), 5000) / 2 + UIUtil.dip2px(getContext(), 23)));

        //我的在里面，那么久先是高亮
        if (checkClickIn(1)) {//判断我是不是在里面
            image_theOne.setAlpha(1.0f);

            if (clickList != null) {//如果点击集合里不为空才加名字
                TextView textView = new TextView(getContext());
                LayoutParams leoParams_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leoParams_text.height = UIUtil.dip2px(getContext(), 20);
                leoParams_text.leftMargin = UIUtil.dip2px(getContext(), 5000) / 2 + UIUtil.dip2px(getContext(), 23);
                leoParams_text.topMargin = UIUtil.dip2px(getContext(), 5000) / 2 - UIUtil.dip2px(getContext(), 10);


                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setTextSize(UIUtil.dip2px(getContext(), 4));
                textView.setGravity(Gravity.CENTER);
                textView.setText("可爱小叔");

                layouPoints.addView(textView, leoParams_text);
            }


        } else {
            image_theOne.setAlpha(0.2f);
        }

        /***
         * 如果是一度关系
         */
        int circle = UIUtil.dip2px(getContext(), 130);//当前一度关系的长度
        ArrayList<AtmanRelation> oneAtmanList = getDeree(1);
        int number = oneAtmanList.size();//当前一度关系的个数
        int x_center = UIUtil.dip2px(getContext(), 5000) / 2;
        int y_center = UIUtil.dip2px(getContext(), 5000) / 2;
        int firstJD = 0;//一度关系开始的角度是从 0 开始的，
        for (int i = 0; i < oneAtmanList.size(); i++) {
            AtmanRelation itemBean = oneAtmanList.get(i);
            //相隔的角度就是
            int jiaod = 360 / oneAtmanList.size();
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

            int trueX1 = X1 - UIUtil.dip2px(getContext(), 17);//减去自身控件的长度
            int trueY1 = Y1 - UIUtil.dip2px(getContext(), 17);//减去自身控件的长度


            //当前点的所在区域则是
            RectPoint leoPoint = new RectPoint(trueX1 - UIUtil.dip2px(getContext(), 34), trueY1 - UIUtil.dip2px(getContext(), 34),
                    trueX1 + UIUtil.dip2px(getContext(), 34), trueY1 + UIUtil.dip2px(getContext(), 34));

            if (checkHaveRect(leoPoint)) {//检查是否有重叠，true为有重叠。要增加半径的长度

                repetAdd(x_center, y_center, circle, currentJD, 1, itemBean);

            } else {//false 没有重叠，则直接添加

                CircleImageView imageView_bottom_yy = new CircleImageView(getContext());
                imageView_bottom_yy.setId(R.id.image_theOne);
                LayoutParams leoParams_bottom_yy = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leoParams_bottom_yy.width = UIUtil.dip2px(getContext(), 35);
                leoParams_bottom_yy.height = UIUtil.dip2px(getContext(), 35);
                leoParams_bottom_yy.leftMargin = trueX1;
                leoParams_bottom_yy.topMargin = trueY1;


                imageView_bottom_yy.setImageResource(R.mipmap.head_1);
                imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_1));
                imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
                layouPoints.addView(imageView_bottom_yy, leoParams_bottom_yy);
                if (itemBean.getNode() == clickNode){
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_tip_point);
                    imageView_bottom_yy.startAnimation(animation);
                }



                if (checkClickIn(itemBean.getNode())) {//判断我是不是在里面
                    imageView_bottom_yy.setAlpha(1.0f);
                    Log.i("这里的bug到底咱那里", "3333");
                    if (degreeRelation > 1) {
                        points.add(new PointLeo(x_center, y_center, X1, Y1, false));
                    } else {
                        points.add(new PointLeo(x_center, y_center, X1, Y1, true));
                    }

                    if (clickList != null) {
                        TextView textView = new TextView(getContext());
                        LayoutParams leoParams_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        leoParams_text.height = UIUtil.dip2px(getContext(), 20);
                        leoParams_text.leftMargin = X1 + UIUtil.dip2px(getContext(), 18);
                        leoParams_text.topMargin = Y1 - UIUtil.dip2px(getContext(), 10);


                        textView.setTextColor(getResources().getColor(R.color.white));
                        textView.setTextSize(UIUtil.dip2px(getContext(), 4));
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("可爱小叔");

                        layouPoints.addView(textView, leoParams_text);
                    }

                } else {
                    imageView_bottom_yy.setAlpha(0.2f);
                    points.add(new PointLeo(x_center, y_center, X1, Y1, false));
                }

                // 把当前的中心点坐标  和当前点的所在区域存储起来。以便放置证据的时候 不覆盖 判断
                itemBean.setX_center(X1);
                itemBean.setY_center(Y1);
                itemBean.setRectPoint(new RectPoint(trueX1 - UIUtil.dip2px(getContext(), 17), trueY1 - UIUtil.dip2px(getContext(), 17),
                        trueX1 + UIUtil.dip2px(getContext(), 17), trueY1 + UIUtil.dip2px(getContext(), 17)));

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
                        Log.i("天呐的小bug", "显示2度，但是2度没有集合");
                    }
                }
            } else {//这里不显示二度，此时有点击clickList集合的时候，看看是否有证据，将证据添加进去。注意一定要是绘制完整个一度时候才添加

                if (i == number - 1) {//代表绘制完整个一度了。拿clickList一点在clickList里找到父类。确定2个点加上证据
                    addZhengju(1);
                    Log.i("天呐的小bug", "不显示2度，显示完1度的证据");

                }

            }

        }
        shaderImageView.setLines(points);
    }


    public void addZhengju(int showDegree) {
        if (clickList != null) {
            for (int m = 0; m < clickList.size(); m++) {
                AtmanRelation findPairBean = clickList.get(m);
                if (findPairBean.getDegree() <= showDegree) {
                    for (int n = 0; n < clickList.size(); n++) {
                        if (findPairBean.getNodePar() == clickList.get(n).getNode()) {//说明找到父类了。那么也确定了2个点。把证据加上去
                            //中心点在

                            int zheng_x_center = findPairBean.getX_center() + (clickList.get(n).getX_center() - findPairBean.getX_center()) / 2;
                            int zheng_y_center = findPairBean.getY_center() + (clickList.get(n).getY_center() - findPairBean.getY_center()) / 2;

                            Log.e("要死了来一个这个bug", findPairBean.getX_center() + "==========");
                            Log.e("要死了来一个这个bug", findPairBean.getY_center() + "==========");

                            Log.e("要死了来一个这个bug", clickList.get(n).getX_center() + "++++++++");
                            Log.e("要死了来一个这个bug", clickList.get(n).getY_center() + "++++++++");
                            Log.e("要死了来一个这个bug", zheng_x_center + "======中心点=====");
                            Log.e("要死了来一个这个bug", zheng_y_center + "======中心点=====");


                            int trueXX1 = zheng_x_center - UIUtil.dip2px(getContext(), 15);
                            int trueYY1 = zheng_y_center - UIUtil.dip2px(getContext(), 15);

                            RectPoint leoPoint = new RectPoint(trueXX1 - UIUtil.dip2px(getContext(), 15), trueYY1 - UIUtil.dip2px(getContext(), 15),
                                    trueXX1 + UIUtil.dip2px(getContext(), 15), trueYY1 + UIUtil.dip2px(getContext(), 15));

                            if (checkHaveZhengjuRect(leoPoint)) {
                                Log.e("要死了来一个这个bug", "包含在内了！！！");
                                zhengjuNoG(findPairBean.getX_center(), findPairBean.getY_center(), (clickList.get(n).getX_center() - findPairBean.getX_center()), (clickList.get(n).getY_center() - findPairBean.getY_center()), 1);
                            } else {
                                RelativeLayout relativeLayout = (RelativeLayout) inflate(getContext(), R.layout.item_zhengju, null);
                                LayoutParams zhengjuLayout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                zhengjuLayout.height = UIUtil.dip2px(getContext(), 30);
                                zhengjuLayout.width = UIUtil.dip2px(getContext(), 30);
                                zhengjuLayout.leftMargin = zheng_x_center - UIUtil.dip2px(getContext(), 15);
                                zhengjuLayout.topMargin = zheng_y_center - UIUtil.dip2px(getContext(), 15);
                                layouPoints.addView(relativeLayout, zhengjuLayout);
                                rectsZhengj.add(leoPoint);
                            }


                        }
                    }
                }
            }
        }
    }


    public void zhengjuNoG(int startX, int startY, int distanceX, int distanceY, int numCount) {

        int zheng_x_center = startX + distanceX * numCount / 50;
        int zheng_y_center = startY + distanceY * numCount / 50;

        int trueXX1 = zheng_x_center - UIUtil.dip2px(getContext(), 15);
        int trueYY1 = zheng_y_center - UIUtil.dip2px(getContext(), 15);

        RectPoint leoPoint = new RectPoint(trueXX1 - UIUtil.dip2px(getContext(), 15), trueYY1 - UIUtil.dip2px(getContext(), 15),
                trueXX1 + UIUtil.dip2px(getContext(), 15), trueYY1 + UIUtil.dip2px(getContext(), 15));

        if (checkHaveZhengjuRect(leoPoint)) {
            int thisCount = numCount + 1;
            Log.e("我看我这里出现了多少次",thisCount+"================");
            if (thisCount >= 50) {
                return;
            } else {
                zhengjuNoG(zheng_x_center, zheng_y_center, distanceX, distanceY, thisCount);
            }
        } else {
            Log.e("成功总是困难的啊",zheng_x_center+"==========");
            Log.e("成功总是困难的啊",zheng_y_center+"==========");
            RelativeLayout relativeLayout = (RelativeLayout) inflate(getContext(), R.layout.item_zhengju, null);
            LayoutParams zhengjuLayout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            zhengjuLayout.height = UIUtil.dip2px(getContext(), 30);
            zhengjuLayout.width = UIUtil.dip2px(getContext(), 30);
            zhengjuLayout.leftMargin = zheng_x_center - UIUtil.dip2px(getContext(), 15);
            zhengjuLayout.topMargin = zheng_y_center - UIUtil.dip2px(getContext(), 15);
            layouPoints.addView(relativeLayout, zhengjuLayout);
            rectsZhengj.add(leoPoint);
        }
    }


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
            int jiaod = 180 / number;
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

            int trueX1 = X1 - UIUtil.dip2px(getContext(), 17);//减去自身控件的长度
            int trueY1 = Y1 - UIUtil.dip2px(getContext(), 17);//减去自身控件的长度

            //当前点的所在区域则是
            RectPoint leoPoint = new RectPoint(trueX1 - UIUtil.dip2px(getContext(), 34), trueY1 - UIUtil.dip2px(getContext(), 34),
                    trueX1 + UIUtil.dip2px(getContext(), 34), trueY1 + UIUtil.dip2px(getContext(), 34));

            if (checkHaveRect(leoPoint)) {//检查是否有重叠，true为有重叠。要增加半径的长度

                repetAdd(x_center, y_center, circle, currentJD, relation, itemBean);

            } else {//false 没有重叠，则直接添加

                CircleImageView imageView_bottom_yy = new CircleImageView(getContext());
                imageView_bottom_yy.setId(R.id.image_theOne);
                LayoutParams leoParams_bottom_yy = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                leoParams_bottom_yy.width = UIUtil.dip2px(getContext(), 35);
                leoParams_bottom_yy.height = UIUtil.dip2px(getContext(), 35);
                leoParams_bottom_yy.leftMargin = trueX1;
                leoParams_bottom_yy.topMargin = trueY1;

                imageView_bottom_yy.setImageResource(R.mipmap.head_1);
                if (relation == 2) {
                    imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_2));
                    imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
                    Log.e("我看到底是谁", "==========================================2");
                } else if (relation == 3) {
                    Log.e("我看到底是谁", "========3");
                    imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_3));
                    imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
                } else if (relation == 4) {
                    imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_4));
                    imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
                } else if (relation == 5) {
                    imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_5));
                    imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
                } else if (relation == 6) {
                    imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_6));
                    imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
                }
                layouPoints.addView(imageView_bottom_yy, leoParams_bottom_yy);
                if (itemBean.getNode() == clickNode){
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_tip_point);
                    imageView_bottom_yy.startAnimation(animation);
                }

                if (checkClickIn(itemBean.getNode())) {//判断我是不是在里面
                    imageView_bottom_yy.setAlpha(1.0f);
                    if (degreeRelation > relation) {
                        points.add(new PointLeo(x_center, y_center, X1, Y1, false));
                    } else {
                        points.add(new PointLeo(x_center, y_center, X1, Y1, true));
                    }

                    if (clickList != null) {
                        TextView textView = new TextView(getContext());
                        LayoutParams leoParams_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        leoParams_text.height = UIUtil.dip2px(getContext(), 20);
                        leoParams_text.leftMargin = X1 + UIUtil.dip2px(getContext(), 18);
                        leoParams_text.topMargin = Y1 - UIUtil.dip2px(getContext(), 10);


                        textView.setTextColor(getResources().getColor(R.color.white));
                        textView.setTextSize(UIUtil.dip2px(getContext(), 4));
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("可爱小叔");
                        layouPoints.addView(textView, leoParams_text);
                    }
                    Log.i("这里的bug到底咱那里", "11111111111");
                } else {
                    imageView_bottom_yy.setAlpha(0.2f);
                    points.add(new PointLeo(x_center, y_center, X1, Y1, false));
                }

                itemBean.setX_center(X1);
                itemBean.setY_center(Y1);
                itemBean.setRectPoint(new RectPoint(trueX1 - UIUtil.dip2px(getContext(), 17), trueY1 - UIUtil.dip2px(getContext(), 17),
                        trueX1 + UIUtil.dip2px(getContext(), 17), trueY1 + UIUtil.dip2px(getContext(), 17)));
                rects.add(new RectPoint(trueX1 - UIUtil.dip2px(getContext(), 34), trueY1 - UIUtil.dip2px(getContext(), 34),
                        trueX1 + UIUtil.dip2px(getContext(), 34), trueY1 + UIUtil.dip2px(getContext(), 34)));

                /**
                 * 这里是添加成功关系以后，进行的下一层 关系的绘制
                 * */
                if (relation == 2) {//保证当前的节点是在2度关系的节点下，才添加3度关系

                    ArrayList<AtmanRelation> sonList = getsonList(itemBean);//一度节点下二度的关系集合
                    if (sonList.size() > 0) {
                        int numbSon = sonList.size();//下一层关系有几个。（注意这里下一层的一度关系是不包括父类的。而且算角度的时候要加个1）
                        reList_3.add(new RelationBean(circle, numbSon, X1, Y1, currentJD + 180, 3, sonList));//倒数第二个参数，第几度关系
                    }

                    if (onClickListener != null) {
                        imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                        imageView_bottom_yy.setOnClickListener(onClickListener);
                    }

                } else if (relation == 3) {//保证当前的节点在3度下，才能添加4度关系
                    ArrayList<AtmanRelation> sonList = getsonList(itemBean);
                    Log.e("四度关系问题", sonList.size() + "======");
                    if (sonList.size() > 0) {
                        int numbSon = sonList.size();
                        reList_4.add(new RelationBean(circle, numbSon, X1, Y1, currentJD + 180, 4, sonList));//倒数第二个参数，第几度关系
                    }

                    if (onClickListener != null) {
                        imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                        imageView_bottom_yy.setOnClickListener(onClickListener);
                    }
                } else if (relation == 4) {//当前节点是4 才能添加5度关系
                    ArrayList<AtmanRelation> sonList = getsonList(itemBean);
                    if (sonList.size() > 0) {
                        int numbSon = sonList.size();
                        reList_5.add(new RelationBean(circle, numbSon, X1, Y1, currentJD + 180, 5, sonList));//倒数第二个参数，第几度关系
                    }

                    if (onClickListener != null) {
                        imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                        imageView_bottom_yy.setOnClickListener(onClickListener);
                    }
                } else if (relation == 5) {//当前节点是5 才能添加6度关系
                    ArrayList<AtmanRelation> sonList = getsonList(itemBean);
                    if (sonList.size() > 0) {
                        int numbSon = sonList.size();
                        reList_6.add(new RelationBean(circle, numbSon, X1, Y1, currentJD + 180, 6, sonList));//倒数第二个参数，第几度关系
                    }

                    if (onClickListener != null) {
                        imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                        imageView_bottom_yy.setOnClickListener(onClickListener);
                    }

                }


            }
            if (relation == 2) {//这里是启动绘制。将二度全部绘制完成后，才绘制3度
                if (isShow3) {
                    if (index == reList_2.size() - 1 && i == numberNex - 1) {
                        if (reList_3.size() > 0) {
                            for (int j = 0; j < reList_3.size(); j++) {
                                RelationBean relationBean = reList_3.get(j);
                                if (relationBean.getNumberNex() > 0) {
                                    addNexAbout(relationBean.getCircle(), relationBean.getNumberNex(), relationBean.getX1(), relationBean.getY1(), relationBean.getTrueJD(), relationBean.getRelation(), relationBean.getAtmanRelations(), j);
                                }
                            }
                        } else {
                            addZhengju(2);
                            Log.i("天呐的小bug", "显示3度，但是3度没有集合");
                        }
                    }
                } else {
                    if (index == reList_2.size() - 1 && i == numberNex - 1) {
                        addZhengju(2);
                        Log.i("天呐的小bug", "不显示显示3度，但展示2度所有证据");
                    }
                }
            } else if (relation == 3) {
                if (isShow4) {
                    if (index == reList_3.size() - 1 && i == numberNex - 1) {
                        if (reList_4.size() > 0) {
                            for (int j = 0; j < reList_4.size(); j++) {
                                RelationBean relationBean = reList_4.get(j);
                                if (relationBean.getNumberNex() > 0) {
                                    addNexAbout(relationBean.getCircle(), relationBean.getNumberNex(), relationBean.getX1(), relationBean.getY1(), relationBean.getTrueJD(), relationBean.getRelation(), relationBean.getAtmanRelations(), j);
                                }
                            }
                        } else {
                            addZhengju(3);
                            Log.i("天呐的小bug", "显示4度，但是4度没有集合");
                        }
                    }
                } else {
                    if (index == reList_3.size() - 1 && i == numberNex - 1) {
                        addZhengju(3);
                        Log.i("天呐的小bug", "不显示4度，但展示3度所有证据");

                    }
                }
            } else if (relation == 4) {
                if (isShow5) {
                    if (index == reList_4.size() - 1 && i == numberNex - 1) {
                        if (reList_5.size() > 0) {
                            for (int j = 0; j < reList_5.size(); j++) {
                                RelationBean relationBean = reList_5.get(j);
                                if (relationBean.getNumberNex() > 0) {
                                    addNexAbout(relationBean.getCircle(), relationBean.getNumberNex(), relationBean.getX1(), relationBean.getY1(), relationBean.getTrueJD(), relationBean.getRelation(), relationBean.getAtmanRelations(), j);
                                }
                            }
                        } else {
                            addZhengju(4);
                            Log.i("天呐的小bug", "显示5度，但是5度没有集合");
                        }
                    }
                } else {
                    if (index == reList_4.size() - 1 && i == numberNex - 1) {
                        addZhengju(4);
                        Log.i("天呐的小bug", "不显示5度，但展示4度所有证据");

                    }
                }
            } else if (relation == 5) {
                if (isShow6) {
                    if (index == reList_5.size() - 1 && i == numberNex - 1) {
                        if (reList_6.size() > 0) {
                            for (int j = 0; j < reList_6.size(); j++) {
                                RelationBean relationBean = reList_6.get(j);
                                if (relationBean.getNumberNex() > 0) {
                                    addNexAbout(relationBean.getCircle(), relationBean.getNumberNex(), relationBean.getX1(), relationBean.getY1(), relationBean.getTrueJD(), relationBean.getRelation(), relationBean.getAtmanRelations(), j);
                                }
                                if (j == reList_6.size() - 1) {//展示完6度最后一个添加证据
                                    addZhengju(6);
                                    Log.i("天呐的小bug", "显示6度，展示6度的所有证据");
                                }
                            }
                        } else {
                            addZhengju(5);
                            Log.i("天呐的小bug", "显示6度，但是6度没有集合");
                        }
                    }
                } else {

                    if (index == reList_5.size() - 1 && i == numberNex - 1) {
                        addZhengju(5);
                        Log.i("天呐的小bug", "不显示6度，但展示5度所有证据");

                    }
                }
            }


        }


    }


    //这里是检查重叠区域，如果有重叠则一直增加半径 直至不重叠位置
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

        int trueX1 = X1 - UIUtil.dip2px(getContext(), 17);//减去自身控件的长度
        int trueY1 = Y1 - UIUtil.dip2px(getContext(), 17);//减去自身控件的长度

        //当前点的所在区域则是
        RectPoint leoPoint = new RectPoint(trueX1 - UIUtil.dip2px(getContext(), 34), trueY1 - UIUtil.dip2px(getContext(), 34),
                trueX1 + UIUtil.dip2px(getContext(), 34), trueY1 + UIUtil.dip2px(getContext(), 34));


        if (checkHaveRect(leoPoint)) {//检查是否有重叠，true为有重叠。要增加半径的长度
            repetAdd(x_center, y_center, circleAdd, currentJD, relation, itemBean);
        } else {//false 没有重叠，则直接添加

            CircleImageView imageView_bottom_yy = new CircleImageView(getContext());
            imageView_bottom_yy.setId(R.id.image_theOne);
            LayoutParams leoParams_bottom_yy = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            leoParams_bottom_yy.width = UIUtil.dip2px(getContext(), 35);
            leoParams_bottom_yy.height = UIUtil.dip2px(getContext(), 35);
            leoParams_bottom_yy.leftMargin = trueX1;
            leoParams_bottom_yy.topMargin = trueY1;
            if (onClickListener != null) {
                imageView_bottom_yy.setTag(R.id.image_theOne, itemBean);
                imageView_bottom_yy.setOnClickListener(onClickListener);
            }

            imageView_bottom_yy.setImageResource(R.mipmap.head_1);
            if (relation == 2) {
                Log.e("我看到底是谁", "==========================================2-----");
                imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_2));
            } else if (relation == 3) {
                Log.e("我看到底是谁", "========3-----");
                imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_3));
            } else if (relation == 4) {
                imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_4));
            } else if (relation == 5) {
                imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_5));
            } else if (relation == 6) {
                imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_6));
            } else {
                imageView_bottom_yy.setBorderColor(getResources().getColor(R.color.jidu_1));
                Log.e("我看到底是谁", "+++++++++++++++++1-----");
            }
            imageView_bottom_yy.setBorderWidth(UIUtil.dip2px(getContext(), 2));
            layouPoints.addView(imageView_bottom_yy, leoParams_bottom_yy);
            if (itemBean.getNode() == clickNode){
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_tip_point);
                imageView_bottom_yy.startAnimation(animation);
            }

            if (checkClickIn(itemBean.getNode())) {//判断我是不是在里面
                imageView_bottom_yy.setAlpha(1.0f);
                if (degreeRelation > relation) {
                    points.add(new PointLeo(x_center, y_center, X1, Y1, false));
                } else {
                    points.add(new PointLeo(x_center, y_center, X1, Y1, true));

                }

                if (clickList != null) {
                    TextView textView = new TextView(getContext());
                    LayoutParams leoParams_text = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    leoParams_text.height = UIUtil.dip2px(getContext(), 20);
                    leoParams_text.leftMargin = X1 + UIUtil.dip2px(getContext(), 18);
                    leoParams_text.topMargin = Y1 - UIUtil.dip2px(getContext(), 10);


                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setTextSize(UIUtil.dip2px(getContext(), 4));
                    textView.setGravity(Gravity.CENTER);
                    textView.setText("可爱小叔");

                    layouPoints.addView(textView, leoParams_text);
                }
                Log.i("这里的bug到底咱那里", "22222");

            } else {
                imageView_bottom_yy.setAlpha(0.2f);
                points.add(new PointLeo(x_center, y_center, X1, Y1, false));

            }
            itemBean.setX_center(X1);
            itemBean.setY_center(Y1);
            itemBean.setRectPoint(new RectPoint(trueX1 - UIUtil.dip2px(getContext(), 17), trueY1 - UIUtil.dip2px(getContext(), 17),
                    trueX1 + UIUtil.dip2px(getContext(), 17), trueY1 + UIUtil.dip2px(getContext(), 17)));
            rects.add(new RectPoint(trueX1 - UIUtil.dip2px(getContext(), 34), trueY1 - UIUtil.dip2px(getContext(), 34),
                    trueX1 + UIUtil.dip2px(getContext(), 34), trueY1 + UIUtil.dip2px(getContext(), 34)));

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
                    reList_5.add(new RelationBean(circle, numberNex, X1, Y1, currentJD + 180, 6, sonList));//倒数第二个参数，第几度关系
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


}
