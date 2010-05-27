package org.herac.tuxguitar.gui.editors.tab;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.editors.tab.layout.ViewLayout;
import org.herac.tuxguitar.song.models.Clef;
import org.herac.tuxguitar.song.models.Direction;
import org.herac.tuxguitar.song.models.TGVoice;

public class TGBeatGroup {

  private static final int DOWN_OFFSET = 35;
  private static final int SCORE_FLAT_POSITIONS[] = new int[] { 7, 6, 6, 5, 5,
      4, 3, 3, 2, 2, 1, 1 };
  private static final int SCORE_MIDDLE_KEYS[] = new int[] { 55, 40, 40, 50 };

  private static final int SCORE_SHARP_POSITIONS[] = new int[] { 7, 7, 6, 6, 5,
      4, 4, 3, 3, 2, 2, 1 };
  private static final int UP_OFFSET = 28;

  public static float getDownOffset(ViewLayout layout) {
    float scale = (layout.getScoreLineSpacing() / 8.0f);
    return (DOWN_OFFSET * scale);
  }

  public static float getUpOffset(ViewLayout layout) {
    float scale = (layout.getScoreLineSpacing() / 8.0f);
    return (UP_OFFSET * scale);
  }

  private Direction direction=Direction.NONE;
  private TGNoteImpl firstMaxNote=null;
  private TGNoteImpl firstMinNote=null;
  private TGNoteImpl lastMaxNote=null;
  private TGNoteImpl lastMinNote=null;
  private TGNoteImpl maxNote=null;
  private TGNoteImpl minNote=null;

  private int voice;

  private List<TGVoice> voices=new ArrayList<TGVoice>();

  public TGBeatGroup(int voice) {
    this.voice = voice;
  }

  private void check(TGNoteImpl note) {
    int value = note.getRealValue();

    // FIRST MIN NOTE
    if (this.firstMinNote == null
        || note.getVoice().getBeat().getStart() < this.firstMinNote.getVoice()
            .getBeat().getStart()) {
      this.firstMinNote = note;
    } else if (note.getVoice().getBeat().getStart() == this.firstMinNote
        .getVoice().getBeat().getStart()) {
      if (note.getRealValue() < this.firstMinNote.getRealValue()) {
        this.firstMinNote = note;
      }
    }
    // FIRST MAX NOTE
    if (this.firstMaxNote == null
        || note.getVoice().getBeat().getStart() < this.firstMaxNote.getVoice()
            .getBeat().getStart()) {
      this.firstMaxNote = note;
    } else if (note.getVoice().getBeat().getStart() == this.firstMaxNote
        .getVoice().getBeat().getStart()) {
      if (note.getRealValue() > this.firstMaxNote.getRealValue()) {
        this.firstMaxNote = note;
      }
    }

    // LAST MIN NOTE
    if (this.lastMinNote == null
        || note.getVoice().getBeat().getStart() > this.lastMinNote.getVoice()
            .getBeat().getStart()) {
      this.lastMinNote = note;
    } else if (note.getVoice().getBeat().getStart() == this.lastMinNote
        .getVoice().getBeat().getStart()) {
      if (note.getRealValue() < this.lastMinNote.getRealValue()) {
        this.lastMinNote = note;
      }
    }
    // LAST MIN NOTE
    if (this.lastMaxNote == null
        || note.getVoice().getBeat().getStart() > this.lastMaxNote.getVoice()
            .getBeat().getStart()) {
      this.lastMaxNote = note;
    } else if (note.getVoice().getBeat().getStart() == this.lastMaxNote
        .getVoice().getBeat().getStart()) {
      if (note.getRealValue() > this.lastMaxNote.getRealValue()) {
        this.lastMaxNote = note;
      }
    }

    if (this.maxNote == null || value > this.maxNote.getRealValue()) {
      this.maxNote = note;
    }
    if (this.minNote == null || value < this.minNote.getRealValue()) {
      this.minNote = note;
    }
  }

  public void check(TGVoiceImpl voice) {
    this.check(voice.getMaxNote());
    this.check(voice.getMinNote());
    this.voices.add(voice);
    if (voice.getDirection() != Direction.NONE) {
      if (voice.getDirection() == Direction.UP) {
        this.direction = Direction.UP;
      } else if (voice.getDirection() == Direction.DOWN) {
        this.direction = Direction.DOWN;
      }
    }
  }

