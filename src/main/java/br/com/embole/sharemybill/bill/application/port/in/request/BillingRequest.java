package br.com.embole.sharemybill.bill.application.port.in.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingRequest {
    @NotNull
    private BigDecimal deliveryAmount;
    @NotNull
    private BigDecimal discountAmount;
    @NotNull
    private List<BillPerCustomerRequest> billsPerCustomer;
}
