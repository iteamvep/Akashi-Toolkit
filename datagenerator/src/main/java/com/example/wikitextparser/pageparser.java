/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.wikitextparser;

import com.example.network.RetrofitAPI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.isBlank;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import retrofit2.Retrofit;

/**
 *
 * @author iTeam_VEP
 */

//https://zh.kcwiki.org/wiki/User:TrickLin/Sandbox/4
//https://zh.kcwiki.org/wiki/Help:%E5%B8%B8%E7%94%A8%E4%BB%A3%E7%A0%81
//https://zh.kcwiki.org/wiki/%E6%96%B0%E6%89%8B%E5%BC%95%E5%AF%BC
//https://zh.kcwiki.org/wiki/Help:%E7%BC%96%E8%BE%91%E6%8C%87%E5%8D%97
public class pageparser {
    private Pattern pattern = null; 
    private Matcher matcher = null; 
    
    public static void main(String[] args) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://zh.kcwiki.org/")
                    .build();
            
            RetrofitAPI.KcwikiService service = retrofit.create(RetrofitAPI.KcwikiService.class);
            ResponseBody body = service.getPage("季节性/2016年白色情人节", "raw").execute().body();
            //System.out.println(body.string());
            new pageparser().splitOrginText(body.string());
        } catch (IOException ex) {
            Logger.getLogger(pageparser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void test(String title) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://zh.kcwiki.org/")
                    .build();
            
            RetrofitAPI.KcwikiService service = retrofit.create(RetrofitAPI.KcwikiService.class);
            ResponseBody body = service.getPage(title, "raw").execute().body();
            //System.out.println(body.string());
            new pageparser().splitOrginText(body.string());
        } catch (IOException ex) {
            Logger.getLogger(pageparser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public HashMap splitOrginText(String wikitext) {
        String line ;
        HashMap<String,Object> pageCatalog = new LinkedHashMap<>();
        
        
        BufferedReader reader = new BufferedReader(new StringReader(wikitext));
        ArrayList<String> lineArray = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                lineArray.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(pageparser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int lastIdCount = -1;
        String idTitle = null;
        int[] idRange = new int[2];
        ArrayList<Object> idRangeList = new ArrayList<>();
        ArrayList<String> idTitleList = new ArrayList<>();
        for(int lineCount = 0; lineCount < lineArray.size(); lineCount++ ) {
            String lineString = lineArray.get(lineCount); 
            if(lineString.startsWith("=")) {
                pattern = Pattern.compile("(=+)"); 
                matcher = pattern.matcher(lineString);
                if(matcher.find() && matcher.group(1).equals(matcher.group(0))){ 
                    idTitle = lineString.replaceAll(matcher.group(0), "");
                    if(lastIdCount == -1 && lineCount != 0) {
                        idRange[0] = 0;
                        idRange[1] = lineCount - 1;
                        lastIdCount = lineCount;
                        idRangeList.add(idRange.clone());
                        idTitleList.add("headTitle");
                    } else if (lastIdCount == -1 && lineCount == 0) {
                        lastIdCount = 0;
                    } else {
                        idRange[0] = lastIdCount + 1;
                        idRange[1] = lineCount - 1;
                        lastIdCount = lineCount;
                        idRangeList.add(idRange.clone());
                    }
                    idTitleList.add(idTitle);
                }
            }
            if(lineString.startsWith("<!--") && lineString.endsWith("-->")) {
                continue;
            }
            if(lineCount == lineArray.size()-1) {
                idRange[0] = lastIdCount + 1;
                idRange[1] = lineCount;
                idRangeList.add(idRange.clone());
                if(lastIdCount == -1) {
                    idTitleList.add("footTitle");
                }
            }
        }
        pageparser pageparser= new pageparser();
        for(int idCount = 0; idCount < idRangeList.size(); idCount++ ) {
            int[] range = (int[]) idRangeList.get(idCount);
            pageCatalog.put(idTitleList.get(idCount), pageparser.getIDContent(range[0],range[1],lineArray));
        }
        
        
        return pageCatalog;
    }
    
    public String getIDContent(int beginCount,int endCount,ArrayList lineList) {
        String result = new String();
        for(int lineCount = beginCount; lineCount <= endCount; lineCount++ ) {
            if(StringUtils.isBlank((CharSequence) lineList.get(lineCount))) {
                continue;
            }
            result += lineList.get(lineCount) + System.getProperty("line.separator");
        }
        return result;
    }
    
    
    public String randGenerator() {
        String val = "";   

        Random random = new Random();   
        for(int i = 0; i < 6; i++)   
        {   
          String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字   

          if("char".equalsIgnoreCase(charOrNum)) // 字符串   
          {   
            int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母   
            val += (char) (choice + random.nextInt(26));   
          }   
          else if("num".equalsIgnoreCase(charOrNum)) // 数字   
          {   
            val += String.valueOf(random.nextInt(10));   
          }   
        }
        return val.toLowerCase();
    }
    
    
    
}
