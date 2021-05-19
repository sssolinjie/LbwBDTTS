package com.example.lbwbdtts;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/** LbwbdttsPlugin */
public class LbwbdttsPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;
  protected SpeechSynthesizer mSpeechSynthesizer;
  protected BaiduConfig config;
  protected Application application;
  protected MessageListener messageListener;

  public static String ename;

  public void playTTsEnd(String eventname){
    ename = eventname;
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        try {
          channel.invokeMethod(ename, "1");
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
    config = BaiduConfig.getInstance();
    //initPermission();
  }
  //此处是旧的插件加载注册方式
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "lbwbdtts");
    channel.setMethodCallHandler(new LbwbdttsPlugin().initPlugin(channel, registrar));
  }
  public LbwbdttsPlugin initPlugin(MethodChannel methodChannel, Registrar registrar) {
    channel = methodChannel;
    context = registrar.context();
    application = ((Application)context.getApplicationContext());
    config = BaiduConfig.getInstance();
    //channel.setMethodCallHandler(this);
    //initPermission();
    return this;
  }
  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  //===================分割线======
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
    else if (call.method.equals("pause")) {
      pause();
    }
    else if (call.method.equals("resume")) {
      resume();
    }
    else if (call.method.equals("stop")) {
      stop();
    }else if(call.method.equals("testBtn")){
      playTTsEnd("tarnish");
    }else {
      result.notImplemented();
    }
  }
  void initSDK(Map<String, String> params){
    config = BaiduConfig.getInstance();
    config.SetAppId(params.get("appid"));
    config.SetAppKey(params.get("appkey"));
    config.SetSecretKey(params.get("secretkey"));
    mSpeechSynthesizer = SpeechSynthesizer.getInstance();
    mSpeechSynthesizer.setContext(context);
    mSpeechSynthesizer.setAppId(config.getAppId());
    mSpeechSynthesizer.setApiKey(config.getAppKey(), config.getSecretKey());

    messageListener = new MessageListener(this);
    mSpeechSynthesizer.setSpeechSynthesizerListener(messageListener);
    speaker("4");
    int result = mSpeechSynthesizer.initTts(TtsMode.ONLINE);
    if (result != 0) {
      //Log.d("LBWBDTTS", "引擎初始化失败");
    }else{
      Log.d("LBWBDTTS", "引擎初始化成功");
    }
  }
  protected Map<String, String> getParams() {
    Map<String, String> params = new HashMap<>();
    // 以下参数均为选填
    // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>, 其它发音人见文档
    params.put(SpeechSynthesizer.PARAM_SPEAKER, config.getSpeaker());
    // 设置合成的音量，0-15 ，默认 5
    params.put(SpeechSynthesizer.PARAM_VOLUME, "15");
    // 设置合成的语速，0-15 ，默认 5
    params.put(SpeechSynthesizer.PARAM_SPEED, "5");
    // 设置合成的语调，0-15 ，默认 5
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
  void speak(String text){
    setParams();
    int r = mSpeechSynthesizer.speak(text);
    if (r != 0) {
     // Log.d("LBWBDTTS", "合成语音失败");
    }else{
      //Log.d("LBWBDTTS", "合成语音成功");
    }

  }

  /**
   * 暂停播放。仅调用speak后生效
   */
  private void pause() {
    mSpeechSynthesizer.pause();
  }

  /**
   * 继续播放。仅调用speak后生效，调用pause生效
   */
  private void resume() {
     mSpeechSynthesizer.resume();
  }

  /*
   * 停止合成引擎。即停止播放，合成，清空内部合成队列。
   */
  private void stop() {
    mSpeechSynthesizer.stop();
  }
}
