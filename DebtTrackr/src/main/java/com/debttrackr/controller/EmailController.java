package com.debttrackr.controller;

import com.debttrackr.domain.PaymentDetail;
import com.debttrackr.repository.PaymentRepository;
import com.debttrackr.service.EmailService;
import com.debttrackr.service.dto.PaymentEmailDTO;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final PaymentRepository paymentRepository;
    private final EmailService emailService;

    @GetMapping("/payment-summary/{personId}")
    public List<PaymentEmailDTO> getPaymentSummary(@PathVariable Long personId) {
        return emailService.fetchPaymentSummary(personId);
    }

    @PostMapping("/send-payment-summary")
    public ResponseEntity<String> sendEmail(@RequestParam Long personId) throws Exception {
        emailService.getPaymentSummaryAndSendEmail(personId);
        return ResponseEntity.ok("Email sent successfully");
    }
// this is what we use
    @PostMapping("/send-reminders")
    public ResponseEntity<String> sendReminders(@RequestParam("personId") Long personId, @RequestParam("sendAll") boolean sendAll) throws MessagingException {
//        emailService.sendPendingReminders(personId, sendAll);
        return ResponseEntity.ok("Reminders sent");
    }


    // this is used for send notification when do tnx
    @PostMapping("/new-transaction")
    public ResponseEntity<String> notifyNewTransaction(@RequestParam("personId") Long personId) throws MessagingException {
        return ResponseEntity.ok("Reminders sent");
    }

}
