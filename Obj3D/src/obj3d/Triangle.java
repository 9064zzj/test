/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obj3d;

/**
 *
 * @author zhongjiezheng
 */
// Triangle.java: Class to store a triangle;

// Copied from Section 2.13 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

// Vertices in logical coordinates.
// Uses: Point2D (Section 1.5).
class Triangle
{  Point2D a, b, c;
   Triangle(Point2D a, Point2D b, Point2D c)
   {  this.a = a; this.b = b; this.c = c;
   }
}
