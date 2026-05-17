package com.Fastlivery_Express.user.mapper;


import com.Fastlivery_Express.user.dto.CustomerDto;
import com.Fastlivery_Express.user.entity.Customer;

public class CustomerMapper {
    public static CustomerDto mapToCustomerDto(Customer customer) {
        if (customer == null) return null;
        CustomerDto customerDto = new CustomerDto();

        customerDto.setUserId(customer.getUserId());
        customerDto.setFirstname(customer.getFirstname());
        customerDto.setLastname(customer.getLastname());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        customerDto.setProfileImageUrl(customer.getProfileImageUrl());
        customerDto.setRole(customer.getRole());
        customerDto.setStatus(customer.getStatus());
        customerDto.setPreferredLanguage(customer.getPreferredLanguage());
        customerDto.setIsVerified(customer.getIsVerified());
        customerDto.setPreferredPaymentMethod(customer.getPreferredPaymentMethod());
        customerDto.setLoyaltyPoints(customer.getLoyaltyPoints());
        customerDto.setTotalOrders(customer.getTotalOrders());
        customerDto.setTotalSpent(customer.getTotalSpent());
        customerDto.setDeliveryAddress(customer.getDeliveryAddress());
        customerDto.setLastOrderTime(customer.getLastOrderTime());
        customerDto.setIsPremiumMember(customer.getIsPremiumMember());
        return customerDto;
    }

     public static Customer mapToCustomer(CustomerDto customerDto) {
         if (customerDto == null) return null;
         Customer customer = new Customer();

         UserMapper.mapUserFields(customerDto, customer);
         mapCustomerFields(customerDto, customer);

         return customer;
     }

     public static void mapCustomerFields(CustomerDto customerDto, Customer customer) {
        if (customerDto == null || customer == null) return;

        customer.setPreferredPaymentMethod(customerDto.getPreferredPaymentMethod());
        customer.setLoyaltyPoints(customerDto.getLoyaltyPoints() != null ? customerDto.getLoyaltyPoints() : 0);
        customer.setTotalOrders(customerDto.getTotalOrders() != null ? customerDto.getTotalOrders() : 0);
        customer.setTotalSpent(customerDto.getTotalSpent() != null ? customerDto.getTotalSpent() : 0.0);
        customer.setDeliveryAddress(customerDto.getDeliveryAddress());
        customer.setLastOrderTime(customerDto.getLastOrderTime());
        customer.setIsPremiumMember(customerDto.getIsPremiumMember() != null ? customerDto.getIsPremiumMember() : Boolean.FALSE);
     }
}
