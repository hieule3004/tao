package com.template.tao.utils;

import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoinUtils {

  private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

  public static BitSet toss(int n) {
    return List.ofAll(RANDOM.ints(n).boxed())
        .zipWithIndex()
        .foldLeft(new BitSet(), (s, t) -> {
          if ((t._1 & 1) == 1) s.set(t._2);
          return s;
        });
  }
}
