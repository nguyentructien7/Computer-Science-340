 /* Name: Kelsey Nguyen
 * Class: CS340
 * Description: Implemented a branch and bound search tree to the knapsack problem because on 
    upper bound function.
 * Due Date: Dec 7, 2017
 */
package project08;

/**
 *
 * @author tnguyen
 */

import java.io.*;
import java.util.*;
import java.lang.*;
 
public class Project08 {

    /**
     * @param args the command line arguments
     */
 
    
    //--start of constants--//
    static final String inputFile = "knapsack.txt";
    //--end of constants--//
    
   //--------------------------------------------------------------------------------------------         
   
  //--start of classes--//
        //Tree node
            public static class Node{
                private float upper;
                private int weight;
                private int value;
                private Node left;
                private Node right;
                private Node parent;
 
                public Node(){
                    this.upper = 0;
                    this.value = 0;
                    this.weight = 0;
                    this.left = null;
                    this.right = null;
                    this.parent = null;
                }
 
                public Node(float up, int val, int we){
                    this();
                    this.upper = up;
                    this.value = val;
                    this.weight = we;
                }
 
                public void setWeight(int w){
                    this.weight = w;
                }
                public int getWeight(){
                    return this.weight;
                }
 
                public void setValue(int v){
                    this.value = v;
                }
                public int getValue(){
                    return this.value;
                }
 
                public void setUpper(float up){
                    this.upper = up;
                }
                public float getUpper(){
                    return this.upper;
                }
 
                public void setLeft(Node l){
                    this.left = l;
                }
                public Node getLeft(){
                    return this.left;
                }
                public void setRight(Node r){
                    this.right = r;
                }
                public Node getRight(){
                    return this.right;
                }
 
                public void setParent(Node p){
                    this.parent = p;
                }
                public Node getParent(){
                    return this.parent;
                }
            }
        //Items
            public static class Item{
                private int weight;
                private int value;
                private int index;
 
                public Item(){
                    this.weight = 0;
                    this.value = 0;
                    this.index = -1;
                }
                public Item(int w, int v, int i){
                    this();
                    this.weight = w;
                    this.value = v;
                    this.index = i;
                }
 
                public void setWeight(int w){
                    this.weight = w;
                }
                public int getWeight(){
                    return this.weight;
                }
 
                public void setValue(int v){
                    this.value = v;
                }
                public int getValue(){
                    return this.value;
                }
 
                public void setIndex(int i){
                    this.index = i;
                }
                public int getIndex(){
                    return this.index;
                }
 
