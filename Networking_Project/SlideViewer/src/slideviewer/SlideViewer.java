/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slideviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

/**
 *
 * @author root
 */
public class SlideViewer {

    private static BufferedImage img;
    private static FileOutputStream out;

    public int OpenSlide() throws IOException {
        // TODO code application logic here
        //creating an empty presentation
        File file = new File("/root/NetBeansProjects/SlideViewer/src/slideviewer/Robo.pptx");
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));

        //getting the dimensions and size of the slide 
        Dimension pgsize = ppt.getPageSize();
        List<XSLFSlide> slide = ppt.getSlides();

        for (int i = 0; i < slide.size(); i++) {
            img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();

            //clear the drawing area
            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

            //render
            slide.get(i).draw(graphics);
            out = new FileOutputStream("/root/NetBeansProjects/SlideViewer/images/ppt_image" + i + ".png");
            javax.imageio.ImageIO.write(img, "png", out);
            ppt.write(out);
        }

        //creating an image file as output
        System.out.println("Image successfully created");
        out.close();
        
        return slide.size();
    }

}