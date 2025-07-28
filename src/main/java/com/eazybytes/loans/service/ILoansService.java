package com.eazybytes.loans.service;

import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.dto.ResponseDto;

import java.util.List;

public interface ILoansService {
    LoansDto createLoans(LoansDto loansDto);

    boolean updateLoans(LoansDto loansDto);

    List<LoansDto> getAllLoans(String mobile);

    void deleteLoan(Long loanNumber);
}
