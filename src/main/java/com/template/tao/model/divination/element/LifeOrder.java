package com.template.tao.model.divination.element;

import com.template.tao.model.divination.energy.Form;
import lombok.Data;

@Data(staticConstructor = "of")
public class LifeOrder {

  private final Form energy;
  private final Phase phase;
  private final int index;

  public Element getElement() {
    return Element.of(energy, phase);
  }
}
