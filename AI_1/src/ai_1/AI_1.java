/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai_1;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.*;

/**
 *
 * @author zhongjiezheng
 */
public class AI_1 {

    public Vertex A1 = new Vertex('A');
    public Vertex B1 = new Vertex('B');
    public Vertex C1 = new Vertex('C');
    public Vertex D1 = new Vertex('D');
    public Vertex E1 = new Vertex('E');
    public int Number_of_Links1 = 8;

    public Vertex A2 = new Vertex('A');
    public Vertex B2 = new Vertex('B');
    public Vertex C2 = new Vertex('C');
    public Vertex D2 = new Vertex('D');
    public Vertex E2 = new Vertex('E');
    public Vertex F2 = new Vertex('F');
    public int Number_of_Links2 = 10;
    
    public String fileName = "report";
    
    public int figureNum =1;
    public ArrayList<Pos> FoundPathtoA = new ArrayList<>();
    public ArrayList<Pos> FoundPathtoAnywhere = new ArrayList<>();
    public ArrayList<Node> Reached_Node = new ArrayList<>();
    public int expandCount1=0;
    public int expandCount2=0;
    public static StringBuffer Context=new StringBuffer();

    
    public AI_1(){
        initialization();
    }
    
    public void initialization(){
        A1.AddAdjacent(B1,C1,D1);
        B1.AddAdjacent(A1,C1,D1);
        C1.AddAdjacent(A1,B1,D1,E1);
        D1.AddAdjacent(A1,B1,C1,E1);
        E1.AddAdjacent(D1,C1);
        PathFinder(A1, Number_of_Links1);
        
        A2.AddAdjacent(B2,C2,D2,F2);
        B2.AddAdjacent(A2,C2,D2,F2);
        C2.AddAdjacent(A2,B2,D2,E2);
        D2.AddAdjacent(A2,B2,C2,E2);
        E2.AddAdjacent(D2,C2);
        F2.AddAdjacent(A2,B2);
        PathFinder(A2, Number_of_Links2);
        fileGenerate();
    }
           
    public class Vertex{
        public char Name;
        public ArrayList<Vertex> Adjacent = new ArrayList<>();
        
        public Vertex(){
           
        }
        
        public Vertex(char Name, ArrayList Adjacent){
            this.Name = Name;
            this.Adjacent = Adjacent;
        }
        
        public Vertex(char Name){
            this.Name = Name;
        }
        
        public boolean AddAdjacent(Vertex...Vertices){
            return Adjacent.addAll(Arrays.asList(Vertices));
        }
        
        @Override
        public String toString(){
            return Character.toString(Name);
        }
    }
    
    public class Link extends HashSet<Vertex>{
//        public Set<Vertex> Vertices = new HashSet<>();
        public Link(){
            
        }
        
        public Link(Vertex A, Vertex B){
            add(A);
            add(B);
        }
    }
    
    public class Node extends ArrayList<Link>{
        
        Node(){
            
        }
        
        public boolean addNode(Link New){
            
            if(contains(New)){
                return false;
            }
            else{
                add(New);
                return true;
            }
        }
        


        public boolean complete(int TargetNum){
            return size()==TargetNum;
        }
        
    }
    
    public class Pos extends ArrayList<Vertex>{
        public Pos(){
            
        }
        
        public void addPos(Vertex Position){
            add(Position);
        }
        
        public Vertex getPos(){
            return get(size()-1);
        }
    }
    
