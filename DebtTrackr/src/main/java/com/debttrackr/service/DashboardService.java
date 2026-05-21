package com.debttrackr.service;

import com.debttrackr.domain.enumeration.TransactionType;
import com.debttrackr.service.dto.DashboardDTO;

public interface DashboardService {

    DashboardDTO getDashboardData(TransactionType type);

}
