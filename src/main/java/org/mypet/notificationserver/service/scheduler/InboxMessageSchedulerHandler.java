package org.mypet.notificationserver.service.scheduler;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.mypet.notificationserver.entity.InboxMessage;
import org.mypet.notificationserver.entity.enums.InboxMessageStatus;
import org.mypet.notificationserver.service.EmailService;
import org.mypet.notificationserver.service.InboxMessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InboxMessageSchedulerHandler {

    private final InboxMessageService inboxMessageService;
    private final EmailService emailService;

    @Scheduled(cron = "${spring.properties.scheduler.processMessageCron}")
    @SchedulerLock(name = "processInboxMessages", lockAtMostFor = "30s", lockAtLeastFor = "30s")
    public void scheduleInboxMessages() {
        inboxMessageService.getNotProcessed().stream()
                .peek(this::processMessage)
                .forEach(inboxMessageService::saveMessage);
    }

    @Scheduled(cron = "${spring.properties.scheduler.processErrorMessageCron}")
    @SchedulerLock(name = "retryProcessInboxMessages", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void retryProcessInboxMessages() {
        inboxMessageService.getWithErrorStatusToRetry().stream()
                .peek(this::processMessage)
                .forEach(inboxMessageService::saveMessage);
    }

    @Scheduled(cron = "${spring.properties.scheduler.clearMessagesCron}")
    @SchedulerLock(name = "clearInboxMessages", lockAtLeastFor = "12h", lockAtMostFor = "12h")
    public void clearProcessedMessages() {
        inboxMessageService.clearProcessedMessages();
    }

    private void processMessage(InboxMessage message) {
        try {
            emailService.sendEmail(message);
            message.setLastAttempt(LocalDateTime.now());
            message.setStatus(InboxMessageStatus.SENT);
        } catch (MessagingException e) {
            message.setLastAttempt(LocalDateTime.now());
            message.setStatus(InboxMessageStatus.ERROR);
        }
    }
}
