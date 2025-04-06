package org.mypet.notificationserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.mypet.notificationserver.entity.InboxMessage;
import org.mypet.notificationserver.entity.enums.InboxMessageStatus;
import org.mypet.notificationserver.repository.InboxMessageRepository;
import org.mypet.notificationserver.service.InboxMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InboxMessageServiceImpl implements InboxMessageService {

    private final InboxMessageRepository inboxMessageRepository;

    @Value("${spring.properties.maxRetrySentCount}")
    private Integer maxRetrySentCount;

    @Override
    public InboxMessage saveMessage(InboxMessage inboxMessage) {
        return inboxMessageRepository.save(inboxMessage);
    }

    @Override
    public List<InboxMessage> getNotProcessed() {
        return inboxMessageRepository.findByStatusIsIn(List.of(InboxMessageStatus.NOT_SENT.name()));
    }

    @Override
    public List<InboxMessage> getWithErrorStatusToRetry() {
        return inboxMessageRepository.findByStatusIsInAndRetryCountLessThan(List.of(InboxMessageStatus.ERROR.name()),
                maxRetrySentCount);
    }

    @Override
    public void clearProcessedMessages() {
        inboxMessageRepository.deleteByStatusIsIn(List.of(InboxMessageStatus.SENT.name()));
    }
}
