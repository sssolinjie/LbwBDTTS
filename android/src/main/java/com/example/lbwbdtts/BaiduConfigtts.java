package com.example.lbwbdtts;

public class BaiduConfigtts
{

    private String appId;

    private String appKey;

    private String secretKey;

    private String Speaker;//语音模式

    private static volatile BaiduConfigtts singleton;
    private BaiduConfigtts() {}
    public static BaiduConfigtts getInstance() {
        if (singleton == null) {
            synchronized (BaiduConfigtts.class) {
                if (singleton == null) {
                    singleton = new BaiduConfigtts();
                }
            }
        }
        return singleton;
    }

    public void SetAppId(String id){
        this.appId = id;
    }
    public String getAppId(){
        return this.appId;
    }
    public void SetAppKey(String key){
        this.appKey = key;
    }
    public String getAppKey(){
        return this.appKey;
    }
    public void SetSecretKey(String key){
        this.secretKey = key;
    }
    public String getSecretKey(){
        return this.secretKey;
    }

    public String GetSn(){
        return "978bbf17-xxxxx-xxxx-0078-20baf";//纯离线合成SDK授权码；离在线合成SDK没有此参数
    }

    ///
    public void SetSpeaker(String num){
        this.Speaker = num;
    }
    public String getSpeaker(){
        return this.Speaker;
    }
}
