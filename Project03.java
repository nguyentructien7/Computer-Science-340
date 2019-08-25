 /* Name: Kelsey Nguyen
 * Class: CS340
 * Description: The program is about depth first search. It tests whether the input is acylic or not.
    It uses topologically sorting.
 * Due Date: Sep 28, 2017
 */
package project03;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
/**
*
* @author tnguyen
*/
public class Project03 {
 
    // A single vertex has an id number and a list of vertexes it can travel to (edges)
    //It's private static class, so the members will be public.  it will only be used internally
    private static class Vertex
    {
        public int vertexNum; //the id number of this vertex
        public ArrayList<Integer>edges; //a list of all vertices that can be traveled to
        public int color;
        public int startTime;
        public int finishTime;
        //default constructor sets the id to 0 and creates a blank list for the edges
        public Vertex() {
            edges = new ArrayList<Integer>();
            vertexNum = 0;
            color = 0;
            startTime = 0;
            finishTime = 0;
        }
    }
 
    private static class Globals
    {
        public static ArrayList<Vertex> graph;
        public static ArrayList<Vertex> stack;
        public static ArrayList<String> loops;
        public static int time;
    }
   
    public static void main(String[] args) throws IOException{
        // A collection of vertexes
        Globals.graph = new ArrayList<Vertex>();
        Globals.stack = new ArrayList<Vertex>();
        Globals.loops = new ArrayList<String>();
       
        //get the input file from the user
        Scanner sc = new Scanner(System.in);
        System.out.println("What input file do you want to use? (graphin1.txt, graphin2.txt or acyclic.txt ): ");
        String inputFile = sc.nextLine();
       
        //read the input file
        Scanner reader = new Scanner(new File(inputFile));
        while(reader.hasNext())
        {
            //input will be formed like as VertexNumber
            String[] line = reader.nextLine().split(":");
            Vertex newVert = new Vertex(); //new vertex
            newVert.vertexNum = Integer.parseInt(line[0]); //setting its ID
            if(line.length > 1) //checking if there are any destinations at all
            {
                
                //these destionations form our edges
                String[] destinations = line[1].trim().split(" "); 
                //we now have a list of destionations
                for(int i=0;i<destinations.length;i++) //go through each destionation
                {
                    //parse it to an int and add it to the list of edges for the new vertex
                    newVert.edges.add(Integer.parseInt(destinations[i]));
                }
            }
            //and add the new vertex 
            Globals.graph.add(newVert);
        }
       
        //reprint the graph
        for(int i=0;i<Globals.graph.size();i++)
        {
            System.out.println(Globals.graph.get(i).vertexNum + ":" + Globals.graph.get(i).edges);
        }
       
        DFS();
 
        if (Globals.loops.size() > 0){
            //graph is not acyclic
            System.out.println("The graph is not acyclic.  Loop edges:");
            for (int i = 0; i < Globals.loops.size(); i++) {
                System.out.println(Globals.loops.get(i));
            }
        }else{
            //graph is acyclic
            System.out.println("The graph is acyclic.  Sorted Vertex order:");
            for (int i = Globals.stack.size()-1; i >= 0; i--){
                System.out.println("Vertex: " + Globals.stack.get(i).vertexNum);
            }
        }
    }
   
    public static void DFS(){
        Globals.time = 0;
        for(int i = 0; i < Globals.graph.size(); i++){
            Vertex u = Globals.graph.get(i);
            u.color = 0;
        }
 
        for(int i = 0; i < Globals.graph.size(); i++){
            Vertex u = Globals.graph.get(i);
            if(u.color == 0){
                DFSvisit(u);
            }
        }
    }
 
    public static void DFSvisit(Vertex u){
        Globals.time = Globals.time + 1;
        u.startTime = Globals.time;
        u.color = 1;
 
        //loop over all adjacent edges to our node 'u'
        for(int i = 0; i < u.edges.size(); i++){
            Vertex v = Globals.graph.get(u.edges.get(i)-1);
            //if visited yet
            if (v.color == 0){
                DFSvisit(v);
            }else{
                //already visited
                if (v.color == 1){
                    //backward edge found! there is a loop
                    Globals.loops.add("Loop on edge between " + u.vertexNum + ", " + v.vertexNum);
                }else{
                    //probably a cross edge
                }
 
            }
        }
        
        //finish with this node
        Globals.time = Globals.time + 1;
        u.color = 2;
       u.finishTime = Globals.time;
        Globals.stack.add(u);
    }
}
