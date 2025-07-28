package com.eazybytes.loans.controllers;

import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.ContactInfoDto;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.dto.ResponseDto;
import com.eazybytes.loans.service.ILoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Loans API's",
        description = "CRUD RESTAPI's for Loans"
)
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class LoansController {

    private final ILoansService loansService;

    public LoansController(ILoansService loansService) {
        this.loansService = loansService;
    }

    @Autowired
    private ContactInfoDto contactInfoDto;

    @Value("${build.version}")
    private String buildVersion;

    @Operation(
            summary = "Create Loans",
            description = "Create Loans record"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Loans created successfully",
            content = @io.swagger.v3.oas.annotations.media.Content
                    (
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoansDto.class)
                    )
    )
    @PostMapping("/create")
    public ResponseEntity<LoansDto> createLoan(@Valid @RequestBody LoansDto loansDto) {
        LoansDto loans = loansService.createLoans(loansDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(loans);
    }

    @Operation(
            summary = "Update Loans",
            description = "Update Loans record"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Loans updated successfully"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Failed to process request"
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoan(@Valid @RequestBody LoansDto loansDto) {
        boolean isUpdated = loansService.updateLoans(loansDto);
        if(!isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        }
    }

    @Operation(
            summary = "Get All Loans",
            description = "Get All Loans linked to a customer"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/loans")
    public ResponseEntity<List<LoansDto>> getAllLoans(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile should be 10 digits") String mobile) {
        List<LoansDto> loans = loansService.getAllLoans(mobile);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loans);
    }

    @Operation(
            summary = "Delete Loan",
            description = "Delete Loan of existing customer"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Loan deleted successfully"
    )
    @DeleteMapping("/loans/{loanNumber}")
    public ResponseEntity<ResponseDto> deleteLoan(@PathVariable @Valid @Pattern(regexp = "(^$|[0-9]{10})", message = "Loan Number should be 10 digits") String loanNumber) {
        loansService.deleteLoan(Long.parseLong(loanNumber));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
    }

    @GetMapping("/contact-info")
    public ResponseEntity<ContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(contactInfoDto);
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }
}
