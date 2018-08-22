package com.lfc.myapplication.qcr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.lfc.myapplication.R;

import java.io.File;

/**
 * 身份证 识别
 *
 * @author Administrator-LFC
 *         created at 2018/8/20 17:08
 */
public class Test_QCR_A extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 102;

    private Button mBtnFrontM;
    private Button mBtnBackM;
    private Button mBtnFrontA;
    private Button mBtnBackA;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcr);
        initView();
        initSDK();
    }


    private void initView() {
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mBtnFrontM = (Button) findViewById(R.id.btn_front_m);
        mBtnBackM = (Button) findViewById(R.id.btn_back_m);
        mBtnFrontA = (Button) findViewById(R.id.btn_front_a);
        mBtnBackA = (Button) findViewById(R.id.btn_back_a);
        mBtnFrontM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                正面手动
                Intent intent = new Intent(Test_QCR_A.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        mBtnBackM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                反面手动
                Intent intent = new Intent(Test_QCR_A.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        mBtnFrontA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                正面自动
                Intent intent = new Intent(Test_QCR_A.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
                // 请手动使用CameraNativeHelper初始化和释放模型
                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
                intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        mBtnBackA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                反面自动
                Intent intent = new Intent(Test_QCR_A.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
                // 请手动使用CameraNativeHelper初始化和释放模型
                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
                intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });


    }

    private void initSDK() {

        OCR.getInstance(this).initAccessTokenWithAkSk(
                new OnResultListener<AccessToken>() {
                    @Override
                    public void onResult(AccessToken result) {

                        // 本地自动识别需要初始化
                        initLicense();

                        Log.d("MainActivity", "onResult: " + result.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Test_QCR_A.this, "初始化认证成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(OCRError error) {
                        error.printStackTrace();
                        Log.e("MainActivity", "onError: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Test_QCR_A.this, "初始化认证失败,请检查 key", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, getApplicationContext(),
                // 需要自己配置 https://console.bce.baidu.com
                "wFQOOrtCrLj5V1Eq5abEmGiA",
                // 需要自己配置 https://console.bce.baidu.com
                "TrvIxsz7mqeEuTfkwfhPbV9zqyjzaqap");

    }


    private void initLicense() {
        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        final String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Test_QCR_A.this,
                                        "本地质量控制初始化错误，错误原因： " + msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        }

    }

    /**
     * 解析身份证图片
     *
     * @param idCardSide 身份证正反面
     * @param filePath   图片路径
     */
    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(40);
        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
/**
 * {direction=0, wordsResultNumber=6, address=河南省鹤壁市鹤山区姬家山乡黄庙沟村1号院121号, idNumber=410602199003034517, birthday=19900303,
 * name=康俊军, gender=男, ethnic=汉}

 {, signDate=20160321, expiryDate=20360321, issueAuthority=鹤壁市公安局鹤山分局}
 */
                    String name = "";
                    String sex = "";
                    String nation = "";
                    String num = "";
                    String address = "";
                    String signDate = ""; //开始日期
                    String expiryDate = "";//结束日期
                    String issueAuthority = "";//发证单位
                    if (result.getName() != null) {
                        name = result.getName().toString();
                    }
                    if (result.getGender() != null) {
                        sex = result.getGender().toString();
                    }
                    if (result.getEthnic() != null) {
                        nation = result.getEthnic().toString();
                    }
                    if (result.getIdNumber() != null) {
                        num = result.getIdNumber().toString();
                    }
                    if (result.getAddress() != null) {
                        address = result.getAddress().toString();
                    }

                    if (result.getSignDate() != null) {
                        signDate = result.getSignDate().toString();
                    }
                    if (result.getExpiryDate() != null) {
                        expiryDate = result.getExpiryDate().toString();
                    }
                    if (result.getIssueAuthority() != null) {
                        issueAuthority = result.getIssueAuthority().toString();
                    }
                    if (TextUtils.isEmpty(issueAuthority) && !TextUtils.isEmpty(name)) {
                        mTvResult.setText("姓名: " + name + "\n" +
                                "性别: " + sex + "\n" +
                                "民族: " + nation + "\n" +
                                "身份证号码: " + num + "\n" +
                                "住址: " + address + "\n");
                    } else {
                        mTvResult.setText("发证日期: " + signDate + "\n" +
                                "截止日期: " + expiryDate + "\n" +
                                "发证单位: " + issueAuthority);
                    }

                }
            }

            @Override
            public void onError(OCRError error) {
                Toast.makeText(Test_QCR_A.this, "识别出错,请查看log错误代码", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "onError: " + error.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        CameraNativeHelper.release();
        // 释放内存资源
        OCR.getInstance(this).release();
        super.onDestroy();

    }
}
