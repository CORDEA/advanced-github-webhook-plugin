package jp.cordea.advancedgithubwebhook;

import hudson.model.Cause;

/**
 * Created by Yoshihiro Tanaka on 2016/10/02.
 */
public class GitHubWebhookCause extends Cause {

    @Override
    public String getShortDescription() {
        return "Started by Advanced GitHub Webhook plugin";
    }

}
