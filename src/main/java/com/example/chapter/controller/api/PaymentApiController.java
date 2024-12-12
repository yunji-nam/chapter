package com.example.chapter.controller.api;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.dto.PaymentCallbackDto;
import com.example.chapter.dto.PaymentPrepareDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/payment/prepare")
    public ResponseEntity<ApiResponse> preparePayment(@RequestBody PaymentPrepareDto requestDto) throws IamportResponseException, IOException {
        return paymentService.preparePayment(requestDto);
    }

    @PostMapping("/payment/validate")
    public IamportResponse<Payment> validatePayment(@RequestBody PaymentCallbackDto requestDto) throws IamportResponseException, IOException {
        return paymentService.validatePayment(requestDto);
    }

    @PostMapping("/payment/complete/{merchantUid}")
    public ResponseEntity<ApiResponse> completePayment(@PathVariable String merchantUid, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @Valid @RequestBody OrderRequestDto dto) {
        return paymentService.completePayment(merchantUid, userDetails.getUser(), dto);
    }
}
