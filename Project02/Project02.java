 /* Name: Kelsey Nguyen
 * Class: CS340
 * Description: User select either binary search tree or red black tree and file size which is 30k,60k,120k, or 
    150k and file type which is permuted or sorted. Then plot all four on 5 sorted and 5 permuted lists on excel.
 * Due Date: Sep 19, 2017

 */

package project02;
import java.util.Scanner;
import java.io.*;
/**
 *
 * @author tnguyen
 */
public class Project02 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        long startTime;
        long endTime;
        long duration;
        
        
        System.out.println("Which type of tree do you want to use?");
        System.out.println("b) binary tree");
        System.out.println("r) red-black tree");
        Scanner sc = new Scanner(System.in);
        char pick = sc.nextLine().charAt(0);
        
        
        GeneralTree tree = null;
        if(pick == 'b')
        {
            tree = new BinaryTree();
        }
        else if(pick == 'r')
        {
            tree = new RedBlackTree();
        }
        else
        {
                System.out.println("You have to choose b or r.  Please try again");
        }
            
        System.out.println("What input file do you want to use? ");
        System.out.println(   "perm30K.txt , perm90K.txt, perm120K.txt, perm150K.txt");
        System.out.println( "sorted30K.txt, sorted90K.txt, sorted120K.txt or sorted150K.txt ");
        System.out.println("");
        String inputFile = sc.nextLine();
        System.out.println("");
        System.out.println("Which word are you searching for?");
        System.out.println("");
        String target = sc.nextLine();
        
        try
        {
            System.out.println("Building library...");
            Scanner readFile = new Scanner(new File(inputFile));
            startTime = System.nanoTime();
            while(readFile.hasNext())
            {
                String read = readFile.nextLine();
               
                tree.insert(read);
            }
            endTime = System.nanoTime();
            duration = (endTime-startTime);
            System.out.println("Took " + duration + " ns");
            System.out.println("Done");
        }
        catch(Exception x)
        {
            System.out.println("Didn't work");
        }
        
        System.out.println("Searching for word...");
        startTime = System.nanoTime();
        boolean found = tree.contains(target);
        endTime = System.nanoTime();
        duration = (endTime-startTime);
        if(tree.contains(target))
        {
            System.out.println("Found");
        }
        else
        {
            System.out.println("Not Found");
        }
        System.out.println("Took " + duration + " ns");
        
        
    }  
}
