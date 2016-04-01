package games.strategy.sound;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import games.strategy.engine.data.properties.IEditableProperty;
import games.strategy.engine.framework.ui.PropertiesSelector;
import games.strategy.sound.SoundPath.SoundType;

/**
 * Sound option window framework.
 */
public final class SoundOptions {
  final ClipPlayer m_clipPlayer;

  /**
   * @param parentMenu
   *        menu where to add the menu item "Sound Options..."
   */
  public static void addToMenu(final JMenu parentMenu, final SoundType soundType) {
    final JMenuItem soundOptions = new JMenuItem("Sound Options...");
    soundOptions.setMnemonic(KeyEvent.VK_S);
    soundOptions.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        new SoundOptions(parentMenu, soundType);
      }
    });
    parentMenu.add(soundOptions);
  }

  public static void addToPanel(final JPanel parentPanel, final SoundType soundType) {
    final JButton soundOptions = new JButton("Sound Options...");
    soundOptions.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        new SoundOptions(parentPanel, soundType);
      }
    });
    parentPanel.add(soundOptions);
  }

  public SoundOptions(final JComponent parent, final SoundType soundType) {
    m_clipPlayer = ClipPlayer.getInstance();
    final String ok = "OK";
    final String cancel = "Cancel";
    final String selectAll = "All";
    final String selectNone = "None";

    final ArrayList<IEditableProperty> properties = SoundPath.getSoundOptions(soundType);
    final Object pressedButton = PropertiesSelector.getButton(parent, "Sound Options", properties,
        new Object[] {ok, selectAll, selectNone, cancel});
    if (pressedButton == null || pressedButton.equals(cancel)) {
    } else if (pressedButton.equals(ok)) {
      for (final IEditableProperty property : properties) {
        m_clipPlayer.setMute(((SoundOptionCheckBox) property).getClipName(), !(Boolean) property.getValue());
      }
      m_clipPlayer.saveSoundPreferences();
    } else if (pressedButton.equals(selectAll)) {
      for (final IEditableProperty property : properties) {
        property.setValue(true);
        m_clipPlayer.setMute(((SoundOptionCheckBox) property).getClipName(), false);
      }
      m_clipPlayer.saveSoundPreferences();
    } else if (pressedButton.equals(selectNone)) {
      for (final IEditableProperty property : properties) {
        property.setValue(false);
        m_clipPlayer.setMute(((SoundOptionCheckBox) property).getClipName(), true);
      }
      m_clipPlayer.saveSoundPreferences();
    }
  }

  public static void addGlobalSoundSwitchMenu(final JMenu parentMenu) {
    final JCheckBoxMenuItem soundCheckBox = new JCheckBoxMenuItem("Enable Sound");
    soundCheckBox.setMnemonic(KeyEvent.VK_N);
    soundCheckBox.setSelected(!ClipPlayer.getBeSilent());
    soundCheckBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        ClipPlayer.setBeSilent(!soundCheckBox.isSelected());
      }
    });
    parentMenu.add(soundCheckBox);
  }

  public static void addGlobalSoundSwitchCheckbox(final JPanel parentPanel) {
    final JCheckBox soundCheckBox = new JCheckBox("Enable Sound");
    soundCheckBox.setSelected(!ClipPlayer.getBeSilent());
    soundCheckBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        ClipPlayer.setBeSilent(!soundCheckBox.isSelected());
      }
    });
    parentPanel.add(soundCheckBox);
  }
}
