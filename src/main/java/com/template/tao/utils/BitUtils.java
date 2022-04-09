package com.template.tao.utils;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.BitSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BitUtils {

  public static BitSet asBits(int value) {
    return BitSet.valueOf(new long[]{value});
  }

  public static int asInt(BitSet bits) {
    return Option.of(bits)
        .filterNot(BitSet::isEmpty)
        .map(bitSet -> (int) bitSet.toByteArray()[0])
        .getOrElse(0);
  }
}
