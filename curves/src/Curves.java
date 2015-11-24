/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package curves;

import java.awt.Frame;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
/**
 *
 * @author zhongjiezheng
 */

    /**
     * @param args the command line arguments
     */


public class Curves extends Frame
{  public static void main(String[] args)
   {  if (args.length == 0)
         System.out.println("Use filename as program argument.");
      else
         new Curves(args[0]);
   }
   Curves(String fileName)
   {  super("Click left or right mouse button to change the level");
      addWindowListener(new WindowAdapter()
         {public void windowClosing(WindowEvent e){System.exit(0);}});
      setSize(800, 600);
      add("Center", new CvFractalGrammars(fileName));
      show();
   }
}

class CvFractalGrammars extends Canvas
{  String fileName, axiom, strF, strf, strX, strY;
   int maxX, maxY, level = 1; 
   double xLast, yLast, dir, rotation, dirStart, fxStart, fyStart,
      lengthFract, reductFact;
   double OrigLen;
   
   
   void error(String str)
   {  System.out.println(str);
      System.exit(1);
   }

   CvFractalGrammars(String fileName)
   {  
      axiom ="X";
      strF = "FF";
      strf =""; 
      strX ="F[+X]F[-X]+X";
      strY ="";
      rotation =20;
      dirStart = 90;
      fxStart = 0.5;
      fyStart = 0.05;
      lengthFract = 0.45;
      reductFact = 0.5;
      
               
      addMouseListener(new MouseAdapter()
      {  public void mousePressed(MouseEvent evt)
         {  if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
            {  level--;      // Right mouse button decreases level
               if (level < 1)
                  level = 1;
            }
            else            
               level++;      // Left mouse button increases level
            repaint();
         }
      });

   }

   Graphics g;
   int iX(double x){return (int)Math.round(x);}
   int iY(double y){return (int)Math.round(maxY-y);}

   void drawTo(Graphics g, double x, double y,double len)
   {  Graphics2D g2=(Graphics2D)g;
      double maxy=Math.max(y, yLast);
      float Stroke=Math.round(600/(maxy+1)*2.5);
   
      g2.setStroke(new BasicStroke(Stroke));
      g2.drawLine(iX(xLast), iY(yLast), iX(x) ,iY(y));
      xLast = x;
      yLast = y;
   }

   void moveTo(Graphics g, double x, double y)
   {  xLast = x;
      yLast = y;
   }

   public void paint(Graphics g) 
   {  Dimension d = getSize();
      maxX = d.width - 1;
      maxY = d.height - 1; 
      xLast = fxStart * maxX;
      yLast = fyStart * maxY;
      dir = dirStart;   // Initial direction in degrees
      turtleGraphics(g, axiom, level, lengthFract * maxY);  
      
   }

   public void turtleGraphics(Graphics g, String instruction, 
      int depth, double len) 
   {  OrigLen=len;
      double xMark=0, yMark=0, dirMark=0;
      for (int i=0;i<instruction.length();i++) 
      {  char ch = instruction.charAt(i);
         switch(ch)
         {
         case 'F': // Step forward and draw
            // Start: (xLast, yLast), direction: dir, steplength: len
            if (depth == 0)
            {  double rad = Math.PI/180 * dir, // Degrees -> radians
                      Rrad=rad+randomDir(rad), Rlen=len+randomLength(len),
                      dx = Rlen* Math.cos(rad), dy = Rlen * Math.sin(rad);
               drawTo(g, xLast + dx, yLast + dy,len);
               
               if(i>0 && i<instruction.length()-1){
                   char ch1=instruction.charAt(i-1);
                   char ch2=instruction.charAt(i+1);
                   if(ch1==']'&&ch2=='[')
                       drawLeaf(g, xLast + dx, yLast + dy,len);
               }
               
            }
            else
               turtleGraphics(g, strF, depth - 1, reductFact * len); 
            break;
         case 'f': // Step forward without drawing
            // Start: (xLast, yLast), direction: dir, steplength: len
            if (depth == 0)
            {  double rad = Math.PI/180 * dir, // Degrees -> radians
                dx = len * Math.cos(rad), dy = len * Math.sin(rad);
               moveTo(g, xLast + dx, yLast + dy);
            }
            else
               turtleGraphics(g, strf, depth - 1, reductFact * len); 
            break;
         case 'X':
            if (depth > 0)
               turtleGraphics(g, strX, depth - 1, reductFact * len);
            break;
         case 'Y':
            if (depth > 0)
               turtleGraphics(g, strY, depth - 1, reductFact * len);
            break;
         case '+': // Turn right
            dir -= rotation+randomDir(rotation); break;
         case '-': // Turn left
            dir += rotation+randomDir(rotation); break;
         case '[': // Save position and direction
            xMark = xLast; yMark = yLast; dirMark = dir; break;
         case ']': // Back to saved position and direction
            xLast = xMark; yLast = yMark; dir = dirMark; break;
         }
      }
   }
   
     public double randomDir(double rad){
         Random rand = new Random();
         double max=rad;
         double min=-rad;
         double randomNum;
         randomNum = rand.nextDouble()*(max-min)+min;
         return randomNum;
     }
     
     public double randomLength(double len){
         Random rand = new Random();
         double max=len/2;
         double min=-len/2;
         double randlength=rand.nextDouble()*(max-min)+min;;
         return randlength;

     }
     
     public void drawLeaf(Graphics g,double x, double y, double len){
//         double x1=0,y1=0,x2=0,y2=len,x3=-0.4*len,y3=0.5*len,x4=0.4*len,y4=0.5*len;
//         double angle=getAngle(xLast,yLast,x,y);
         double midx=(x+xLast)/2;
         double midy=(y+yLast)/2;
         double offsetx=(y-yLast)*0.3;
         double offsety=(x-xLast)*0.3;
         double point3x=midx+offsetx,point3y=midy-offsety;
         double point4x=midx-offsetx,point4y=midy+offsety;
         g.setColor(Color.GREEN);
         g.drawLine(iX(x), iY(y), iX(point3x), iY(point3y));
         g.drawLine(iX(x), iY(y), iX(point4x), iY(point4y));
         g.drawLine(iX(xLast), iY(yLast), iX(point3x), iY(point3y));
         g.drawLine(iX(xLast), iY(yLast), iX(point4x), iY(point4y));
         g.setColor(Color.BLACK);
         
     }
     
     public double getAngle(double px1,double py1, double px2, double py2){
         double x=px2-px1;
         double y=py2-py1;
         double hypotenuse = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
         double cos = x/hypotenuse;
         return Math.acos(cos);
     }
}


