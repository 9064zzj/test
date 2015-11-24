/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stairs;

import java.awt.Point;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zhongjiezheng
 */
public class Stairs {
    //3 parameter
    public static int StairNum;
    public static Double StairDegree;
    public static String FileName;
//    public static LinkedList<Node> Nodes = new LinkedList();
//    public static LinkedList<Face> Faces = new LinkedList();
    public static LinkedList<stair> Stairs = new LinkedList();
    public static LinkedList<Node> Cylinder= new LinkedList();
    public static StringBuffer Context=new StringBuffer();
    
    public Stairs(){
    
    }
    
    public class Face
    {
        public LinkedList<Integer> FacesNode= new LinkedList();
        
        public Face(){
        
       }
        public int get(int Index){
            return this.FacesNode.get(Index);
        }
        
        public void add(int arrs[])
        {
            for(int i=0; i<arrs.length; i++){
                FacesNode.offer(arrs[i]);
            }
        }
        
        public void add(int Index)
        {
            this.FacesNode.offer(Index);
        }
        
        public int size()
        {
            return FacesNode.size();
        }
        
        public String toString(){
            int size = FacesNode.size();
            String all="";
            for(int i=0;i<size-1;i++){
                all+=FacesNode.get(i)+" ";
            }
            all+=FacesNode.get(size-1);
            all+=".";
            return all;
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
        
        @Override
        public String toString(){
            return this.index+" "+(float)(this.x)+" "+(float)(this.y)+" "+(float)(this.z);
        }
         
    }
    
    public class stair{
        Node Node[]=new Node[10];       //pillar node in 8,9  
        Face Face[]=new Face[7];        //pillar in face 7
        
        public stair(int index){
            int i=index-1;
            Double Radian=(StairDegree*i/180)*Math.PI;
            stair Zero=new stair();
            
            this.Node[0]=new Node();
            this.Node[1]=new Node();
            this.Node[2]=new Node();
            this.Node[3]=new Node();
            this.Node[4]=new Node();
            this.Node[5]=new Node();
            this.Node[6]=new Node();
            this.Node[7]=new Node();
            this.Node[8]=new Node();
            this.Node[9]=new Node();
            // node transformation
            this.Node[0]=Trans(Zero.Node[0],Radian,i);
            this.Node[1]=Trans(Zero.Node[1],Radian,i);
            this.Node[2]=Trans(Zero.Node[2],Radian,i);
            this.Node[3]=Trans(Zero.Node[3],Radian,i);
            this.Node[4]=Trans(Zero.Node[4],Radian,i);
            this.Node[5]=Trans(Zero.Node[5],Radian,i);
            this.Node[6]=Trans(Zero.Node[6],Radian,i);
            this.Node[7]=Trans(Zero.Node[7],Radian,i);
            this.Node[8]=Trans(Zero.Node[8],Radian,i);
            this.Node[9]=Trans(Zero.Node[9],Radian,i);
             // Face index change
            this.Face[0]=Change(Zero.Face[0],i);
            this.Face[1]=Change(Zero.Face[1],i);
            this.Face[2]=Change(Zero.Face[2],i);
            this.Face[3]=Change(Zero.Face[3],i);
            this.Face[4]=Change(Zero.Face[4],i);
            this.Face[5]=Change(Zero.Face[5],i);
            this.Face[6]=Change(Zero.Face[6],i);
            
        }
        
        public stair()
        {
        this.Node[0] = new Node(1,7d,-1d,0);
        this.Node[1] = new Node(2,7d,1d,0);
        this.Node[2] = new Node(3,1d,1d,0);
        this.Node[3] = new Node(4,1d,-1d,0);
        this.Node[4] = new Node(5,7d,-1d,0.2);
        this.Node[5] = new Node(6,7d,1d,0.2);
        this.Node[6] = new Node(7,1d,1d,0.2);
        this.Node[7] = new Node(8,1d,-1d,0.2);
        this.Node[8] = new Node(9,7d,0,0.1);
        this.Node[9] = new Node(10,7d,0,6d);
        
        this.Face[0]=new Face();
        this.Face[1]=new Face();
        this.Face[2]=new Face();
        this.Face[3]=new Face();
        this.Face[4]=new Face();
        this.Face[5]=new Face();
        this.Face[6]=new Face();
        
        this.Face[0].add(new int[]{1,2,6,5});
        this.Face[1].add(new int[]{4,8,7,3});
        this.Face[2].add(new int[]{5,6,7,8});
        this.Face[3].add(new int[]{1,4,3,2});
        this.Face[4].add(new int[]{2,3,7,6});
        this.Face[5].add(new int[]{1,5,8,4});
        this.Face[6].add(new int[]{9,10});
        
        }
        
        private Node Trans(Node ZPoint,Double Radian,int i)
        {
            Node Temp=new Node();
            Double sin=Math.sin(Radian);
            Double cos=Math.cos(Radian);
            
            Temp.index=ZPoint.index+10*i;
            Temp.x=(cos*ZPoint.x)+(-sin*ZPoint.y);
            Temp.y=(sin*ZPoint.x)+(cos*ZPoint.y);
            Temp.z=ZPoint.z+i;
            
            return Temp;
        }
        
