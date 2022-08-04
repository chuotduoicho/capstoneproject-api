package com.jovinn.capstoneproject.dto.client.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryNotMilestoneResponse {
    @NotNull UUID id;
    UUID contractId;
    String file;
    String description;

    public DeliveryNotMilestoneResponse(UUID id, UUID contractId, String file, String description) {
        this.id = id;
        this.contractId = contractId;
        this.file = file;
        this.description = description;
    }
}
