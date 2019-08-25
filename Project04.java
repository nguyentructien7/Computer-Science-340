 /* Name: Kelsey Nguyen
 * Class: CS340
 * Description: The program implemented Kruskal's Algorithm and Prim Algorithm for findng
minimum spanning trees of undirected positive weighted edge.
 * Due Date: Oct 17, 2017
 */
package project04;

/**
 *
 * @author tnguyen
 */


import java.io.*;
import java.util.*;
import java.lang.*;
 
public class Project04 {

   
    /**
     * @param args the command line arguments
     */
    
    
//class definitions
    
    private static class Edge{
        public int weight;
        public int to;
        public int from;
        public Edge(){
            weight = 0;
            to = 0;
            from = 0;
        }
    }
//---------------------------------------------------------------------------------------------    
    private static class Vertex{
        public int vertexNum; //the id number of this vertex
        public int depth; //represents how deep our parent tree is
        public int parent;//represents the index in global graph
        public int qPos;//represnts our position in the MST priority queue
        public ArrayList<Edge>edges; //a list of all vertices that can be traveled to
        public Vertex() {
            edges = new ArrayList<Edge>();
            vertexNum = -1;
            depth = 1;
            parent = -1;
            qPos = -1;
        }
    }
//---------------------------------------------------------------------------------------------    
    private static class Globals{
        public static ArrayList<Vertex> graph;
        public static ArrayList<Edge> forest;
        public static ArrayList<Edge> allEdges;
    }
//---------------------------------------------------------------------------------------------    
    private static class Node{
        public int vertexNum;
        public int key;
        public int parent;
        public Node(){
            vertexNum = -1;
            key = -1;
            parent = -1;
        }
    }
 //---------------------------------------------------------------------------------------------       
    private static class MST{
        public ArrayList<Node> tree;
        public MST(){
            tree = new ArrayList<Node>();
        }
        //inserts into tree and preserves any queue postion references
        public void Insert(Node r){
            int i = 0;
            //move farther in queue while we are larger and the node is not 'infinite weight'
            while (i < this.tree.size() && r.key > this.tree.get(i).key && this.tree.get(i).key > 0){
                i++;
            }
            //insert into tree
            this.tree.add(i, r);
            //update queue position references
            int j = 0;
            while (j+i < this.tree.size()){
                Vertex v = Globals.graph.get(this.tree.get(j+i).vertexNum-1);
                v.qPos = j+i;
                Globals.graph.set(this.tree.get(j+i).vertexNum-1, v);
                j++;
            }
        }
        //should one be used to move a node forward
        public void DecreaseKey(int i, int w, int p){
            Node r = this.tree.get(i);
            if (w < r.key || r.key < 0){
                this.tree.remove(i);
                r.key = w;
                r.parent = p;
                this.Insert(r);
            }
        }
        //takes the first node out of the priority queue
        public Node ExtractMin(){
            Node r = this.tree.get(0);
            this.tree.remove(0);
            //update queue position references
            for (int j = 0; j < this.tree.size(); j++) {
                Vertex v = Globals.graph.get(this.tree.get(j).vertexNum-1);
                v.qPos = j;
                Globals.graph.set(this.tree.get(j).vertexNum-1, v);
            }
            return r;
        }
    }
   //---------------------------------------------------------------------------------------------
    //main driver function
    public static void main(String[] args) throws IOException{
        // A collection of vertexes
        Globals.graph = new ArrayList<Vertex>();
        Globals.forest = new ArrayList<Edge>();
        Globals.allEdges = new ArrayList<Edge>();
        ArrayList<Edge> edges;
       
        ReadFile();
       
        kruskal();
 
        prim();
    }
 //---------------------------------------------------------------------------------------------
    //reads from our input file
    public static void ReadFile() throws IOException{
        //get the input file from the user
        Scanner sc = new Scanner(System.in);
        System.out.println("What input file do you want to use? example: graphin.txt or graphin1.txt");
        String inputFile = sc.nextLine();
      
      
        
        //read the input file
        Scanner reader = new Scanner(new File(inputFile));
        while(reader.hasNext())
        {
            //get the next line & split by the ':'.  This separates the ID from the edge list
            String[] line = reader.nextLine().split(":");
            Vertex newVert = new Vertex(); // new vertex
            newVert.vertexNum = Integer.parseInt(line[0]); //setting its ID
            newVert.parent =-1;
            if(line.length > 1) //checking if there are any destinations at all
            {
                String[] destinations = line[1].trim().split(" "); //trim the leading space & split by space
                for(int i=0;i<destinations.length;i+=2) //go through each destionation
                {
                    //add our new edge
                    Edge newEdge = new Edge();
                    newEdge.from = newVert.vertexNum;
                    newEdge.to = Integer.parseInt(destinations[i]);
                    //attemp to fetch weight info
                    if (destinations.length > i+1){
                        newEdge.weight = Integer.parseInt(destinations[i+1]);
                    }else{
                        newEdge.weight = -1;
                    }
                    newVert.edges.add(newEdge);
                }
            }
            //add the new vertex to the graph
            Globals.graph.add(newVert);
        }
    }
 //---------------------------------------------------------------------------------------------
    //--Heap sort starts here--//
    //Heap sort is used for sorting our edges in kruskal's algorithm
    public static void BuildHeap(int n, int i){
        int l = 2*i + 1;
        int r = 2*i + 2;
        int largest;
 
        //find largest of i and l
        if (l < n && Globals.allEdges.get(l).weight > Globals.allEdges.get(i).weight){
            largest = l;
        }else{
            largest = i;
        }
 
        //if right child is larger
        if (r < n && Globals.allEdges.get(r).weight > Globals.allEdges.get(largest).weight){
            largest = r;
        }
 
        //if largest is not root
        if (largest != i){
            Edge a = Globals.allEdges.get(largest);
            Globals.allEdges.set(largest, Globals.allEdges.get(i));
            Globals.allEdges.set(i, a);
 
            //recursively heapify the affected sub-tree
            BuildHeap(n, largest);
        }
    }
 
