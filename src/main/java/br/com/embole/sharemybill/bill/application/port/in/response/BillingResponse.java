package br.com.embole.sharemybill.bill.application.port.in.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingResponse {
    private List<BillPerCustomerResponse> billsPerCustomer;
}
