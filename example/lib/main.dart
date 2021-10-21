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

  int curindex = 0;
  int sction = 0;

  String sp =
      "平台涵盖文化动态、公共文化服务指南、课程报名、活动抢票、场地预约、街头艺人、慕课教学、文化志愿者、文化日历、文化地图、文化直播、线上VR展厅、慕课培训、直播课堂、文化志愿者、文化超市、街头艺人管理、场馆监控、场馆人流、大数据分析、智能推送等23个功能。";
  List<String> liststr;
  List<TextSpan> span = [];
  ScrollController controller = new ScrollController();
  bool open = false;
  @override
  void initState() {
    super.initState();
    genlist();
    Lbwbdtts.addListen((a, v) {
      if (a == "ing") {
        curindex++;
      }
      if (open) {
        if (controller.position.pixels >= controller.position.maxScrollExtent) {
          open = false;
        } else {
          double bili = curindex / sp.length;
          controller.jumpTo(controller.position.maxScrollExtent * bili);
        }
      }
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

  void genlist() {
    span.clear();

    for (int i = 0; i < sp.length; ++i) {
      Color color = Colors.black;
      if (i < curindex) color = Colors.white;
      span.add(
        TextSpan(
            text: sp[i],
            style: TextStyle(
              color: color,
            )),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: GestureDetector(
            onTap: () {
              open = true;
              curindex = 0;
              sction = 0;
              controller.jumpTo(0);
              //Lbwbdtts.speak(sp);

              liststr = split(sp);
              for (int i = 0; i < liststr.length; ++i) {
                if (i == liststr.length - 1) {
                  Lbwbdtts.speakList(liststr[i], 'over');
                } else
                  Lbwbdtts.speakList(liststr[i], i.toString());
              }
            },
            child: Container(
              height: 100,
              color: Colors.blue,
              margin: EdgeInsets.all(20),
              padding: EdgeInsets.all(10),
              child: SingleChildScrollView(
                controller: controller,
                child: Center(
                  child: RichText(
                    // key: UniqueKey(),
                    text: TextSpan(
                      children: span,
                    ),
                  ),
                ),
              ),
            ),
          ),
        ),
        // body: Center(
        //   child: Column(children: [
        //     FlatButton(
        //         onPressed: () {
        //           ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
        //           Lbwbdtts.setSpeaker("0");
        //           Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
        //         },
        //         child: Text("普通女生")),
        //     FlatButton(
        //         onPressed: () {
        //           ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
        //           Lbwbdtts.setSpeaker("1");
        //           Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
        //         },
        //         child: Text("普通男声")),
        //     FlatButton(
        //         onPressed: () {
        //           ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
        //           Lbwbdtts.setSpeaker("2");
        //           Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
        //         },
        //         child: Text("2特别男声")),
        //     FlatButton(
        //         onPressed: () {
        //           ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
        //           Lbwbdtts.setSpeaker("3");
        //           Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
        //         },
        //         child: Text("3情感男声")),
        //     FlatButton(
        //         onPressed: () {
        //           ////设置合成音频0普通女生 1普通男声 2特别男声 3情感男声 4 情感儿童声
        //           Lbwbdtts.setSpeaker("4");
        //           Lbwbdtts.speak("御剑乘风来,除魔天地间,有酒乐逍遥,无酒我亦癫");
        //         },
        //         child: Text("情感儿童声")),
        //     FlatButton(
        //         onPressed: () {
        //           Lbwbdtts.speakList("我是小明1", "0");
        //           Lbwbdtts.speakList("我是小明2", "1");
        //           Lbwbdtts.speakList("我是小明3", "2");
        //           Lbwbdtts.speakList("我是小明4", "3");
        //           Lbwbdtts.speakList("我是小明5", "4");
        //         },
        //         child: Text("我是小明")),
        //     FlatButton(
        //         onPressed: () {
        //           Lbwbdtts.pause();
        //         },
        //         child: Text("pause")),
        //     FlatButton(
        //         onPressed: () {
        //           Lbwbdtts.resume();
        //         },
        //         child: Text("resume")),
        //     FlatButton(
        //         onPressed: () {
        //           Lbwbdtts.stop();
        //         },
        //         child: Text("stop")),
        //   ]),
        // ),
      ),
    );
  }

  List<String> split(String str) {
    List<String> newstr = [];
    List<String> a = [',', ';', '.', '、', '。', '，', '；', '！'];
    int curindex = 0;
    for (int i = 0; i < str.length; ++i) {
      for (int k = 0; k < a.length; ++k) {
        if (str[i] == a[k]) {
          newstr.add(str.substring(curindex, i));
          curindex = i + 1;
          break;
        }
      }
    }
    return newstr;
  }
}
