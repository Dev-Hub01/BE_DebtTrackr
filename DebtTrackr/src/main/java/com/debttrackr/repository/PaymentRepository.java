package com.debttrackr.repository;

import com.debttrackr.domain.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentDetail, Long> {

    List<PaymentDetail> findAllByPersonId(Long personId);
}
