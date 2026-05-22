package com.Fastlivery_Express.user.controller;

import com.Fastlivery_Express.user.dto.ApiResponse;
import com.Fastlivery_Express.user.dto.CustomerDto;
import com.Fastlivery_Express.user.service.ICustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Customer in Fastlivery Express",
        description = "CRUD REST APIs in Fastlivery to CREATE, UPDATE, FETCH AND DELETE account details of Customer"
)
@RestController
@RequestMapping(path = "/api/v1/customers", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class CustomerController {

    private ICustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto created = customerService.createCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Customer created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerDto>>> searchCustomers(@RequestParam(required = false) String status,
                                                                         @RequestParam(required = false) Boolean premium,
                                                                         @RequestParam(required = false) String email,
                                                                         @RequestParam(required = false) String name,
                                                                         @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Customers fetched successfully",
                customerService.searchCustomers(status, premium, email, name, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerById(@Valid @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customerService.getCustomerById(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerByEmail(@Valid @PathVariable String email) {
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customerService.getCustomerByEmail(email)));
    }

    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(@Valid @PathVariable String email,
                                                                   @Valid @RequestBody CustomerDto customerDto) {
        customerService.updateCustomer(email, customerDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success("Customer updated successfully", customerDto));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@Valid @PathVariable String email) {
        customerService.deleteCustomer(email);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", null));
    }
}
