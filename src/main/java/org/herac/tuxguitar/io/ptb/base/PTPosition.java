package org.herac.tuxguitar.io.ptb.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PTPosition {

  private List<PTComponent> components;
  private int position;

  public PTPosition(int position) {
    this.position = position;
    this.components = new ArrayList<PTComponent>();
  }

  public void addComponent(PTComponent component) {
    this.components.add(component);
  }

  public List<PTComponent> getComponents() {
    return this.components;
  }

  public int getPosition() {
    return this.position;
  }

  public void sort() {
    Collections.sort(getComponents(), new Comparator<PTComponent>() {
      public int compare(PTComponent o1, PTComponent o2) {
        if (o1 instanceof PTDirection) {
          return 1;
        }
        if (o2 instanceof PTDirection) {
          return -1;
        }
        return 0;
      }
    });
  }
}
