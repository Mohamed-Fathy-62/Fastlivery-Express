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
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public Page<CustomerDto> searchCustomers(String status, Boolean premium, String email, String name, Pageable pageable) {
        return userRepository.findAll(buildCustomerSpecification(status, premium, email, name), pageable)
                .map(this::requireCustomer)
                .map(CustomerMapper::mapToCustomerDto);
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

    private Specification<User> buildCustomerSpecification(String status, Boolean premium, String email, String name) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            var customerRoot = criteriaBuilder.treat(root, Customer.class);
            predicates.add(root.type().in(Customer.class));

            if (hasText(status)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase()));
            }
            if (premium != null) {
                predicates.add(criteriaBuilder.equal(customerRoot.get("isPremiumMember"), premium));
            }
            if (hasText(email)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (hasText(name)) {
                String searchName = "%" + name.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstname")), searchName),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), searchName)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
