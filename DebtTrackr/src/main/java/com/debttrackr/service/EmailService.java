package com.debttrackr.service;

import com.debttrackr.domain.PaymentDetail;
import com.debttrackr.domain.Person;
import com.debttrackr.domain.TransactionRecord;
import com.debttrackr.domain.enumeration.TransactionStatus;
import com.debttrackr.repository.PaymentRepository;
import com.debttrackr.repository.PersonRepository;
import com.debttrackr.repository.TransactionRepository;
import com.debttrackr.service.dto.PaymentEmailDTO;
import com.debttrackr.service.dto.PendingTransactionDTO;
import com.debttrackr.service.dto.TransactionResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private  JavaMailSender mailSender;
    @Autowired
    private  TemplateEngine templateEngine;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private  PersonRepository personRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    
    
    
    
    
    public void getPaymentSummaryAndSendEmail(Long personId) throws MessagingException {
        System.out.println("Request to get payment details and send email ");
        List<PaymentEmailDTO> paymentEmailDTOS = this.fetchPaymentSummary(personId);
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        String toEmail = person.getEmail();
        String name = person.getName();
        List<String> defaultCc = Arrays.asList(Constants.DEFAULT_CC);

        sendPaymentSummaryEmail(toEmail, defaultCc, name, paymentEmailDTOS);

    }
    
    

    public void sendPaymentSummaryEmail(
            String to,
            List<String> cc,
            String name,
            List<PaymentEmailDTO> payments
    ) throws MessagingException {

        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("name", name);
        context.setVariable("payments", payments);

        BigDecimal total = payments.stream()
                .map(PaymentEmailDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        context.setVariable("totalAmount", total);

        String html = getPreviewMailTemplate( context,"transaction_mail_template");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);

        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc.toArray(new String[0]));
        }

        helper.setFrom(Constants.DEFAULT_FROM);
        helper.setSubject("Payment Summary");
        helper.setText(html, true);

        mailSender.send(message);
    }


    public String getPreviewMailTemplate(Context context, String emailTemplateName){
        emailTemplateName = "mail/" + emailTemplateName;
        return templateEngine.process(emailTemplateName, context);
    }



    public List<PaymentEmailDTO> fetchPaymentSummary(Long personId){
        List<PaymentDetail> payments = paymentRepository.findAllByPersonId(personId);
        return payments.stream().map(p -> new PaymentEmailDTO(
                p.getTransaction().getTransactionId(),
                p.getAmount(),
                p.getPaymentMode().name(),
                p.getPaymentReference(),
                p.getPaymentDate()
        )).toList();
    }


/* commented may 21

    public void sendPendingReminders(Long personId, boolean sendAll) throws MessagingException {

        List<TransactionRecord> transactions = new ArrayList<>();
        if(sendAll) {
            transactions =
                    transactionRepository.findByStatusIn(
                            List.of(TransactionStatus.PENDING, TransactionStatus.PARTIAL)
                    );
        } else if (!sendAll && personId != null) {
            transactions =
                    transactionRepository.findByPersonIdAndStatusInOrderByCreatedAtAsc(personId,
                            List.of(TransactionStatus.PENDING, TransactionStatus.PARTIAL)
                    );
        }


        //  Group by person
        Map<Person, List<TransactionRecord>> grouped =
                transactions.stream()
                        .collect(Collectors.groupingBy(TransactionRecord::getPerson));

        // Send email per person
        for (Map.Entry<Person, List<TransactionRecord>> entry : grouped.entrySet()) {

            Person person = entry.getKey();
            List<TransactionRecord> txnList = entry.getValue();

            List<PendingTransactionDTO> dtoList = txnList.stream()
                    .map(txn -> new PendingTransactionDTO(
                            txn.getTransactionId(),
                            txn.getAmount(),
                            txn.getBalanceAmount(),
                            txn.getDueDate(),
                            txn.getRemarks(),
                            txn.getStatus(),
                            txn.getAmountRepaid(),
                            true,
                            txn.getTransactionDate()
                    ))    .sorted(Comparator.comparing(PendingTransactionDTO::getTnxDate).reversed())

                    .toList();

            BigDecimal totalDue = dtoList.stream()
                    .map(PendingTransactionDTO::getBalanceAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

           sendReminderEmail(
                    person.getEmail(),
                    person.getName(),
                    dtoList,
                    totalDue
            );
        }
    }

*/

    public void sendReminderEmail(
            String to,
            String name,
            List<PendingTransactionDTO> transactions,
            BigDecimal totalDue
    ) throws MessagingException {

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("transactions", transactions);
        context.setVariable("totalDue", totalDue);
        context.setVariable("ownerName",Constants.OWNER_NAME);
        context.setVariable("transactionCount", transactions.size());

        //        context.setVariable("dueDateCount",     getDueDateCount());
        String html = getPreviewMailTemplate(context, "payment_return_reminder_template");
//        String html = getPreviewMailTemplate(context, "pending_transaction_mail_template");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setCc(Constants.DEFAULT_CC);
        helper.setSubject("Pending Payment Reminder");
        helper.setText(html, true);

        mailSender.send(message);
    }


    public void notifyTransaction(TransactionResponse response){
        System.out.println("Request to notify person: {} for new transaction: "+response.getPersonName());





//        Person person = personRepository.findById(personId).orElseThrow(() -> new RuntimeException("Person not found"));



    }


    public void sendNewTransactionEmail(
            String to,
            String name,
            List<PendingTransactionDTO> transactions,
            BigDecimal totalDue
    ) throws MessagingException {

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("transactions", transactions);
        context.setVariable("totalDue", totalDue);
        context.setVariable("ownerName",Constants.OWNER_NAME);
        context.setVariable("transactionCount", transactions.size());

        //        context.setVariable("dueDateCount",     getDueDateCount());
        String html = getPreviewMailTemplate(context, "payment_return_reminder_template");
//        String html = getPreviewMailTemplate(context, "pending_transaction_mail_template");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setCc(Constants.DEFAULT_CC);
        helper.setSubject("Pending Payment Reminder");
        helper.setText(html, true);

        mailSender.send(message);
    }
}
