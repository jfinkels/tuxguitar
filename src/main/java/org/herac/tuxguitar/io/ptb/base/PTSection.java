package org.herac.tuxguitar.io.ptb.base;

import java.util.ArrayList;
import java.util.List;

public class PTSection {

  private int number;
  private List<PTPosition> positions;
  private int staffs;

  public PTSection(int number) {
    this.number = number;
    this.positions = new ArrayList<PTPosition>();
  }

  public int getNextPositionNumber() {
    int next = 0;
    for (final PTPosition p : getPositions()) {
      next = Math.max(next, (p.getPosition() + 1));
    }
    return next;
  }

  public int getNumber() {
    return this.number;
  }

  public PTPosition getPosition(int position) {
    for (final PTPosition p : getPositions()) {
      if (p.getPosition() == position) {
        return p;
      }
    }
    PTPosition p = new PTPosition(position);
    getPositions().add(p);
    return p;
  }

  public List<PTPosition> getPositions() {
    return this.positions;
  }

  public int getStaffs() {
    return this.staffs;
  }

  public void setStaffs(int staffs) {
    this.staffs = staffs;
  }

  public void sort() {
    int count = getPositions().size();
    for (int i = 0; i < count; i++) {
      PTPosition minimum = null;
      for (int j = i; j < count; j++) {
        PTPosition position = (PTPosition) getPositions().get(j);
        if (minimum == null || position.getPosition() < minimum.getPosition()) {
          minimum = position;
        }
      }
      getPositions().remove(minimum);
      getPositions().add(i, minimum);
    }
  }
}