        private Face Change(Face Origin,int i)
        {
            Face Temp=new Face();
            int size = Origin.FacesNode.size();
            int index;
            
            for(int j=0;j<size;j++){
                index=Origin.get(j);
                Temp.FacesNode.offer(index+i*10);
            }
            
            return Temp;
        }
        
    }
    
    public void Cylinder(){
        int edge=20;
        Double Theta=2*Math.PI/edge;
        Double R=1d;
        
        
        //Generate the base circle
        for(int i=0;i<edge;i++){
            Node Temp=new Node();
            Double sin=Math.sin(Theta*i);
            Double cos=Math.cos(Theta*i);
            
            Temp.index=i+1+(StairNum*10);
            Temp.y=sin*R;
            Temp.x=cos*R;
            Temp.z=0;
            Cylinder.offer(Temp);
        }
        //Generate the top circle
        for(int i=0;i<edge;i++){
            Node Temp=new Node();
            Double sin=Math.sin(Theta*i);
            Double cos=Math.cos(Theta*i);
            
            Temp.index=i+1+(StairNum*10)+20;
            Temp.y=sin*R;
            Temp.x=cos*R;
            Temp.z=StairNum+6;
            Cylinder.offer(Temp);
        }
        
        
    }
    
    public void Generate(){
        //Get all stairs in Stairs
        for(int index=1;index<=StairNum; index++){
            stair token = new stair(index);
            Stairs.offer(token);
        }
        Cylinder();
        
        
        
        //Now we have cyclinder nodes,stairs nodes,that's everything
        //we put stairs node first
        stair stair=new stair();
        
        for(int i=0;i<StairNum;i++){//retrieve every stair
            stair=Stairs.get(i);
            for(int j=0;j<10;j++){       //retrieve every Node
                Context.append(stair.Node[j].toString()+"\r\n");
            }
        }
        //We put cylinder node then
        for(int i=0;i<Cylinder.size();i++){
            Context.append(" "+Cylinder.get(i).toString()+"\r\n");
        }
 /////////////////////////////////////////////////////////////////////////       
 /////////////////////faces///////////////////////////////////////////////       
        //Then faces
        Context.append("Faces:"+"\r\n");      //Add Faces segment label here
        //First cyclinder's circle;
        int half=Cylinder.size()/2;
        for(int i=half-1;i>-1;i--){              //Bottom circle
            Context.append(" "+Cylinder.get(i).index);
        }
        Context.append("."+"\r\n");     //Enter
        
        for(int i=half;i<Cylinder.size();i++){    //Top circle
            Context.append(" "+Cylinder.get(i).index);
        }
        Context.append("."+"\r\n");     //Enter
        
 //////////////////Now the cylinder's side polygon
        for(int i=0;i<half-1;i++){
            Node N1=Cylinder.get(i);
            Node N2=Cylinder.get(i+1);
            Node N3=Cylinder.get(half+i+1);
            Node N4=Cylinder.get(half+i);
            Context.append(" "+N1.index+" "+N2.index+" "+N3.index+" "+N4.index+"."+"\r\n");
        }
        ////////The last polygon is a special case
        Node N1=Cylinder.get(half-1);
        Node N2=Cylinder.get(0);
        Node N3=Cylinder.get(half);
        Node N4=Cylinder.get(Cylinder.size()-1);
        Context.append(" "+N1.index+" "+N2.index+" "+N3.index+" "+N4.index+"."+"\r\n");
///////////////Now it's the stair face
        for(int i=0;i<StairNum;i++){
            stair token=Stairs.get(i);
            for(int j=0;j<6;j++){
                Context.append(token.Face[j].toString()+"\r\n");
            }
        }
/////////////Now it's the pillar
        for(int i=0;i<StairNum-1;i++){
            Context.append(Stairs.get(i).Face[6].toString()+"\r\n");
            Context.append((i+1)*10+" "+(i+2)*10+"."+"\r\n");
        }
        Context.append(Stairs.get(StairNum-1).Face[6].toString()+"\r\n");
    }
    
    public void fileGenerate(){
        
        Generate();
        try {
            File f = new File("/Users/zhongjiezheng/Desktop/"+FileName+".dat");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(Context.toString());
            bw.close();
        } catch (IOException ex) {
            
        }

    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Stairs A=new Stairs();
        String arrs[],command;
        System.out.println("Please type command in format like \"java Stair StairNum StairRotation FileName\":" );
        Scanner input = new Scanner(System.in);
        command=input.nextLine();
        arrs=command.split(" ");
        A.StairNum=Integer.parseInt(arrs[2]);
        A.StairDegree=Double.parseDouble(arrs[3]);
        A.FileName=arrs[4];
        A.fileGenerate();
    }
}
