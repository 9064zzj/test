/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pj3;
import java.awt.*;
import java.awt.event.*;
import java.awt.Canvas;
import java.awt.Graphics;
import java.text.AttributedCharacterIterator;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
/**
 *
 * @author zhongjiezheng
 */
public class PJ3 extends JFrame{
    
    ArrayList<Point> Node=new ArrayList();
    private boolean paintmode=true;  //True paint Pythagoras
    private boolean nod_link=true;    //True if draw node
    private boolean Start_End=true;         //True if drawing starter point
    private boolean GetStart=false;         //Use for check whether starter is in node
    private Point P_Starter,P_End;
    private int VERTICAL_VALVE;
    private int HORIZON_VALVE;
    private LinkedList<Point> nodelist = new LinkedList();
    private LinkedList<Point> Startlist = new LinkedList();
    private LinkedList<Point> Endlist = new LinkedList();
    
    Demo demo;
  
 
   
    PJ3(){
        
    super("Demo");
        
    JPanel JP= new JPanel();
    JMenuBar Jbar =new JMenuBar();
    JMenu JM = new JMenu("Options");
    JMenuItem JMenuItem=new JMenuItem("Pythagoras");
    JMenuItem JMenuItem1=new JMenuItem("Graph");
    JMenuItem JMenuItem2=new JMenuItem("Quit");
    
    JMenuItem.addActionListener( 
            new ActionListener() 
            { 
                public void actionPerformed(ActionEvent e) 
                { 
                    paintmode=true;//Action
                } 
            } 
        ); 
    JMenuItem1.addActionListener( 
            new ActionListener() 
            { 
                public void actionPerformed(ActionEvent e) 
                { 
                    paintmode=false;//Action
                    demo.repaint();
                    nodelist.clear();
                    Startlist.clear();
                    Endlist.clear();
                } 
            } 
        ); 
    
    JMenuItem2.addActionListener( 
            new ActionListener() 
            { 
                public void actionPerformed(ActionEvent e) 
                { 
                    System.exit(0);
                } 
            } 
        ); 
    
    JM.add(JMenuItem);
    JM.add(JMenuItem1);
    JM.add(JMenuItem2);
    Jbar.add(JM);
    setJMenuBar(Jbar);
    
    JButton Button= new JButton("Node");
    JButton Button1= new JButton("Link"); 
    
    
   Button.addActionListener( 
            new ActionListener() 
            { 
                public void actionPerformed(ActionEvent e) 
                { 
                   nod_link=true;
                   demo.first=null;
                   demo.last=null;
                } 
            } 
        ); 

        Button1.addActionListener( 
            new ActionListener() 
            { 
                public void actionPerformed(ActionEvent e) 
                { 
                   nod_link=false; 
                   demo.first=null;
                   demo.last=null;
                } 
            } 
        );
    
    
    JP.add(Button);
    JP.add(Button1, BorderLayout.AFTER_LAST_LINE);
  
    JScrollBar js=new JScrollBar(1,0,10,0,300);
    JScrollBar js1=new JScrollBar(0,0,10,0,300);
    js.addAdjustmentListener(
           new AdjustmentListener(){ 
               public void adjustmentValueChanged(AdjustmentEvent e){
                 demo.offsetY = e.getValue();
//                 demo.first=null;
//                   demo.last=null;
                 demo.repaint();
               } 
            } 
       );
    js1.addAdjustmentListener(
           new AdjustmentListener(){ 
               public void adjustmentValueChanged(AdjustmentEvent e){
                 demo.offsetX = e.getValue();
//                 demo.first=null;
//                   demo.last=null;
                 demo.repaint();
               } 
            } 
       );
    
    
    JP.setBorder(new LineBorder(Color.BLUE,5));
    
    this.add(js, BorderLayout.EAST);
    this.add(js1, BorderLayout.SOUTH);
    this.add(JP, BorderLayout.WEST);
    demo = new Demo();
    this.add(demo,BorderLayout.CENTER);
    
        
        addWindowListener(new WindowAdapter()
     {public void windowClosing(WindowEvent e){System.exit(0);}});
    
    setSize (800, 600);
    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    repaint();
    this.setVisible(true);
    }
    
