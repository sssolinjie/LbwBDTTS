package com.example.lbwbdtts;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.PluginRegistry;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/** LbwbdttsPlugin */
public class LbwbdttsPlugin implements FlutterPlugin, MethodCallHandler,ActivityAware,PluginRegistry.ActivityResultListener {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;
  protected SpeechSynthesizer mSpeechSynthesizer;
  protected BaiduConfigtts config;
  protected Application application;
  protected MessageListener messageListener;
  private static Activity activity;
  public static String ename;

  public void playTTsEnd(String eventname, final String data){
    ename = eventname;
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        try {
          channel.invokeMethod(ename, data);
        }
        catch (Exception e){
          Log.d("TAG11111111", e.toString());
        }
      }
    });
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "lbwbdtts");
    channel.setMethodCallHandler(this);
    application = ((Application) flutterPluginBinding.getApplicationContext());
    context = application.getApplicationContext();
    config = BaiduConfigtts.getInstance();
    //initPermission();
  }
  //???????????????????????????????????????
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "lbwbdtts");
    channel.setMethodCallHandler(new LbwbdttsPlugin().initPlugin(channel, registrar));
  }
  public LbwbdttsPlugin initPlugin(MethodChannel methodChannel, Registrar registrar) {
    channel = methodChannel;
    context = registrar.context();
    application = ((Application)context.getApplicationContext());
    config = BaiduConfigtts.getInstance();
    //channel.setMethodCallHandler(this);
    //initPermission();
    return this;
  }
  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  //===================?????????======
  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("initsdk")) {
      Map<String, String> params = call.arguments();
      initSDK(params);
    }
    else if (call.method.equals("speaker")) {
      String message = call.arguments();
      speaker(message);
    }
    else if (call.method.equals("speak")) {
      String message = call.arguments();
      speak(message);
    }
    else if (call.method.equals("speaklist")) {
      Map<String, String> params = call.arguments();
      String message = params.get("message");
      String messageid = params.get("messageid");
      speakList(message,messageid);
    }
    else if (call.method.equals("pause")) {
      pause();
    }
    else if (call.method.equals("resume")) {
      resume();
    }
    else if (call.method.equals("stop")) {
      stop();
    }else if(call.method.equals("testBtn")){
      //playTTsEnd("tarnish");
    }else {
      result.notImplemented();
    }
  }
  void initSDK(Map<String, String> params){
    initPermission();
    config = BaiduConfigtts.getInstance();
    config.SetAppId(params.get("appid"));
    config.SetAppKey(params.get("appkey"));
    config.SetSecretKey(params.get("secretkey"));
    mSpeechSynthesizer = SpeechSynthesizer.getInstance();
    mSpeechSynthesizer.setContext(context);
    mSpeechSynthesizer.setAppId(config.getAppId());
    mSpeechSynthesizer.setApiKey(config.getAppKey(), config.getSecretKey());

    messageListener = new MessageListener(this);
    mSpeechSynthesizer.setSpeechSynthesizerListener(messageListener);

   // mSpeechSynthesizer.loadModel("",  "");

    speaker("4");
    int result = mSpeechSynthesizer.initTts(TtsMode.MIX);
    if (result != 0) {
      //Log.d("LBWBDTTS", "?????????????????????");
    }else{
      Log.d("LBWBDTTS", "?????????????????????");
     
    }
  }
  protected Map<String, String> getParams() {
    Map<String, String> params = new HashMap<>();
    // ????????????????????????
    // ??????????????????????????? 0 ???????????????????????? 1 ???????????? 3 ????????????<?????????> 4 ???????????????<?????????>, ????????????????????????
    params.put(SpeechSynthesizer.PARAM_SPEAKER, config.getSpeaker());
    // ????????????????????????0-15 ????????? 5
    params.put(SpeechSynthesizer.PARAM_VOLUME, "15");
    // ????????????????????????0-15 ????????? 5
    params.put(SpeechSynthesizer.PARAM_SPEED, "5");
    // ????????????????????????0-15 ????????? 5
    params.put(SpeechSynthesizer.PARAM_PITCH, "5");
    return params;
  }

  public void setParams(){
    Map<String, String> params = getParams();
    if (params != null) {
      for (Map.Entry<String, String> e : params.entrySet()) {
        mSpeechSynthesizer.setParam(e.getKey(), e.getValue());
      }
    }
    mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, config.getSpeaker());
  }
  void speaker(String msg)
  {
    config.SetSpeaker(msg);
    setParams();
  }
  void speakList(String text,String message){
    setParams();
    int r = mSpeechSynthesizer.speak(text, message);
    if (r != 0) {
     // Log.d("LBWBDTTS", "??????????????????");
    }else{
      //Log.d("LBWBDTTS", "??????????????????");
    }
  }
  void speak(String text){
    setParams();
    int r = mSpeechSynthesizer.speak(text);
    if (r != 0) {
      // Log.d("LBWBDTTS", "??????????????????");
    }else{
      //Log.d("LBWBDTTS", "??????????????????");
    }
  }

  /**
   * ????????????????????????speak?????????
   */
  private void pause() {
    mSpeechSynthesizer.pause();
  }

  /**
   * ????????????????????????speak??????????????????pause??????
   */
  private void resume() {
     mSpeechSynthesizer.resume();
  }

  /*
   * ???????????????????????????????????????????????????????????????????????????
   */
  private void stop() {
    mSpeechSynthesizer.stop();
  }



  private void initPermission() {
    String permissions[] = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };

    ArrayList<String> toApplyList = new ArrayList<String>();

    for (String perm : permissions) {
      if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this.context, perm)) {
        toApplyList.add(perm);
        //?????????????????????????????????.
      }
    }
    String tmpList[] = new String[toApplyList.size()];
    if (!toApplyList.isEmpty()) {
      ActivityCompat.requestPermissions(activity, toApplyList.toArray(tmpList), 123);
    }

  }
  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.i("BaiduFacePlugin",
            "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode + ", data=" + data);
    return false;
  }

  ///activity ????????????
  @Override
  public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    Log.e("onAttachedToActivity", "onAttachedToActivity");
    this.activity = activityPluginBinding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    //Log.e("onDetachedFromActivityForConfigChanges", "onDetachedFromActivityForConfigChanges");
    //EventBus.getDefault().unregister(this);
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
    //Log.e("onReattachedToActivityForConfigChanges", "onReattachedToActivityForConfigChanges");
  }

  @Override
  public void onDetachedFromActivity() {
    //Log.e("onDetachedFromActivity", "onDetachedFromActivity");
  }
}
