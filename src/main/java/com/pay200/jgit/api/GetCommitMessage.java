package com.pay200.jgit.api;

/*
   Copyright 2013, 2014 Dominik Stadler

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.pay200.jgit.helper.CookbookHelper;

/**
 * Simple snippet which shows how to retrieve the commit-message based on object id.
 */
public class GetCommitMessage {

    public static void main(String[] args) throws Exception {
    	try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
            Ref head = repository.findRef("refs/heads/master");
            System.out.println("Found head: " + head);
            // a RevWalk allows to walk over commits based on some filtering that is defined
            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(head.getObjectId());
                RevTree tree=commit.getTree();
                System.out.println(tree.name());
                
              
               // System.out.println(getLog(head.getObjectId()));
                System.out.println("\nCommit-Message: " + commit.getFullMessage());

                walk.dispose();
            }
        }
    }
    public static List<DiffEntry> getLog() throws Exception {  
    	Repository repository = CookbookHelper.openJGitCookbookRepository();
    	Git git=new Git(repository);
    	ObjectId mergeBase = repository.resolve("refs/heads/master");
    	System.out.println(mergeBase.getName());
    	git.pull().call();
    	
        Iterable<RevCommit> allCommitsLater = git.log().add(mergeBase).call();  
        Iterator<RevCommit> iter = allCommitsLater.iterator();  
        RevCommit commit = iter.next();  
        TreeWalk tw = new TreeWalk(repository);  
        tw.addTree(commit.getTree());  
  
        commit = iter.next();  
        if (commit != null)  
            tw.addTree(commit.getTree());  
        else  
            return null;  
  
        tw.setRecursive(true);  
        RenameDetector rd = new RenameDetector(repository);  
        rd.addAll(DiffEntry.scan(tw));  
        System.out.println(rd.compute());
        return rd.compute();  
    }  
    
}
