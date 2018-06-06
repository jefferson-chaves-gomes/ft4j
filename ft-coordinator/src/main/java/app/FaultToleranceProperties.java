package app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("coordinator")
public class FaultToleranceProperties {

    private String shellScriptsPath;

    public String getShellScriptsPath() {
        return this.shellScriptsPath;
    }

    public void setShellScriptsPath(final String shellScriptsPath) {
        this.shellScriptsPath = shellScriptsPath;
    }
}
