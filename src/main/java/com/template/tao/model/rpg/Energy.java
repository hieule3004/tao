package com.template.tao.model.rpg;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Energy {
  BODY("Physical"), MIND("Mental"), SOUL("Magical");

  private final String power;
}
