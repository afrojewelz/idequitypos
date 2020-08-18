package tk.lemetweaku.idequitypos.entity;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class Order implements Comparable<Order>, Serializable {

    private static final long serialVersionUID = -3784225532203340315L;

    @Getter
    @Setter
    private static Long transactionID;

    @NotNull(message = "version should not null")
    @DecimalMin("1")
    private Integer tradeID;

    @NotNull(message = "version should not null")
    @DecimalMin("1")
    private Integer version;

    @NotNull(message = "version should not null")
    @DecimalMin("1")
    private Integer quantity;

    @NotBlank(message = "securityCode should not null")
    private String securityCode;

    @NotBlank(message = "command should not null")
    private String command;

    @NotBlank(message = "tradeMark should not null")
    private String tradeMark;


    @Override
    public int compareTo(Order ord) {
        if (this.tradeID == ord.tradeID) {
            return this.version - ord.version;
        } else {
            return this.tradeID - ord.tradeID;
        }
    }

}
