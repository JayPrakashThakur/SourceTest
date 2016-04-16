package com.appniche.sourcetest;

/**
 * Created by JayPrakash on 16-04-2016.
 */
public class SourceObject {

    private String personName;
    private String commit;
    private String commitMessage;

    public SourceObject(String personName, String commit, String commitMessage) {
        this.personName = personName;
        this.commit = commit;
        this.commitMessage = commitMessage;
    }

    public String getPersonName() {
        return personName;
    }

    public String getCommit() {
        return commit;
    }

    public String getCommitMessage() {
        return commitMessage;
    }
}
