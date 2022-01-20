package io.jmakowiecki.model;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
class Audit {

    @PrePersist
    void prePersist() {
        LocalDateTime createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preMerge() {
        LocalDateTime updatedOn = LocalDateTime.now();
    }

}
