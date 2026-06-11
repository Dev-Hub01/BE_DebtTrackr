package com.debttrackr.controller;

import com.debttrackr.domain.enumeration.TransactionType;
import com.debttrackr.service.DashboardService;
import com.debttrackr.service.DebtPublisher;
import com.debttrackr.service.dto.DashboardDTO;
import io.swagger.v3.oas.annotations.Operation;
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

    @Autowired
    private DebtPublisher publisher;


    @GetMapping("")
    public DashboardDTO getDashboard(@RequestParam TransactionType type) {
log.info("Rest request to get dashboard summary data for {}" , type);
        return dashboardService.getDashboardData(type);
    }



    @Operation(
            summary = "Publish debt message",
            description = "Publishes debt to RabbitMQ queue"
    )
    @PostMapping("/publish")
    public String publish(@RequestBody String message) {
        publisher.publish(message);
        return "Message pushed to queue!";
    }
}
