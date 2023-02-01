package br.com.embole.sharemybill.bill.adapter.out.integration.picpay.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicPayBuyerRequest {
    private String document;
}
