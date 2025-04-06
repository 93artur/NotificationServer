package org.mypet.notificationserver.kafka.listener;


import lombok.RequiredArgsConstructor;
import org.mypet.notificationserver.dto.TransferAccountDto;
import org.mypet.notificationserver.entity.InboxMessage;
import org.mypet.notificationserver.entity.enums.InboxMessageStatus;
import org.mypet.notificationserver.service.InboxMessageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaEmailMessageListener {

    private final InboxMessageService inboxMessageService;
    private final String ACCOUNT_TRANSFER = "Money transfer";

    @KafkaListener(topics = "${spring.kafka.topics.test-topic}")
    public void saveMessage(TransferAccountDto transferAccountDto) {
        InboxMessage inboxMessage = InboxMessage.builder()
                .recipient(transferAccountDto.getEmail())
                .subject(ACCOUNT_TRANSFER)
                .status(InboxMessageStatus.NOT_SENT)
                .message(transferAccountDto.getTransferInfo())
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        inboxMessageService.saveMessage(inboxMessage);
    }
}
