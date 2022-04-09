package com.template.tao.utils;

import io.vavr.collection.Stream;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Enumeration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeqUtils {

  public static <E> Stream<E> stream(Enumeration<E> el) {
    return Stream.iterate(() -> Option.when(el.hasMoreElements(), el::nextElement));
  }
}
