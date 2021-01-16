package com.xiaoaiframework.util.base;

import com.xiaoaiframework.util.file.FileUtil;
import com.xiaoaiframework.util.math.RandomUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;

/**
 * @author edison
 */
public class ImageUtil {


    public void like(BufferedImage image ){




    }


    /**
     * 获取缓冲区图像
     * @param file
     * @return
     */
    public static BufferedImage getBufferedImage(File file){
        try {
            return ImageIO.read(FileUtil.input(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage getBufferedImage(String file){
        try {
            return ImageIO.read(FileUtil.input(new File(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static BufferedImage resize(File source, int width, int height){
        return resize(getBufferedImage(source), width, height);
    }

    public static BufferedImage resize(String source, int width, int height){
        return resize(getBufferedImage(source), width, height);
    }


    public static BufferedImage resize(BufferedImage source, int width, int height) {

        // targetW，targetH分别表示目标长和宽
        int type = source.getType();

//        double sx = (double) width / source.getWidth();
//        double sy = (double) height / source.getHeight();
//
//        //这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
//        //则将下面的if else语句注释即可
//        if(sx>sy) {
//            sx = sy;
//            width = (int)(sx * source.getWidth());
//        }else{
//            sy = sx;
//            height = (int)(sy * source.getHeight());
//        }

        BufferedImage target = null;
        if (type == BufferedImage.TYPE_CUSTOM) {
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            target = new BufferedImage(width, height, type);
        }
        Graphics2D g = target.createGraphics();
        //smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);


        g.drawRenderedImage(source, AffineTransform.getScaleInstance(width, height));
        g.dispose();
        return target;
    }


    /**
     * 图片写出
     * @param image
     * @param file
     * @param formatName
     */
    public static void write(BufferedImage image,File file,String formatName){

        try(FileOutputStream out = new FileOutputStream(file);){
            ImageIO.write(image,formatName,out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将彩色图片变为灰色的图片
     * @param imagePath
     */
    public static BufferedImage makeImageColorToBlackWhite(String imagePath) {
       return makeImageColorToBlackWhite(getBufferedImage(imagePath));
    }

    /**
     * 将彩色图片变为灰色的图片
     * @param image
     */
    public static BufferedImage makeImageColorToBlackWhite(BufferedImage image) {
        int[][] result = getImageGRB(image);
        int[] rgb = new int[3];
        BufferedImage bi = new BufferedImage(result.length, result[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                rgb[0] = (result[i][j] & 0xff0000) >> 16;
                rgb[1] = (result[i][j] & 0xff00) >> 8;
                rgb[2] = (result[i][j] & 0xff);
                int color = (int)(rgb[0]* 0.3 + rgb[1] * 0.59 + rgb[2] * 0.11);
                //color = color > 128 ? 255 : 0;
                bi.setRGB(i, j, (color << 16) | (color << 8) | color);
            }
        }
        return bi;
    }



    /**
     * 获取图片的像素点
     * @param filePath
     * @return
     */
    public static int[][] getImageGRB(BufferedImage image) {

        int height = image.getHeight();
        int width = image.getWidth();
        int[][] result = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result[i][j] = image.getRGB(i, j) & 0xFFFFFF;
            }
        }

        return result;
    }

    /**
     * 获取图片的像素点
     * @param filePath
     * @return
     */
    public static int[][] getImageGRB(String filePath) {
        File file = new File(filePath);
        int[][] result = null;
        if (!file.exists()) {
            System.out.println("图片不存在");
            return result;
        }
        return getImageGRB(getBufferedImage(filePath));
    }

    /**
     * 获取灰度平均值
     * @param rgb
     * @param w
     * @param h
     * @return
     */
    public static double getGrayAverage(int[][] rgb) {
        int sum = 0;

        //计算平均值

        for (int[] ints : rgb) {

            for (int anInt : ints) {
                //Color c = new Color(anInt);
                sum += anInt;//sum + (int)(0.299 *  c.getRed() + 0.587 * c.getBlue() + 0.114 * c.getGreen());
            }

        }

        int aver = sum / (rgb.length * rgb[0].length);

        return aver;
    }

    public static void main(String[] args) {

        /*
            1.图片缩小到8*8
            2.图片转为64灰度
            3.得到每个格子的GRB值
            4.进行计算
         */


        System.out.println(fingerprint("C:\\Users\\15355\\Pictures\\123.jpg").equals(fingerprint("C:\\Users\\15355\\Pictures\\321.jpg")));
       // System.out.println(fingerprint("C:\\Users\\15355\\Pictures\\lalala.png").equals(fingerprint("C:\\Users\\15355\\Pictures\\567.jpg")));
    }

    public static String fingerprint(String file){


        BufferedImage image = makeImageColorToBlackWhite(resize(file,7,7));
        int[][] rgb = getImageGRB(image);





        //write(image, new File("F://"+new File(file).getName()),"jpg");

        double ave = getGrayAverage(rgb);
        String hash = "";
        for (int[] ints : rgb) {

            for (int anInt : ints) {
                hash += ""+(ave>anInt?1:0);
            }

        }

        System.out.println(hash);
        return hash;
    }
}

