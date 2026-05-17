package com.Fastlivery_Express.user.service.impl;

import com.Fastlivery_Express.user.dto.CustomerDto;
import com.Fastlivery_Express.user.entity.Customer;
import com.Fastlivery_Express.user.entity.User;
import com.Fastlivery_Express.user.exception.UserAlreadyExistsException;
import com.Fastlivery_Express.user.exception.UserEmailAlreadyUsed;
import com.Fastlivery_Express.user.exception.UserNotFoundException;
import com.Fastlivery_Express.user.mapper.CustomerMapper;
import com.Fastlivery_Express.user.mapper.UserMapper;
import com.Fastlivery_Express.user.repository.UserRepository;
import com.Fastlivery_Express.user.service.ICustomerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class CustomerServiceImpl implements ICustomerService {

    private UserRepository userRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        validateNewUser(customerDto.getEmail(), customerDto.getMobileNumber());
        if (customerDto.getRole() == null) {
            customerDto.setRole("ROLE_CUSTOMER");
        }
        Customer savedCustomer = userRepository.save(CustomerMapper.mapToCustomer(customerDto));
        return CustomerMapper.mapToCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDto getCustomerById(String userId) {
        return userRepository.findByUserId(userId)
                .map(this::requireCustomer)
                .map(CustomerMapper::mapToCustomerDto)
                .orElseThrow(() -> new UserNotFoundException("Customer with id " + userId + " not found."));
    }

    @Override
    public CustomerDto getCustomerByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::requireCustomer)
                .map(CustomerMapper::mapToCustomerDto)
                .orElseThrow(() -> new UserNotFoundException("Customer with email " + email + " not found."));
    }

    @Override
    public boolean updateCustomer(String email, CustomerDto customerDto) {
        Customer customer = userRepository.findByEmail(email)
                .map(this::requireCustomer)
                .orElseThrow(() -> new UserNotFoundException("Customer with email " + email + " not found."));

        validateUniqueEmailForUpdate(email, customerDto.getEmail());
        validateUniqueMobileForUpdate(customer.getMobileNumber(), customerDto.getMobileNumber());
        UserMapper.mapUserFields(customerDto, customer);
        CustomerMapper.mapCustomerFields(customerDto, customer);
        userRepository.save(customer);
        return true;
    }

    @Override
    public boolean deleteCustomer(String email) {
        Customer customer = userRepository.findByEmail(email)
                .map(this::requireCustomer)
                .orElseThrow(() -> new UserNotFoundException("Customer with email " + email + " not found."));
        userRepository.delete(customer);
        return true;
    }

    private Customer requireCustomer(User user) {
        if (user instanceof Customer customer) {
            return customer;
        }
        throw new UserNotFoundException("The requested user is not a customer.");
    }

    private void validateNewUser(String email, String mobileNumber) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }
        if (userRepository.findByMobileNumber(mobileNumber).isPresent()) {
            throw new UserAlreadyExistsException("User with mobile number " + mobileNumber + " already exists.");
        }
    }

    private void validateUniqueEmailForUpdate(String currentEmail, String newEmail) {
        if (newEmail == null || newEmail.equals(currentEmail)) return;
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new UserEmailAlreadyUsed("User with email " + newEmail + " already exists.");
        }
    }

    private void validateUniqueMobileForUpdate(String currentMobileNumber, String newMobileNumber) {
        if (newMobileNumber == null || newMobileNumber.equals(currentMobileNumber)) return;
        if (userRepository.findByMobileNumber(newMobileNumber).isPresent()) {
            throw new UserAlreadyExistsException("User with mobile number " + newMobileNumber + " already exists.");
        }
    }
}
