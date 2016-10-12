package jp.cordea.advancedgithubwebhook;

import com.squareup.moshi.Moshi;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.UnprotectedRootAction;
import hudson.security.ACL;
import jenkins.model.Jenkins;
import jp.cordea.advancedgithubwebhook.model.Error;
import jp.cordea.advancedgithubwebhook.model.Payload;
import jp.cordea.advancedgithubwebhook.model.Pusher;
import jp.cordea.advancedgithubwebhook.model.Response;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.io.IOUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yoshihiro Tanaka on 2016/10/02.
 */
@Extension
public class GitHubWebhook implements UnprotectedRootAction {

    public static final String URL_NAME = "advanced-github-webhook";

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getUrlName() {
        return URL_NAME;
    }

    @RequirePOST
    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException {
        Jenkins jenkins = Jenkins.getActiveInstance();

        rsp.setStatus(200);
        rsp.setContentType("application/json");

        String payloadString = IOUtils.toString(req.getInputStream());
        Moshi moshi = new Moshi.Builder().build();
        Payload payload;
        try {
            payload = moshi.adapter(Payload.class).fromJson(payloadString);
        } catch (IOException e) {
            e.printStackTrace();
            rsp.getWriter().append(moshi.adapter(Response.class)
                    .toJson(new Response(new Error(1, "Failed to deserialization of the payload object."))));
            rsp.getWriter().append('\n');
            return;
        }
        String jobName = getJobName(req.getQueryString());
        if (jobName == null) {
            rsp.getWriter().append(moshi.adapter(Response.class)
                    .toJson(new Response(new Error(1, "Job name was not found."))));
            rsp.getWriter().append('\n');
            return;
        }

        SecurityContext context = ACL.impersonate(ACL.SYSTEM);
        try {
            for (AbstractProject<?, ?> item : jenkins.getAllItems(AbstractProject.class)) {
                if (!jobName.equals(item.getName())) {
                    continue;
                }
                GitHubWebhookTrigger trigger = item.getTrigger(GitHubWebhookTrigger.class);
                if (trigger != null) {
                    if (isNeedBuild(payload, trigger)) {
                        item.scheduleBuild(0, new GitHubWebhookCause());
                        rsp.getWriter().append(moshi.adapter(Response.class).toJson(new Response()));
                        rsp.getWriter().append('\n');
                        return;
                    }
                }
            }
        } finally {
            SecurityContextHolder.setContext(context);
        }

        rsp.getWriter().append(moshi.adapter(Response.class)
                .toJson(new Response(new Error(1, "Not find a match to the specified parameters."))));
        rsp.getWriter().append('\n');
    }

    private String getJobName(String query) {
        if (query == null) {
            return null;
        }
        Matcher matcher = Pattern.compile("job=([^&]+)").matcher(query);
        if (matcher.matches()) {
            try {
                return URLDecoder.decode(matcher.group(1), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean isNeedBuild(Payload payload, GitHubWebhookTrigger trigger) {
        String ref = trigger.getRef();
        String name = trigger.getName();
        String email = trigger.getEmail();

        if (!payload.refEquals(ref)) {
            return false;
        }

        Pusher pusher = payload.getPusher();
        return pusher.nameEquals(name) && pusher.emailEquals(email);

    }

}
