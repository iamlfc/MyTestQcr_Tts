package com.lfc.myapplication.NewbieGuide;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnHighlightDrewListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.listener.OnPageChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.HighlightOptions;
import com.lfc.myapplication.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class TestDemo_A extends AppCompatActivity {

    private LinearLayout mLayAll;
    private TextView mTv01;
    private TextView mTv02;
    private TextView mTv03;
    private RoundedImageView mImg1;
    private RoundedImageView mImg2;
    private Activity baseContext;
    private AlphaAnimation enterAnimation;
    private AlphaAnimation exitAnimation;
    private String TAG = "--lfc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_demo);
        baseContext = this;
        initView();
    }


    private void initView() {
        enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(300);
        enterAnimation.setFillAfter(true);

        exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(300);
        exitAnimation.setFillAfter(true);


        mLayAll = (LinearLayout) findViewById(R.id.lay_all);
        mTv01 = (TextView) findViewById(R.id.tv_01);
        mTv02 = (TextView) findViewById(R.id.tv_02);
        mTv03 = (TextView) findViewById(R.id.tv_03);
        mImg1 = (RoundedImageView) findViewById(R.id.img1);
        mImg2 = (RoundedImageView) findViewById(R.id.img2);
        showGuide();
        mTv01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(baseContext)
                        .setLabel("guide1")
//                        .setShowCounts(3)//控制次数
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                        .addHighLight(mTv01)
                                        .setEnterAnimation(enterAnimation)//进入动画
                                        .setExitAnimation(exitAnimation)//退出动画
//                                .addHighLight(new RectF(0, 800, 200, 1200))// 另外绘制一个高亮区域
                                        .setLayoutRes(R.layout.view_guide_simple)
                                        .setBackgroundColor(getResources().getColor(R.color.bgColor))//设置背景色，建议使用有透明度的颜色
                        )
                        .show();
            }
        });
        mTv02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(baseContext)
                        .setLabel("guide1")
//                        .setShowCounts(3)//控制次数
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                        .addHighLight(mTv02)
                                        .setEnterAnimation(enterAnimation)//进入动画
                                        .setExitAnimation(exitAnimation)//退出动画
                                        .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
//                                .addHighLight(new RectF(0, 800, 200, 1200))// 另外绘制一个高亮区域
                                        .setLayoutRes(R.layout.view_guide_text2)
                                        .setBackgroundColor(getResources().getColor(R.color.bgColor))//设置背景色，建议使用有透明度的颜色
                                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                            @Override
                                            public void onLayoutInflated(View view, final Controller controller) {
                                                TextView tv = view.findViewById(R.id.tv_info);
                                                tv.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
//                                                        controller.showPreviewPage();
                                                        controller.remove();
                                                    }
                                                });
                                            }
                                        })
                        )
                        .show();
            }
        });
        mTv03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewbieGuide.with(baseContext)
                        .setLabel("guide1")
//                        .setShowCounts(3)//控制次数
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                        .addHighLight(mTv03)
                                        .setEnterAnimation(enterAnimation)//进入动画
                                        .setExitAnimation(exitAnimation)//退出动画
                                        .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
//                                .addHighLight(new RectF(0, 800, 200, 1200))// 另外绘制一个高亮区域
                                        .setLayoutRes(R.layout.view_guide_text3)
                                        .setBackgroundColor(getResources().getColor(R.color.bgColor))//设置背景色，建议使用有透明度的颜色
                                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                            @Override
                                            public void onLayoutInflated(View view, final Controller controller) {
                                                TextView tv = view.findViewById(R.id.tv_info);
                                                tv.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        controller.remove();
                                                    }
                                                });
                                            }
                                        })
                        )
                        .show();
            }
        });
        mImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighlightOptions options = new HighlightOptions.Builder()
                        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                            @Override
                            public void onHighlightDrew(Canvas canvas, RectF rectF) {
                                Paint paint = new Paint();
                                paint.setColor(Color.WHITE);
//                                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(10);
                                paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));//圆圈的 间隔大小
                                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                            }
                        })
                        .build();
                NewbieGuide.with(baseContext)
                        .setLabel("anchor")
                        .anchor(mLayAll)
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLightWithOptions(mImg1, HighLight.Shape.CIRCLE, options)
                                .setEverywhereCancelable(false)
                                .setLayoutRes(R.layout.view_guide_anchor_img1)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, final Controller controller) {
                                        TextView tv = view.findViewById(R.id.tv_info);
                                        tv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                controller.remove();
                                            }
                                        });
                                    }
                                }))
                        .show();
            }
        });
        mImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighlightOptions options = new HighlightOptions.Builder()
                        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                            @Override
                            public void onHighlightDrew(Canvas canvas, RectF rectF) {
                                Paint paint = new Paint();
                                paint.setColor(Color.WHITE);
//                                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(10);
                                paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));//圆圈的 间隔大小
//                                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                                //画椭圆
                                RectF re2 = new RectF(rectF.left - 10, rectF.top - 10, rectF.right + 10, rectF.bottom + 10);
                                canvas.drawOval(re2, paint);
                            }
                        })
                        .build();
                NewbieGuide.with(baseContext)
                        .setLabel("anchor")
                        .anchor(mLayAll)
                        .alwaysShow(true)//总是显示，调试时可以打开
                        .addGuidePage(GuidePage.newInstance()
                                .addHighLightWithOptions(mImg2, HighLight.Shape.OVAL, options)
                                .setBackgroundColor(getResources().getColor(R.color.bgColor))//设置背景色，建议使用有透明度的颜色
                                .setLayoutRes(R.layout.view_guide_anchor)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.tv_info);
                                        tv.setText("TTS语音合成");
                                        //得到AssetManager
                                        AssetManager mgr = getAssets();
                                        //根据路径得到Typeface
                                        Typeface tf = Typeface.createFromAsset(mgr, "fonts/hhhh.ttf");
                                        //设置字体
                                        tv.setTypeface(tf);
                                    }
                                }))

                        .show();


            }
        });

    }

    private void showGuide() {
        HighlightOptions options = new HighlightOptions.Builder()
                .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                    @Override
                    public void onHighlightDrew(Canvas canvas, RectF rectF) {
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
//                                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(10);
                        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));//圆圈的 间隔大小
                        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                    }
                })
                .build();
        HighlightOptions options2 = new HighlightOptions.Builder()
                .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                    @Override
                    public void onHighlightDrew(Canvas canvas, RectF rectF) {
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
//                                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(10);
                        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));//圆圈的 间隔大小
