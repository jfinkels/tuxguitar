package org.herac.tuxguitar.song.models;

public class Pair<S, T> {
  protected S left;
  protected T right;
  
  public Pair(final S left, final T right){
    this.left = left;
    this.right = right;
  }
  
}
