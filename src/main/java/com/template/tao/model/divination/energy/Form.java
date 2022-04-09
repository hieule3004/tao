package com.template.tao.model.divination.energy;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.BitSet;

@Getter
@AllArgsConstructor
public enum Form {

  YIN("-",  "———   ———"),
  YANG("+", "—————————"),
  ;

  private final String sign;
  private final String representation;

  @Override
  public String toString() {
    return this.sign;
//    return String.valueOf((char) ('\u268b' - this.ordinal()));
  }
}
