package com.services;

import com.model.events.PostCreatedEvent;
import com.services.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class EventHandlerService {

    @Autowired
    private StorageService storageService;

    @EventListener(PostCreatedEvent.class)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onPostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        log.info("Post suffered rollback. Deleting local saved file.. {}", postCreatedEvent.getFilename());

        storageService.deleteResource(postCreatedEvent.getFilename());
    }
}
