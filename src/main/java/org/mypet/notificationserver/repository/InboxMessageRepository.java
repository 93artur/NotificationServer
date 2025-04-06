package org.mypet.notificationserver.repository;

import org.mypet.notificationserver.entity.InboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface InboxMessageRepository extends JpaRepository<InboxMessage, Long> {

    List<InboxMessage> findByStatusIsIn(Collection<String> status);

    List<InboxMessage> findByStatusIsInAndRetryCountLessThan(Collection<String> status, Integer maxRetryCount);

    void deleteByStatusIsIn(Collection<String> status);
}
