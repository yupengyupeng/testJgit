package com.pay200.jgit;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.pay200.jgit.porcelain.AddFile;
import com.pay200.jgit.porcelain.CommitAll;
import com.pay200.jgit.porcelain.CommitFile;
import com.pay200.jgit.porcelain.CreateAndDeleteBranch;
import com.pay200.jgit.porcelain.ListBranches;
import com.pay200.jgit.porcelain.ListTags;
import com.pay200.jgit.porcelain.ListUncommittedChanges;
import com.pay200.jgit.porcelain.ShowBranchDiff;
import com.pay200.jgit.porcelain.ShowLog;
import com.pay200.jgit.porcelain.WalkAllCommits;

/** 
* @author yupeng
* @version 创建时间：2018年10月25日 下午6:25:10 
* 类说明 
*/
public class GitTest {
	
	public static void main(String[] args) throws IOException, GitAPIException {
		//将新文件提交到索引
		System.out.println("将新文件提交到索引");
		AddFile.addFile();
		
		//将文件提交到现有存储库
		System.out.println("将文件提交到现有存储库");
		CommitFile.commitFile();
		
		//提交所有更改
		System.out.println("提交所有更改");
		CommitAll.commitAll();
		
		//列表提交，即日志
		System.out.println("列表提交，即日志");
		ShowLog.showLog();
		
		//列出存储库中所有的标记
		System.out.println("列出存储库中所有的标记");
		ListTags.ListTags();
		
		//列出存储库中所有的分支
		System.out.println("列出存储库中所有的分支");
		ListBranches.listBranches();
		
		//列出存储库中所有的提交
		System.out.println("列出存储库中所有的提交");
		WalkAllCommits.walkAllCommits();
		
		//列出存储库中未提交更改
		System.out.println("列出存储库中未提交更改");
		ListUncommittedChanges.listUncommittedChanges();
		
		//创建和删除分支
		System.out.println("创建和删除分支");
		CreateAndDeleteBranch.createAndDeleteBranch();
		
		//返回两个分支之间的差异
		System.out.println("返回两个分支之间的差异");
		ShowBranchDiff.showBranchDiff();
	}
	

}
