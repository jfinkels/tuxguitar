package org.herac.tuxguitar.gui.mixer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.undo.undoables.track.UndoableTrackChannel;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGTrack;

public class TGMixerTrackChannel {

  private class ChannelIndex {
    private int channel;
    private int index;

    public ChannelIndex(int index, int channel) {
      this.index = index;
      this.channel = channel;
    }

    public int getChannel() {
      return this.channel;
    }

    public int getIndex() {
      return this.index;
    }
  }

  private class ChannelList {
    private List<ChannelIndex> channelIndexs;

    public ChannelList() {
      this.channelIndexs = new ArrayList<ChannelIndex>();
    }

    public void addChannel(int index, int channel) {
      this.channelIndexs.add(new ChannelIndex(index, channel));
    }

    public void clear() {
      this.channelIndexs.clear();
    }

    public int getChannel(int index) {
      for (final ChannelIndex channelIndex : this.channelIndexs) {
        if (index == channelIndex.getIndex()) {
          return channelIndex.getChannel();
        }
      }
      return -1;
    }
  }

  protected Combo effectChannel;
  protected ChannelList effectChannels;
  private TGMixerTrack mixer;

  protected Combo normalChannel;

  protected ChannelList normalChannels;

  public TGMixerTrackChannel(TGMixerTrack mixer) {
    this.mixer = mixer;
  }

  public void fireChannelChange(TGChannel channel) {
    this.mixer.getMixer().fireChanges(channel, TGMixer.CHANNEL);
  }

  protected TGChannel getChannel() {
    return this.mixer.getTrack().getChannel();
  }

  protected TGTrack getTrack() {
    return this.mixer.getTrack();
  }

  public void init(Composite parent) {
    this.normalChannels = new ChannelList();
    this.normalChannel = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
    this.normalChannel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    this.effectChannels = new ChannelList();
    this.effectChannel = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
    this.effectChannel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    this.normalChannel.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        UndoableTrackChannel undoable = UndoableTrackChannel.startUndo();

        TGChannel channel = getTrack().getChannel();
        boolean[] usedChannels = TuxGuitar.instance().getSongManager()
            .getUsedChannels();
        int idx = TGMixerTrackChannel.this.normalChannels
            .getChannel(TGMixerTrackChannel.this.normalChannel
                .getSelectionIndex());
        if (!usedChannels[idx]) {
          channel.setChannel((short) idx);
          channel.setEffectChannel(channel.getChannel());
        } else {
          channel = TuxGuitar.instance().getSongManager().getUsedChannel(idx);
        }
        fireChannelChange(channel);

        TuxGuitar.instance().getUndoableManager().addEdit(undoable.endUndo());
        TuxGuitar.instance().getFileHistory().setUnsavedFile();
        TuxGuitar.instance().getTable().fireUpdate(false);
        TuxGuitar.instance().updateCache(true);
      }
    });

    this.effectChannel.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        UndoableTrackChannel undoable = UndoableTrackChannel.startUndo();

        TGChannel channel = getTrack().getChannel();
        int idx = TGMixerTrackChannel.this.effectChannels
            .getChannel(TGMixerTrackChannel.this.effectChannel
                .getSelectionIndex());
        channel.setEffectChannel((short) idx);
        fireChannelChange(channel);

        TuxGuitar.instance().getUndoableManager().addEdit(undoable.endUndo());
        TuxGuitar.instance().getFileHistory().setUnsavedFile();
        TuxGuitar.instance().updateCache(true);
      }
    });

    this.updateItems(true);
  }

  private void updateEffectChannel() {
    this.effectChannel.removeAll();
    this.effectChannels.clear();

    boolean[] usedChannels = TuxGuitar.instance().getSongManager()
        .getUsedChannels();
    boolean[] usedEffectChannels = TuxGuitar.instance().getSongManager()
        .getUsedEffectChannels();
    if (getChannel().isPercussionChannel()) {
      this.effectChannel.add(Integer
          .toString(TGChannel.DEFAULT_PERCUSSION_CHANNEL));
      this.effectChannel.select(0);
      this.effectChannels.addChannel(0, 0);
    } else {
      int itemIndex = 0;
      for (int i = 0; i < usedEffectChannels.length; i++) {
        if (!TGChannel.isPercussionChannel(i)) {
          if ((!usedEffectChannels[i] || getChannel().getEffectChannel() == i)
              && (!usedChannels[i] || i == getChannel().getChannel())) {
            this.effectChannel.add(Integer.toString(i));

            if (i == getChannel().getEffectChannel()) {
              this.effectChannel.select(itemIndex);
            }
            this.effectChannels.addChannel(itemIndex, i);
            itemIndex++;
          }
        }
      }
    }
  }

  public void updateItems(boolean reload) {
    if (reload) {
      this.updateNormalChannel();
      this.updateEffectChannel();
    }
    this.normalChannel
        .setEnabled(!TuxGuitar.instance().getPlayer().isRunning());
    this.effectChannel
        .setEnabled(!TuxGuitar.instance().getPlayer().isRunning());
  }

  private void updateNormalChannel() {
    this.normalChannel.removeAll();
    this.normalChannels.clear();

    boolean[] usedChannels = TuxGuitar.instance().getSongManager()
        .getUsedChannels();
    boolean[] usedEffectChannels = TuxGuitar.instance().getSongManager()
        .getUsedEffectChannels();
    if (getChannel().isPercussionChannel()) {
      this.normalChannel.add(Integer
          .toString(TGChannel.DEFAULT_PERCUSSION_CHANNEL));
      this.normalChannel.select(0);
      this.normalChannels.addChannel(0, 0);
    } else {
      int itemIndex = 0;
      for (int i = 0; i < usedChannels.length; i++) {
        if (!TGChannel.isPercussionChannel(i)) {
          if (!usedEffectChannels[i]
              || (usedEffectChannels[i] && usedChannels[i])
              || (getChannel().getChannel() == i && getChannel()
                  .getEffectChannel() == getChannel().getChannel())) {
            String itemName = new String();
            if (usedChannels[i]
                && (getChannel().getChannel() != i || TuxGuitar.instance()
                    .getSongManager().countTracksForChannel(i) > 1)) {
              itemName = i + " " + TuxGuitar.getProperty("mixer.channel.link");
            } else {
              itemName = i + " " + TuxGuitar.getProperty("mixer.channel.free");
            }
            this.normalChannel.add(itemName);

            if (i == getChannel().getChannel()) {
              this.normalChannel.select(itemIndex);
            }
            this.normalChannels.addChannel(itemIndex, i);
            itemIndex++;
          }
        }
      }
    }
  }
}
