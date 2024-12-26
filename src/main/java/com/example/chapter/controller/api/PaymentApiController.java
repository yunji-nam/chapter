package com.example.chapter.controller.api;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.dto.OrderRequestDto;
import com.example.chapter.dto.PaymentCallbackDto;
import com.example.chapter.dto.PaymentDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/prepare")
    public ApiResponse<String> preparePayment(@RequestBody PaymentDto requestDto) throws IamportResponseException, IOException {
        return paymentService.preparePayment(requestDto);
    }

    @PostMapping("/validate")
    public IamportResponse<Payment> validatePayment(@RequestBody PaymentCallbackDto requestDto) throws IamportResponseException, IOException {
        return paymentService.validatePayment(requestDto);
    }

    @PostMapping("/complete/{merchantUid}")
    public ApiResponse<String> completePayment(@PathVariable String merchantUid, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @Valid @RequestBody OrderRequestDto dto) {  // binding
        return paymentService.completePayment(merchantUid, userDetails.getUser(), dto);
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestBody PaymentDto dto) throws IamportResponseException, IOException {
        return paymentService.cancelPayment(dto);
    }
}
