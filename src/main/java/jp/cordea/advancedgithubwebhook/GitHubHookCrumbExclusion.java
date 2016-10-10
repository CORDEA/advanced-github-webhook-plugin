package jp.cordea.advancedgithubwebhook;

import hudson.Extension;
import hudson.security.csrf.CrumbExclusion;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Yoshihiro Tanaka on 2016/10/02.
 */
@Extension
public class GitHubHookCrumbExclusion extends CrumbExclusion {

    @Override
    public boolean process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
        String path = httpServletRequest.getPathInfo();
        if (path != null && path.equals("/" + GitHubWebhook.URL_NAME + "/")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return true;
        }
        return false;
    }

}
