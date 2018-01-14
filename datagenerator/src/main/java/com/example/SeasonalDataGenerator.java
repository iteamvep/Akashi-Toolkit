/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author iTeam_VEP
 */
public class SeasonalDataGenerator {
    private Pattern pattern = null; 
    private Matcher matcher = null; 
    
    public HashMap mainpage(Document html){
        HashMap<String,Object> dataMap = new LinkedHashMap<>();
        HashMap<String,Object> seasonMap = new LinkedHashMap<>();
        Elements links = html.select("#mw-content-text").select("ul").first().children();
        
        HashMap<String,String> seasons = new LinkedHashMap<>();
        for(Element link:links) {
            link = link.select("a").first();
            if(link.attr("title").contains("页面不存在")) {
                continue;
            }
            seasons.put(link.ownText(), link.attr("href"));
        }
        /*//set proxy
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "1090");*/
        
        for(String season:seasons.keySet()) {
            Document doc = null;
            try {
                System.out.println("https://zh.kcwiki.org" + seasons.get(season));
                doc = Jsoup.connect("https://zh.kcwiki.org" + seasons.get(season))
                        .timeout(5000)
                        .get(); 
            } catch (IOException ex) {
                Logger.getLogger(SeasonalDataGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
            Element content = doc.select("#mw-content-text").first();
            if(!content.select(".gallerybox").isEmpty()) {
                seasonMap.put("gallery", getGallery(content.html()));
            }
            
            if(!content.select("[id^=MWPlayer-]").isEmpty()) {
                Elements musics = content.select("[id^=MWPlayer-]");
                Elements javascripts = content.select("script");
                ArrayList<String> musicList = new ArrayList<>();
                for (Element music:musics) {
                    String id = music.attr("id");
                    for (Element javascript:javascripts) {
                        if(javascript.html().contains(id)) {
                            String js = javascript.html();
                            js = js.replace("<!--// <![CDATA[", "");
                            js = js.replace("// ]]> -->", "");
                            String[] items = js.split(",");
                            for (String item:items) {
                                if (item.contains("file")) {
                                    items = item.split("\"");
                                    //System.out.println(items[1]);
                                    musicList.add(items[1]);
                                }
                            }
                        }
                    }
                }
                seasonMap.put("music", musicList.clone());
            }
            content = doc.select("#mw-content-text").first();
            if(!content.select("h3").isEmpty() && content.select(".wikitable").html().contains("语音")) {
                seasonMap.put("voice", getVoice(content.parent().html()).clone());
            }
            dataMap.put(season, seasonMap.clone());
            seasonMap.clear();
        }
        String filepath = "L:/NetBeans/NetBeansProjects/Akashi-Toolkit/src/json/SeasonalData.json";
        com.example.utils.RWFile.writeLog(JSON.toJSONString(dataMap.clone()), filepath);
        return dataMap;
    }
    
    public HashMap getGallery(String html) {
        HashMap<String,Object> galleryDataMap = new LinkedHashMap<>();
        Document doc = Jsoup.parse(html);
        
        Elements dom = doc.select("[class=gallery mw-gallery-traditional]").first().parent().children();
        for(int index = 0 ; index < dom.size() ; index++) {
            //System.out.println("block: \r\n" + dom.get(index).html());
        
        if(dom.get(index).select(".gallerybox").isEmpty() || !(dom.get(index-1).text().contains("立绘") || dom.get(index-1).text().contains("图像") || dom.get(index-1).text().contains("限定") || dom.get(index-1).text().contains("mode") || dom.get(index-1).text().contains("MODE") )) {
            continue;
        }
        if((dom.get(index-1).text().contains("旧") || dom.get(index-1).text().contains("往年") || dom.get(index-1).text().contains("家具")) ) {
            continue;
        }
        
        Elements items = dom.get(index).select(".gallerybox");
        
        HashMap<String,Object> tmpMap = new LinkedHashMap<>();
        ArrayList<Object> typeList = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>(); 
        for (int i=0; i<items.size(); i++){
            //String illustrationHref = items.get(i).select(".thumb").select("a").first().attr("href");
            String illustrationType = null;
            String shipHref = null;
            String shipName = null;
            String title = null;
            String illustrationHref = null ;
            
            pattern = Pattern.compile("data-url=\"(.*?png)/"); 
            matcher = pattern.matcher(items.get(i).select(".thumb").select("a").html()); 
            if(matcher.find()){ 
                illustrationHref = matcher.group(1).replace("/thumb", "");
                //System.out.println(illustrationHref);
            } else {
                illustrationHref = items.get(i).select(".thumb").select("a").first().attr("href");
            }
            if(items.get(i).select(".gallerytext").first().children().isEmpty()) {
                illustrationType = items.get(i).select(".thumb").select("img").first().attr("alt");
            } else {
                illustrationType = items.get(i).select(".gallerytext").select("p").first().ownText();
            }
            if(items.get(i).select(".gallerytext").select("p").isEmpty()){
                title = strFilter(illustrationType);
            } else if (items.get(i).select(".gallerytext").select("a").isEmpty()){
                System.out.println(items.get(i).html());
                illustrationType = items.get(i).select(".gallerytext").select("p").first().ownText();
                title = strFilter(illustrationType);
            } else {
                shipHref = items.get(i).select(".gallerytext").select("a").first().attr("href");
                shipName = items.get(i).select(".gallerytext").select("a").first().ownText();
                illustrationType = items.get(i).select(".gallerytext").select("p").first().ownText();
                title = strFilter(shipName + " " + illustrationType);
            }
            
            
            tmpMap.put("illustrationHref", illustrationHref);
            tmpMap.put("shipHref", shipHref);
            tmpMap.put("shipName", shipName);
            tmpMap.put("illustrationType", illustrationType);
            
            galleryDataMap.put(title, tmpMap.clone());
            tmpMap.clear();
        }
        }
        return galleryDataMap;
    }
    
    public HashMap getVoice(String html) {
        Document doc = Jsoup.parse(html);
        Elements items = doc.select("#mw-content-text").first().children();
        HashMap<String,Object> voiceDataMap = new LinkedHashMap<>();
        
        for (int i=0; i<items.size(); i++){
            if(!(items.get(i).is("h3") && items.get(i+1).is("table"))) {
                continue;
            }
            HashMap<String,Object> tmpMap = new LinkedHashMap<>();
            ArrayList<Object> typeList = new ArrayList<>();
            ArrayList<String> titleList = new ArrayList<>(); 
            Elements trs = items.get(i+1).select("tr");
            for (int j=0; j<trs.size(); j++){
                Elements ths = trs.get(j).select("th");
                if(ths.size()>0){
                    for (int k=0; k<ths.size(); k++){
                        String txt = ths.get(k).text();
                        titleList.add(txt);
                    }
                }
                
                Elements tds = trs.get(j).select("td");
                if(tds.size()>0){
                        String txt = null;
                        
                        /*
                        if(tds.size() == 3) {
                            //0
                                String url = tds.get(0).select("li").first().html();
                                pattern = Pattern.compile("data-filesrc=\"(.*)\""); 
                                matcher = pattern.matcher(url); 
                                if(matcher.find()){ 
                                    //System.out.println(matcher.group(1));
                                    tmpMap.put("voice", matcher.group(1));
                                } else {
                                    tmpMap.put("voice", null);
                                }
                            
                            //1
                                txt = tds.get(1).text();
                                tmpMap.put("ship", txt);
                            
                            //2
                                System.out.println(tds.get(2).html());
                                if(tds.get(2).select("span").isEmpty()){
                                    txt = tds.get(2).text();
                                } else {
                                    txt = tds.get(2).select("span").first().text();
                                }
                                tmpMap.put("jp", txt);
                            
                            //3
                                txt = tds.get(3).text();
                                tmpMap.put("zh", txt);
                            
                        
                        }
                        */

                        if(tds.size() == 4) {
                            //0
                                String url = tds.get(0).select("li").first().html();
                                pattern = Pattern.compile("data-filesrc=\"(.*)\""); 
                                matcher = pattern.matcher(url); 
                                if(matcher.find()){ 
                                    //System.out.println(matcher.group(1));
                                    tmpMap.put("voice", matcher.group(1));
                                } else {
                                    tmpMap.put("voice", null);
                                }
                            
                            //1
                                txt = tds.get(1).text();
                                tmpMap.put("ship", txt);
                            
                            //2
                                System.out.println(tds.get(2).html());
                                if(tds.get(2).select("span").isEmpty()){
                                    txt = tds.get(2).text();
                                } else {
                                    txt = tds.get(2).select("span").first().text();
                                }
                                tmpMap.put("jp", txt);
                            
                            //3
                                txt = tds.get(3).text();
                                tmpMap.put("zh", txt);

                    }
                        
                    if(tds.size() == 5) {
                        
                    }
                }
                if(!tmpMap.isEmpty()) {
                    typeList.add(tmpMap.clone());
                    tmpMap.clear(); 
                }
            }
            if(!typeList.isEmpty()){
                voiceDataMap.put(items.get(i).text(), typeList.clone());
                typeList.clear();
            }   
        }
        
        return voiceDataMap;
    }
    
    public String strFilter(String str) {
        String regEx = "[`~!@#$%^&*()\\-+={}':;,\\[\\].<>/?￥%…（）_+|【】‘；：”“’。，、？\\s]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    
    public static void main(String[] args) {

        try {
            Document doc = Jsoup.connect("https://zh.kcwiki.org/wiki/%E5%AD%A3%E8%8A%82%E6%80%A7")
                    //.data("query", "Java")   // 请求参数
                    //.userAgent("I ’ m jsoup") // 设置 User-Agent
                    //.cookie("auth", "token") // 设置 cookie
                    .timeout(3000)           // 设置连接超时时间
                    .get();                 // 使用 POST 方法访问 URL   
            
            new SeasonalDataGenerator().mainpage(doc);

                } catch (IOException ex) {
                    Logger.getLogger(SeasonalDataGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
}
