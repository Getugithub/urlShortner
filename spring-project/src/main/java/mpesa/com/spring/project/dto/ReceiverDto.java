package mpesa.com.spring.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiverDto {
    private String phoneNumber;
}