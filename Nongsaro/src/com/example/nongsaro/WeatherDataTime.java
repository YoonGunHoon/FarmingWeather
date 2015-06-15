package com.example.nongsaro;

public class WeatherDataTime {
    String hour; // 시간
    String day; // 날짜
    String temp;// 기온
    String tmx;// 최고기온
    String tmn;// 최저기온
    String sky;//하늘상태
    String pty;//강수
    String wfKor; // 날씨상태
    String wfEn;
    String pop;
    String r12;
    String s12;
    String ws;
    String wd;
    String wdKor; //풍향
    String wdEn;
    String reh;//습도
    String r06;
    String s06;

    public WeatherDataTime(String hour, String day, String temp, String tmx,
                           String tmn, String sky, String pty, String wfKor, String wfEn,
                           String pop, String r12, String s12, String ws, String wd,
                           String wdKor, String wdEn, String reh, String r06, String s06) {
        super();
        this.hour = hour;
        this.day = day;
        this.temp = temp;
        this.tmx = tmx;
        this.tmn = tmn;
        this.sky = sky;
        this.pty = pty;
        this.wfKor = wfKor;
        this.wfEn = wfEn;
        this.pop = pop;
        this.r12 = r12;
        this.s12 = s12;
        this.ws = ws;
        this.wd = wd;
        this.wdKor = wdKor;
        this.wdEn = wdEn;
        this.reh = reh;
        this.r06 = r06;
        this.s06 = s06;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTmx() {
        return tmx;
    }

    public void setTmx(String tmx) {
        this.tmx = tmx;
    }

    public String getTmn() {
        return tmn;
    }

    public void setTmn(String tmn) {
        this.tmn = tmn;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public String getPty() {
        return pty;
    }

    public void setPty(String pty) {
        this.pty = pty;
    }

    public String getWfKor() {
        return wfKor;
    }

    public void setWfKor(String wfKor) {
        this.wfKor = wfKor;
    }

    public String getWfEn() {
        return wfEn;
    }

    public void setWfEn(String wfEn) {
        this.wfEn = wfEn;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getR12() {
        return r12;
    }

    public void setR12(String r12) {
        this.r12 = r12;
    }

    public String getS12() {
        return s12;
    }

    public void setS12(String s12) {
        this.s12 = s12;
    }

    public String getWs() {
        return ws;
    }

    public void setWs(String ws) {
        this.ws = ws;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getWdKor() {
        return wdKor;
    }

    public void setWdKor(String wdKor) {
        this.wdKor = wdKor;
    }

    public String getWdEn() {
        return wdEn;
    }

    public void setWdEn(String wdEn) {
        this.wdEn = wdEn;
    }

    public String getReh() {
        return reh;
    }

    public void setReh(String reh) {
        this.reh = reh;
    }

    public String getR06() {
        return r06;
    }

    public void setR06(String r06) {
        this.r06 = r06;
    }

    public String getS06() {
        return s06;
    }

    public void setS06(String s06) {
        this.s06 = s06;
    }

}
