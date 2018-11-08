package com.pay200.jgit.api;

import java.io.IOException;

import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;

import com.pay200.jgit.helper.CookbookHelper;

/**
 * Simple snippet which shows how to use RevWalk to read tags
 */
public class ReadTagFromName {

    public static void main(String[] args) throws IOException {
        try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
            // a RevWalk allows to retrieve information from the repository
            try (RevWalk walk = new RevWalk(repository)) {
                // a simple tag that is not annotated
                Ref simpleTag = repository.findRef("initialtag");
                RevObject any = walk.parseAny(simpleTag.getObjectId());
                System.out.println("Commit: " + any);

                // an annotated tag
                Ref annotatedTag = repository.findRef("secondtag");
                any = walk.parseAny(annotatedTag.getObjectId());
                System.out.println("Tag: " + any);

                // finally try to print out the tag-content
                System.out.println("\nTag-Content: \n");
                ObjectLoader loader = repository.open(annotatedTag.getObjectId());
                loader.copyTo(System.out);

                walk.dispose();
            }
        }
    }
}
