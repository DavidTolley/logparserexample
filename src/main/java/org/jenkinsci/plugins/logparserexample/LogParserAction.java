package org.jenkinsci.plugins.logparserexample;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: dtolley
 * Date: 8/20/13
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogParserAction implements Action {

    private AbstractBuild build;
    private boolean error;
    private String errorText;
    private String errorAction;

    @DataBoundConstructor
    public LogParserAction(AbstractBuild build, boolean error, String errorText, String errorAction) {
        this.build = build;
        this.error = error;
        this.errorText = errorText;
        this.errorAction = errorAction;
    }

    public boolean getHasError() {
        return this.error;
    }

    public String getErrorText(){
        return this.errorText;
    }

    public String getErrorAction(){
        return this.errorAction;
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return null;
    }

}
