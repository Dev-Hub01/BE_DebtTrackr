package com.debttrackr.service;

import com.debttrackr.domain.TransactionRecord;
import com.debttrackr.domain.enumeration.TransactionType;
import com.debttrackr.mapper.TransactionMapper;
import com.debttrackr.repository.TransactionRepository;
import com.debttrackr.service.dto.DashboardDTO;
import com.debttrackr.service.dto.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl
        implements DashboardService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public DashboardDTO getDashboardData(TransactionType type) {

        DashboardDTO dashboardDTO =
                new DashboardDTO();

        List<TransactionRepository.DashboardProjection>
                transactionDataSummary = transactionRepository.getTransactionDataSummary(type.name());

        List<TransactionRecord> recentTransactions = transactionRepository.findTop5ByTypeOrderByTransactionDateDesc(type);

        dashboardDTO.setSummary(transactionDataSummary);
        List<TransactionResponse> dtoList = transactionMapper.toDtoList(recentTransactions);
        dashboardDTO.setRecentTransactions(dtoList);

        return dashboardDTO;
    }


}
