package com.Fastlivery_Express.user.service;

import com.Fastlivery_Express.user.dto.CustomerDto;

public interface ICustomerService {
    CustomerDto createCustomer(CustomerDto customerDto);
    CustomerDto getCustomerById(String userId);
    CustomerDto getCustomerByEmail(String email);
    boolean updateCustomer(String email, CustomerDto customerDto);
    boolean deleteCustomer(String email);
}
