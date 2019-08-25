 /* Name: Kelsey Nguyen
 * Class: CS340
 * Description: The program implemented DAG SP Algorithm, Dijkstra's Algorithm and Bellman-Ford
for single shortest path spanning trees of undirected positive weighted edge.
 * Due Date: Oct 31, 2017
 */
package project05;

/**
 *
 * @author tnguyen
 */

import java.io.*;
import java.util.*;
import java.lang.*;

public class Project05 {
    
    
        /**
     * @param args the command line arguments
     */

        //--Constants--//
        static final int INFINITY = 99999;
        static final int WHITE = 0;
        static final int GRAY = 1;
        static final int BLACK = 2;
        
        //--class definitions--//
        private static class Edge{
            public int weight;
            public int to;
            public String toStr;
            public int from;
            public Edge(){
                weight = 0;
                to = 0;
                toStr = "";
                from = 0;
            }
        }
        private static class Vertex{
            public String vertexName; //the character name of this vertex
            public int vertexNum; //the id number of this vertex
            public int parent;//represents the index in global graph
            public int distance;//used to calculate weight to this vertex
            public int color;//used for DFS search to know if a node is visited
            public int beginTime;//DFS search begin time
            public int finishTime;//DFS seach finish time
            public ArrayList<Edge>edges; //a list of all vertices that can be traveled to
            public Vertex(){
                edges = new ArrayList<Edge>();
                vertexName = "";
                vertexNum = INFINITY;
                parent = INFINITY;
                distance = INFINITY;
                color = WHITE;
                beginTime = INFINITY;
                finishTime = INFINITY;
            }
            
            public Vertex(String Vname, int Vnum){
                this();
                this.vertexName = Vname;
                this.vertexNum = Vnum;
            }
        }
        private static class Globals{
            public static ArrayList<Vertex> graph;
            public static ArrayList<Vertex> stack;
            public static int Negatives;//store how many negative weight DFS found
            public static int Loops;//store how many loops DFS found
        }  //--End Of Classes--//
        
 //--------------------------------------------------------------------------------------------
    //main driver function
        public static void main(String[] args) throws IOException{
            Globals.graph = new ArrayList<Vertex>();
            Globals.stack = new ArrayList<Vertex>();
            int source;
           
            ReadFile();
 
            source = PickSource();
 
            Algorithms(source);
 
            Output(source);
        }
        
