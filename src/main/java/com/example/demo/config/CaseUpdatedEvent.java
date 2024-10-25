package com.example.demo.config;

import com.example.demo.entity.Case;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public class CaseUpdatedEvent extends ApplicationEvent {
    private final Case aCase;
    public CaseUpdatedEvent(Object source, Case aCase) {
        super(source);
        this.aCase=aCase;
    }
    public Case getCase() {
        return aCase;
    }
}
