package report;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class CapacityReportDataDTO implements Serializable {
  String type;
  Long saleGroupId;
  String saleGroupName;
  String saleGroupCode;
  Instant actualDate;
  Instant requestDate;
  Double totalQuantity;
}
