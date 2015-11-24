/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obj3d;

/**
 *
 * @author zhongjiezheng
 */
// CvPainter.java: Used in the file Painter.java.

// Copied from Section 7.3 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

import java.awt.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class CvPainter extends Canvas3D implements Runnable
{  private int maxX, maxY, centerX, centerY;
   private Obj3D obj=new Obj3D();
   private Point2D imgCenter;
   Image image;
   Graphics gImage;
   double sunTheta = 0;
   Thread thr = new Thread(this);

   Obj3D getObj(){return obj;}
   void setObj(Obj3D obj){this.obj = obj;}
   int iX(float x){return Math.round(centerX + x - imgCenter.x);}
   int iY(float y){return Math.round(centerY - y + imgCenter.y);}


    
    public void run(){
        Double SunTheta=0d;
        Double r=1d;
        
            try {
                while(true){
                obj.sunX=r*Math.cos(SunTheta);
                obj.sunY=r*Math.sin(SunTheta);
                obj.sunZ=r*Math.cos(obj.phi);
                SunTheta=SunTheta+0.02;
                repaint();
                Thread.sleep(100);
                
                
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(CvPainter.class.getName()).log(Level.SEVERE, null, ex);
            }  
    }
    
    int w, h;
CvPainter(){thr.start();}
public void update(Graphics g){paint(g);}
    
   void sort(Tria[] tr, int[] colorCode, float[] zTr, int l, int r)
   {  int i = l, j = r, wInt;
      float x = zTr[(i + j)/2], w;
      Tria wTria;
      do
      {  while (zTr[i] < x) i++;
         while (zTr[j] > x) j--;
         if (i < j)
         {  w = zTr[i]; zTr[i] = zTr[j]; zTr[j] = w;
            wTria = tr[i]; tr[i] = tr[j]; tr[j] = wTria;
            wInt = colorCode[i]; colorCode[i] = colorCode[j];
            colorCode[j] = wInt;
            i++; j--;
         }  else
         if (i == j) {i++; j--;}
      }  while (i <= j);
      if (l < j) sort(tr, colorCode, zTr, l, j);
      if (i < r) sort(tr, colorCode, zTr, i, r);
   }

   public void paint(Graphics g) 
   {  if (obj == null) return;
      Vector polyList = obj.getPolyList();
      if (polyList == null) return;
      int nFaces = polyList.size();
      if (nFaces == 0) return;

      

      Dimension dim = getSize();
      maxX = dim.width - 1; maxY = dim.height - 1;
      centerX = maxX/2; centerY = maxY/2;
      // ze-axis towards eye, so ze-coordinates of
      // object points are all negative. 
      // obj is a java object that contains all data:
      // - Vector w        (world coordinates)
      // - Array e         (eye coordinates)
      // - Array vScr      (screen coordinates)
      // - Vector polyList (Polygon3D objects)

      // Every Polygon3D value contains:
      // - Array 'nrs' for vertex numbers
      // - Values a, b, c, h for the plane ax+by+cz=h.
      // - Array t (with nrs.length-2 elements of type Tria)

      // Every Tria value consists of the three vertex
      // numbers iA, iB and iC.
      obj.eyeAndScreen(dim);  
            // Computation of eye and screen coordinates.

      imgCenter = obj.getImgCenter();
      obj.planeCoeff();    // Compute a, b, c and h.

      // Construct an array of triangles in
      // each polygon and count the total number 
      // of triangles:
      if (w != dim.width || h != dim.height)
      {   w = dim.width; h = dim.height;
          image = createImage(w, h);
          gImage = image.getGraphics();
      }
      gImage.clearRect (0, 0, w, h);
      g.drawImage(image, 0, 0, null);
      
      
      
      int nTria = 0;
      for (int j=0; j<nFaces; j++)
      {  Polygon3D pol = (Polygon3D)(polyList.elementAt(j));
         if (pol.getNrs().length < 3 || pol.getH() >= 0) 
            continue;
         pol.triangulate(obj); 
         nTria += pol.getT().length;
      }
      Tria[] tr = new Tria[nTria];
      int[] colorCode = new int[nTria];
      float[] zTr = new float[nTria];
      int iTria = 0;
      Point3D[] e = obj.getE();
      Point2D[] vScr = obj.getVScr();

      for (int j=0; j<nFaces; j++)
      {  Polygon3D pol = (Polygon3D)(polyList.elementAt(j));
         if (pol.getNrs().length < 3 || pol.getH() >= 0) continue;
         int cCode = 
            obj.colorCode(pol.getA(), pol.getB(), pol.getC());
         Tria[] t = pol.getT();
         for (int i=0; i< t.length; i++)
         {  Tria tri = t[i];
            tr[iTria] = tri;
            colorCode[iTria] = cCode;
            float zA = e[tri.iA].z, zB = e[tri.iB].z, 
               zC = e[tri.iC].z;
            zTr[iTria++] = zA + zB + zC;
         }
      }

      sort(tr, colorCode, zTr, 0, nTria - 1);

      for (iTria=0; iTria<nTria; iTria++)
      {  Tria tri = tr[iTria];
         Point2D a = vScr[tri.iA],
                 b = vScr[tri.iB],
                 c = vScr[tri.iC];
         int cCode = colorCode[iTria];
         g.setColor(new Color(cCode, cCode, 0));
         int[] x = {iX(a.x), iX(b.x), iX(c.x)};
         int[] y = {iY(a.y), iY(b.y), iY(c.y)};
         g.fillPolygon(x, y, 3);
      }
   }
}

