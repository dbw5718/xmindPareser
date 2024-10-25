package com.example.demo.config;

import com.example.demo.entity.Case;
import com.example.demo.entity.CaseView;
import com.example.demo.respository.CaseRepository;
import com.example.demo.respository.CaseViewRepository;
import com.example.demo.service.CaseUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

public class CaseUpdateListener implements ApplicationListener<CaseUpdatedEvent> {
    private final CaseViewRepository viewRepository;
    public CaseUpdateListener(CaseViewRepository viewrepository){
        this.viewRepository=viewrepository;
    }
    public void onApplicationEvent(CaseUpdatedEvent caseUpdatedEvent){
        Case aCase=caseUpdatedEvent.getCase();
        for (CaseView view: aCase.getViews()) {
            view.setDescription(aCase.getDescription());
            viewRepository.save(view);
        }
        System.out.println("Updated views for test case: " + aCase.getId());
    }

}