    public boolean PathFinder(Vertex Start,int TargetNum){
        
        Queue<Node> Linkqueue = new LinkedList<>();
        Queue<Pos> Posqueue = new LinkedList<>();
        
        Node starter = new Node();
        Pos starterPos = new Pos();
        starterPos.addPos(Start);
        
        Linkqueue.add(starter);
        Posqueue.add(starterPos);
        
        while(!Linkqueue.isEmpty()){
            Node LinkStatus = Linkqueue.poll();
            Pos PosStatus = Posqueue.poll();
            
            Reached_Node.add(LinkStatus);
            if(FoundPathtoA.isEmpty())
                   expandCount1++;
            if(FoundPathtoAnywhere.isEmpty())
                   expandCount2++;

            Vertex CurrentPos = PosStatus.getPos();
            for(int i=0; i< CurrentPos.Adjacent.size();i++){
                Vertex adj = CurrentPos.Adjacent.get(i);
                Link NewLink = new Link(CurrentPos,adj);
                   
                Node ChildNode = (Node)LinkStatus.clone();
                Pos ChildPos = (Pos)PosStatus.clone();
                   
                //it is a ChildNode only if adding is succced
                if(ChildNode.addNode(NewLink))
                    ChildPos.addPos(adj);
             
                if(!Reached_Node.contains(ChildNode) && !Linkqueue.contains(ChildNode)){
                         if(ChildNode.complete(TargetNum)){
                             if(ChildPos.get(ChildPos.size()-1).equals(Start))
                                FoundPathtoA.add(ChildPos);
                             else{
                                 FoundPathtoAnywhere.add(ChildPos);
                             }
                         }
                         else{
                              Linkqueue.add(ChildNode);
                              Posqueue.add(ChildPos);
                         }
                }
            } 
        }
        
        PathPrinter();
        return !FoundPathtoA.isEmpty() || !FoundPathtoAnywhere.isEmpty();
    }
    
    
    public void PathPrinter(){
        int Number1 = FoundPathtoA.size();
        int Number2 = FoundPathtoAnywhere.size();
        
        System.out.println("Figure "+figureNum+" from A to A:");
        Context.append("Figure "+figureNum+" from A to A:"+"\n");
        if(Number1>0){
            System.out.println("\nThe first found Paths");
            Context.append("\nThe first found Paths"+"\n");
            System.out.println(FoundPathtoA.get(0));
            Context.append(FoundPathtoA.get(0)+"\n");
            System.out.println("Number of Nodes expand: "+expandCount1+"\n");
            Context.append("Number of Nodes expand: "+expandCount1+"\n"+"\n");
            System.out.println("There are: "+Number1+" Paths for figure "+figureNum);
            Context.append("There are: "+Number1+" Paths for figure "+figureNum+"\n");

            for(int i=0; i<FoundPathtoA.size();i++){
                System.out.println(FoundPathtoA.get(i));
                Context.append(FoundPathtoA.get(i)+"\n");
            }
            System.out.println("\n");
            Context.append("\n"+"\n");
        }
        else{
            System.out.println("There are no solutions. \n");
            Context.append("There are no solutions. \n"+"\n");
        }
///////////////////////////////////////////////////////////////////
        System.out.println("Figure "+figureNum+" from A to Anywhere:");
        Context.append("Figure "+figureNum+" from A to Anywhere:"+"\n");
        if(Number2>0){
            System.out.println("\nThe first found Paths");
            Context.append("\nThe first found Paths"+"\n");
            System.out.println(FoundPathtoAnywhere.get(0));
            Context.append(FoundPathtoAnywhere.get(0)+"\n");
            System.out.println("Number of Nodes expand: "+expandCount2+"\n");
            Context.append("Number of Nodes expand: "+expandCount2+"\n"+"\n");
            System.out.println("There are: "+Number2+" Paths for figure "+figureNum);
            Context.append("There are: "+Number2+" Paths for figure "+figureNum+"\n");
            
            for(int i=0; i<FoundPathtoAnywhere.size();i++){
                System.out.println(FoundPathtoAnywhere.get(i));
                Context.append(FoundPathtoAnywhere.get(i)+"\n");
            }
            System.out.println("\n \n");
            Context.append("\n \n"+"\n");
        }
        else{
            System.out.println("There are no solutions. \n");
            Context.append("There are no solutions. \n"+"\n");
        }
        
        
        figureNum++;
        clear();
    }
    
    public void clear(){
        FoundPathtoA.clear();
        FoundPathtoAnywhere.clear();
        Reached_Node.clear();
        expandCount1=1;
        expandCount2=1;
    }
            
    public void fileGenerate(){
        try {
            File f = new File("/Users/zhongjiezheng/Desktop/"+fileName+".txt");
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
        AI_1 demo = new AI_1();
        
        
    }
}
