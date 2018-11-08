package com.pay200.jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.util.List;
/** 
* @author yupeng
* @version 创建时间：2018年10月17日 下午5:44:10 
* 类说明 
*/
public class GitUtil {
	
	
	//克隆仓库
    public String cloneRepository(String url, String localPath) {
        try {
            System.out.println("开始下载......");
            Git git = Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(localPath))
                    .setCloneAllBranches(true)
                    .call();
            System.out.println("下载完成......");
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    //切换分支
    public void checkoutBranch(String localPath, String branchName){
        String projectURL = localPath + "\\.git";
        Git git = null;
        try {
            git = Git.open(new File(projectURL));
            //检测dev分支是否已经存在 若不存在则新建分支
            List<Ref> localBranch = git.branchList().call();
            boolean isCreate = true;
            for (Ref branch : localBranch) {
            	System.out.println(branch.getName());
                if (branch.getName().endsWith(branchName)) {
                    isCreate = false;
                    break;
                }
            }
            if(isCreate) {
            	 //新建分支
                Ref ref = git.branchCreate().setName(branchName).call();
                //推送到远程
                git.push().add(ref).call();
            }
            git.checkout().setCreateBranch(isCreate).setName(branchName).call();
            git.pull().call();
            System.out.println("切换分支成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("切换分支失败");
        } finally{
            if (git != null) {
                git.close();
            }
        }   
    }

    //提交代码
    public void commit(String localPath,String pushMessage)  {
        String projectURL = localPath + "\\.git";
        Git git = null;
        try {
            git = Git.open(new File(projectURL));
            git.pull().call();
            Status status = git.status().call();
            if (status.hasUncommittedChanges() == false) {
                System.out.println("无已修改文件");
                return;
            }
            //忽略GitUtil.java文件
//            git.add().addFilepattern("GitUtil.java").call();
            git.add().addFilepattern(".").call();
            git.commit().setMessage(pushMessage).call();
            git.pull().call();
            git.push().call();

//            //查看log信息
//            for(RevCommit revCommit : git.log().call()){
//                System.out.println(revCommit);
//                System.out.println(revCommit.getFullMessage());
//                System.out.println(revCommit.getCommitterIdent().getName() + " " + revCommit.getCommitterIdent().getEmailAddress());
//            }
            System.out.println("提交成功");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("提交失败");
        } finally{
            if (git != null) {
                git.close();
            }
        }
    }
    public static void main(String[] args) {
        GitUtil gitUtil = new GitUtil();
        //git远程url地址
        String url = "https://gitee.com/yupengqaq/testJgit.git";
//        String localPath = "d:/jgitTest";
        String localPath ="F:\\opt\\testJgit";
        String branchName = "master";
        try {
        	//    gitUtil.cloneRepository(url,localPath);
        	//gitUtil.checkoutBranch(localPath,branchName);
            gitUtil.commit(localPath,"测试提交1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
