package com.pay200.jgit.node;

import java.util.Scanner;

/** 
* @author yupeng
* @version 创建时间：2018年10月19日 下午1:58:57 
* 类说明 
*/
public class HashSearch {

	
	public static int data[] = {69,65,90,37,92,6,28,54};
    public static int hash[] = new int[25];
     
     
    //将关键字插入到散列表中
    public static void insertHash(int hash[],int m,int data){
        int i = 0;
        i = data%25;//计算散列位置
        while (hash[i] >0) {//位置已经被占用
            i = (++i)%m;//先行探索解决冲突
        }
        hash[i] = data;
    }
 
    //创建散列表
    public static void createHash(int hash[],int m,int data[],int n){
        for (int i = 0;i<hash.length;i++) {
            hash[i] = 0;
        }
        for (int i = 0;i<n;i++) {
            insertHash(hash, m, data[i]);
        }
         
    }
 
    //散列表的查找函数的编写
    public static int hashSearch(int [] hash,int m,int key){
        int i = 0;
        i = key%25;
        while (hash[i] > 0 && hash[i] != key) {//判断是不是冲突
            i = (++i)%m;
        }
        if (hash[i] == 0) {
            return -1;
        }else{
            return i;
        }
     
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        //调用函数创建散列表
        createHash(hash, 25, data, 8);
        System.out.println("散列表各元素的值：");
        for(int i = 0;i<25;i++){
            System.out.print(hash[i]+" ");
        }
        System.out.println();
        System.out.println("输入查找的关键字");
        int key = input.nextInt();
        int pos = hashSearch(hash, 25, key);
        if (pos >=0) {
            System.out.printf("查找成功，该关键字位于数组的第%d个位置\n",pos+1);
        }else{
            System.out.println("查找失败！"+pos);
        }
    }
 
}
