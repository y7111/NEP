package com.nep.mobile;

import android.app.Application;
import com.nep.io.FileIO;
import com.nep.entity.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File; //

public class NEMobileApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileIO.setApplicationContext(getApplicationContext());

        // 检查文件是否存在，如果不存在则初始化数据
        // 生产环境中应避免这种方式，或提供更安全的数据管理机制
        if (!new File(getApplicationContext().getFilesDir(), "admin.txt").exists()) { //
            initializeTestData();
        }
    }

    private void initializeTestData() {
        // Testadmin.java 逻辑
        Admin a1 = new Admin(); //
        a1.setLoginCode("1001"); //
        a1.setPassword("111"); //
        a1.setRealName("赵本山"); //
        Admin a2 = new Admin(); //
        a2.setLoginCode("1002"); //
        a2.setPassword("222"); //
        a2.setRealName("刘德华"); //
        List<Admin> alist = new ArrayList<Admin>(); //
        alist.add(a1); //
        alist.add(a2); //
        FileIO.writeObject("admin.txt", alist); //

        // Testprovince_city.java 逻辑
        ProvinceCity p1 = new ProvinceCity(); //
        p1.setProvinceId(1001); //
        p1.setProvinceName("辽宁省"); //
        List<String> city1 = new ArrayList<String>(); //
        city1.add("沈阳市"); //
        city1.add("大连市"); //
        city1.add("鞍山市"); //
        city1.add("抚顺市"); //
        p1.setCityName(city1); //

        ProvinceCity p2 = new ProvinceCity(); //
        p2.setProvinceId(1002); //
        p2.setProvinceName("吉林省"); //
        List<String> city2 = new ArrayList<String>(); //
        city2.add("长春市"); //
        city2.add("吉林市"); //
        city2.add("四平市"); //
        p2.setCityName(city2); //

        ProvinceCity p3 = new ProvinceCity(); //
        p3.setProvinceId(1003); //
        p3.setProvinceName("黑龙江省"); //
        List<String> city3 = new ArrayList<String>(); //
        city3.add("哈尔滨市"); //
        city3.add("大庆市"); //
        city3.add("齐齐哈尔市"); //
        p3.setCityName(city3); //

        List<ProvinceCity> plist = new ArrayList<ProvinceCity>(); //
        plist.add(p1); //
        plist.add(p2); //
        plist.add(p3); //
        FileIO.writeObject("province_city.txt", plist); //

        // Testaqi.java 逻辑
        Aqi aqi1 = new Aqi("一级", "优", "空气质量令人满意,基本无空气污染"); //
        Aqi aqi2 = new Aqi("二级", "良", "空气质量可接受,但某些污染物可能对极少数异常敏感人群健康有较弱的影响"); //
        Aqi aqi3 = new Aqi("三级", "轻度污染", "易感人群症状有轻度加剧,健康人群出现刺激症状"); //
        Aqi aqi4 = new Aqi("四级", "中度污染", "进一步加剧易感人群症状,可能对健康人群心脏、呼吸系统有影响"); //
        Aqi aqi5 = new Aqi("五级", "重度污染", "心脏病和肺病患者症状显著加剧,运动耐受力降低,健康人群普遍出现症状"); //
        Aqi aqi6 = new Aqi("六级", "严重污染", "健康人群耐受力降低,有明显强烈症状,提前出现某些疾病"); //
        List<Aqi> aqilist = new ArrayList<Aqi>(); //
        aqilist.add(aqi1); //
        aqilist.add(aqi2); //
        aqilist.add(aqi3); //
        aqilist.add(aqi4); //
        aqilist.add(aqi5); //
        aqilist.add(aqi6); //
        FileIO.writeObject("aqi.txt", aqilist); //

        // Testaqifeedback.java 逻辑
        List<AqiFeedback> afList = new ArrayList<AqiFeedback>(); //
        FileIO.writeObject("aqifeedback.txt", afList); //

        // TestGM.java 逻辑
        GridMember gm1 = new GridMember(); //
        gm1.setLoginCode("2024001"); //
        gm1.setPassword("111"); //
        gm1.setRealName("郭晓川"); //
        gm1.setGmTel("13888888888"); //
        gm1.setState("工作中"); //
        GridMember gm2 = new GridMember(); //
        gm2.setLoginCode("2024002"); //
        gm2.setPassword("222"); //
        gm2.setRealName("韩德君"); //
        gm2.setGmTel("13555555555"); //
        gm2.setState("工作中"); //
        GridMember gm3 = new GridMember(); //
        gm3.setLoginCode("2024003"); //
        gm3.setPassword("333"); //
        gm3.setRealName("李晓旭"); //
        gm3.setGmTel("13444444444"); //
        gm3.setState("工作中"); //
        GridMember gm4 = new GridMember(); //
        gm4.setLoginCode("2024004"); //
        gm4.setPassword("444"); //
        gm4.setRealName("赵继伟"); //
        gm4.setGmTel("13222222222"); //
        gm4.setState("休假中"); //
        GridMember gm5 = new GridMember(); //
        gm5.setLoginCode("2024005"); //
        gm5.setPassword("555"); //
        gm5.setRealName("易建联"); //
        gm5.setGmTel("13666666666"); //
        gm5.setState("工作中"); //
        List<GridMember> glist = new ArrayList<GridMember>(); //
        glist.add(gm1); //
        glist.add(gm2); //
        glist.add(gm3); //
        glist.add(gm4); //
        glist.add(gm5); //
        FileIO.writeObject("gridmember.txt", glist); //

        // TestsecurityQuestion.java 逻辑
        securityQuestion sq1 = new securityQuestion("您的出生地是哪里?"); //
        securityQuestion sq2 = new securityQuestion("您母亲的姓名是什么?"); //
        securityQuestion sq3 = new securityQuestion("您宠物的名字是什么?"); //
        securityQuestion sq4 = new securityQuestion("您最喜欢的颜色是什么?"); //
        List<securityQuestion> sqlist = new ArrayList<securityQuestion>(); //
        sqlist.add(sq1); //
        sqlist.add(sq2); //
        sqlist.add(sq3); //
        sqlist.add(sq4); //
        FileIO.writeObject("securityQuestion.txt", sqlist); //

        // Testsupervisor.java 逻辑
        Supervisor s1 = new Supervisor(); //
        s1.setLoginCode("202501"); //
        s1.setPassword("123"); //
        s1.setRealName("tom"); //
        s1.setSex("男"); //
        s1.setSecurityQuestion("您的出生地是哪里?"); //
        s1.setSecurityAnswer("202501"); //
        Supervisor s2 = new Supervisor(); //
        s2.setLoginCode("202502"); //
        s2.setPassword("123"); //
        s2.setRealName("jack"); //
        s2.setSex("男"); //
        s2.setSecurityQuestion("您的出生地是哪里?"); //
        s2.setSecurityAnswer("202502"); //
        Supervisor s3 = new Supervisor(); //
        s3.setLoginCode("202503"); //
        s3.setPassword("123"); //
        s3.setRealName("rose"); //
        s3.setSex("女"); //
        s3.setSecurityQuestion("您的出生地是哪里?"); //
        s3.setSecurityAnswer("202503"); //
        List<Supervisor> slist = new ArrayList<Supervisor>(); //
        slist.add(s1); //
        slist.add(s2); //
        slist.add(s3); //
        FileIO.writeObject("supervisor.txt", slist); //
    }
}
