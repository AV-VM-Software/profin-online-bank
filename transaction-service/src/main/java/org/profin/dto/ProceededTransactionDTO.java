package org.profin.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.profin.entity.PaymentStatus;
import org.profin.entity.TransactionType;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProceededTransactionDTO extends TransactionDTO {
    private String userEmail;
    //might be null
    private String recipientEmail;
}