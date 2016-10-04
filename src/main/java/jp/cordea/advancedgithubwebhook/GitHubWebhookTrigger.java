package jp.cordea.advancedgithubwebhook;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import lombok.Getter;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Created by Yoshihiro Tanaka on 2016/10/02.
 */
public class GitHubWebhookTrigger extends Trigger<AbstractProject<?, ?>> {

    @Getter
    private final String ref;

    @Getter
    private final String name;

    @Getter
    private final String email;

    @DataBoundConstructor
    public GitHubWebhookTrigger(String ref, String name, String email) {
        this.ref = ref;
        this.name = name;
        this.email = email;
    }

    @Override
    public TriggerDescriptor getDescriptor() {
        return super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends TriggerDescriptor {

        private static final String BLOCK_NAME = "GitHubWebhookBlock";

        public DescriptorImpl() {
            super(GitHubWebhookTrigger.class);
            load();
        }

        @Override
        public boolean isApplicable(Item item) {
            return true;
        }

        @Getter
        private String ref;

        @Getter
        private String name;

        @Getter
        private String email;

        @Override
        public String getDisplayName() {
            return "Build by the GitHub push event";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            JSONObject object = json.getJSONObject(BLOCK_NAME);
            ref = object.getString("ref");
            name = object.getString("name");
            email = object.getString("email");
            save();
            return true;
        }
    }

}
