package org.jenkinsci.plugins.logparserexample;

import jenkins.model.Jenkins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;

public class ErrorsAndActions {

    private String errorsAndActionsText;
    private HashMap<String, String> knownErrorTypes;

    public ErrorsAndActions(String errorsAndActionsText) throws IOException {
        knownErrorTypes = new HashMap<String, String>();

        BufferedReader br = new BufferedReader(new StringReader(errorsAndActionsText));
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            String[] splitLine = inputLine.split(":");
            knownErrorTypes.put(splitLine[0], splitLine[1]);
        }
        br.close();
    }

    public HashMap<String, String> getKnownErrorTypes() {
        return this.knownErrorTypes;
    }
}
