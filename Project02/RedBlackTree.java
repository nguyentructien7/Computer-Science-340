/* Name: Kelsey Nguyen
 * Class: CS340
 * Description: red black tree
 * Due Date: Sep 19, 2017

 */
package project02;

/**
 *
 * @author tnguyen
 */
public class RedBlackTree implements GeneralTree {

    private class RedBlackTreeNode {

        String data;
        public RedBlackTreeNode left;
        public RedBlackTreeNode right;
        public RedBlackTreeNode parent;
        public boolean isBlack;

        RedBlackTreeNode(String data, RedBlackTreeNode parent) {
            this.data = data;
            this.parent = parent;
        }

        RedBlackTreeNode getSibling() {
            if (parent == null) {
                return null;
            }
            if (parent.left == this) {
                return parent.right;
            } else //parent right is this
            {
                return parent.left;
            }
        }

        RedBlackTreeNode getUncle() {
            if (parent == null) {
                return null;
            } else {
                return parent.getSibling();
            }
        }

        boolean getBlack() {
            return isBlack;
        }

        void setBlack(boolean isBlack) {
            this.isBlack = isBlack;
        }
    }

    //-------------------------------------------------------------------------------------------  
    RedBlackTreeNode head;

    public RedBlackTree() {
        head = null;
    }

