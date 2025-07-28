package com.eazybytes.loans.service.impl;

import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.entity.Loans;
import com.eazybytes.loans.exception.ResourceNotFoundException;
import com.eazybytes.loans.mapper.LoansMapper;
import com.eazybytes.loans.repository.LoansRepository;
import com.eazybytes.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {

    private LoansRepository loansRepository;

    @Override
    public LoansDto createLoans(LoansDto loansDto) {
        Loans loans = LoansMapper.mapToLoans(loansDto, new Loans());

        loans.setLoanNumber(getUniqueLoanNumber().toString());
        loansRepository.save(loans);

        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    @Override
    public boolean updateLoans(LoansDto loansDto) {
        boolean isUpdated = false;
        Loans loans = loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loansDto.getLoanNumber().toString()));
        if(loans != null) {
            Loans loansUpdated = LoansMapper.mapToLoans(loansDto, loans);
            loansRepository.save(loansUpdated);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public List<LoansDto> getAllLoans(String mobile) {
        return loansRepository
                .findByMobile(mobile)
                .stream()
                .map(loan -> LoansMapper.mapToLoansDto(loan, new LoansDto()))
                .toList();
    }

    @Override
    public void deleteLoan(Long loanNumber) {
        loansRepository.findByLoanNumber(loanNumber.toString()).orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loanNumber.toString()));

        loansRepository.deleteByLoanNumber(loanNumber.toString());
    }

    private Long getUniqueLoanNumber() {
        return 1000000000L + new Random().nextInt(900000000);
    }
}