//                                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                        //画椭圆
                        RectF re2 = new RectF(rectF.left - 10, rectF.top - 10, rectF.right + 10, rectF.bottom + 10);
                        canvas.drawOval(re2, paint);
                    }
                })
                .build();
        //新增多页模式，即一个引导层显示多页引导内容
        NewbieGuide.with(this)
                .setLabel("page")//设置引导层标示区分不同引导层，必传！否则报错
//                .anchor(anchor)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e(TAG, "NewbieGuide onShowed: ");
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e(TAG, "NewbieGuide  onRemoved: ");
                        //引导层消失（多页切换不会触发）
                    }
                })
                .setOnPageChangedListener(new OnPageChangedListener() {
                    @Override
                    public void onPageChanged(int page) {
                        //引导页切换，page为当前页位置，从0开始
                        Toast.makeText(baseContext, "引导页切换：" + page, Toast.LENGTH_SHORT).show();
                    }
                })
                .alwaysShow(true)//是否每次都显示引导层，默认false，只显示一次
                .addGuidePage(GuidePage.newInstance()
                                .addHighLight(mTv01)
                                .setEnterAnimation(enterAnimation)//进入动画
//                                .setExitAnimation(exitAnimation)//退出动画
//                                .addHighLight(new RectF(0, 800, 200, 1200))// 另外绘制一个高亮区域
                                .setLayoutRes(R.layout.view_guide_simple)
                                .setBackgroundColor(getResources().getColor(R.color.bgColor))//设置背景色，建议使用有透明度的颜色

                )
                .addGuidePage(GuidePage.newInstance()
                                .addHighLight(mTv02)
//                                .setEnterAnimation(enterAnimation)//进入动画
//                                .setExitAnimation(exitAnimation)//退出动画
//                                .addHighLight(new RectF(0, 800, 200, 1200))// 另外绘制一个高亮区域
                                .setLayoutRes(R.layout.view_guide_text2, R.id.tv_info)//引导页布局，点击跳转下一页或者消失引导层的控件id
                                .setBackgroundColor(getResources().getColor(R.color.bgColor))//设置背景色，建议使用有透明度的颜色
                                .setEverywhereCancelable(false)
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view, Controller controller) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.tv_info);
                                        tv.setText("生死看淡，不服就干！\n                        --来自非洲大草原的平头哥");
                                        //得到AssetManager
                                        AssetManager mgr = getAssets();
                                        //根据路径得到Typeface
                                        Typeface tf = Typeface.createFromAsset(mgr, "fonts/maobi.ttf");
                                        //设置字体
                                        tv.setTypeface(tf);
                                    }
                                })
                )
                .addGuidePage(GuidePage.newInstance()
                                .addHighLight(mTv03).setEverywhereCancelable(false)
//                                .setEnterAnimation(enterAnimation)//进入动画
//                                .setExitAnimation(exitAnimation)//退出动画
//                                .addHighLight(new RectF(0, 800, 200, 1200))// 另外绘制一个高亮区域
                                .setLayoutRes(R.layout.view_guide_text3, R.id.tv_info)//引导页布局，点击跳转下一页或者消失引导层的控件id
                                .setBackgroundColor(getResources().getColor(R.color.bgColor))//设置背景色，建议使用有透明度的颜色
                )
                .addGuidePage(GuidePage.newInstance()
                        .addHighLightWithOptions(mImg1, HighLight.Shape.CIRCLE, options)
                        .setLayoutRes(R.layout.view_guide_anchor_img1)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                //引导页布局填充后回调，用于初始化
                                TextView tv = view.findViewById(R.id.tv_info);
                                tv.setText("要么是在打架，要么就是在去打架的路上！");
                                //得到AssetManager
                                AssetManager mgr = getAssets();
                                //根据路径得到Typeface
                                Typeface tf = Typeface.createFromAsset(mgr, "fonts/HKYuanMini.ttf");
                                //设置字体
                                tv.setTypeface(tf);
                            }
                        }))
                .addGuidePage(GuidePage.newInstance()
                        .addHighLightWithOptions(mImg2, HighLight.Shape.OVAL, options2)
                        .setBackgroundColor(getResources().getColor(R.color.bgColor))//设置背景色，建议使用有透明度的颜色
                        .setLayoutRes(R.layout.view_guide_anchor, R.id.tv_info)
                        .setEverywhereCancelable(false)
                        .setExitAnimation(exitAnimation)//退出动画
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                //引导页布局填充后回调，用于初始化
                                TextView tv = view.findViewById(R.id.tv_info);
                                tv.setText("我是椭圆");
                                //得到AssetManager
                                AssetManager mgr = getAssets();
                                //根据路径得到Typeface
                                Typeface tf = Typeface.createFromAsset(mgr, "fonts/hhhh.ttf");
                                //设置字体
                                tv.setTypeface(tf);
                            }
                        }))

                .show();//显示引导层(至少需要一页引导页才能显示)

    }

}
