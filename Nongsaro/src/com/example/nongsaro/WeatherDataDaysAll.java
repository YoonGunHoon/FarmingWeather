package com.example.nongsaro;

public class WeatherDataDaysAll {
    String province; // 시도
    String numEf; // 날짜 수
    String tmEf; // 날짜
    String wf;// 날씨
    String tmn;// 최저기온
    String tmx;// 최고기온
    String reliability; // 신뢰도

    public WeatherDataDaysAll(String province, String numEf, String tmEf,
                              String wf, String tmn, String tmx, String reliability) {
        super();
        this.province = province;
        this.numEf = numEf;
        this.tmEf = tmEf;
        this.wf = wf;
        this.tmn = tmn;
        this.tmx = tmx;
        this.reliability = reliability;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNumEf() {
        return numEf;
    }

    public void setNumEf(String numEf) {
        this.numEf = numEf;
    }

    public String getTmEf() {
        return tmEf;
    }

    public void setTmEf(String tmEf) {
        this.tmEf = tmEf;
    }

    public String getWf() {
        return wf;
    }

    public void setWf(String wf) {
        this.wf = wf;
    }

    public String getTmn() {
        return tmn;
    }

    public void setTmn(String tmn) {
        this.tmn = tmn;
    }

    public String getTmx() {
        return tmx;
    }

    public void setTmx(String tmx) {
        this.tmx = tmx;
    }

    public String getReliability() {
        return reliability;
    }

    public void setReliability(String reliability) {
        this.reliability = reliability;
    }

}
