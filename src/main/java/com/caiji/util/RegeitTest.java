package com.caiji.util;

/**
 * Created by Administrator on 2017-9-15.
 */
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class RegeitTest {
    static public void main(String[] args) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpUriRequest getMethod = new HttpGet("验证码URL");
        /*for(int i=1;i<=10;i++){
            try {
                String yzm = "";
                HttpResponse res = httpclient.execute(getMethod);
                HttpEntity entity = res.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    BufferedImage bi = ImageIO.read(instream);
                    instream.close();
                    *//************************************//*
                    yzm = ImageRead.read(bi,i);
*//********************************************//*
                }
                System.out.println(yzm+":===="+i+"   ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


        for(int i=1;i<=10;i++){
            InputStream inputStream=new FileInputStream(new File("d:\\yzm\\" + i + ".png"));
            BufferedImage bi=ImageIO.read(inputStream);
            inputStream.close();
            String yzm="";
            yzm=ImageRead.read(bi,i);
            System.out.println("yzm="+i+" :"+yzm);
        }
    }

}
