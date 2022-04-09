package parse;

import java.util.*;

public class Analyser {

  private final Map<Record, Integer> counters = new HashMap<>();
  private final Set<Record> errors = new HashSet<>();

  public Analyser(List<Record> dataset) {
    for (Record record : dataset) counters.put(record, 0);
  }

  public void addData(Record record) {
    if (errors.contains(record)) return;
    if (isError(record)) errors.add(record);
    else counters.computeIfPresent(record, (u, c) -> c + 1);
  }

  private boolean isError(Record record) {
    return record == null;
  }

  public Result getResult(Record record) {
    if (errors.contains(record)) return Result.ERROR;
    if (counters.containsKey(record)) {
      if (counters.get(record) < 2) return Result.MATCH;
      return Result.DUPLICATE;
    } else return Result.NO_MATCH;
  }

  public enum Result {
    MATCH, NO_MATCH, DUPLICATE, ERROR
  }
}
