package tools;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.nio.charset.StandardCharsets;



public class Tools
{
	public static boolean isValidImage(byte[] bytes)
	{
		return true; 
	}	

  public static String sha256(String data) 
  {
    StringBuilder hexString = new StringBuilder();

    try
    {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      byte[] bytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

      for (int i = 0; i < bytes.length; i++)
      {
        String hex = Integer.toHexString(0xFF & bytes[i]);
        if (hex.length() == 1)
          hexString.append('0');
        hexString.append(hex);
      }
    }
    catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
    
    return hexString.toString();
  }
}
