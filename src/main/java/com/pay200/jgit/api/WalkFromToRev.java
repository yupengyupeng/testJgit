package com.pay200.jgit.api;

import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import com.pay200.jgit.helper.CookbookHelper;


/**
 * Simple snippet which shows how to use RevWalk to iterate over objects
 */
public class WalkFromToRev {

    public static void main(String[] args) throws IOException {
        try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
            String from = "17c34cba4247ac1b6781b4e722488ace0c089a8c";
            String to = "1ea6c125b418f1fc4d03e3ce81a607ea3e7f269f";

            // a RevWalk allows to walk over commits based on some filtering that is defined
            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(repository.resolve(to));
                System.out.println("Start-Commit: " + commit);
                System.out.println("Walking all commits starting at " + to + " until we find " + from);
                walk.markStart(commit);
                int count = 0;
                for (RevCommit rev : walk) {
                	TreeWalk treeWalk = new TreeWalk(repository);
                	treeWalk.addTree(rev.getTree());
                	treeWalk.setRecursive(true);
                	while(treeWalk.next()) {
                		System.out.println(treeWalk.getPathString());
                	}
                    count++;
                    if(rev.getId().getName().equals(from)) {
                        System.out.println("Found from, stopping walk");
                        break;
                    }
                    System.out.println("Commit: " + rev.getFullMessage()+rev.getTree());
                }
                System.out.println(count);
                walk.dispose();
            }
        }
    }
}
