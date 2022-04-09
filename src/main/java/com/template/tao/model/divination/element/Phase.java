package com.template.tao.model.divination.element;

import com.template.tao.utils.MathUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Phase {

  MU("木"),
  HUO("火"),
  TU("土"),
  JIN("金"),
  SHUI("水");

  private final String symbol;

  @Override
  public String toString() {
    return this.symbol;
  }

  private Phase jump(int offset) {
    return values()[MathUtils.mod(this.ordinal() + offset, values().length)];
  }

  public Phase created() {
    return this.jump(1);
  }

  public Phase enhanced() {
    return this.jump(-1);
  }

  public Phase destroyed() {
    return this.jump(2);
  }

  public Phase weakened() {
    return this.jump(-2);
  }
}
