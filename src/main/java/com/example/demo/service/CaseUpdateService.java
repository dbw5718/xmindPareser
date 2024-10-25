package com.example.demo.service;

import com.example.demo.config.CaseUpdatedEvent;
import com.example.demo.entity.Case;
import com.example.demo.respository.CaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CaseUpdateService {
    private  final CaseRepository repository;
    private  final ApplicationEventPublisher  eventPublisher;

    public CaseUpdateService(CaseRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }
    @Transactional
    public void updateCase(Long id , String newDescription){
        Case aCase = repository.findById(id).orElseThrow();
        if(!aCase.getViews().isEmpty()){
            aCase.setDescription(newDescription);
            repository.save(aCase);
            eventPublisher.publishEvent(new CaseUpdatedEvent(this, aCase));
        }

    }
}
