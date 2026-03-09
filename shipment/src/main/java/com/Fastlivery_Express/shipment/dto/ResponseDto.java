package com.Fastlivery_Express.shipment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
@Data
@AllArgsConstructor
public class ResponseDto {
    @Schema(
            description = "Schema to hold response status code", example = "200 OK"
    )
    private HttpStatus statusCode;
    @Schema(
            description = "Schema to hold response message", example = "User created successfully"
    )
    private String message;
}
