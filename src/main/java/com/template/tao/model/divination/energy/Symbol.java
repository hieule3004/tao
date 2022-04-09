package com.template.tao.model.divination.energy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Symbol {

  KUN("GROUND"),
  GEN("MOUNTAIN"),
  KAN("WATER"),
  XUN("WIND"),
  ZHEN("THUNDER"),
  LI("FIRE"),
  DUI("MARSH"),
  QIAN("HEAVEN");

  private final String nature;

  @Override
  public String toString() {
    return String.valueOf((char) ('\u2637' - this.ordinal()));
  }
}
