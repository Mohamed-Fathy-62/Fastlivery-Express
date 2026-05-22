package com.Fastlivery_Express.user.service;

import com.Fastlivery_Express.user.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);
    Page<CustomerDto> searchCustomers(String status, Boolean premium, String email, String name, Pageable pageable);
    CustomerDto getCustomerById(String userId);
    CustomerDto getCustomerByEmail(String email);
    boolean updateCustomer(String email, CustomerDto customerDto);
    boolean deleteCustomer(String email);
}
