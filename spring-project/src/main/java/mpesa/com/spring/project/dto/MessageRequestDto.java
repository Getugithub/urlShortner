package mpesa.com.spring.project.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MessageRequestDto {
    private String requestRefid;
    private String content;
    private String sender;
    private List<ReceiverDto> receivers;
}