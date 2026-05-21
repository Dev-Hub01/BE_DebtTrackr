package com.debttrackr.mapper;

import com.debttrackr.domain.TransactionRecord;
import com.debttrackr.service.dto.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {})
public interface TransactionMapper {

    TransactionRecord toEntity (TransactionResponse dto);
    List<TransactionRecord> toEntityList (List<TransactionResponse> transactionResponses);
//    @Mapping(target = "personName", source = "person.name")
    TransactionResponse toDto (TransactionRecord transactionRecord);
    List<TransactionResponse> toDtoList (List<TransactionRecord> transactionRecords);
}
