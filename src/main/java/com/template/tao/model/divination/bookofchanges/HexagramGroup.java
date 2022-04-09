package com.template.tao.model.divination.bookofchanges;

import com.template.tao.utils.BitUtils;
import io.vavr.Tuple;
import io.vavr.collection.List;
import lombok.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HexagramGroup {

  public static final HexagramGroup NULL = new HexagramGroup(0, List.empty());
  private static int generatorId = 0;

  @EqualsAndHashCode.Include
  private final int index;
  private final List<Item> group;

  public HexagramGroup(List<Hexagram> group) {
    this(++generatorId, group.zipWithIndex(Item::new));
  }

  /**
   * POsitive semi shpere
   * All hexagram in a group belong to same row
   */
  public int getRow() {
    return Tuple.of(BitUtils.asBits(this.group.get().hexagram.getValue()).cardinality())
        .map(cardinality -> Math.min(cardinality, Hexagram.NUM_LINES - cardinality))
        ._1;
  }

  @Override
  public String toString() {
    return String.format("%2d", this.index);
//      return String.format("%2d (%1d) [%s]", this.index, this.getRow(),
//          this.group
//              .distinct().sorted()
////              .map(h -> String.format("%2d %s", h.index, MathUtils.toBinaryString(h.value, 6)))
//              .map(String::valueOf)
//              .collect(Collectors.joining(", ")));
  }

  @Getter
  @AllArgsConstructor
  public static class Item {

    private final Hexagram hexagram;
    private final int index;
  }
}
