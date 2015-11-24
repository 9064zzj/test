/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project5_1;

/**
 *
 * @author zhongjiezheng
 */
// MandelbrotZoom.java: Mandelbrot set, cropping and zooming in.

// Copied from Section 8.4 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

public class Project5_1 extends JFrame
{  public static void main(String[] args){new Project5_1();}

   Project5_1()
   {  super("Drag left mouse button to crop and zoom. " +
            "Click right mouse button to restore.");
      addWindowListener(new WindowAdapter()
         {public void windowClosing(WindowEvent e){System.exit(0);}});
      setSize(800, 600);
      add("Center", new CvMandelbrotZoom());
      show();
   }
}

class CvMandelbrotZoom extends Canvas 
{  final double minRe0 = -2.0, maxRe0 = 1.0, 
                minIm0 = -1.0, maxIm0 = 1.0;
   double minRe = minRe0, maxRe =  maxRe0, 
                  minIm = minIm0, maxIm =  maxIm0, factor, r;
   int n, xs, ys, xe, ye, w, h;
   int GotX, GotY;

   void drawWhiteRectangle(Graphics g)
   {  g.drawRect(Math.min(xs, xe), Math.min(ys, ye), 
                 Math.abs(xe - xs), Math.abs(ye - ys));
   }

   boolean isLeftMouseButton(MouseEvent e)
   {  return (e.getModifiers() & InputEvent.BUTTON3_MASK) == 0;
   }

   CvMandelbrotZoom()
   {  addMouseListener(new MouseAdapter()
      {  public void mousePressed(MouseEvent e)
         {  if (isLeftMouseButton(e))
            {  xs = xe = e.getX(); // Left button
               ys = ye = e.getY();
            }
            else
            {  minRe = minRe0;     // Right button
               maxRe = maxRe0;
               minIm = minIm0;
               maxIm = maxIm0;
               repaint();
            }
         }
         
         public void mouseReleased(MouseEvent e)
         {  if (isLeftMouseButton(e))
            {  xe = e.getX(); // Left mouse button released
               ye = e.getY(); // Test if points are really distinct:
               if (xe != xs && ye != ys)
               {  int xS = Math.min(xs, xe), xE = Math.max(xs, xe),
                      yS = Math.min(ys, ye), yE = Math.max(ys, ye),
                      w1 = xE - xS, h1 = yE - yS, a = w1 * h1,
                      h2 = (int)Math.sqrt(a/r), w2 = (int)(r * h2),
                      dx = (w2 - w1)/2, dy = (h2 - h1)/2;
                  xS -= dx; xE += dx; 
                  yS -= dy; yE += dy; // aspect ration corrected
                  maxRe = minRe + factor * xE;
                  maxIm = minIm + factor * yE;
                  minRe += factor * xS;
                  minIm += factor * yS;
                  repaint();
               }
            }
         }
         
         public void mouseClicked(MouseEvent e){
             if(isLeftMouseButton(e)){
                 GotX=e.getX();
                 GotY=e.getY();
                 double x = minRe + GotX * factor,
                        y = minIm + GotY * factor;
//                 
                 new Julia(810,20,x,y);
             }
         }
         
      });

      addMouseMotionListener(new MouseMotionAdapter()
      {  public void mouseDragged(MouseEvent e)
         {  if (isLeftMouseButton(e))
            {  Graphics g = getGraphics();
               g.setXORMode(Color.black);
               g.setColor(Color.white);
               if (xe != xs || ye != ys)
                  drawWhiteRectangle(g); // Remove old rectangle:
               xe = e.getX();
               ye = e.getY();
               drawWhiteRectangle(g);    // Draw new rectangle:
            }
         }
      });
      
      
   }

   public void paint(Graphics g)
   {  
       Mendalbrot(g);
   }
   
   
    public void Mendalbrot(Graphics g){
      w = getSize().width;
      h = getSize().height;
      r = w/h; // Aspect ratio, used in mouseReleased
      factor = Math.max((maxRe - minRe)/w, (maxIm - minIm)/h);
      for(int yPix=0; yPix<h; ++yPix)
      {  double cIm = minIm + yPix * factor; 
         for(int xPix=0; xPix<w; ++xPix)
         {  double cRe = minRe + xPix * factor, x = cRe, y = cIm;
            int nMax = 100, n;
            for (n=0; n<nMax; ++n)
            {  double x2 = x * x, y2 = y * y;
               if (x2 + y2 > 4)
                  break;   // Outside
               y = 2 * x * y + cIm;
               x = x2 - y2 + cRe;
            }
            g.setColor(n == nMax ? Color.black            // Inside
                : new Color(100 + 155 * n / nMax, 0, 0)); // Outside
            g.drawLine(xPix, yPix, xPix, yPix);
         }        
      }
    }
   
}

