package br.com.embole.sharemybill.bill.application.service;

import br.com.embole.sharemybill.bill.application.port.in.BillingUseCase;
import br.com.embole.sharemybill.bill.application.port.in.request.BillingRequest;
import br.com.embole.sharemybill.bill.application.port.in.request.ProductRequest;
import br.com.embole.sharemybill.bill.application.port.in.response.BillPerCustomerResponse;
import br.com.embole.sharemybill.bill.application.port.in.response.BillingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService implements BillingUseCase {

    @Override
    public BillingResponse calculateBilling(BillingRequest request) {
        BigDecimal totalSpent = request.getBillsPerCustomer().stream()
                .map(billPerCustomer -> calculateTotalSpentByCustomer(billPerCustomer.getConsumedProducts()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<BillPerCustomerResponse> responsesByCustomer = new ArrayList<>();

        request.getBillsPerCustomer()
                .forEach(billPerCustomer -> {
                    BigDecimal totalByCustomer = calculateTotalSpentByCustomer(billPerCustomer.getConsumedProducts());
                    BigDecimal amountToPayForDelivery = totalByCustomer.multiply(request.getDeliveryAmount())
                            .setScale(2, RoundingMode.HALF_UP)
                            .divide(totalSpent, RoundingMode.HALF_UP)
                            .setScale(2, RoundingMode.HALF_UP);
                    BigDecimal amountToGetByDiscount = totalByCustomer.multiply(request.getDiscountAmount())
                            .setScale(2, RoundingMode.HALF_UP)
                            .divide(totalSpent, RoundingMode.HALF_UP).
                            setScale(2, RoundingMode.HALF_UP);

                    BillPerCustomerResponse responseByCustomer = BillPerCustomerResponse.builder()
                            .amountToPay(totalByCustomer.subtract(amountToGetByDiscount).add(amountToPayForDelivery))
                            .clientDocument(billPerCustomer.getClientDocument())
                            .originalAmount(totalByCustomer)
                            .totalDiscount(amountToGetByDiscount)
                            .build();

                    responsesByCustomer.add(responseByCustomer);
                });

        return BillingResponse.builder()
                .billsPerCustomer(responsesByCustomer)
                .build();
    }

    private BigDecimal calculateTotalSpentByCustomer(List<ProductRequest> consumedProducts){
        return consumedProducts.stream().map(ProductRequest::getProductPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
