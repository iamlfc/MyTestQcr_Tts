package com.lfc.myapplication.tts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.lfc.myapplication.R;
import com.lfc.myapplication.tts.control.InitConfig;
import com.lfc.myapplication.tts.control.MySyntherizer;
import com.lfc.myapplication.tts.control.NonBlockSyntherizer;
import com.lfc.myapplication.tts.listener.UiMessageListener;
import com.lfc.myapplication.tts.util.AutoCheck;
import com.lfc.myapplication.tts.util.OfflineResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.lfc.myapplication.tts.control.MainHandlerConstant.PRINT;
import static com.lfc.myapplication.tts.control.MainHandlerConstant.UI_CHANGE_INPUT_TEXT_SELECTION;
import static com.lfc.myapplication.tts.control.MainHandlerConstant.UI_CHANGE_SYNTHES_TEXT_SELECTION;

public class Test_Tts_A extends AppCompatActivity {
    private static final String TAG = "--lfc";

    // ================== 初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
     */
    protected String appId = "11702607";

    protected String appKey = "wFQOOrtCrLj5V1Eq5abEmGiA";

    protected String secretKey = "TrvIxsz7mqeEuTfkwfhPbV9zqyjzaqap";
    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.MIX;

    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
    protected String offlineVoice = OfflineResource.VOICE_MALE;

    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================
    // 主控制类，所有合成控制方法从这个类开始
    protected MySyntherizer synthesizer;
    protected Handler mainHandler;

    private EditText mEtNote;
    private SeekBar mSeek01;
    private SeekBar mSeek02;
    private SeekBar mSeek03;
    private Button mBtnExVoice;
    private Button mBtnSpeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__tts);
        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handle(msg);
            }

        };
        initView();
        initialTts();
    }

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */

    private void initialTts() {

        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        Map<String, String> params = getParams();


        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
//                        toPrint(message); // 可以用下面一行替代，在logcat中查看代码
                        Log.w(TAG, message);
                    }
                }
            }

        });
        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程

    }

    private void initView() {
        mEtNote = (EditText) findViewById(R.id.et_note);
        mSeek01 = (SeekBar) findViewById(R.id.seek_01);
        mSeek02 = (SeekBar) findViewById(R.id.seek_02);
        mSeek03 = (SeekBar) findViewById(R.id.seek_03);
        mBtnExVoice = (Button) findViewById(R.id.btn_ex_voice);
        mBtnSpeek = (Button) findViewById(R.id.btn_speek);
        mBtnExVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExVoice();
            }
        });
        mBtnSpeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(); // 合成并播放
            }
        });
        mSeek01.setProgress(5);
        mSeek02.setProgress(5);
        mSeek03.setProgress(5);
    }

    private void ExVoice() {
        // 切换离线资源

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("引擎空闲时切换");
        final Map<String, String> map = new LinkedHashMap<>(4);
        map.put("离线女声", OfflineResource.VOICE_FEMALE);
        map.put("离线男声", OfflineResource.VOICE_MALE);
        map.put("离线度逍遥", OfflineResource.VOICE_DUXY);
        map.put("离线度丫丫", OfflineResource.VOICE_DUYY);
        final String[] keysTemp = new String[4];
        final String[] keys = map.keySet().toArray(keysTemp);
        builder.setItems(keys, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadModel(map.get(keys[which]));
            }
        });
        builder.show();
    }

    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    private void speak() {
        String text = mEtNote.getText().toString();
        // 需要合成的文本text的长度不能超过1024个GBK字节。
        if (TextUtils.isEmpty(mEtNote.getText())) {
            text = "百度语音，面向广大开发者永久免费开放语音合成技术。";
            mEtNote.setText(text);
        }
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, mSeek01.getProgress() + "");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, mSeek02.getProgress() + "");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, mSeek03.getProgress() + "");
        synthesizer.setParams(params);
        int result = synthesizer.speak(text);
        checkResult(result, "speak");
    }

    /**
     * 切换离线发音。注意需要添加额外的判断：引擎在合成时该方法不能调用
     */
    private void loadModel(String mode) {
        offlineVoice = mode;
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        Log.d(TAG, "切换离线语音：" + offlineResource.getModelFilename());
        int result = synthesizer.loadModel(offlineResource.getModelFilename(), offlineResource.getTextFilename());
        checkResult(result, "loadModel");
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
            Log.d(TAG, "error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }


    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "5");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            Log.e(TAG, "【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }


    protected void handle(Message msg) {
        int what = msg.what;
        switch (what) {
            case PRINT:
                String message = (String) msg.obj;
                Log.d(TAG, message);
                break;
            case UI_CHANGE_INPUT_TEXT_SELECTION:
             /*   if (msg.arg1 <= mEtNote.getText().length()) {
                    mEtNote.setSelection(0, msg.arg1);
                }*/
                break;
            case UI_CHANGE_SYNTHES_TEXT_SELECTION:
               /* SpannableString colorfulText = new SpannableString(mEtNote.getText().toString());
                if (msg.arg1 <= colorfulText.toString().length()) {
                    colorfulText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, msg.arg1, Spannable
                            .SPAN_EXCLUSIVE_EXCLUSIVE);
                    mEtNote.setText(colorfulText);
                }*/
                break;
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }


    private void startAct(Class activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    @Override
    protected void onDestroy() {
        synthesizer.release();
        Log.i(TAG, "释放资源成功");
        super.onDestroy();
    }

}