    //-------------------------------------------------------------------------------------------  
    @Override
    public void insert(String word) {
        //make root black
        if (head == null) {
            head = new RedBlackTreeNode(word, null);
            head.isBlack = true;
        } else {
            RedBlackTreeNode runner = head;
            while (true) {   //check to insert
                if (word.compareTo(runner.data) < 0) {
                    if (runner.left == null) {   //insert red node
                        runner.left = new RedBlackTreeNode(word, runner);
                        runner.left.isBlack = false;

                        RedBlackTreeNode checkMe = runner.left;
                        //-------------------------------------------------------------------------------------------                       
                        //red parent
                        if (checkMe.parent != null && checkMe.parent.isBlack == false) {
                            //uncle black or null
                            if (checkMe.getUncle() == null || checkMe.getUncle().isBlack) {
                                if (checkMe == checkMe.parent.left && checkMe.parent == checkMe.parent.parent.left) {
                                    //check me is parents left, & parent is it's parent's left
                                    //rotate right
                                    RedBlackTreeNode A = checkMe;//z
                                    RedBlackTreeNode B = checkMe.parent;//parent
                                    RedBlackTreeNode C = B.parent;//grandparent
                                    RedBlackTreeNode origin = C.parent;//root
                                    RedBlackTreeNode T1 = A.left;
                                    RedBlackTreeNode T2 = A.right;
                                    RedBlackTreeNode T3 = B.right;
                                    RedBlackTreeNode T4 = C.right;

                                    if (origin.left == C) {
                                        origin.left = B;
                                    } else {
                                        origin.right = B;
                                    }
                                    B.left = A;
                                    B.right = C;
                                    A.left = T1;
                                    A.right = T2;
                                    C.left = T3;
                                    C.right = T4;
                                    C.isBlack = false;
                                    B.isBlack = true;
                                } else if (checkMe == checkMe.parent.right && checkMe.parent == checkMe.parent.parent.right) {
                                    //check me is parents right, & parent is it's parent's right
                                    //rotate left
                                    RedBlackTreeNode A = checkMe;
                                    RedBlackTreeNode B = checkMe.parent;
                                    RedBlackTreeNode C = B.parent;
                                    RedBlackTreeNode origin = C.parent;
                                    RedBlackTreeNode T1 = A.right;
                                    RedBlackTreeNode T2 = A.left;
                                    RedBlackTreeNode T3 = B.left;
                                    RedBlackTreeNode T4 = C.left;

                                    if (origin.left == C) {
                                        origin.left = B;
                                    } else {
                                        origin.right = B;
                                    }
                                    B.left = C;
                                    B.right = A;
                                    A.right = T1;
                                    A.left = T2;
                                    C.left = T4;
                                    C.right = T3;
                                    C.isBlack = false;
                                    B.isBlack = true;
                                } else if (checkMe == checkMe.parent.right && checkMe.parent == checkMe.parent.parent.left) {
                                    //check me is it's parents right, & it's parent is it's parent's left
                                    //rotate left-right
                                    RedBlackTreeNode B = checkMe;
                                    RedBlackTreeNode A = checkMe.parent;
                                    RedBlackTreeNode C = B.parent;
                                    RedBlackTreeNode origin = C.parent;
                                    RedBlackTreeNode T1 = A.left;
                                    RedBlackTreeNode T2 = B.left;
                                    RedBlackTreeNode T3 = B.right;
                                    RedBlackTreeNode T4 = C.right;

                                    if (origin.left == C) {
                                        origin.left = B;
                                    } else {
                                        origin.right = B;
                                    }
                                    B.left = A;
                                    B.right = C;
                                    A.left = T1;
                                    A.right = T2;
                                    C.left = T3;
                                    C.right = T4;
                                    B.isBlack = true;
                                } else if (checkMe == checkMe.parent.left && checkMe.parent == checkMe.parent.parent.right) {
                                    //check me is it's parents left, & it's parent is it's parent's right
                                    //rotate right-left
                                    RedBlackTreeNode B = checkMe;
                                    RedBlackTreeNode A = checkMe.parent;
                                    RedBlackTreeNode C = B.parent;
                                    RedBlackTreeNode origin = C.parent;
                                    RedBlackTreeNode T1 = A.right;
                                    RedBlackTreeNode T2 = B.right;
                                    RedBlackTreeNode T3 = B.left;
                                    RedBlackTreeNode T4 = C.left;

                                    if (origin.left == C) {
                                        origin.left = B;
                                    } else {
                                        origin.right = B;
                                    }
                                    B.right = A;
                                    B.left = C;
                                    A.right = T1;
                                    A.left = T2;
                                    C.right = T3;
                                    C.left = T4;
                                    B.isBlack = true;
                                }
                            }
                        }
                        return;
                    } else {
                        runner = runner.left;
                    }
                } //-------------------------------------------------------------------------------------------  
                //check to insert
                else if (word.compareTo(runner.data) > 0) {
                    if (runner.right == null) {   //insert a red node
                        runner.right = new RedBlackTreeNode(word, runner);
                        runner.right.isBlack = false;

                        RedBlackTreeNode checkMe = runner.left;
                        if (checkMe == null) {
                            break;
                        }

                        //red parent
                        if (checkMe.parent != null && checkMe.parent.isBlack == false) {
                            //uncle black or null
                            if (checkMe.getUncle() == null || checkMe.getUncle().isBlack) {
                                if (checkMe == checkMe.parent.left && checkMe.parent == checkMe.parent.parent.left) {
                                    //check me is parents left, & parent is it's parent's left
                                    //rotate right
                                    RedBlackTreeNode A = checkMe;
                                    RedBlackTreeNode B = checkMe.parent;
                                    RedBlackTreeNode C = B.parent;
                                    RedBlackTreeNode origin = C.parent;
                                    RedBlackTreeNode T1 = A.left;
                                    RedBlackTreeNode T2 = A.right;
                                    RedBlackTreeNode T3 = B.right;
                                    RedBlackTreeNode T4 = C.right;

                                    if (origin.left == C) {
                                        origin.left = B;
                                    } else {
                                        origin.right = B;
                                    }
                                    B.left = A;
                                    B.right = C;
                                    A.left = T1;
                                    A.right = T2;
                                    C.left = T3;
                                    C.right = T4;
                                    C.isBlack = false;
                                    B.isBlack = true;
                                } else if (checkMe == checkMe.parent.right && checkMe.parent == checkMe.parent.parent.right) {
                                    //check me is parents right, & parent is it's parent's right
                                    //rotate left
                                    RedBlackTreeNode A = checkMe;
                                    RedBlackTreeNode B = checkMe.parent;
                                    RedBlackTreeNode C = B.parent;
                                    RedBlackTreeNode origin = C.parent;
                                    RedBlackTreeNode T1 = A.right;
                                    RedBlackTreeNode T2 = A.left;
                                    RedBlackTreeNode T3 = B.left;
                                    RedBlackTreeNode T4 = C.left;

                                    if (origin.left == C) {
                                        origin.left = B;
                                    } else {
                                        origin.right = B;
                                    }
                                    B.left = C;
                                    B.right = A;
                                    A.right = T1;
                                    A.left = T2;
                                    C.left = T4;
                                    C.right = T3;
                                    C.isBlack = false;
                                    B.isBlack = true;
                                } else if (checkMe == checkMe.parent.right && checkMe.parent == checkMe.parent.parent.left) {
                                    //check me is it's parents right, & it's parent is it's parent's left
                                    //rotate left-right
                                    RedBlackTreeNode B = checkMe;
                                    RedBlackTreeNode A = checkMe.parent;
                                    RedBlackTreeNode C = B.parent;
                                    RedBlackTreeNode origin = C.parent;
                                    RedBlackTreeNode T1 = A.left;
                                    RedBlackTreeNode T2 = B.left;
                                    RedBlackTreeNode T3 = B.right;
                                    RedBlackTreeNode T4 = C.right;

                                    if (origin.left == C) {
                                        origin.left = B;
                                    } else {
                                        origin.right = B;
                                    }
                                    B.left = A;
                                    B.right = C;
                                    A.left = T1;
                                    A.right = T2;
                                    C.left = T3;
                                    C.right = T4;
                                    B.isBlack = true;
                                } else if (checkMe == checkMe.parent.left && checkMe.parent == checkMe.parent.parent.right) {
                                    //check me is it's parents left, & it's parent is it's parent's right
                                    //rotate right-left
                                    RedBlackTreeNode B = checkMe;
                                    RedBlackTreeNode A = checkMe.parent;
                                    RedBlackTreeNode C = B.parent;
                                    RedBlackTreeNode origin = C.parent;
                                    RedBlackTreeNode T1 = A.right;
                                    RedBlackTreeNode T2 = B.right;
                                    RedBlackTreeNode T3 = B.left;
                                    RedBlackTreeNode T4 = C.left;

                                    if (origin.left == C) {
                                        origin.left = B;
                                    } else {
                                        origin.right = B;
                                    }
                                    B.right = A;
                                    B.left = C;
                                    A.right = T1;
                                    A.left = T2;
                                    C.right = T3;
                                    C.left = T4;
                                    B.isBlack = true;
                                }
                            }
                        }
                        return;
                    } else {
                        runner = runner.right;
                    }
                } else {
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
        RedBlackTreeNode runner = head;
        while (runner != null) {
            if (word.compareTo(runner.data) < 0) {
                runner = runner.left;
            } else if (word.compareTo(runner.data) > 0) {
                runner = runner.right;
            } else {
                return true;
            }
        }
        return false;
    }

}
