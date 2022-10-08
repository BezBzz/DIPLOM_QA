package db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayDto {
    private String id;
    private int amount;
    private Timestamp created;
    private String status;
    private String transaction_id;
}