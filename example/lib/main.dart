/*
 * @Author: your name
 * @Date: 2020-12-22 16:02:21
 * @LastEditTime: 2020-12-23 10:27:48
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /example/lib/main.dart
 */
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:lbwbdtts/lbwbdtts.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  Lbwbdtts.initSDK(
      appid: "24230858",
      appkey: "OyBZwoyRfGwdX4sGuV0l3G5L",
      secretkey: "kp3b34iGfgxPuDLXwU6EVVr3hwmRgz8E");
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    Lbwbdtts.addListen((a, v) {
      print(a);
      print(v);
    });
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await Lbwbdtts.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(children: [
            FlatButton(
                onPressed: () {
                  ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
                  Lbwbdtts.setSpeaker("0");
                  Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
                },
                child: Text("普通女生")),
            FlatButton(
                onPressed: () {
                  ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
                  Lbwbdtts.setSpeaker("1");
                  Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
                },
                child: Text("普通男声")),
            FlatButton(
                onPressed: () {
                  ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
                  Lbwbdtts.setSpeaker("2");
                  Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
                },
                child: Text("2特别男声")),
            FlatButton(
                onPressed: () {
                  ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
                  Lbwbdtts.setSpeaker("3");
                  Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
                },
                child: Text("3情感男声")),
            FlatButton(
                onPressed: () {
                  ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
                  Lbwbdtts.setSpeaker("4");
                  Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
                },
                child: Text("情感儿童声")),
            FlatButton(
                onPressed: () {
                  Lbwbdtts.speak("我是小明");
                },
                child: Text("我是小明")),
            FlatButton(
                onPressed: () {
                  Lbwbdtts.pause();
                },
                child: Text("pause")),
            FlatButton(
                onPressed: () {
                  Lbwbdtts.resume();
                },
                child: Text("resume")),
            FlatButton(
                onPressed: () {
                  Lbwbdtts.stop();
                },
                child: Text("stop")),
          ]),
        ),
      ),
    );
  }
}
