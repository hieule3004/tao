package report;

import java.util.Arrays;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<CapacityReportDataDTO> dataList = Arrays.asList();
    Report report = Report.from(dataList);
    System.out.println(report);
  }
}
