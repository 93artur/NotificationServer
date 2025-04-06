package org.mypet.notificationserver.service;

import org.mypet.notificationserver.entity.InboxMessage;

import java.util.List;

public interface InboxMessageService {

    InboxMessage saveMessage(InboxMessage inboxMessage);

    List<InboxMessage> getNotProcessed();

    List<InboxMessage> getWithErrorStatusToRetry();

    void clearProcessedMessages();
}
