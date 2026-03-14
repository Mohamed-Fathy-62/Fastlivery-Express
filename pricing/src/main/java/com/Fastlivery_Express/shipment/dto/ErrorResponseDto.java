package com.Fastlivery_Express.shipment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
@Data
@AllArgsConstructor
public class ErrorResponseDto {
    @Schema(
            description = "Schema to hold API path where error occurred", example = "/api/v1/users"
    )
    private String apiPath;
    @Schema(
            description = "Schema to hold error status code", example = "400 BAD_REQUEST"
    )
    private HttpStatus errorCode;
    @Schema(
            description = "Schema to hold error message"
    )
    private String errorMessage;
    @Schema(
            description = "Schema to hold error timestamp", example = "2024-06-01T12:00:00"
    )
    private LocalDateTime errorTime;
}