    class Demo extends Canvas{
        Point start=null, end=null, temp=null; // for Pythagoras
        Point Node=new Point();
        Point Begin=null, Finish=null;   //for Graphic temp line drawing
        Point first,last;
        public int offsetY,offsetX;
        
        
        Demo(){
                this.setSize(800, 600);
                
            
            addMouseListener(new MouseAdapter(){
                public void mousePressed(MouseEvent e){
                       
                       if(paintmode){  //Draw Pythagoras
                           if(Start_End){
                                  P_End=null;
                                  start=e.getPoint();
                                  P_Starter=start;
                                  P_Starter.y+=offsetY;
                                  P_Starter.x+=offsetX;
                                  Start_End=false;
                                  repaint();
                           }
                            
                           else{
                                 end=e.getPoint();
                                 P_End=end;
                                 P_End.y+=offsetY;
                                 P_End.y+=offsetX;
                                Start_End=true; 
                                repaint();
                           }       
                    }//End paint mode true;
                       else{  //Draw Graphic
                           if(nod_link){   //if draw node
                               Node=e.getPoint();
                               Node.y+=offsetY;
                               Node.x+=offsetX;
                               nodelist.offer(Node);
                               repaint();
                           }
                           else{   //draw link
//                              Finish=null;
                              Begin=e.getPoint();
                              Begin.y+=offsetY;
                              Begin.x+=offsetX;
                              first=(Point) Begin.clone();

                           }//End else
                       }
                }
                
                public void mouseReleased(MouseEvent e){

                    if(paintmode==false){//only works in Graphic
                          if(nod_link==false){ //only works in Link drawing
                              last=e.getPoint();
                              last.y+=offsetY;
                              last.x+=offsetX;
                              Begin=null;
                              Finish=null;
                              repaint();
                   } //End if
                }//End if
                         
                    
                    
                        
                    
                }               
            });
                
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {

                }

                public void mouseDragged(MouseEvent e) {

                      if(paintmode==false){//only works in Graphic
                          if(nod_link==false){ //only works in Link drawing
                              Finish=e.getPoint();
                              Finish.y+=offsetY;
                              Finish.x+=offsetX;
                              
                   } //End if
                }//End if
                      repaint();
             }//End drag
            });
            
            
             
        }
        
        public void paint(Graphics g){
            if(paintmode)
                Pythagoras(g);
            else
                Graph(g);
            
  //          g.drawString("*"+Startlist.get(0), 40, 40);
        }

        private void Pythagoras(Graphics g) {
            Point Start=null,End=null;
            if(P_Starter!=null)
            g.fillOval(P_Starter.x-offsetX-2, P_Starter.y-offsetY-2, 5, 5);
            if(P_End!=null)
            g.fillOval(P_End.x-offsetX-2, P_End.y-offsetY-2, 5,5);
            
            
            
        if(P_Starter!=null && P_End!=null){
                Start= (Point) P_Starter.clone();
                End=(Point) P_End.clone();
                End.y-=offsetY;
                Start.y-=offsetY;  
                End.x-=offsetX;
                Start.x-=offsetX;
                DrawPythagoras(g,Start, End);
                g.drawLine(Start.x, Start.y, End.x, End.y); 
              }
        
            
        
                
            }
        
        
    private void DrawPythagoras(Graphics g,Point A, Point B){
      
            Point C=new Point();
            Point D=new Point();
            Point E=new Point();
            int u1=(B.x-A.x);
            int u2=(B.y-A.y);
            
            if(Math.abs(B.x-A.x)<5 && Math.abs(B.y-A.y)<5){
                ///END Recursion
            }
            else{
                D.x=A.x-u2;
                D.y=A.y+u1;
                C.x=D.x+u1;
                C.y=D.y+u2;
                E.x=D.x+(u1-u2)/2;
                E.y=D.y+(u1+u2)/2;
                
                g.drawLine(B.x, B.y, C.x, C.y);
                g.drawLine(C.x, C.y, D.x, D.y);
                g.drawLine(D.x, D.y, A.x, A.y);
                
                g.drawLine(C.x, C.y, E.x, E.y);
                g.drawLine(D.x, D.y, E.x, E.y);
                
                DrawPythagoras(g,E,C);
                DrawPythagoras(g,D,E);
            }
                
        }
        
    private void Graph(Graphics g){
            //For node drawing
             int n_size=nodelist.size();
         
             Point nod, P, Q;
             //draw node
             for(int i=0;i<n_size;i++){
                 nod=nodelist.get(i);
                 g.fillRect(nod.x-offsetX-2, nod.y-offsetY-2, 5, 5);
             }
             //draw temp line
              if(Begin!=null && Finish!=null)
             g.drawLine(Begin.x-offsetX, Begin.y-offsetY, Finish.x-offsetX, Finish.y-offsetY);
             //Judge if it's in the node
             
             if(first!=null && last!=null)
             Judge(first, last);
             
             int l_size=Endlist.size();
             //draw link
             for(int j=0;j<l_size;j++){
                 P=Startlist.get(j);
                 Q=Endlist.get(j);
                  if(P!=null && Q!=null)
                 g.drawLine(P.x-offsetX, P.y-offsetY, Q.x-offsetX, Q.y-offsetY);
             }
        }
    private void Judge(Point p,Point q){
        int size=nodelist.size();
        Point temp,Candy1=null,Candy2=null;
        Boolean Hit_1=false,Hit_2=false;
        
        
        for(int i=0;i<size;i++){
            temp=nodelist.get(i);
            if(Near(p,temp)){
                 Hit_1=true;
                 Candy1=temp;
            }
               
            if(Near(q,temp)){
                Hit_2=true;
                Candy2=temp;
            }         
        }
        
        if(Hit_1==true && Hit_2==true){
            Startlist.offer(Candy1);
            Endlist.offer(Candy2);
        }
        
        
    }
    
    private boolean Near(Point P, Point Q){
        int distanceX=Math.abs(P.x-Q.x);
        int distanceY=Math.abs(P.y-Q.y);
          if(distanceX<3 && distanceY<3)
              return true;
          else
              return false;
    }
    
    
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new PJ3();
    }
}
