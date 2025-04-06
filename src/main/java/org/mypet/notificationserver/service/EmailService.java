package org.mypet.notificationserver.service;

import jakarta.mail.MessagingException;
import org.mypet.notificationserver.entity.InboxMessage;

public interface EmailService {
    void sendEmail(InboxMessage inboxMessage) throws MessagingException;
}
