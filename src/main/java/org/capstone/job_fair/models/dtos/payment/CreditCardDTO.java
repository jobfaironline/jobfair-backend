package org.capstone.job_fair.models.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditCardDTO {
    private String number;
    private String exp_month;
    private String exp_year;
    private String cvc;
}
