package br.com.embole.sharemybill.bill.application.port.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillPerCustomerRequest {
    @NotBlank
    private String clientDocument;
    @NotNull
    private List<ProductRequest> consumedProducts;
}
