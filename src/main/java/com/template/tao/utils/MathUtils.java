package com.template.tao.utils;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathUtils {

  public static Tuple2<Integer, Integer> divMod(int dividend, int divisor) {
    return Tuple.of(dividend / divisor)
        .apply(quotient -> Tuple.of(quotient, dividend - quotient * divisor));
  }

  public static int mod(int number, int modulo) {
    return (number % modulo + modulo) % modulo;
  }

  public static String toBinaryString(int value, int length) {
    return Integer.toBinaryString((1 << length) | value).substring(1);
  }

  public static List<Integer> getFactors(int n) {
    return List.rangeClosed(2, n / 2).filter(i -> n % i == 0).append(n);
  }

  public static List<List<Integer>> getCombinations(List<Integer> list) {
    return Stream.iterate(1, i -> i << 1).take(list.size()).combinations()
        .map(bs -> bs.foldLeft(0, Integer::sum)).toList()
        .zip(list.combinations())
        .sorted(Comparator.comparing(Tuple2::_1))
        .map(Tuple2::_2);
  }
}
