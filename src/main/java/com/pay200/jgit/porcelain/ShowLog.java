package com.pay200.jgit.porcelain;

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

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import com.pay200.jgit.helper.CookbookHelper;



/**
 * Simple snippet which shows how to get the commit-ids for a file to provide log information.
 *
 * @author dominik.stadler at gmx.at
 */
public class ShowLog {

    @SuppressWarnings("unused")
    public static void showLog() throws IOException, GitAPIException {
        try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
            try (Git git = new Git(repository)) {
                Iterable<RevCommit> logs = git.log()
                        .call();
                int count = 0;
                for (RevCommit rev : logs) {
                    //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
                    count++;
                }
                System.out.println("Had " + count + " commits overall on current branch");

                logs = git.log()
                        .add(repository.resolve("remotes/origin/master"))
                        .call();
                count = 0;
                for (RevCommit rev : logs) {
                    System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
                    count++;
                }
                System.out.println("Had " + count + " commits overall on test-branch");

                logs = git.log()
                        .not(repository.resolve("master"))
                        .add(repository.resolve("remotes/origin/master"))
                        .call();
                count = 0;
                for (RevCommit rev : logs) {
                    System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
                    count++;
                }
                System.out.println("Had " + count + " commits only on test-branch");

                logs = git.log()
                        .all()
                        .call();
                count = 0;
                for (RevCommit rev : logs) {
                    //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
                    count++;
                }
                System.out.println("Had " + count + " commits overall in repository");

                logs = git.log()
                        // for all log.all()
                        .addPath("README.md")
                        .call();
                count = 0;
                for (RevCommit rev : logs) {
                    //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
                    count++;
                }
                System.out.println("Had " + count + " commits on README.md");

                logs = git.log()
                        // for all log.all()
                        .addPath("pom.xml")
                        .call();
                count = 0;
                for (RevCommit rev : logs) {
                    //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
                    count++;
                }
                System.out.println("Had " + count + " commits on pom.xml");
            }
        }
    }
}
