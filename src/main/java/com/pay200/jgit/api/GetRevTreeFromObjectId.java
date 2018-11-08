package com.pay200.jgit.api;

import java.io.IOException;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;

import com.pay200.jgit.helper.CookbookHelper;

/**
 * Simple snippet which shows how to use RevWalk to iterate over objects
 */
public class GetRevTreeFromObjectId {

    public static void main(String[] args) throws IOException {
        try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
            // See e.g. GetRevCommitFromObjectId for how to use a SHA-1 directly
            Ref head = repository.findRef("HEAD");
            System.out.println("Ref of HEAD: " + head + ": " + head.getName() + " - " + head.getObjectId().getName());

            // a RevWalk allows to walk over commits based on some filtering that is defined
            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(head.getObjectId());
                System.out.println("Commit: " + commit);

                // a commit points to a tree
                RevTree tree = walk.parseTree(commit.getTree().getId());
                System.out.println("Found Tree: " + tree);

                walk.dispose();
            }
        }
    }
}
