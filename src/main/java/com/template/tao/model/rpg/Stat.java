package com.template.tao.model.rpg;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Stat {
  QUA("Quantity", Type.RECOVERY, Quantity.LIMIT),
  CON("Constitution", Type.RECOVERY, Quantity.QUALITY),
  STA("Stamina", Type.RECOVERY, Quantity.RATE),
  STR("Strength", Type.CONVERSION, Quantity.LIMIT),
  AFF("Affinity", Type.CONVERSION, Quantity.QUALITY),
  DEX("Dexterity", Type.CONVERSION, Quantity.RATE),
  ;

  private final String name;
  private final Type type;
  private final Quantity quantity;

  public static Stat from(Type type, Quantity quantity) {
    return Stat.values()[type.ordinal() * Type.values().length + quantity.ordinal()];
  }

  @Getter
  @AllArgsConstructor
  public enum Type {
    RECOVERY("Input"), CONVERSION("Output");

    private final String effect;
  }

  public enum Quantity {
    LIMIT, QUALITY, RATE
  }
}
