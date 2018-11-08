package com.pay200.jgit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidMergeHeadsException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;

import com.pay200.jgit.helper.CookbookHelper;

/** 
* @author yupeng
* @version 创建时间：2018年10月26日 下午2:33:20 
* 类说明 
*/
public class JGitUtils {
	static Git git;
	static CredentialsProvider cp;
	static Repository repository;
	public static String remoteRepoURI = "https://gitee.com/yupengqaq/testJgit.git";
	static{
	         try {
	       		repository = CookbookHelper.openJGitCookbookRepository();
				git=new Git(repository);
				cp= new UsernamePasswordCredentialsProvider("yupengqaq", "qwer1234" );
	         } catch (Exception e) {
				e.printStackTrace();
			}
	  }
	/**
	 * 切换分支
	 */
    public static String checkoutBranch(String branchName){
        try {
            //检测dev分支是否已经存在 若不存在则新建分支
            List<Ref> localBranch = git.branchList().setListMode(ListMode.ALL).call();
            boolean isCreate = true;
            for (Ref branch : localBranch) {
            	System.out.println(branch.getName());
                if (branch.getName().endsWith(branchName)) {
                    isCreate = false;
                    break;
                }
            }
            if(isCreate) {
            	 System.out.println("分支不存在");
            	 return "分支不存在";
            }
            git.checkout().setName(branchName).call();
            System.out.println("切换分支成功");
            return "切换分支成功";
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("切换分支失败");
            return "切换分支失败"+e.getMessage();
        } 
    }
	
	/**
	 * 删除分支
	 */
	public static String delBranch(String branchName) {
		try {
		List<Ref> refs = git.branchList().call();
		boolean isCreate = true;
		for (Ref ref : refs) {
			System.out.println("Had branch: " + ref.getName());
			if (ref.getName().equals("refs/heads/"+branchName)) {
				isCreate = false;
				System.out.println("Removing branch before");
				git.branchDelete().setBranchNames(branchName).setForce(true).call();
				break;
			}
		}
		if(!isCreate) {
        	 System.out.println("分支不存在");
        	 return "分支不存在";
        }
		System.out.println("删除分支成功");
        return "删除分支成功";
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("删除分支失败");
            return "删除分支失败"+e.getMessage();
		}
	}
	public static void getBranch() throws GitAPIException {
		List<Ref> refs =git.branchList().setListMode(ListMode.ALL).call();
    	for (Ref ref : refs) {
			System.out.println("Had branch: " + ref.getName());
		}
	}
	/**
	 * 返回两个分支的差异
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public static void showBranchDiff(String bath1,String bath2){
        try {
		if(repository.exactRef("refs/heads/"+bath1) == null) {
                    // first we need to ensure that the remote branch is visible locally
                    Ref ref = git.branchCreate().setName(bath1).setStartPoint("origin/"+bath1).call();
                    System.out.println("Created local testbranch with ref: " + ref);
                }
                // the diff works on TreeIterators, we prepare two for the two branches
                AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, "refs/heads/"+bath1);
                AbstractTreeIterator newTreeParser = prepareTreeParser(repository, "refs/heads/"+bath2);
                // then the procelain diff-command returns a list of diff entries
                List<DiffEntry> diff = git.diff().setOldTree(oldTreeParser).setNewTree(newTreeParser).call();
                for (DiffEntry entry : diff) {
                    System.out.println("Entry: " + entry);
                }
        }catch (Exception e) {
			e.printStackTrace();
		}
        
    }
	private static AbstractTreeIterator prepareTreeParser(Repository repository, String ref) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        Ref head = repository.exactRef(ref);
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(head.getObjectId());
            RevTree tree = walk.parseTree(commit.getTree().getId());
            commit.getTree();
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        }
    }
	/**
     * 新建一个分支并同步到远程仓库
     * @param branchName
     * @throws IOException
     * @throws GitAPIException
     */
	public static String newBranch(String branchName) {
		try {
		String newBranchIndex = "refs/heads/" + branchName;
		System.out.println("开始创建新分支:" + newBranchIndex);
		List<Ref> refs = git.branchList().call();
		boolean isCreate = true;
		for (Ref ref : refs) {
			System.out.println("Had branch: " + ref.getName());
			if (ref.getName().equals("refs/heads/"+branchName)) {
				System.out.println("分支已存在!");
				isCreate = false;
				git.branchDelete().setBranchNames(branchName).setForce(true).call();
				break;
			}
		}
		if(!isCreate) {
       	 System.out.println("分支已存在");
       	 return "分支已存在";
       }
		// run the add-call
		git.branchCreate().setName(branchName).call();
		return "分支创建成功";
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("分支创建失败");
            return "分支创建失败"+e.getMessage();
		}
	}
	public static void commit(List<String> files,String remark) throws NoFilepatternException, GitAPIException {
		//判断工作区与暂存区的文件内容是否有变更 
        List<DiffEntry> diffEntries = git.diff()  
            .setPathFilter(PathFilterGroup.createFromStrings(files))  
            .setShowNameAndStatusOnly(true).call();  
        if (diffEntries == null || diffEntries.size() == 0) {  
            System.out.println("提交的文件内容都没有被修改，不能提交");
            return;
        }
        //被修改过的文件  
        List<String> updateFiles = new ArrayList<String>();  
        ChangeType changeType;  
        for (DiffEntry entry : diffEntries) {  
            changeType = entry.getChangeType();  
            switch (changeType) {  
                case ADD:  
                case COPY:  
                case RENAME:  
                case MODIFY:  
                    updateFiles.add(entry.getNewPath());  
                    break;
                case DELETE:  
                    updateFiles.add(entry.getOldPath());  
                    break;  
            }  
        }
        //将文件提交到git仓库中，并返回本次提交的版本号  
        //1、将工作区的内容更新到暂存区  
        AddCommand addCmd = git.add();  
        for (String file : updateFiles) {  
            addCmd.addFilepattern(file); 
        }
        addCmd.call();  
        //2、commit  
        CommitCommand commitCmd = git.commit();  
        for (String file : updateFiles) {  
            commitCmd.setOnly(file);  
        }
        commitCmd.setMessage(remark).call();
        git.pull().setCredentialsProvider(cp).call();
        git.push().setCredentialsProvider(cp).call();
        System.out.println("提交成功");
	}
	
	public static void merge(String branch1,String branch2) throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException, NoHeadException, ConcurrentRefUpdateException, CheckoutConflictException, InvalidMergeHeadsException, WrongRepositoryStateException, NoMessageException, GitAPIException {
		 git.checkout().setName(branch2).call();
		 ObjectId mergeBase = repository.resolve(branch1);
		 MergeResult merge = git.merge().
                 include(mergeBase).
                 setCommit(true).
                 setFastForward(MergeCommand.FastForwardMode.NO_FF).
                 //setSquash(false).
                 setMessage("Merged changes").
                 call();
         System.out.println("Merge-Results for id: " + mergeBase + ": " + merge);
         for (Map.Entry<String,int[][]> entry : merge.getConflicts().entrySet()) {
             System.out.println("Key: " + entry.getKey());
             for(int[] arr : entry.getValue()) {
                 System.out.println("value: " + Arrays.toString(arr));
             }
         }
	}
	public static void main(String[] args) throws GitAPIException, RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException {
			//System.out.println(newBranch("test03"));
		//showBranchDiff("mastertemp","test01");
		//getBranch();
		List<String>list=new ArrayList<>();
		list.add("src/main/java/com/pay200/jgit/JGitUtils.java");
		//checkoutBranch("master");
		//commit(list,"切换分支提交文件");
		merge("test02","test01");
	}

}
