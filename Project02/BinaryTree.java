 /* Name: Kelsey Nguyen
 * Class: CS340
 * Description: binary search tree
 * Due Date: Sep 19, 2017

 */
package project02;

/**
 *
 * @author tnguyen
 */



public class BinaryTree implements GeneralTree {
//-------------------------------------------------------------------------------------------     
    private class BinaryTreeNode
    {
        public String data;
        public BinaryTreeNode left;
        public BinaryTreeNode right;
        BinaryTreeNode(String data)
               

        {
            this.data = data;
        }
    }
 //-------------------------------------------------------------------------------------------   
    BinaryTreeNode head;
    
    public BinaryTree()
    {
        head = null;
    }
//-------------------------------------------------------------------------------------------  
    //If it's less than go to lef if it's greater than go to right
    @Override
    public void insert(String word) {
        if(head == null)
        {
            head = new BinaryTreeNode(word);
        }
        else
        {
            BinaryTreeNode runner = head;
            while(true)
            {
                if(word.compareTo(runner.data) < 0)
                {
                    if(runner.left == null)
                    {
                        runner.left = new BinaryTreeNode(word);
                        return;
                    }
                    else
                    {
                        runner = runner.left;
                    }
                }
                else if(word.compareTo(runner.data) > 0)
                {
                    if(runner.right == null)
                    {
                        runner.right = new BinaryTreeNode(word);
                        return;
                    }
                    else
                    {
                        runner = runner.right;
                    }
                }
                else
                {
                    System.out.println("WORD " + word + " ALREADY IN TREE, IGNORING");
                    return;
                }
            }
        }
    }

    
 //-------------------------------------------------------------------------------------------  
   //search for the word
    @Override
    public boolean contains(String word) {
        BinaryTreeNode runner = head;
        while(runner != null)
        {
                if(word.compareTo(runner.data) < 0)
                {
                    runner = runner.left;
                }
                else if(word.compareTo(runner.data) > 0)
                {
                    runner = runner.right;
                }
                else
                {
                    return true;
                }
        }
        return false;
    }
    
}
