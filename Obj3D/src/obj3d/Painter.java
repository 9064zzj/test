/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obj3d;

/**
 *
 * @author zhongjiezheng
 */
// Painter.java: Perspective drawing using an input file that lists
//    vertices and faces. Based on the Painter's algorithm.

// Copied from Section 7.3 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

// Uses: Fr3D (Section 5.5) and CvPainter (Section 7.3),
//       Point2D (Section 1.5), Point3D (Section 3.9), 
//       Obj3D, Polygon3D, Tria, Fr3D, Canvas3D (Section 5.5).
import java.awt.*;

public class Painter extends Frame
{   
    public static void main(String[] args)
   {  new Fr3D(args.length > 0 ? args[0] : null, new CvPainter(), 
         "Painter");
   }
}
