package com.template.tao.model.divination.energy;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Phenomena {

  TAIYIN(  "——— x ———"),
  SHAOYANG("—————————"),
  SHAOYIN( "———   ———"),
  TAIYANG( "————⊖————")
  ;

  private String representation;

  public int getValue() {
    return this.ordinal() + 6;
  }

  public static Phenomena fromValue(int value) {
    return Try.of(() -> values()[value - 6]).getOrNull();
  }

  public Form getForm() {
    return Form.values()[this.ordinal() & 1];
  }

  public boolean isChange() {
    return this.getValue() % 3 == 0;
  }

  @Override
  public String toString() {
    return String.valueOf((char) ('\u268f' - this.ordinal()));
  }
}
