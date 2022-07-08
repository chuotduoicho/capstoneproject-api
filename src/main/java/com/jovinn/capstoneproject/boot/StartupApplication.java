package com.jovinn.capstoneproject.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupApplication implements ApplicationListener<ContextRefreshedEvent> {
    @Value(value = "${service.name}")
    private String service;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("=============================================");
        log.info("============= {} START =============", service);
        log.info("=============================================");
    }
}
