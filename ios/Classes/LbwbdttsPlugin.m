#import "LbwbdttsPlugin.h"
@implementation LbwbdttsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"lbwbdtts"
            binaryMessenger:[registrar messenger]];
  LbwbdttsPlugin* instance = [[LbwbdttsPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else if ([@"initsdk" isEqualToString:call.method]) {
      NSLog(@"TTS该插件不支持ios");
  }else if ([@"speaker" isEqualToString:call.method]) {
      NSLog(@"TTS speaker");
  }
  else if ([@"speak" isEqualToString:call.method]) {
      NSLog(@"TTS speak");
  }
  else if ([@"pause" isEqualToString:call.method]) {
      NSLog(@"TTS pause");
  }
  else if ([@"resume" isEqualToString:call.method]) {
      NSLog(@"TTS resume");
  }
  else if ([@"stop" isEqualToString:call.method]) {
      NSLog(@"TTS stop");
  }else {
    result(FlutterMethodNotImplemented);
  }
}

@end