  public void finish(ViewLayout layout, TGMeasureImpl measure) {
    if (this.direction == Direction.NONE) {
      if (measure.getNotEmptyVoices() > 1) {
        this.direction = this.voice == 0 ? Direction.UP : Direction.DOWN;
      } else if ((layout.getStyle() & ViewLayout.DISPLAY_SCORE) == 0) {
        this.direction = Direction.DOWN;
      } else {
        int selection = 0;
        switch (measure.getClef()) {
        case ALTO:
          selection = 3;
          break;
        case BASS:
          selection = 1;
          break;
        case TENOR:
          selection = 2;
          break;
        case TREBLE:
          selection = 0;
          break;
        }
        int max = Math.abs(this.minNote.getRealValue()
            - (SCORE_MIDDLE_KEYS[selection] + 100));
        int min = Math.abs(this.maxNote.getRealValue()
            - (SCORE_MIDDLE_KEYS[selection] - 100));
        if (max > min) {
          this.direction = Direction.UP;
        } else {
          this.direction = Direction.DOWN;
        }
      }
    }
  }

  public Direction getDirection() {
    return this.direction;
  }

  public TGNoteImpl getMaxNote() {
    return this.maxNote;
  }

  public TGNoteImpl getMinNote() {
    return this.minNote;
  }

  public List<TGVoice> getVoices() {
    return this.voices;
  }

  public int getY1(ViewLayout layout, TGNoteImpl note, int key, Clef clef) {
    double scale = (layout.getScoreLineSpacing() / 2.00);
    int noteValue = note.getRealValue();

    int scoreLineY = 0;
    if (key <= 7) {
      scoreLineY = (int) ((SCORE_SHARP_POSITIONS[noteValue % 12]) * scale - (7 * (noteValue / 12))
          * scale);
    } else {
      scoreLineY = (int) ((SCORE_FLAT_POSITIONS[noteValue % 12]) * scale - (7 * (noteValue / 12))
          * scale);
    }

    int selection = 0;
    switch (clef) {
    case ALTO:
      selection = 3;
      break;
    case BASS:
      selection = 1;
      break;
    case TENOR:
      selection = 2;
      break;
    case TREBLE:
      selection = 0;
      break;
    }

    scoreLineY += TGMeasureImpl.SCORE_KEY_OFFSETS[selection] * scale;

    return scoreLineY;
  }

  public int getY2(ViewLayout layout, int x, int key, Clef clef) {
    int maxDistance = 10;
    float upOffset = TGBeatGroup.getUpOffset(layout);
    float downOffset = TGBeatGroup.getDownOffset(layout);
    if (this.direction == Direction.NONE) {
      if (this.minNote != this.firstMinNote && this.minNote != this.lastMinNote) {
        return (int) (getY1(layout, this.minNote, key, clef) + downOffset);
      }

      int y = 0;
      int x1 = this.firstMinNote.getPosX()
          + this.firstMinNote.getBeatImpl().getSpacing();
      int x2 = this.lastMinNote.getPosX()
          + this.lastMinNote.getBeatImpl().getSpacing();
      int y1 = (int) (getY1(layout, this.firstMinNote, key, clef) + downOffset);
      int y2 = (int) (getY1(layout, this.lastMinNote, key, clef) + downOffset);

      if (y1 > y2 && (y1 - y2) > maxDistance)
        y2 = (y1 - maxDistance);
      if (y2 > y1 && (y2 - y1) > maxDistance)
        y1 = (y2 - maxDistance);

      // int y = (int)((((double)y1 -(double)y2) / ((double)x1 - (double)x2)) *
      // ((double)x1 - (double)x));
      if ((y1 - y2) != 0 && (x1 - x2) != 0 && (x1 - x) != 0) {
        y = (int) ((((double) y1 - (double) y2) / ((double) x1 - (double) x2)) * ((double) x1 - (double) x));
      }
      return y1 - y;
    } else if (this.maxNote != this.firstMaxNote
        && this.maxNote != this.lastMaxNote) {
      return (int) (getY1(layout, this.maxNote, key, clef) - upOffset);
    } else {
      int y = 0;
      int x1 = this.firstMaxNote.getPosX()
          + this.firstMaxNote.getBeatImpl().getSpacing();
      int x2 = this.lastMaxNote.getPosX()
          + this.lastMaxNote.getBeatImpl().getSpacing();
      int y1 = (int) (getY1(layout, this.firstMaxNote, key, clef) - upOffset);
      int y2 = (int) (getY1(layout, this.lastMaxNote, key, clef) - upOffset);

      if (y1 < y2 && (y2 - y1) > maxDistance)
        y2 = (y1 + maxDistance);
      if (y2 < y1 && (y1 - y2) > maxDistance)
        y1 = (y2 + maxDistance);

      if ((y1 - y2) != 0 && (x1 - x2) != 0 && (x1 - x) != 0) {
        y = (int) ((((double) y1 - (double) y2) / ((double) x1 - (double) x2)) * ((double) x1 - (double) x));
      }
      return y1 - y;
    }
  }
}
