/*
 * @Author: your name
 * @Date: 2020-12-22 16:02:20
 * @LastEditTime: 2021-01-18 18:20:47
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /example/Users/imacmini/ParentNode/Flutter/Package/lbwbdtts/lib/lbwbdtts.dart
 */
import 'dart:async';

import 'package:flutter/services.dart';

class Lbwbdtts {
  //固定写死的, tts播放成功
  static String success = "tarnish";
  //播放失败
  static String defeated = "terror";

  static const MethodChannel _channel = const MethodChannel('lbwbdtts');
  static List<Function> callbackList = [];
  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> initSDK(
      {String appid, String appkey, String secretkey}) async {
    Map<String, String> params = Map<String, String>();
    params['appid'] = appid;
    params['appkey'] = appkey;
    params['secretkey'] = secretkey;
    await _channel.invokeMethod("initsdk", params);
  }

  //设置语音模式
  ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
  static Future<void> setSpeaker(String message) async {
    await _channel.invokeMethod("speaker", message);
  }

  //合成并播放
  static Future<void> speak(String message) async {
    await _channel.invokeMethod("speak", message);
  }

  //暂停
  static Future<void> pause() async {
    await _channel.invokeMethod("pause");
  }

  //继续播放
  static Future<void> resume() async {
    await _channel.invokeMethod("resume");
  }

  //停止播放
  static Future<void> stop() async {
    await _channel.invokeMethod("stop");
  }

  //监听tts播放结果
  static void addListen(Function callback) {
    callbackList.add(callback);
    _channel.setMethodCallHandler((call) {
      for (int i = 0; i < callbackList.length; ++i) {
        callbackList[i](call.method, call.arguments);
      }
    });
  }

  //测试
  static Future<void> testBtn() async {
    await _channel.invokeMethod("testBtn");
  }
}
