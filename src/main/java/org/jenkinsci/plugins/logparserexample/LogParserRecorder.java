package org.jenkinsci.plugins.logparserexample;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class LogParserRecorder extends Recorder {

    private String errorsAndActionsText;
    private ErrorsAndActions errorsAndActions;

    @DataBoundConstructor
    public LogParserRecorder(String errorsAndActionsText) throws IOException {
        this.errorsAndActionsText = errorsAndActionsText;
        this.errorsAndActions = new ErrorsAndActions(errorsAndActionsText);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {

        if (build.getResult().equals(Result.FAILURE) || build.getResult().equals(Result.UNSTABLE)) {
            PrintStream logger = listener.getLogger();

            logger.println("Parsing build for known issues.");

            boolean error = false;
            String errorText = "";
            String errorAction = "";

            StringBuilder sbLogFile = new StringBuilder();
            Scanner scanner = new Scanner(build.getLogFile());

            boolean continueParsing = true;

            while (scanner.hasNextLine() && continueParsing == true) {
                String line = scanner.nextLine();

                for (String errorToParseFor : errorsAndActions.getKnownErrorTypes().keySet()) {

                    if (line.toLowerCase().contains(errorToParseFor.toLowerCase())) {
                        error = true;
                        continueParsing = false;
                        errorText = errorToParseFor;
                        errorAction = errorsAndActions.getKnownErrorTypes().get(errorText);
                    }
                }
            }

            if (error && build.getResult().equals(Result.FAILURE)) {
                logger.println("Failed with known error:");
                logger.println(errorText);
                logger.println(errorAction);
                build.addAction(new LogParserAction(build, error, errorText, errorAction));
            } else {
                build.addAction(new LogParserAction(build, error, errorText, errorAction));
            }
        }

        return true;
    }

    @Extension
    public static class LogParserRecorderDescriptor extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Parse for known errors";
        }
    }

    @Override
    public LogParserRecorderDescriptor getDescriptor() {
        return (LogParserRecorderDescriptor) super.getDescriptor();
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

}
