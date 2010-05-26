package org.herac.tuxguitar.song.models;

public class IntegerPair extends Pair<Integer, Integer> {

  public IntegerPair(final Integer left, final Integer right) {
    super(left, right);
  }

  public boolean isEqual(final IntegerPair other) {
    return other.left.equals(this.left) && other.right.equals(this.right);
  }

}
