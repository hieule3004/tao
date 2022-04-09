package report;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class GroupReport {

  private final String saleGroupCode;
  private final String saleGroupName;
  private int beginStock = 10;
  private int totalIn;
  private int totalOut;
  private int endStock;

  /*package*/ void addData(CapacityReportDataDTO data) {
    switch (data.getType()) {
      case "in":
        totalIn += data.getTotalQuantity();
        break;
      case "out":
        totalOut += data.getTotalQuantity();
        break;
      default:
        // silent pass
        break;
    }
  }

  /*package*/ void calculateStock(MonthReport prevMonthReport) {
    GroupReport prevGroupReport = prevMonthReport.getReportBySaleGroupCode(saleGroupCode);
    beginStock = prevGroupReport.endStock;
    endStock = beginStock + totalIn - totalOut;
  }

  @Override
  public String toString() {
    return String.format(" %10s | %15s | %11s | %8s | %9s | %9s |",
        saleGroupCode, saleGroupName, beginStock, totalIn, totalOut, endStock);
  }
}
