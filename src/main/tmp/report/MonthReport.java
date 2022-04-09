package report;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor(staticName = "of")
public class MonthReport {

  public static final MonthReport DUMMY = MonthReport.of(LocalDate.now());
  private static final String TOTAL_CODE = "";

  private final LocalDate date;
  private final Map<String, GroupReport> groupReports = new HashMap<>(
      Collections.singletonMap(TOTAL_CODE, GroupReport.of(TOTAL_CODE, "Total")));

  public Iterable<String> getSaleGroupsWithTotal() {
    Set<String> strings = new HashSet<>(groupReports.keySet());
    strings.add(TOTAL_CODE);
    return strings;
  }

  /*package*/ void addData(CapacityReportDataDTO data) {
    GroupReport groupReport = groupReports.computeIfAbsent(data.getSaleGroupCode(),
        code -> GroupReport.of(code, data.getSaleGroupName()));
    groupReport.addData(data);
  }

  /*package*/ void calculateStock(String saleGroup, MonthReport prev) {
    GroupReport groupReport = groupReports.get(saleGroup);
    groupReport.calculateStock(prev);
  }

  /*package*/ GroupReport getReportBySaleGroupCode(String saleGroupCode) {
    return groupReports.get(saleGroupCode);
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    String top = String.format("%02d/%4d", date.getMonthValue(), date.getYear());
    for (GroupReport groupReport : groupReports.values()) {
      joiner.add(String.format("| %7s |%s", top, groupReport));
      top = "";
    }
    return joiner.toString();
  }
}