 //-------------------------------------------------------------------------------------------- 
    //--Helper functions start--//
        //reads from our input file
        public static void ReadFile() throws IOException{
            //get the input file from the user
            Scanner sc = new Scanner(System.in);
            System.out.println("What input file do you want to use? ");
            System.out.println("example: graphin.txt, graphin1.txt, graphin2.txt, graphin3.txt");
            System.out.println("graphin4.txt, graphin5.txt, graphin6.txt, graphin7.txt or graphin8.txt");
            String inputFile = sc.nextLine();
           
            //read the input file
            Scanner reader = new Scanner(new File(inputFile));
            while(reader.hasNext())
            {
                //get the next line & split by the ':'.  This separates the ID from the edge list
                String[] line = reader.nextLine().split(":");
                Vertex newVert = new Vertex(line[0], Globals.graph.size()+1); //creating the new vertex
                if(line.length > 1) //checking if there are any destinations at all
                {
                    String[] destinations = line[1].trim().split(" "); //trim the leading space & split by space
                    for(int i=0; i+1<destinations.length; i+=2) //go through each destionation
                    {
                        //add our new edge
                        Edge newEdge = new Edge();
                        newEdge.from = newVert.vertexNum;
                        newEdge.toStr = destinations[i];
 
                        //attemp to fetch weight info
                        if (destinations.length > i+1){
                            newEdge.weight = Integer.parseInt(destinations[i+1]);
                        }else{
                            newEdge.weight = INFINITY;
                        }
                        newVert.edges.add(newEdge);
                    }
                }
                //add the new vertex to the graph
                Globals.graph.add(newVert);
            }
            
        
              //now parse our edges into actual integers if they arent already.
                for (int i = 0; i < Globals.graph.size(); i++) {//vertexes
                    for (int j = 0; j < Globals.graph.get(i).edges.size(); j++) {//edges
                        int x = 0;
                        String lookAt = Globals.graph.get(x).vertexName;
                        while (!lookAt.equals(Globals.graph.get(i).edges.get(j).toStr)){//checking
                            x++;
                            lookAt = Globals.graph.get(x).vertexName;
                        }
                        //found x that points at our edge 'to'
                        Globals.graph.get(i).edges.get(j).to = x+1;
                    }
                }
            
        }
        
 
 //--------------------------------------------------------------------------------------------
        //sorts graph
        public static void Algorithms(int source){
            DFS();
            if (Globals.Loops == 0)
            {
                //no negative edge or loop
                System.out.printf("Graph had no loops, we will use the DAG-SP algorithm\n");
                DagSP(source);
            }else{
                if (Globals.Negatives == 0){    
                    //no negative edges but does have a loop
                    System.out.printf("Graph had no negative edges we will use the Djikstra algorithm\n");
                    Djikstra(source);
                }else{
                    //everything else
                    System.out.printf("Graph had loops and Negative edges, we will use the Bellman-Ford algorithm\n");
                    if (BellmanFord(source) == false){
                        System.out.printf("Negative Edge Weight cycle was found.  Path weights are not well defined.\n");
                        System.exit(-1);
                    }
                }
            }
        }
//--------------------------------------------------------------------------------------------  
        //takes user input for our source vertex
        public static int PickSource(){
            Scanner input = new Scanner(System.in);
            int source;
            do{
                System.out.printf("\nSpecify source vertex number.  Valid inputs are from 1 to %d.\n", Globals.graph.size());
                source = input.nextInt();
            }while(source < 1 || source > Globals.graph.size());
 
            return --source;
        }
 //--------------------------------------------------------------------------------------------
        //takes user input for destination vertex and displays distance to it
        public static void Output(int source){
            Scanner input = new Scanner(System.in);
            int destination;
            do{
                do{
                    System.out.printf("\nSpecify destination vertex number.  Valid inputs are from 0 to %d. '0' is used to exit.\n", Globals.graph.size());
                    destination = input.nextInt();
                }while(destination < 0 || destination > Globals.graph.size());
                if (destination == 0){return;};
 
                if (Globals.graph.get(destination-1).distance != INFINITY){
                    System.out.printf("Distance to destination is: %d\n", Globals.graph.get(destination-1).distance);
                    //trace path by following parent pointer
                    System.out.printf("Tracing path back to source:\n");
                    int parent = Globals.graph.get(destination-1).parent;
                    int from = destination;
                    while(parent != INFINITY){
                        System.out.printf("Path from vertex %d to vertex %d\n", from, parent);
 
                        from = parent;
                        parent = Globals.graph.get(parent-1).parent;
                    };
                }else{
                    System.out.printf("Distance to this node is infinity.  No path exists from source to this node.\n");
                }
            }while(true);
        }
    //--Helper functions end--//
        
 //--------------------------------------------------------------------------------------------
    
//--Heap sort starts here--//
    //Heap sort is used for sorting our edges in kruskal's algorithm
        public static ArrayList<Edge> BuildHeapEdge(int n, int i, ArrayList<Edge> edges){
            int l = 2*i + 1;
            int r = 2*i + 2;
            int largest;
 
            //find largest of i and l
            if (l < n && edges.get(l).weight > edges.get(i).weight){
                largest = l;
            }else{
                largest = i;
            }
 
            //if right child is larger
            if (r < n && edges.get(r).weight > edges.get(largest).weight){
                largest = r;
            }
 
            //if largest is not root
            if (largest != i){
                Edge a = edges.get(largest);
                edges.set(largest, edges.get(i));
                edges.set(i, a);
 
                //recursively heapify the affected sub-tree
                edges = BuildHeapEdge(n, largest, edges);
            }
            return edges;
        }
 
        public static void HeapSortEdge(int n, ArrayList<Edge> edges){
            //populate heap
            for (int i = n/2 -1; i >= 0; i--){
                edges = BuildHeapEdge(n, i, edges);
            }
 
            //pull from top of heap into array
            //this does our sorting
            for (int i = n-1; i >= 0; i--){
                //move current root to the end
                Edge a = edges.get(0);
                edges.set(0, edges.get(i));
                edges.set(i, a);
 
                //call max heapify on the reduced heap
                edges = BuildHeapEdge(i, 0, edges);
            }
        }
        

        //--Heap sort ends here--//
        
 //--------------------------------------------------------------------------------------------   
    //--Sorting Algorithms Start--//
        //Relax
            public static boolean Relax (int u, int v, Edge uv){
                if (Globals.graph.get(v).distance > Globals.graph.get(u).distance + uv.weight){
                    Globals.graph.get(v).distance = Globals.graph.get(u).distance + uv.weight;
                    Globals.graph.get(v).parent = u+1;
                    return true;
                }
                return false;
            }
            
//--------------------------------------------------------------------------------------------   
            
