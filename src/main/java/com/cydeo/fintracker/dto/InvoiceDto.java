package com.cydeo.fintracker.dto;
import com.cydeo.fintracker.enums.InvoiceStatus;
import com.cydeo.fintracker.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InvoiceDto {

    private Long id;
    private String invoiceNo;
    private InvoiceStatus invoiceStatus;
    private InvoiceType invoiceType;
    private LocalDate date;
    private CompanyDto company;
    @NotNull(message = "Client vendor is a required field.")
    private ClientVendorDto clientVendor;
    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal total;
}
