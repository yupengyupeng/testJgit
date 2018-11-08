package com.pay200.jgit.porcelain;

import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import com.pay200.jgit.helper.CookbookHelper;



/**
 * Simple snippet which shows how to use RevWalk to quickly iterate over all available commits,
 * not just the ones on the current branch
 */
public class WalkAllCommits {

    public static void walkAllCommits() throws IOException, GitAPIException {
        try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
            try (Git git = new Git(repository)) {
                Iterable<RevCommit> commits = git.log().all().call();
                int count = 0;
                for (RevCommit commit : commits) {
                    System.out.println("LogCommit: " + commit);
                    count++;
                }
                System.out.println(count);
            }
        }
    }
}
