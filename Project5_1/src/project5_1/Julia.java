/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project5_1;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author zhongjiezheng
 */
public class Julia extends JFrame {
    double Gotx,Goty;
    
    
    Julia(int XPos,int YPos,double Gotx, double Goty){
        super("Julia Set drawing");
        this.Gotx=Gotx;
        this.Goty=Goty;
        addWindowListener(new WindowAdapter()
         {public void windowClosing(WindowEvent e){System.exit(0);}});
      setSize(900, 500);
      add("Center", new CvJuliaSet());
      this.setLocation(XPos, YPos);
      show();
    }
    
    public class CvJuliaSet extends Canvas{
        final double minRe0 = -2.0, maxRe0 = 1.0, 
                minIm0 = -1.0, maxIm0 = 1.0;
   double minRe = minRe0, maxRe =  maxRe0, 
                  minIm = minIm0, maxIm =  maxIm0, factor, r;
   int n, xs, ys, xe, ye, w, h;
   int GotX, GotY;

   void drawWhiteRectangle(Graphics g)
   {  g.drawRect(Math.min(xs, xe), Math.min(ys, ye), 
                 Math.abs(xe - xs), Math.abs(ye - ys));
   }

   CvJuliaSet()
   {  
       
    }
   
   public void paint(Graphics g)
   {  
       JuliaSet(g);
       g.setColor(Color.GREEN);
       g.drawString("z="+Gotx+"+"+Goty+"i",20,20);
   }
   
   public void JuliaSet(Graphics g){
      Dimension d = getSize();
      w = getSize().width;
      h = getSize().height;
      r = w/h;
      double cRe = Gotx, cIm = Goty;
      factor = Math.max((maxRe - minRe)/w, (maxIm - minIm)/h);
      for(int yPix=0; yPix<h; ++yPix)
      {  for(int xPix=0; xPix<w; ++xPix)
         {  double x = minRe + xPix * factor,
                   y = minIm + yPix * factor;
            int nMax = 100, n;
            for (n=0; n<nMax; ++n)
            {  double x2=x*x,y2=y*y;
               if (x2 + y2 > 4)
                  break;      // Outside
               y = 2 * x * y + cIm;
               x = x2 - y2 + cRe;
            }
            g.setColor(n == nMax ? Color.black             // Inside
                 : new Color(100 + 155*n/nMax,0,0));       // Outside
            g.drawLine(xPix, yPix, xPix, yPix);
          }
       }
      }
    }

}
