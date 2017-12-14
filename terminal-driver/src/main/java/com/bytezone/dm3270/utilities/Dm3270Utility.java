package com.bytezone.dm3270.utilities;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class Dm3270Utility
{
  public static final String EBCDIC = "CP1047";
  public static final String FROG = "CP1047";
  private static final int LINESIZE = 16;
  public static final int[] ebc2asc = new int['Ā'];
  public static final int[] asc2ebc = new int['Ā'];
  
  static
  {
    byte[] values = new byte['Ā'];
    for (int i = 0; i < 256; i++) {
      values[i] = ((byte)i);
    }
    try
    {
      String s = new String(values, "CP1047");
      char[] chars = s.toCharArray();
      for (int i = 0; i < 256; i++)
      {
        int val = chars[i];
        ebc2asc[i] = val;
        asc2ebc[val] = i;
      }
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
  }
  
  public static String ebc2asc(byte[] buffer)
  {
    byte[] newBuffer = new byte[buffer.length];
    int ptr = 0;
    for (int i = 0; i < buffer.length; i++) {
      if (buffer[i] != 0)
        newBuffer[(ptr++)] = ((byte)ebc2asc[(buffer[i] & 0xFF)]);
    }
    return new String(newBuffer);
  }
  
  public static String getString(byte[] buffer)
  {
    return getString(buffer, 0, buffer.length);
  }
  
  public static String getString(byte[] buffer, int offset, int length)
  {
    try
    {
      if (offset + length > buffer.length)
        length = buffer.length - offset - 1;
      return new String(buffer, offset, length, "CP1047");
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace(); }
    return "FAIL";
  }
  

  public static String getSanitisedString(byte[] buffer, int offset, int length)
  {
    if (offset + length > buffer.length)
      length = buffer.length - offset - 1;
    return getString(sanitise(buffer, offset, length));
  }
  
  private static byte[] sanitise(byte[] buffer, int offset, int length)
  {
    byte[] cleanBuffer = new byte[length];
    for (int i = 0; i < length; i++)
    {
      int b = buffer[(offset++)] & 0xFF;
      cleanBuffer[i] = (b < 64 ? 64 : (byte)b);
    }
    return cleanBuffer;
  }
  
  public static int unsignedShort(byte[] buffer, int offset)
  {
    return (buffer[offset] & 0xFF) * 256 + (buffer[(offset + 1)] & 0xFF);
  }
  
  public static int packUnsignedShort(int value, byte[] buffer, int offset)
  {
    buffer[(offset++)] = ((byte)(value >> 8 & 0xFF));
    buffer[(offset++)] = ((byte)(value & 0xFF));
    
    return offset;
  }
  
  public static int unsignedLong(byte[] buffer, int offset)
  {
    return (buffer[offset] & 0xFF) * 16777216 + (buffer[(offset + 1)] & 0xFF) * 65536 + 
      (buffer[(offset + 2)] & 0xFF) * 256 + (buffer[(offset + 3)] & 0xFF);
  }
  
  public static int packUnsignedLong(long value, byte[] buffer, int offset)
  {
    buffer[(offset++)] = ((byte)(int)(value >> 24 & 0xFF));
    buffer[(offset++)] = ((byte)(int)(value >> 16 & 0xFF));
    buffer[(offset++)] = ((byte)(int)(value >> 8 & 0xFF));
    buffer[(offset++)] = ((byte)(int)(value & 0xFF));
    
    return offset;
  }
  
  public static String toHex(byte[] b)
  {
    return toHex(b, 0, b.length);
  }
  
  public static String toHex(byte[] b, int offset)
  {
    return toHex(b, offset, b.length - offset);
  }
  
  public static String toHex(byte[] b, int offset, int length)
  {
    return toHex(b, offset, length, true);
  }
  
  public static String toHex(byte[] b, boolean ebcdic)
  {
    return toHex(b, 0, b.length, ebcdic);
  }
  
  public static String toHex(byte[] b, int offset, int length, boolean ebcdic)
  {
    StringBuilder text = new StringBuilder();
    
    try
    {
      int ptr = offset; for (int max = offset + length; ptr < max; ptr += 16)
      {
        StringBuilder hexLine = new StringBuilder();
        StringBuilder textLine = new StringBuilder();
        for (int linePtr = 0; linePtr < 16; linePtr++)
        {
          if (ptr + linePtr >= max) {
            break;
          }
          int val = b[(ptr + linePtr)] & 0xFF;
          hexLine.append(String.format("%02X ", new Object[] { Integer.valueOf(val) }));
          
          if (ebcdic) {
            if ((val < 64) || (val == 255)) {
              textLine.append('.');
            } else
              textLine.append(new String(b, ptr + linePtr, 1, "CP1047"));
          } else if ((val < 32) || (val >= 240)) {
            textLine.append('.');
          } else
            textLine.append(new String(b, ptr + linePtr, 1));
        }
        text.append(String.format("%04X  %-48s %s%n", new Object[] { Integer.valueOf(ptr), hexLine.toString(), 
          textLine.toString() }));
      }
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    
    if (text.length() > 0) {
      text.deleteCharAt(text.length() - 1);
    }
    return text.toString();
  }
  
  public static void hexDump(byte[] b, boolean ebcdic)
  {
    hexDump(b, 0, b.length, ebcdic);
  }
  
  public static void hexDump(byte[] b)
  {
    hexDump(b, 0, b.length);
  }
  
  public static void hexDump(byte[] b, int offset, int length)
  {
      System.out.println(toHex(b, offset, length));
  }
  
  public static void hexDump(byte[] b, int offset, int length, boolean ebcdic)
  {
      System.out.println(toHex(b, offset, length, ebcdic));
  }
  
  public static String toHexString(byte[] buffer)
  {
    StringBuilder text = new StringBuilder();
    for (int i = 0; i < buffer.length; i++)
      text.append(String.format("%02X ", new Object[] { Byte.valueOf(buffer[i]) }));
    return text.toString();
  }
  
  public static String toHexString(byte[] buffer, int offset, int length)
  {
    StringBuilder text = new StringBuilder();
    int ptr = offset; for (int max = offset + length; ptr < max; ptr++)
      text.append(String.format("%02X ", new Object[] { Integer.valueOf(buffer[ptr] & 0xFF) }));
    if (text.length() > 0)
      text.deleteCharAt(text.length() - 1);
    return text.toString();
  }
  
  public static void printStackTrace() {
    StackTraceElement[] arrayOfStackTraceElement;
    int j = (arrayOfStackTraceElement = Thread.currentThread().getStackTrace()).length; for (int i = 0; i < j; i++) { StackTraceElement ste = arrayOfStackTraceElement[i];
      System.out.println(ste);
    }
  }
  
  public static boolean showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR, message, new ButtonType[0]);
    alert.getDialogPane().setHeaderText(null);
    Optional<ButtonType> result = alert.showAndWait();
    return (result.isPresent()) && (result.get() == ButtonType.OK);
  }
}
