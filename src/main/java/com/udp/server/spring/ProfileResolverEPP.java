package com.udp.server.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public class ProfileResolverEPP implements EnvironmentPostProcessor{
    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment configurableEnvironment, final SpringApplication springApplication) {
        final String profile = configurableEnvironment.getActiveProfiles()[0];
        if(profile.equals("prod")){
            if(System.getProperty("os.name").contains("Linux")){
                throw new RuntimeException("Change profile to 'dev' in application properties");
            }
        }
    }
}
