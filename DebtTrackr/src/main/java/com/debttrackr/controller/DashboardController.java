package com.debttrackr.controller;

import com.debttrackr.domain.enumeration.TransactionType;
import com.debttrackr.service.DashboardService;
import com.debttrackr.service.dto.DashboardDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class DashboardController {


    @Autowired
    private DashboardService dashboardService;

    @GetMapping("")
    public DashboardDTO getDashboard(@RequestParam TransactionType type) {
log.info("Rest request to get dashboard summary data for {}" , type);
        return dashboardService.getDashboardData(type);
    }
}
