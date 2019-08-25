 /* Name: Kelsey Nguyen
 * Class: CS340
 * Description:  Implementing the dymaic programming based sequence aligment algorithm on the maximizations
of scores. The match core a, gap score b and two types of mismatch score such as vowels pairs and non.
 * Due Date: Nov 28, 2017
 */
package project06;

/**
 *
 * @author tnguyen
 */

import java.io.*;
import java.util.*;
import java.lang.*;

public class Project06 {

    /**
     * @param args the command line arguments
     */
   
   
    //--start of constants--//
        static final char[][] PAIRS = new char[][] {
            {'b','p'},
            {'c','k'},
            {'c','s'},
            {'d','t'},
            {'e','y'},
            {'g','j'},
            {'g','k'},
            {'i','y'},
            {'k','q'},
            {'m','n'},
            {'s','z'},
            {'v','w'}
        };
    //--end of constants--//
        
    //--start of classes--//
        private static class Globals{
            public static int[][] graph;
            public static int[][] path;
            public static String strone;
            public static String strtwo;
            public static int match;
            public static int gap;
            public static int subsim;
            public static int subdis;
        }
    //--end of classes--//
   //--------------------------------------------------------------------------------------------
    //main driver function
        public static void main(String[] args) throws IOException{
            //pick the problem and requisite inputs
            int problem = Input();
 
            //switch based on problem chosen
            if (problem == 1){
                LongestOpt(Globals.graph.length - 1, (Globals.graph[Globals.graph.length - 1]).length - 1);
            }else{
                Optimal();
            }
 
            //finally display output
            Output();
        }
//--------------------------------------------------------------------------------------------
    //--Helper functions start--//
        //takes user input for our problem and compairison strings
        public static int Input(){
            Scanner input = new Scanner(System.in);
            int problem;
            do{
                System.out.printf("\nSpecify the desired problem.  (1) Longest common subsequence or (2) Optimal Sequence Alignment.\n");
                problem = input.nextInt();
            }while(problem > 2 || problem < 1 );
 
            //take the two compairison strings
            System.out.printf("\nSpecify the first string to compair\n");
            Globals.strone = input.next();
 
            System.out.printf("\nSpecify the second string to compair\n");
            Globals.strtwo = input.next();
 
            Globals.graph = new int[Globals.strone.length()+1][Globals.strtwo.length()+1];
            Globals.path = new int[Globals.strone.length()+1][Globals.strtwo.length()+1];
 
            return problem;
        }
//-------------------------------------------------------------------------------------------- 
        //displays output for chosen problem
        public static void Output(){
            int i, j;
            i = Globals.graph.length - 1;
            j = (Globals.graph[Globals.graph.length - 1]).length - 1;
 
            System.out.printf("\nLongest Common Substring score is: %d\n", Globals.graph[i][j]);
 
            while(i > 0 && j > 0){
                if (Globals.path[i][j] == 1){
                    //match
                    System.out.printf("Score %d, Match or Substitution at: %d, %d.  Match: %s\n", Globals.graph[i][j], i, j, Globals.strone.charAt(i-1));
                    i--;
                    j--;
                }else{
                    if (Globals.path[i][j] == 2){
                        //deletion
                        System.out.printf("Score %d, Deletion at: %d, %d.  Deleting: %s\n", Globals.graph[i][j], i, j, Globals.strtwo.charAt(i-1));
                        i--;
                    }else{
                        //insertion
                        System.out.printf("Score %d, Insertion at: %d, %d.  Inserting: %s\n", Globals.graph[i][j], i, j, Globals.strone.charAt(i-1));
                        j--;
                    }
                }
            }
            return;
        }
    //--Helper functions end--//
//--------------------------------------------------------------------------------------------
    //--algorithms start--//
        //Longest common subsequence algorithm
        //recursive function to find optimal number in any given cell
            public static int LongestOpt(int i, int j) {
                if (i < 0 || i >= Globals.graph.length || j < 0 || j >= Globals.graph.length){
                    //invalid index... return zero
                    return 0;
                }
                //switch based on cases
                if (i == 0 || j == 0) {
                    //we are along the 0 index edge
                    Globals.graph[i][j] = 0;
                    Globals.path[i][j] = 0;
                    return 0;
                }else{
                    int match = 1 + LongestOpt(i-1, j-1);
                    int delete = LongestOpt(i-1, j);
                    int insert = LongestOpt(i, j-1);
                    if (Globals.strone.charAt(i-1) == Globals.strtwo.charAt(j-1) && (match > delete && match > insert)) {
                        //characters matched and match score is best
                        Globals.graph[i][j] = match;
                        Globals.path[i][j] = 1;
                        return match;
                    }else{
                        //characters do not match or match score did not win
                        if ( delete > insert ){
                            //delete score won
                            Globals.graph[i][j] = delete;
                            Globals.path[i][j] = 2;
                            return delete;
                        }else{
                            //insert score won
                            Globals.graph[i][j] = insert;
                            Globals.path[i][j] = 3;
                            return insert;
                        }
                    }
                }
            }
 
 
        //Optimal Sequence Alignment algorithm
            //entry function... finds the remaining needed inputs
            public static void Optimal(){
                Scanner input = new Scanner(System.in);
                //find the value for the following and continue to ask until condititions are filled.
                do{
                    System.out.printf("\nSpecify the match score\n");
                    Globals.match = input.nextInt();
 
                    System.out.printf("Specify the gap score\n");
                    Globals.gap = input.nextInt();
 
                    System.out.printf("Specify the substitution score for similar characters.\n");
                    Globals.subsim = input.nextInt();
 
                    System.out.printf("Specify the substitution score for dissimilar characters.\n");
                    Globals.subdis = input.nextInt();
                    if ( !(Globals.match > Globals.subsim && Globals.subsim > 0 && Globals.subdis < 0 && Globals.gap < 0) ){
                        System.out.printf("Conditions for variables were not met.\n");
                    }
                }while( !(Globals.match > Globals.subsim && Globals.subsim > 0 && Globals.subdis < 0 && Globals.gap < 0) );
 
                OptimalOpt(Globals.graph.length - 1, (Globals.graph[Globals.graph.length - 1]).length - 1);
 
                return;
            }
 
