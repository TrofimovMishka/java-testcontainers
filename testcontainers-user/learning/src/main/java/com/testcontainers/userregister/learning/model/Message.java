package com.testcontainers.userregister.learning.model;

import com.testcontainers.userregister.learning.model.enums.EventEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.ldap.PagedResultsControl;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Message implements Serializable {

    private String id = UUID.randomUUID().toString();
    private String msg;
    private EventEnum event;
}
