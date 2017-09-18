package com.caiji.opencv;

import com.caiji.util.ImageFilter;
import com.caiji.util.ImageIOHelper;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.MORPH_RECT;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.photo.Photo.INPAINT_TELEA;

/**
 * author: lishengzhu
 * eamil:530735771@qq.com
 * date:2017/9/17
 * time:23:15
 */
public class Demo1 {

    /**
     *
     * @param filePath
     * @return  返回文件路径
     * @throws IOException
     */
    public static String jdkImage(String filePath) throws IOException {
        File file=new File(filePath);
        BufferedImage bufferedImage = ImageIOHelper.getImage(file);
        String parent=file.getParent();
        String fileName=filePath.substring(filePath.lastIndexOf("\\")+1,filePath.lastIndexOf("."));
        System.out.println(parent+"===="+fileName);
        int w=bufferedImage.getWidth();
        int h=bufferedImage.getHeight();
        BufferedImage imageCopy=bufferedImage.getSubimage(0, 0,w,h);

        Color color1=new Color(254,101,101);
        Color color2=new Color(101,101,254);
        for (int i = 0; i <w-2 ; i++) {
            for(int j=0;j<h-2;j++){

                int rgb0=bufferedImage.getRGB(i,j);
                int rgb1 = bufferedImage.getRGB(i+1, j);
                int rgb2 = bufferedImage.getRGB(i , j+1);
                int rgb3=bufferedImage.getRGB(i+1,j+1);
                if(( color1.getRGB()==rgb0||color2.getRGB()==rgb0)&& rgb0==rgb1 && rgb1==rgb2&&rgb2==rgb3){
                    imageCopy.setRGB(i,j,Color.white.getRGB());
                    imageCopy.setRGB(i,j+1,Color.white.getRGB());
                    imageCopy.setRGB(i+1,j,Color.white.getRGB());
                    imageCopy.setRGB(i+1,j+1,Color.white.getRGB());
                }

            }
        }

        String outPath=parent+File.separator+fileName+".tiff";
        ImageIO.write(imageCopy,"tiff",new File(outPath));
        return outPath;
    }


    public static void opencvImage(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat= Mat.eye(3,3, CV_8UC1);
        System.out.println("mat="+mat.dump());

        Mat mat1 = Highgui.imread("d:\\yzm\\out\\vcode_out.png");

        Mat mat2=mat1.clone();
        Mat mat3=mat1.clone();
        Mat kern = Imgproc.getStructuringElement(MORPH_RECT, new Size(2, 2));
        Imgproc.erode(mat1,mat2,kern);  //腐蚀
        dilate(mat1,mat3,kern);  //膨胀
        Highgui.imwrite("d:\\fushi.png",mat2);
        Highgui.imwrite("d:\\pengzhang.png",mat3);

        Imgproc.erode(mat3,mat3,kern);
        Highgui.imwrite("d:\\f-p.png",mat3);

        Imgproc.medianBlur(mat1,mat3,1);

        Highgui.imwrite("d:\\nedianBlur.png",mat3);
//        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);
//        //二值化
//        Imgproc.adaptiveThreshold(img, img, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 25, 10);
//        Highgui.imwrite("d:\\gray.png", img);

    }

    /**
     *
     * @param path
     */
    public static void toGrey(String path){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat=Highgui.imread(path);
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
        Imgproc.adaptiveThreshold(mat,mat,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY_INV,7,10);
        Highgui.imwrite(path,mat);
    }

    public static void toGrey(BufferedImage bufferedImage){
        int width=bufferedImage.getWidth();
        int height=bufferedImage.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = bufferedImage.getRGB(x, y);
                Color color = new Color(rgb); // 根据rgb的int值分别取得r,g,b颜色。
                int gray = (int) (0.3 * color.getRed() + 0.59 * color.getGreen() + 0.11 * color.getBlue());
                Color newColor = new Color(gray, gray, gray);
                bufferedImage.setRGB(x, y, newColor.getRGB());
            }
        }
    }

    public static void grey2(BufferedImage bufferedImage){
        int width=bufferedImage.getWidth();
        int height=bufferedImage.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = bufferedImage.getRGB(x, y);
                Color color = new Color(rgb); // 根据rgb的int值分别取得r,g,b颜色。
                Color newColor = new Color(255 - color.getRed(), 255 - color
                        .getGreen(), 255 - color.getBlue());
                bufferedImage.setRGB(x, y, newColor.getRGB());
            }
        }
    }

    public static void conveseGrey(BufferedImage bufferedImage){
        int width=bufferedImage.getWidth();
        int height=bufferedImage.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = bufferedImage.getRGB(x, y);
                Color color = new Color(rgb); // 根据rgb的int值分别取得r,g,b颜色。
                int value = 255 - color.getBlue();
                if (value > 80) {
                    Color newColor = new Color(0, 0, 0);
                    bufferedImage.setRGB(x, y, newColor.getRGB());
                } else {
                    Color newColor = new Color(255, 255, 255);
                    bufferedImage.setRGB(x, y, newColor.getRGB());
                }
            }
        }

    }


    public static void xiufu(String path){
        File file=new File(path);
        String parent=file.getParent();
        System.out.println("parent:"+parent);
        String fileName=path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf("."));


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat=Highgui.imread(path);
        Mat matGray=mat.clone();
        Imgproc.cvtColor(mat,matGray,Imgproc.COLOR_RGB2GRAY,0);
        Mat imageMask = new Mat(mat.size(), CV_8UC1, Scalar.all(0));


        //通过阈值处理生成Mask
        Imgproc.threshold(matGray, imageMask, 240, 100, Imgproc.THRESH_BINARY);
        Mat Kernel = Imgproc.getStructuringElement(MORPH_RECT, new Size(3, 3));
        //对Mask膨胀处理，增加Mask面积
        //dilate(imageMask, imageMask, Kernel);
        //erode(imageMask,imageMask,Kernel);
        //图像修复
        Photo.inpaint(mat, imageMask, mat, 5, INPAINT_TELEA);
        Highgui.imwrite(parent+File.separator+fileName+"xiufu.tif",mat);
        Highgui.imwrite(parent+File.separator+fileName+"yanma.tif",imageMask);

    }





    public static void main(String[] args) throws IOException {

//        for (int i = 1; i <10 ; i++) {
//            String s = jdkImage("d:\\yzm\\" + i + ".png");
//            toGrey(s);
//        }

//        File file=new File("d:\\yzm\\1.png");
//        BufferedImage bi=ImageIO.read(file);
//        toGrey(bi);
//
//        conveseGrey(bi);
//
//        boolean tif = ImageIO.write(bi, "tif", new File("d:\\yzm\\1test.tif"));
        for (int i=1;i<=10;i++){
            String s = jdkImage("d:\\yzm\\" + i + ".png");
            //xiufu(s);
            toGrey(s);
            File file=new File(s);
            BufferedImage image = ImageIOHelper.getImage(file);
            grey2(image);
          //  GaussianBlur(src, src, Size(3, 3), 0, 0, BORDER_DEFAULT);
            BufferedImage sharp = ImageFilter.sharp(image);
            ImageIO.write(image,"tif", file);
        }
    }
}