            //finds how similar the characters are
            public static int OptimalMatch(char a, char b){
                if (a == b){
                    return Globals.match;
                }else{
                    //search for matching pair in similar characters
                    boolean found = false;
                    for(int i = 0; i < PAIRS.length; i++){
                        if (PAIRS[i][0] == a && PAIRS[i][1] == b){
                        }
                    }
                    if (found){
                        return Globals.subsim;
                    }else{
                        return Globals.subdis;
                    }
                }
            }
//-------------------------------------------------------------------------------------------- 
            //recursive function to find optimal number in any given cell
            public static int OptimalOpt(int i, int j){
                if (i < 0 || i >= Globals.graph.length || j < 0 || j >= Globals.graph.length){
                    //invalid index... return zero
                    return 0;
                }
                //switch based on cases
                if (i == 0 && j == 0) {
                    //we are along the 0 index edge corner
                    Globals.graph[i][j] = 0;
                    Globals.path[i][j] = 0;
                    return 0;
                }else{
                    int match;
                    if (i > 0 && j > 0){
                        match = OptimalOpt(i-1, j-1) + OptimalMatch(Globals.strone.charAt(i-1), Globals.strtwo.charAt(j-1));
                    }else{
                        match = (i+j)*Globals.gap - 1;
                    }
                    int delete = OptimalOpt(i-1, j) + Globals.gap;
                    int insert = OptimalOpt(i, j-1) + Globals.gap;
                    if (match > delete && match > insert) {
                        //characters matched and match score is best
                        Globals.graph[i][j] = match;
                        Globals.path[i][j] = 1;
                        return match;
                    }else{
                        //characters do not match or match score did not win
                        if ( delete > insert ){
                            //delete score won
                            Globals.graph[i][j] = delete;
                            Globals.path[i][j] = 2;
                            return delete;
                        }else{
                            //insert score won
                            Globals.graph[i][j] = insert;
                            Globals.path[i][j] = 3;
                            return insert;
                        }
                    }
                }
            }
    //--algorithms end--//
}