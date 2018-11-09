package com.pay200.jgit;

import java.util.ArrayList;
import java.util.List;

/** 
 * 
 * 
 * 
* @author yupeng
* @version 创建时间：2018年10月18日 下午8:09:39 
* 类说明 
*/
public class BinTree {
	///
	private BinTree lChild;//左孩子
	private BinTree rChild;//右孩子
	private BinTree root;//根节点
	private Object data; //数据域
	private List<BinTree> datas;//存储所有的节点
	public BinTree(BinTree lChild, BinTree rChild, Object data) {
		super();
		this.lChild = lChild;
		this.rChild = rChild;
		this.data = data;
	}
	public BinTree(Object data) {
		this(null, null, data);
	}
	public BinTree() {
		super();
	}
	
	public void createTree(Object[] objs){
		datas=new ArrayList<BinTree>();
		for (Object object : objs) {
			datas.add(new BinTree(object));
		}
		root=datas.get(0);//将第一个作为根节点
		for (int i = 0; i < objs.length/2; i++) {
			datas.get(i).lChild=datas.get(i*2+1);
			if(i*2+2<datas.size()){//避免偶数的时候 下标越界
				datas.get(i).rChild=datas.get(i*2+2);
			}
		}
	}
	//先序遍历
	public void preorder(BinTree root){
		if(root!=null){
			visit(root.getData());
			preorder(root.lChild);
			preorder(root.rChild);
		}
		
	}
	//中序遍历
	public void inorder(BinTree root){
		if(root!=null){
			inorder(root.lChild);
			visit(root.getData());
			inorder(root.rChild);
		}
		
	}
	//后序遍历
	public void afterorder(BinTree root){
		if(root!=null){
			afterorder(root.lChild);
			afterorder(root.rChild);
			visit(root.getData());
		}
		
	}
	private void visit(Object obj) {
		System.out.print(obj+" ");
	}
	public Object getData() {
		return data;
	}
	public BinTree getRoot() {
		return root;
	}
  public static void main(String[] args) {
	  BinTree binTree=new BinTree();
		Object[] objs={2,4,12,45,21,6,111};
		binTree.createTree(objs);
		binTree.preorder(binTree.getRoot()); //先序遍历
		//binTree.inorder(binTree.getRoot()); //中序遍历
		//	binTree.afterorder(binTree.getRoot()); //后序遍历
}
}
