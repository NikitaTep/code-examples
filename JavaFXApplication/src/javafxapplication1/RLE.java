/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
 
public class RLE {
 
    public static void compress(String source, String dis) {
        try {
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(dis);
            int next = 0;
            int count = in.read();  
            while ((next = in.read()) >= 0) {
                int counter = 1;
                if (count == next) {
                    counter++;
                    while (next == (count = in.read())) {
                        counter++;
                    }
                    while (counter >= 63) {    
                        out.write(255);      
                        out.write(next);
                        counter -= 63;
                    }
                    if (counter > 1) { 
                        out.write(0xc0 + counter);     
                        out.write(next);
                       
                    }
                } else {
                    if (count <= 0xc0) {      
                        out.write(count);
                        count = next;
                    } else {
                        out.write(0xc1);     
                        out.write(count);
                        count = next;
                    }
                }
            }
            if (count <= 0xc0) {      
                out.write(count);
            } else {
                out.write(0xc1);
                out.write(count);
            }
            in.close();
            out.close();
        } catch (IOException e) {
        }
    }
 
    public static void decompress(String source, String dis) {
        try {
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(dis);
            int count = 0;
            while ((count = in.read()) >= 0) {
                if (count == 0xc1) {
                    out.write(in.read());
                } else if (count <= 0xc0) {
                    out.write(count);
                } else if (count > 0xc1) {
                    int next = in.read();
                    for (int i = 0; i < (count - 0xc0); i++) {
                        out.write(next);
                    }
                }
            }
            in.close();
            out.close();
        } catch (Exception e) {
        }
    }
    
     public static void testCmykToRgb(String source) throws Exception {
        BufferedImage cmykImage = ImageIO.read(new File(source));
        BufferedImage rgbImage = new BufferedImage(cmykImage.getWidth(),
                cmykImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        WritableRaster resultRaster = rgbImage.getRaster();

        ColorConvertOp op = new ColorConvertOp(null);
        op.filter(cmykImage, rgbImage);

        ImageIO.write(rgbImage, "JPEG", new File("temp/convertedImage.jpg"));

    }
 
    public static void main(String[] args) throws IOException{
//        new RLE().compress("input.bmp", "compressed.rle");
//        new RLE().decompress("compressed.rle", "output.bmp");
    }
}
/**
 *
 * @author Nikita
 */

