package com.pay200.jgit;
/** 
* @author yupeng
* @version 创建时间：2018年10月18日 下午7:37:21 
* 类说明 
*/
public class BinaryTree {
	private Node root;  
    
    /** 
     *  
     * 内部节点类 
     */  
    private class Node{  
        private Node left;  
        private Node right;  
        private int data;  
        public Node(int data){  
            this.left = null;  
            this.right = null;  
            this.data = data;  
        }  
    }  
      
    public BinaryTree(){  
        root = null;  
    }  
      
    /** 
     * 递归创建二叉树 
     * @param node 
     * @param data 
     */  
    public void buildTree(Node node,int data){
        if(root == null){  
            root = new Node(data);  
        }else{
        	//System.out.println(data+":"+node.data);
            if(data < node.data){  
                if(node.left == null){  
                    node.left = new Node(data);  
                }else{
                	//System.out.println("左");
                    buildTree(node.left,data);  
                }  
            }else{  
                if(node.right == null){  
                    node.right = new Node(data);  
                }else{  
                	//System.out.println("右");
                    buildTree(node.right,data);  
                }  
            }  
        }  
    }  
      
    /** 
     * 前序遍历 
     * @param node 
     */  
    public void preOrder(Node node){
    	System.out.println("前序遍历");
        if(node != null){  
            System.out.println(node.data);  
            preOrder(node.left);  
            preOrder(node.right);  
        }  
    }  
      
    /** 
     * 中序遍历 
     * @param node 
     */  
    public void inOrder(Node node){
    	System.out.println("中序遍历");
        if(node != null){  
            inOrder(node.left);  
            System.out.println(node.data);  
            inOrder(node.right);  
        }  
    }  
      
    /** 
     * 后序遍历 
     * @param node 
     */  
    public void postOrder(Node node){  
    	System.out.println("后序遍历 ");
        if(node != null){  
            postOrder(node.left);  
            postOrder(node.right);  
            System.out.println(node.data);  
        }  
    }  
      
    public static void main(String[] args) {  
        int[] a = {2,4,12,45,21,6,111};  
        BinaryTree bTree = new BinaryTree();  
        for (int i = 0; i < a.length; i++) {  
            bTree.buildTree(bTree.root, a[i]);  
        }  
        //2 4 45 21 12 6 111 
         bTree.preOrder(bTree.root);  
        //bTree.inOrder(bTree.root);  
       // bTree.postOrder(bTree.root);  
    }  
}
