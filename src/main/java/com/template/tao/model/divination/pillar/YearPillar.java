package com.template.tao.model.divination.pillar;

import com.template.tao.model.divination.element.Element;
import com.template.tao.model.divination.element.LifeOrder;
import com.template.tao.model.divination.element.Phase;
import com.template.tao.model.divination.energy.Form;
import com.template.tao.utils.MathUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class YearPillar {

  private static final int ORIGIN_YEAR = 1984;

  private static final Phase[] SECRET = {Phase.JIN, Phase.TU, Phase.SHUI, Phase.MU, Phase.HUO};
  private static final LifeOrder[] CYCLE = new LifeOrder[60];

  static {
    for (int i = 0; i < CYCLE.length; i++) {
      int core = i / 4;
      int row = core / 3;
      int col = core % 3;
      Form energy = Form.values()[(i & 1) ^ 1];
      Phase phase = SECRET[MathUtils.mod(row - col, SECRET.length)];
      CYCLE[i] = LifeOrder.of(energy, phase, col);
    }
    System.out.println();
  }

  private int year;

  public Element getElement() {
    return CYCLE[(this.year - ORIGIN_YEAR) % CYCLE.length].getElement();
  }
}
