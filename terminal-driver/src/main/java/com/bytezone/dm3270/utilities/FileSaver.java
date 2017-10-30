package com.bytezone.dm3270.utilities;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSaver
{
  public static Path getHomePath(String siteFolderName)
  {
    String userHome = System.getProperty("user.home");
    return Paths.get(userHome, new String[] { "dm3270", "files", siteFolderName });
  }
  
  public static Path getHomePath(ISite site)
  {
    if (site == null)
    {
      System.out.println("Site is null");
      return null;
    }
    String userHome = System.getProperty("user.home");
    return Paths.get(userHome, new String[] { "dm3270", "files", site.getFolder() });
  }
  

  public static String[] getSegments(String datasetName)
  {
    String[] segments = datasetName.split("\\.");
    int last = segments.length - 1;
    

    if ((last >= 0) && (segments[last].endsWith(")")))
    {
      int pos = segments[last].indexOf('(');
      segments[last] = segments[last].substring(0, pos);
    }
    
    return segments;
  }
  


  public static String getSaveFolderName(Path homePath, String datasetName)
  {
    String[] segments = getSegments(datasetName);
    



    int nextSegment = 0;
    String buildPath = homePath.toString();
    
    while (nextSegment < segments.length)
    {
      Path nextPath = Paths.get(buildPath, new String[] { segments[(nextSegment++)] });
      
      if ((java.nio.file.Files.notExists(nextPath, new LinkOption[0])) || (!java.nio.file.Files.isDirectory(nextPath, new LinkOption[0])))
      {

        return buildPath;
      }
      
      buildPath = nextPath.toString();
      
      Path filePath = Paths.get(buildPath, new String[] { datasetName });
      if (java.nio.file.Files.exists(filePath, new LinkOption[0]))
      {

        return buildPath;
      }
    }
    
    return buildPath;
  }
}
