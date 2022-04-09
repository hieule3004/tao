package report;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Report {

  public static final Report DUMMY = new Report(LocalDate.now(), LocalDate.now(), Collections.emptyList());

  private final LocalDate firstMonth;
  private final LocalDate lastMonth;
  private final MonthReport[] monthReports;

  private Report(LocalDate firstMonth, LocalDate lastMonth, List<CapacityReportDataDTO> dataList) {
    this.firstMonth = firstMonth;
    this.lastMonth = lastMonth;
    this.monthReports = new MonthReport[getMonthsBetween(firstMonth, lastMonth) + 1];
    this.monthReports[0] = MonthReport.DUMMY;

    for (CapacityReportDataDTO data : dataList) addData(data);
    calculateStock();
  }

  public static Report from(List<CapacityReportDataDTO> dataList) {
    if (dataList.isEmpty()) return DUMMY;
    dataList.sort(Comparator.comparing(Report::getDate));
    LocalDate firstMonth = getDate(dataList.get(0));
    LocalDate lastMonth = getDate(dataList.get(dataList.size() - 1));
    return new Report(firstMonth, lastMonth, dataList);
  }

  private static LocalDate getDate(CapacityReportDataDTO data) {
    Instant instant = data.getActualDate() == null ? data.getRequestDate() : data.getActualDate();
    if (instant == null) throw new UnsupportedOperationException("This cannot happen by SRS");
    return instant.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate().withDayOfMonth(1);
  }

  private void addData(CapacityReportDataDTO data) {
    MonthReport monthReport = getMonth(data);
    monthReport.addData(data);
  }

  private MonthReport getMonth(CapacityReportDataDTO data) {
    LocalDate date = getDate(data);
    int index = getMonthsBetween(firstMonth, date);
    if (monthReports[index] == null) monthReports[index] = MonthReport.of(date);
    return monthReports[index];
  }

  private int getMonthsBetween(LocalDate date1, LocalDate date2) {
    return (int) ChronoUnit.MONTHS.between(date1, date2);
  }

  private void calculateStock() {
    Map<String, MonthReport> prevMap = new HashMap<>();

    for (MonthReport monthReport : monthReports) {
      for (String saleGroup : monthReport.getSaleGroupsWithTotal()) {
        MonthReport prev = prevMap.computeIfAbsent(saleGroup, code -> MonthReport.DUMMY);
        monthReport.calculateStock(saleGroup, prev);
        prevMap.put(saleGroup, monthReport);
      }
    }
  }

  @Override
  public String toString() {
    String rowSep = "+---------+------------+-----------------+-------------+----------+-----------+-----------+";
    StringJoiner joiner = new StringJoiner("\n")
        .add(rowSep)
        .add("|  Cycle  | Sale Group | Sale Group Name | Begin Stock | Total In | Total Out | End Stock |")
        .add(rowSep);
    for (MonthReport monthReport : monthReports) {
      joiner.add(monthReport.toString()).add(rowSep);
    }
    return joiner.toString();
  }
}
