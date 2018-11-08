package com.pay200.jgit.api;

import java.io.IOException;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import com.pay200.jgit.helper.CookbookHelper;
/**
 * Simple snippet which shows how to retrieve a Ref for some reference string.
 */
public class GetRefFromName {

    public static void main(String[] args) throws IOException {
        try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
            // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
            Ref head = repository.exactRef("refs/heads/master");
            System.out.println("Ref of refs/heads/master: " + head + ": " + head.getName() + " - " + head.getObjectId().getName());
        }
    }
}
