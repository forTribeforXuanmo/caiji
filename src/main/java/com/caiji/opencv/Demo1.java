package com.caiji.opencv;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * author: lishengzhu
 * eamil:530735771@qq.com
 * date:2017/9/17
 * time:23:15
 */
public class Demo1 {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat= Mat.eye(3,3, CvType.CV_8UC1);
        System.out.println("mat="+mat.dump());

        Mat img = Highgui.imread("g:\\vcodeimg.jpg");
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);
        //二值化
        Imgproc.adaptiveThreshold(img, img, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 25, 10);
        Highgui.imwrite("g:\\gray.jpg", img);
    }
}
