package com.pay200.jgit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.pay200.jgit.helper.CookbookHelper;

/** 
* @author yupeng
* @version 创建时间：2018年10月18日 上午10:36:20 
* 类说明 
*/
public class GitUtilClass {
	public static String localRepoPath = "F:\\opt\\git";
    public static String localRepoGitConfig = "F:/opt/testJgit/.git";
    public static String remoteRepoURI = "https://gitee.com/yupengqaq/testJgit.git";
    public static String localCodeDir = "F:/opt/git";
    static Git git;
    static CredentialsProvider cp;
    static{
        //建立与远程仓库的联系，仅需要执行一次
         try {
        	// CloneCommand cloneCommand = Git.cloneRepository();
        	 cp= new UsernamePasswordCredentialsProvider("yupengqaq", "qwer1234" );
//			 cloneCommand.setURI( "https://gitee.com/yupengqaq/testJgit.git" );
//			 cloneCommand.setCredentialsProvider(cp);
//			 cloneCommand.setDirectory(new File(localRepoPath));
			 //git=cloneCommand.call();
			 Repository repository = CookbookHelper.openJGitCookbookRepository();
			//克隆代码库命令
			//System.out.println(repository);
			git=new Git(repository);
         } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 新建一个分支并同步到远程仓库
     * @param branchName
     * @throws IOException
     * @throws GitAPIException
     */
    public static String newBranch(String branchName) throws IOException{
    	//Git git=Git.open(new File("F:/git//.git"));
        String newBranchIndex = "refs/heads/"+branchName;
        System.out.println(newBranchIndex);
        String gitPathURI = "";
        try {
            //检查新建的分支是否已经存在，如果存在则将已存在的分支强制删除并新建一个分支
            List<Ref> refs = git.branchList().call();
            for (Ref ref : refs) {
                if (ref.getName().equals(newBranchIndex)) {
                    System.out.println("Removing branch before");
                    git.branchDelete().setBranchNames(branchName).setForce(true)
                            .call();
                    break;
                }
            }            
            //新建分支
            Ref ref = git.branchCreate().setName(branchName).call();
            //推送到远程
            git.push().setCredentialsProvider( cp ).add(ref).call();
            gitPathURI = remoteRepoURI + " " + "feature/" + branchName;
        } catch (GitAPIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return gitPathURI;                
    }
    
    public static void commitFiles() throws IOException, GitAPIException{
        String filePath = "";
        Git git = Git.open( new File(localRepoGitConfig) );
        //创建用户文件的过程
        File myfile = new File(filePath);
        myfile.createNewFile();
        git.add().addFilepattern("pets").call();   
        //提交
        git.commit().setMessage("Added pets").call();   
        //推送到远程
        git.push().call();
    }
    
    public static boolean pullBranchToLocal(String cloneURL){
        boolean resultFlag = false;
        String[] splitURL = cloneURL.split(" ");
        String branchName = splitURL[1];
        String fileDir = localCodeDir+"/"+branchName;
        //检查目标文件夹是否存在
        File file = new File(fileDir);
        if(file.exists()){
            deleteFolder(file);
        }
        Git git;
        try {
            git = Git.open( new File(localRepoGitConfig) );
            git.cloneRepository().setURI(cloneURL).setDirectory(file).call();
            resultFlag = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GitAPIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultFlag;    
    }
    
    public static void deleteFolder(File file){
        if(file.isFile() || file.list().length==0){
            file.delete();
        }else{
            File[] files = file.listFiles();
            for(int i=0;i<files.length;i++){
                deleteFolder(files[i]);
                files[i].delete();
            }
        }
    }
  public static void main(String[] args) throws IOException {
	  newBranch("test03");
}
    
    
    
}
