package com.bytezone.dm3270.assistant;

import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

import com.bytezone.dm3270.display.Screen;

import javafx.scene.control.MenuBar;

public class FilesTab extends AbstractTransferTab 
{

  public FilesTab (Screen screen, TSOCommand tsoCommand, Preferences prefs)
  {
    super ("Local Files", screen, tsoCommand);
  }
  @Override
  protected void setText ()
  {
    
  }

  // ---------------------------------------------------------------------------------//
  // fileSelected() Listener events
  // ---------------------------------------------------------------------------------//

  private final Set<FileSelectionListener> selectionListeners = new HashSet<> ();

  void fireFileSelected (String filename)
  {
    selectionListeners.forEach (l -> l.fileSelected (filename));
  }

  void addFileSelectionListener (FileSelectionListener listener)
  {
    if (!selectionListeners.contains (listener))
    {
      
    }
  }

  void removeFileSelectionListener (FileSelectionListener listener)
  {
    if (selectionListeners.contains (listener))
      selectionListeners.remove (listener);
  }
}
