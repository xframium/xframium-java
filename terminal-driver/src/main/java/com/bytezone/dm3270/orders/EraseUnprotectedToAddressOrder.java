package com.bytezone.dm3270.orders;

import java.util.HashSet;

import com.bytezone.dm3270.display.Cursor;
import com.bytezone.dm3270.display.Field;
import com.bytezone.dm3270.display.DisplayScreen;
import com.bytezone.dm3270.display.Pen;
import com.bytezone.dm3270.display.Screen;

public class EraseUnprotectedToAddressOrder extends Order
{
  private final BufferAddress stopAddress;

  public EraseUnprotectedToAddressOrder (byte[] buffer, int offset)
  {
    assert buffer[offset] == Order.ERASE_UNPROTECTED;
    stopAddress = new BufferAddress (buffer[offset + 1], buffer[offset + 2]);

    this.buffer = new byte[3];
    System.arraycopy (buffer, offset, this.buffer, 0, this.buffer.length);
  }

  @Override
  public void process (DisplayScreen dispScreen)
    {
        if (dispScreen instanceof Screen)
        {
            Screen screen = (Screen) dispScreen;
          
            Pen pen = screen.getPen ();
            Cursor cursor = screen.getScreenCursor ();
            int cursorPostion = cursor.getLocation ();
            Field resetField = null;
            Field firstField = null;
      
            for (Field field : screen.getFieldManager ().getUnprotectedFields ())
            {
                if (field.contains (cursorPostion))
                {
                    resetField = field;
                    firstField = field;
                    break;
                }
            }
      
            HashSet haveSeen = new HashSet();
            
            while ((resetField != null) &&
                   (!haveSeen.contains(resetField)))
            {
                resetField.clearData(false);       // don't set modified (is this correct?)
                haveSeen.add(resetField);
                
                resetField = resetField.getNextUnprotectedField();
            }

            if (firstField != null)
            {
                cursor.moveTo (firstField.getFirstLocation());
            }

            screen.restoreKeyboard();
        }
    }

  @Override
  public String toString ()
  {
    return "EUA : " + stopAddress;
  }
}
