package com.template.tao.model.divination.bookofchanges;

import com.template.tao.utils.MathUtils;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hexagram implements Comparable<Hexagram> {

  public static final int NUM_LINES = 6;

  private final int value;
  @EqualsAndHashCode.Include
  private final int index;
  private final String name;
  private final String judgement;
  private final String image;
  private final List<String> lines;
  private final String judgementComment;
  private final String imageComment;
  private final List<String> lineComments;
  private final Hexagram nuclear;
  private List<Hexagram> symmetries;
  private List<Hexagram> changes;

  @Override
  public int compareTo(Hexagram that) {
    return Integer.compare(this.index, that.index);
  }

  @Override
  public String toString() {
    return String.format("%2d %s", this.index, (char) ('\u4dc0' + this.index - 1));
  }

  public Hexagram getNuclear() {
    return Tuple.of(this.value)
        .map(v -> ((v & 0b011100) << 1) + ((v & 0b001110) >> 1))
        .apply(BookOfChanges::fromLineValue);
  }

  public List<Hexagram> getSymmetries() {
    // chunk of factor
    return Tuple.of(Tuple.of(MathUtils.getCombinations(MathUtils.getFactors(NUM_LINES).reverse()),
                List.ofAll(MathUtils.toBinaryString(this.value, NUM_LINES).chars().mapToObj(i -> i - '0')))
            .apply((combinations, bits) -> combinations
                .map(c -> c.foldLeft(bits, (bs, size) -> bs.grouped(size).map(List::reverse).reduce((List::appendAll))))))
        .apply(ls -> ls.appendAll(ls.map(bs -> bs.map(i -> 1 - i))))
        .map(ls -> ls.reduceLeft((a, b) -> (a << 1) + b))
        .map(BookOfChanges::fromLineValue);
  }

  public List<Hexagram> getChanges() {
    return Stream.iterate(1, i -> i << 1).take(NUM_LINES)
        .map(bm -> BookOfChanges.fromLineValue(this.value ^ bm))
        .distinct()
        .sorted().toList();
  }
}
