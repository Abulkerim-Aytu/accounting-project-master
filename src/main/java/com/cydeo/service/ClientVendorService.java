package com.cydeo.service;



import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {

    List<ClientVendorDto> getAllClientVendors(ClientVendorType clientVendorType);
    List<ClientVendorDto> getAll();
    ClientVendorDto findById(Long id);
    ClientVendorDto findByClientVendorName(String username);
    ClientVendorDto saveClientVendor(ClientVendorDto clientVendorDto);
    ClientVendorDto update(Long id,ClientVendorDto clientVendor);
    void delete(Long id);
}