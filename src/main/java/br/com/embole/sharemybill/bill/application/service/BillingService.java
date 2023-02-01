package br.com.embole.sharemybill.bill.application.service;

import br.com.embole.sharemybill.bill.adapter.out.persistence.BillRepository;
import br.com.embole.sharemybill.bill.application.port.in.BillingUseCase;
import br.com.embole.sharemybill.bill.application.port.in.request.BillingRequest;
import br.com.embole.sharemybill.bill.application.port.in.request.ProductRequest;
import br.com.embole.sharemybill.bill.application.port.in.response.BillPerCustomerResponse;
import br.com.embole.sharemybill.bill.application.port.in.response.BillingResponse;
import br.com.embole.sharemybill.bill.application.port.out.PaymentRequestService;
import br.com.embole.sharemybill.bill.application.port.out.request.BuyerRequest;
import br.com.embole.sharemybill.bill.application.port.out.request.PaymentRequest;
import br.com.embole.sharemybill.bill.application.port.out.response.PaymentResponse;
import br.com.embole.sharemybill.bill.domain.Bill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService implements BillingUseCase {
    private final PaymentRequestService paymentRequestService;
    private final BillRepository billRepository;

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

                    UUID billId = UUID.randomUUID();

                    PaymentResponse paymentResponse = paymentRequestService.requestPaymentForClient(PaymentRequest.builder()
                            .value(responseByCustomer.getAmountToPay())
                            .callbackUrl("http://localhost:8080/bill/" + billId)
                            .referenceId(billId.toString())
                            .buyer(BuyerRequest.builder()
                                    .document(responseByCustomer.getClientDocument())
                                    .build())
                            .build());

                    Bill billForCustomer = new Bill();
                    billForCustomer.setClientDocument(responseByCustomer.getClientDocument());
                    billForCustomer.setId(billId);
                    billForCustomer.setOriginalAmount(totalByCustomer);
                    billForCustomer.setAmountToPay(responseByCustomer.getAmountToPay());
                    billForCustomer.setTotalDiscount(responseByCustomer.getTotalDiscount());
                    billForCustomer.setPaymentUrl(paymentResponse.getPaymentUrl());

                    responseByCustomer.setPaymentUrl(paymentResponse.getPaymentUrl());

                    billRepository.save(billForCustomer);

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
