package br.com.embole.sharemybill.bill.application.service;

import br.com.embole.sharemybill.bill.application.port.in.BillingUseCase;
import br.com.embole.sharemybill.bill.application.port.in.request.BillingRequest;
import br.com.embole.sharemybill.bill.application.port.in.request.ProductRequest;
import br.com.embole.sharemybill.bill.application.port.in.response.BillingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService implements BillingUseCase {

    @Override
    public BillingResponse calculateBilling(BillingRequest request) {
        BigDecimal totalSpent = request.getBillsPerCustomer().stream()
                .map(billPerCustomer -> calculateTotalSpentByCustomer(billPerCustomer.getConsumedProducts()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return null;
    }

    private BigDecimal calculateTotalSpentByCustomer(List<ProductRequest> consumedProducts){
        return consumedProducts.stream().map(ProductRequest::getProductPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
