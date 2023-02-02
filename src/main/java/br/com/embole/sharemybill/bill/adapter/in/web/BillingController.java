package br.com.embole.sharemybill.bill.adapter.in.web;

import br.com.embole.sharemybill.bill.application.port.in.BillingUseCase;
import br.com.embole.sharemybill.bill.application.port.in.request.BillingRequest;
import br.com.embole.sharemybill.bill.application.port.in.response.BillingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(("/ap1/v1/billing"))
@RequiredArgsConstructor
public class BillingController {

    private final BillingUseCase billingUseCase;

    @PostMapping
    public ResponseEntity<BillingResponse> calculateBilling(@RequestBody @Valid BillingRequest request,
                                                            UriComponentsBuilder uriComponentsBuilder){
        BillingResponse billingResponse = billingUseCase.calculateBilling(request);
        URI uri = uriComponentsBuilder.path("/billings").buildAndExpand().toUri();

        return ResponseEntity.created(uri).body(billingResponse);
    }
}
