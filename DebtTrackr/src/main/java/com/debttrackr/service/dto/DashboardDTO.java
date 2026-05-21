package com.debttrackr.service.dto;

import com.debttrackr.repository.TransactionRepository;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    List<TransactionRepository.DashboardProjection>summary;

    private List<TransactionResponse> recentTransactions;



}
