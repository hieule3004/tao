package com.template.tao.model.divination.element;

import com.template.tao.model.divination.energy.Form;
import lombok.Data;

@Data(staticConstructor = "of")
public class Element {

  private final Form energy;
  private final Phase phase;

  @Override
  public String toString() {
    return String.format("%s%s", this.energy, this.phase);
  }
}