            //DFS starting function
            public static void DFS(){
                int time = 0;
                for(int i = 0; i < Globals.graph.size(); i++){
                    Globals.graph.get(i).color = WHITE;
                }
 
                for(int i = 0; i < Globals.graph.size(); i++){
                    if(Globals.graph.get(i).color == WHITE){
                        time = DFSvisit(i, time);
                    }
                }
                return;
            }
            
//--------------------------------------------------------------------------------------------            
            //Visit each node with DFS
            public static int DFSvisit(int i, int time){
                Edge uv;//current edge
 
                //visit this node
                time = time + 1;
                Globals.graph.get(i).beginTime = time;
                Globals.graph.get(i).color = GRAY;
 
                //now search each edge
                for (int j = 0; j < Globals.graph.get(i).edges.size(); j++) {
                    //grab an edge
                    uv = Globals.graph.get(i).edges.get(j);
                    //negative weight? does not need to be an exact number
                    if(uv.weight < 0){
                        Globals.Negatives++;
                    }
                    //already visited?
                    if(Globals.graph.get(uv.to-1).color == WHITE){
                        //regular tree edge
                        Globals.graph.get(uv.to-1).parent = i;
                        time = DFSvisit(uv.to-1, time);
                    }else{
                        //not a normal edge
                        if(Globals.graph.get(uv.to-1).color == GRAY){
                            //should be a loop
                            Globals.Loops++;
                        }else{
                            //probably a cross or forward edge
                        }
                    }
                }
                //done with this vertex
                //push to stack
                Globals.stack.add(Globals.graph.get(i));
                //update info
                Globals.graph.get(i).color = BLACK;
                time = time + 1;
                Globals.graph.get(i).finishTime = time;
                return time;
            }//--DFS End Here--//
            
//-------------------------------------------------------------------------------------------- 
        //Djikstra
            public static void Djikstra(int source){
                //initialize variables
                ArrayList<Edge> Cut = new ArrayList<Edge>();
 
                int v = source;//vertex index we are working on
                Globals.graph.get(v).distance = 0;
 
                //start main loop
                do{
                    //add selected cut vertices edges to the cut
                    for (int i = 0; i < Globals.graph.get(v).edges.size(); i++) {
                        Edge adj = Globals.graph.get(v).edges.get(i);
                        if (Relax(adj.from-1, adj.to-1, adj)){
                            Cut.add(adj);
                        }
                    }
 
                    //sort the cut
                    HeapSortEdge(Cut.size(), Cut);
 
                    if(Cut.size() == 0){return;};
 
                    //pick a cut vertex
                    v = Cut.get(0).to-1;
                    //remove edges no longer valid in cut
                    int i = 0;
                    while(i < Cut.size()){
                        if (Cut.get(i).to-1 == v){
                            Cut.remove(i);
                            i--;
                        }
                        i++;
                    }
                }while(Cut.size() > 0);
                return;
            }//--Djikstra End Here--//
//--------------------------------------------------------------------------------------------             
        //Bellman-Ford
            public static boolean BellmanFord(int source){
                //initialize variables
                ArrayList<Edge> edges = new ArrayList<Edge>();
                Globals.graph.get(source).distance = 0;//starting source at 0
 
                //grab all edges
                for (int i = 0; i < Globals.graph.size(); ++i)
                {
                    for (int j = 0; j < Globals.graph.get(i).edges.size(); j++) {
                        edges.add(Globals.graph.get(i).edges.get(j));
                    }
                }
 
                //relax all edges for each vertex
                for (int i = 0; i < Globals.graph.size(); ++i)
                {
                    for (int j = 0; j < edges.size(); ++j)
                    {
                        Relax(edges.get(j).from-1, edges.get(j).to-1, edges.get(j));
                    }
                }
                //If any relax occurs then a negative cycle was found
                for (int i = 0; i < edges.size(); ++i)
                {
                    if (Relax(edges.get(i).from-1, edges.get(i).to-1, edges.get(i))){
                        return false;
                    }
                }
                return true;
            }//--Bellman-Ford End Here--//
            
//--------------------------------------------------------------------------------------------             
        //DAG-SP
            public static void DagSP(int source){
                Globals.graph.get(source).distance = 0;//set source distance to zero
 
                //go through our stack relaxing each edge for every vertex
                for (int i = Globals.stack.size()-1; i >= 0; i--) {
                    for (int j = 0; j < Globals.stack.get(i).edges.size(); j++) {
                        Relax(Globals.stack.get(i).edges.get(j).from-1, Globals.stack.get(i).edges.get(j).to-1, Globals.stack.get(i).edges.get(j));
                    }
                }
                return;
            }//--DAG-SP END HERE--//
}