    public static void HeapSort(int n){
        //populate heap
        for (int i = n/2 -1; i >= 0; i--){
            BuildHeap(n, i);
        }
 
        //pull from top of heap into array
        //this does our sorting
        for (int i = n-1; i >= 0; i--){
            //move current root to the end
            Edge a = Globals.allEdges.get(0);
            Globals.allEdges.set(0, Globals.allEdges.get(i));
            Globals.allEdges.set(i, a);
 
            //call max heapify on the reduced heap
            BuildHeap(i, 0);
        }
    }
    //--Heap sort ends here--//
   //---------------------------------------------------------------------------------------------
    //Kruskal's algorithm
    public static void kruskal() throws FileNotFoundException{
        //populate the all edges array
        for (int i = 0; i < Globals.graph.size(); i++) {
            Globals.allEdges.addAll(Globals.graph.get(i).edges);
        }
 
        //sort edges by weight
        HeapSort(Globals.allEdges.size());
 
        //attempt adding edges to forest
        for (int i = 0; i < Globals.allEdges.size(); i++) {
            Edge x = Globals.allEdges.get(i);
            Vertex u = Globals.graph.get(x.from-1);
            Vertex v = Globals.graph.get(x.to-1);
 
            //find the tree of U and V
            while(u.parent > 0){
                u = Globals.graph.get(u.parent-1);
            }
 
            while(v.parent > 0){
                v = Globals.graph.get(v.parent-1);
            }
 
            //only add edge if u and v are on seperate trees
            if (u.vertexNum != v.vertexNum){
                //include the edge
                Globals.forest.add(x);
                if (u.depth > v.depth){
                    //the tree of u is bigger so add v to u
                    v.parent = u.vertexNum;
                    u.depth = v.depth + u.depth;
                }else{
                    //the tree of v is bigger so add u to v
                    u.parent = v.vertexNum;
                    v.depth = v.depth + u.depth;
                }
            }
        }
           //create an print writer for writing to a file
        PrintStream kruskalout = new PrintStream(new FileOutputStream("kruskalout.txt"));
           System.setOut(kruskalout);
        //print our edges
        System.out.println("Kruskal's Sorted MST tree:");
        
        for(int i=0; i<Globals.forest.size(); i++)
        {
            System.out.println("Edge from: " + Globals.forest.get(i).from + " to " + Globals.forest.get(i).to);
            
        }
    }
   //--------------------------------------------------------------------------------------------- 
    //Prim's algorithm
    public static void prim() throws FileNotFoundException{
        MST src = new MST();
        MST res = new MST();
 
        //initialize MST's
        for (int i = 0; i < Globals.graph.size(); i++){
            Node u = new Node();
            u.vertexNum = Globals.graph.get(i).vertexNum;
            src.Insert(u);
        }
        //make arbitrary root
        src.DecreaseKey(0,0,-1);
           //create an print writer for writing to a file
        PrintStream primout = new PrintStream(new FileOutputStream("primout.txt"));
        System.setOut(primout);
        System.out.println("Prim's Sorted MST tree:");
        
        //attempt adding nodes
        while(src.tree.size() > 0){
            //find minimum
            Node u = src.ExtractMin();
            res.Insert(u);
            Vertex v = Globals.graph.get(u.vertexNum-1);
            //display new included edge
            if (u.parent > 0){
                System.out.println("Edge from: " + u.parent + " to " + u.vertexNum);
            }
            //decrease key on all connected edges
            for (int i = 0; i < v.edges.size(); i++) {
                //find vertex edge goes to
                Vertex w = Globals.graph.get(v.edges.get(i).to-1);
                //if qPos says we are still in src tree
                if (w.qPos > 0 && w.qPos < src.tree.size()){
                    src.DecreaseKey(w.qPos, v.edges.get(i).weight, u.vertexNum);
                    
                }
            }
        }
    }
}
