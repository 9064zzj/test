/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obj3d;

/**
 *
 * @author zhongjiezheng
 */
// Point3D.java: Representation of a point in 3D space.
// Copied from Section 3.9.1 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
class Point3D
{  float x, y, z;
   Point3D(double x, double y, double z)
   {  this.x = (float)x; 
      this.y = (float)y; 
      this.z = (float)z;
   }
}