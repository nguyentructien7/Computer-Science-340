 /* Name: Kelsey Nguyen
 * Class: CS340
 * Description: Implemented the matrix based dynamic to solve the Knapsack problem.
 * Due Date: Nov 28, 2017
 */
package project07;

/**
 *
 * @author tnguyen
 */

import java.io.*;
import java.util.*;
import java.lang.*; 

public class Project07 {

    /**
     * @param args the command line arguments
     */


   
    //--start of constants--//
    static final String inputFile = "knapsack.txt";
    //--end of constants--//
    //--start of classes--//
        private static class Globals{
            public static int[][] graph;
            public static int[][] path;
            public static int[] weights;
            public static int[] values;
        }
    //--end of classes--//
//--------------------------------------------------------------------------------------------        
    //main driver function
        public static void main(String[] args) throws IOException{
            //read the input file
            InputFile();
 
            //run the knapsack algorithm
            Knapsack(Globals.graph.length - 1, (Globals.graph[Globals.graph.length - 1]).length - 1);
 
            //finally display output
            Output();
        }
 
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
                        Globals.weights = new int[Integer.parseInt(line[0])];
                        Globals.values = new int[Integer.parseInt(line[0])];
                        Globals.graph = new int[Integer.parseInt(line[0]) + 1][Integer.parseInt(line[1]) + 1];
                        Globals.path = new int[Integer.parseInt(line[0]) + 1][Integer.parseInt(line[1]) + 1];
                        i++;
                    }else{
                        Globals.weights[i-1] = Integer.parseInt(line[0]);
                        Globals.values[i-1] = Integer.parseInt(line[1]);
                        i++;
                    }
                }
            }
            return;
        }
//-------------------------------------------------------------------------------------------- 
        //displays output for knapsack problem
        public static void Output(){
            int i, j;
            i = Globals.graph.length - 1;
            j = (Globals.graph[Globals.graph.length - 1]).length - 1;
 
            System.out.printf("\nKnapsack score is: %d\n", Globals.graph[i][j]);
 
            while(i > 0 && j > 0){
                if (Globals.path[i][j] == 1){
                    //added item
                    System.out.printf("Score %d, Used item #%d\n", Globals.graph[i][j], i);
                    j-= Globals.weights[i-1];
                    i--;
                }else{
                    //skipped item
                    i--;
                }
            }
            return;
        }
    //--Helper functions end--//
//-------------------------------------------------------------------------------------------- 
    //--knapsack start--//
        //recursive function to find score in any given cell
        public static int Knapsack(int i, int w) {
            if (i < 0 || i >= Globals.graph.length || w < 0 || w >= (Globals.graph[Globals.graph.length - 1]).length){
                //invalid index... return zero
                return 0;
            }
            //switch based on cases
            if (i == 0 || w == 0){
                //we are along the 0 index edge
                Globals.graph[i][w] = 0;
                Globals.path[i][w] = 0;
                return 0;
            }else{
                int add = Globals.values[i-1] + Knapsack(i-1, w-Globals.weights[i-1]);
                int skip = Knapsack(i-1, w);
                if (w >= Globals.weights[i-1] && add >= skip){
                    //added item
                    Globals.graph[i][w] = add;
                    Globals.path[i][w] = 1;
                    return add;
                }else{
                    //skipped item
                    Globals.graph[i][w] = skip;
                    Globals.path[i][w] = 2;
                    return skip;
                }
            }
        }
    //--knapsack end--//
}