/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dgenerate;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.Canvas;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.AttributedCharacterIterator;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import quicktime.qd3d.math.Point3D;
/**
 *
 * @author zhongjiezheng
 */
public class Stairs extends JFrame {
    public static LinkedList<Node> Nodes = new LinkedList();
    public static LinkedList<Face> Faces = new LinkedList();
    public static LinkedList<Point2D> ViewNodes =new LinkedList();
    
    
   
    //Consturcution
    public Stairs()
    {
        super("Java 3D");
        addWindowListener(new WindowAdapter()
     {public void windowClosing(WindowEvent e){System.exit(0);}});
      add(new Demo());
      setSize (800, 600);
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      this.setVisible(true);
        
    }
    
    class Demo extends Canvas
    {
        public double PixelWidth=1;
        
        Demo(){
            addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent e){
                       //
                    }
                @Override
                public void mouseReleased(MouseEvent e){
                      //
                }               
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                   //
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                  //
                }
            });
        }
        
        @Override
        public void paint(Graphics g){
            paint3D(g);
        }
        
        public void paint3D(Graphics g){
            int FaceNum=Faces.size();
            Face Temp;
            Trans();
    //        GetMaxWidth();
            for(int i=0;i<FaceNum;i++){
                Temp=Faces.get(i);
                PrintFace(g,Temp);
            }
            
        }
        
        public void PrintFace(Graphics g,Face face){
            int NodeNum=face.size();
            int P_index, Q_index;
            Point2D P,Q;
            
            for(int i=0; i<NodeNum-1; i++){
                P_index=face.get(i);
                Q_index=face.get(i+1);
                P=GetPoint(P_index);
                Q=GetPoint(Q_index);
                if(P.index==-1 || Q.index==-1){
                    g.drawString("ERROR! There is an invaild node in faces, the image is incomplete", 40, 40);
                    break;
                }
                g.drawLine(iX(P.x), iY(P.y), iX(Q.x), iY(Q.y));
            }//Ends for
            
            //Connect the last and first node
            P_index=face.get(0);
            Q_index=face.get(NodeNum-1);
            P=GetPoint(P_index);
            Q=GetPoint(Q_index);
            if(P.index!=-1 && Q.index!=-1) 
                g.drawLine(iX(P.x), iY(P.y), iX(Q.x), iY(Q.y));
     
        }
        
        public void GetMaxWidth(){
            Double Maxx=0d,Minx=0d,Maxy=0d,Miny=0d,Widthx,Widthy,Width;
            int size=ViewNodes.size();
            Point2D Temp;
            
            for(int i=0;i<size;i++){
                Temp=ViewNodes.get(i);
                if(Temp.x>Maxx)
                    Maxx=Temp.x;
                if(Temp.x<Minx)
                    Minx=Temp.x;
                if(Temp.y>Maxx)
                    Maxx=Temp.y;
                if(Temp.y<Minx)
                    Minx=Temp.y;
            }
            
            Widthx=Maxx-Minx;
            Widthy=Maxy-Miny;
            
            if(Widthx>Widthy)
                Width=Widthx;
            else
                Width=Widthy;
            
            PixelWidth=Width/600;
                    
        }
        
        public int iX(Double x){
            return (int) (x/PixelWidth)+400;
        }
        
        public int iY(Double y){
            return(int)(y/PixelWidth)+300;
        }
        
        public Point2D GetPoint(int index){
            Point2D Temp=new Point2D(-1,0,0);
            int size=ViewNodes.size();
            for( int i=0 ; i<size; i++){
                Temp=ViewNodes.get(i);
                if(Temp.index==index)
                    break;
            }
            return Temp;
        }
        
        public void Trans(){
            
            Node zeropoint=new Node(0,0,-80,40);
            Double Theta=Math.PI/2;         //Theta is π/4 (45')
            Double Phi=-Math.PI/2.5;           //Phi is π/4 (45')
            Double Distance=1500d;
            int Length=Nodes.size();
            
            for (int i=0;i<Length;i++){
                Node ViewN=Nodes.get(i);
                Point2D Point=new Point2D();
                ViewN=D3CoordStep1(ViewN,zeropoint);
                ViewN=D3CoordStep2(ViewN, Theta);
                ViewN=D3CoordStep3(ViewN, Phi);
                Point=D2Coord(ViewN,Distance);
                ViewNodes.offer(Point);    
            }//End for loop
                 
    }
        
        
        public Node D3CoordStep1(Node Point, Node zeropoint){
                Node ViewN=new Node();
                ViewN=Point.reduce(zeropoint);
                return ViewN;
        }
        
        public Node D3CoordStep2(Node Point,Double Theta ){
            Node ViewN = new Node();
            Double sin=Math.sin(Theta);
            Double cos=Math.cos(Theta);
            
            ViewN.index=Point.index;
            ViewN.x=(-sin*Point.x)+(-cos*Point.y);
            ViewN.y=(-cos*Point.y)+(-sin*Point.y);
            ViewN.z=Point.z;
            return ViewN;
        }
        
        public Node D3CoordStep3(Node Point,Double Phi){
            Node ViewN = new Node();
            Double sin=Math.sin(Phi);
            Double cos=Math.cos(Phi);
            
            ViewN.index=Point.index;
            ViewN.x=Point.x;
            ViewN.y=cos*Point.y+sin*Point.z;
            ViewN.z=-sin*Point.y+cos*Point.z;
            return ViewN;
        }
        
        public Point2D D2Coord(Node Point,Double d){
            Point2D ViewP = new Point2D();
            
            ViewP.index=Point.index;
            ViewP.x=-d*(Point.x/Point.z);
            ViewP.y=-d*(Point.y/Point.z);
            
            return ViewP;
        }
        
    }
    
    public class Node
    {
        public int index;
        public double x;
        public double y;
        public double z;
        
        Node(int i, double x, double y, double z)
       {
            index=i;
            this.x=x;
            this.y=y;
            this.z=z;
        }
        
        Node(){
            index=0;
            this.x=0;
            this.y=0;
            this.z=0;
        }
        
        public Node add( Node B){
            Node Temp =new Node();
            Temp.index=this.index;
            Temp.x=this.x+B.x;
            Temp.y=this.y+B.y;
            Temp.z=this.z+B.z;
            return Temp;
        }
        public Node reduce(Node B){
            Node Temp =new Node();
            Temp.index=this.index;
            Temp.x=this.x-B.x;
            Temp.y=this.y-B.y;
            Temp.z=this.z-B.z;
            return Temp;
        }
        
        public Node Minus(Node B){
            Node Temp =new Node();
            Temp.index=this.index;
            Temp.x=-this.x;
            Temp.y=-this.y;
            Temp.z=-this.z;
            return Temp;
        }
         
    }
    
    public class Point2D
    {
        int index;
        double x;
        double y;
        
        Point2D(){
            index=0;
            x=0;
            y=0;
        }
        
        Point2D(int index, int x, int y){
            this.index=index;
            this.x=x;
            this.y=y;
        }
    }
    
    public class Face
    {
        public LinkedList<Integer> FacesNode= new LinkedList();
        
        public Face(){
        
       }
        public int get(int Index){
            return this.FacesNode.get(Index);
        }
        public void add(int Index){
            this.FacesNode.offer(Index);
        }
        public int size(){
            return FacesNode.size();
        }
        
    }
    
    public void Input()
    {
        boolean Nod_Face=true;     //True for reading node
        
        try{
            FileInputStream fstream = new FileInputStream("/Users/zhongjiezheng/Desktop/stairs.dat");     //Change the file addr. here
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String temp= null; 
            String arrs[];
            String arrs_1[];
            String arrs_2[];
            
            while((temp = br.readLine()) != null) {
                if(temp.contains("Faces:"))
                    Nod_Face=false;
                else{

                    
                    
                    if(Nod_Face){        //For nod fetching 
                           arrs_1=temp.split(" ");
                           arrs=EmptyTest(arrs_1);
                           Node A=new Node();
                           A.index=Integer.parseInt(arrs[0]);
                           A.x=Double.parseDouble(arrs[1]);
                           A.y=Double.parseDouble(arrs[2]);
                           A.z=Double.parseDouble(arrs[3]);
                           Nodes.offer(A);
                       }
                    else{
                        arrs_2=temp.split("\\.");
                        arrs_1=arrs_2[0].split(" ");
                        arrs=EmptyTest(arrs_1);
                        
                        Face F = new Face();
                        for(int i=0;i<arrs.length;i++){  //for faces construct
                            F.add(Integer.parseInt(arrs[i]));
                        }
                        Faces.offer(F);
                    }
                 }
                
                
                
            }
            br.close(); 
        }catch (IOException e) {
           System.out.println("error"+e.getMessage());   
       }
        
    } 
    
    public String[] EmptyTest(String arrs[])
    {
        boolean badchar=false;                  //if there is ""
        int length= arrs.length;   
        String arrs1[]=new String[length-1];
        int i=0;                                 //initial the "" index
        
        for (; i<length; i++){                  
            if(arrs[i].equals("")){
                badchar=true;                    //there is ""
                break;                           // break, now i is the bad char index
            }
        }
        
        if(badchar){
            for(int j=0; j<i; j++){              //Now fill the first half 
                arrs1[j]=arrs[j];
            }
            
            for(int j=i+1;j<length; j++)              //Swap forward
            {
                arrs1[j-1]=arrs[j];
            }
            return arrs1;
        }
        else
            return arrs;
        
    }

    //
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
      Stairs A=new  Stairs();
      A.Input();
      A.repaint();
      
    }
}
