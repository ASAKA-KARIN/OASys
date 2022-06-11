package cn.gson.oasys.configuration;

import freemarker.template.DefaultObjectWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author 86156
 */
@Configuration
public class FreeMarkerConfig {
    @Autowired
    private freemarker.template.Configuration configuration;

    @PostConstruct
    public void configuration() {
        this.configuration.setAPIBuiltinEnabled(true);
        DefaultObjectWrapper defaultObjectWrapper = (DefaultObjectWrapper) configuration.getObjectWrapper();
        defaultObjectWrapper.setUseAdaptersForContainers(true);
    }

}