                public float getRatio(){
                    return (this.value/(float)this.weight);
                }
            }
        //Globals
            private static class Globals{
                public static ArrayList<Node> possibles = new ArrayList<Node>();
                public static Item[] items;
                public static int maxW;//max weight
                public static int maxI;//how many items
            }
    //--end of classes--//
            
//--------------------------------------------------------------------------------------------                    
    
//main driver function
        public static void main(String[] args) throws IOException{
            //read the input file
            InputFile();
 
            //sort the items by ratio
            SortItems();
 
            //run the knapsack algorithm
            Node root = new Node(Globals.items[0].getRatio() * Globals.maxW, 0, 0);
            Globals.possibles.add(root);
            Node result = Knapsack(root);
 
            //finally display output
            Output(result);
        }
//--------------------------------------------------------------------------------------------         
    //--Helper functions start--//
        //takes file input
            public static void InputFile() throws IOException {            
                //read the input file
                Scanner reader = new Scanner(new File(inputFile));
                int i = 0;
                while(reader.hasNext()){
                    //get the next line & split by the ' '.  This separates the value from the weight
                    String[] line = reader.nextLine().split(" ");
                    if(line.length > 1) //checking if there are any entries at all
                    {
                        if (i == 0){
                            Globals.maxI = Integer.parseInt(line[0]);
                            Globals.items = new Item[Integer.parseInt(line[0])];
                            Globals.maxW = Integer.parseInt(line[1]);
                            i++;
                        }else{
                            Globals.items[i-1] = new Item(Integer.parseInt(line[0]), Integer.parseInt(line[1]), i);
                            i++;
                        }
                    }
                }
                return;
            }
//--------------------------------------------------------------------------------------------         
        //Sorts the items (uses selection sort)
            public static void SortItems(){
                //steo through whole list
                for (int i = 0; i < Globals.maxI-1; i++)
                {
                    //find maximum
                    int max = i;
                    for (int j = i+1; j < Globals.maxI; j++)
                        if (Globals.items[j].getRatio() > Globals.items[max].getRatio()){
                            max = j;
                        }
             
                    //Switch max and ith index
                    Item temp = Globals.items[i];
                    Globals.items[i] = Globals.items[max];
                    Globals.items[max] = temp;
                }
            }
//--------------------------------------------------------------------------------------------        l 
        //displays output for knapsack problem
            public static void Output(Node result){
                //backtracing to get what items were used
                Node cur = result;
                Node par = cur.getParent();
                int i = Globals.maxI;
 
                System.out.printf("\nKnapsack final score is: %d\n", result.getValue());
                while(par != null){
                    if (par.getLeft() == cur){
                        //used an item
                        System.out.printf("Score %d, Used item #%d\n", cur.getValue(), Globals.items[i-1].getIndex());
                    }else{
                        //skipped an item
                        System.out.printf("Score %d, skipped item #%d\n", cur.getValue(), Globals.items[i-1].getIndex());
                    }
                    i--;
                    cur = par;
                    par = par.getParent();
                }
                return;
            }
    //--Helper functions end--//
//--------------------------------------------------------------------------------------------         
    //--knapsack start--//
        //recursive function to evaluate most promising node
        public static Node Knapsack(Node fmpn) {
            //fmpn = first most promising node
 
            //find how deep in the tree we are
            //does "Backtracking"
            Node trace = fmpn;
            int j = -1;
            while(trace != null){
                trace = trace.getParent();
                j++;
            }
            if (j >= Globals.maxI){return fmpn;};
 
            //spawn left and right node
            //Calculation of the Upper Bound is done for both left and right in this section
            int val = fmpn.getValue();
            int we = fmpn.getWeight();
            float up = val;
            if (j+1 < Globals.maxI){
                up = val + Globals.items[j+1].getRatio() * (Globals.maxW - we);
            }
 
            Node right = new Node(up, val, we);
            right.setParent(fmpn);
            fmpn.setRight(right);
 
            val = fmpn.getValue() + Globals.items[j].getValue();
            we = fmpn.getWeight() + Globals.items[j].getWeight();
            up = val;
            if (j+1 < Globals.maxI){
                up = val + Globals.items[j+1].getRatio() * (Globals.maxW - we);
            }
 
            Node left = new Node(up, val, we);
            left.setParent(fmpn);
            fmpn.setLeft(left);
 
            //attempts adding leafs to list of possible nodes
            //pruning is done here by skipping adding any invalid nodes
            if (right.getWeight() <= Globals.maxW){
                Globals.possibles.add(right);
            }
            if (left.getWeight() <= Globals.maxW){
                Globals.possibles.add(left);
            }
            //remove current node
            Globals.possibles.remove(Globals.possibles.indexOf(fmpn));
 
            //find next most promising node
            //Best-first visitations is probably this?
            Node max = Globals.possibles.get(0);
            for (int k = 0; k < Globals.possibles.size(); k++) {
                Node test = Globals.possibles.get(k);
                //check if test is greater than max.  value is used as a tie breaker
                if (test.getUpper() > max.getUpper() || (test.getUpper() == max.getUpper() && test.getValue() > max.getValue())){
                    max = test;//new max
                }
            }
 
            //recursive call
            return Knapsack(max);
        }
    //--knapsack end--//
}
