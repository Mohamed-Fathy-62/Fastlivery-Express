package com.Fastlivery_Express.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(
        name = "CustomerDto",
        description = "Schema to hold CustomerDto information")
public class CustomerDto extends UserDto{
    private String preferredPaymentMethod;
    private String deliveryAddress;
    private Integer loyaltyPoints;
    private Integer totalOrders;
    private Double totalSpent;
    private String lastOrderTime;
    private Boolean isPremiumMember;
}
