package com.caiji.util;

/**
 * Created by Administrator on 2017-9-15.
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ImageRead {

    public static String read(BufferedImage bi, int ii) {
        ImageFilter imgFliter = new ImageFilter(bi);
        BufferedImage ss = imgFliter.changeGrey();

        imgFliter = new ImageFilter(ss);
        ss = imgFliter.median();

        imgFliter = new ImageFilter(ss);
        ss = imgFliter.grayFilter();

        File xx = ImageIOHelper.createImage(ss);

        try {
            FileInputStream input = new FileInputStream(xx);
            FileOutputStream output = new FileOutputStream( "d:\\yzm\\out\\" + ii + ".tiff");// 把扩展名添加到原来文件的后面
            int in = input.read();
            while (in != -1) {
                output.write(in);
                in = input.read();
            }
            input.close();
            output.close();
            OCR ocr = new OCR();
            String rlt = ocr.recognizeText(xx, "tiff");
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < rlt.length(); i++) {
                String s = rlt.substring(i, i + 1);
                try {
                    int t = Integer.valueOf(s);
                    str.append(t);
                } catch (Exception e) {
                }
            }
            return str.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}