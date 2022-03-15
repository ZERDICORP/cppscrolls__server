package tools;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;



public class Tools
{
	public static String replaceParams(String s, String[] params)
	{
		for (String param : params)
			s = s.replaceFirst("\\?", param);

		return s;
	}

	public static Timestamp currentTimestamp() { return new Timestamp(System.currentTimeMillis()); }

	public static long daysPassed(String fromDate) { return hoursPassed(fromDate) / 24; }
	public static long hoursPassed(String fromDate)
	{
		long m1 = Timestamp.valueOf(fromDate).getTime();
		long m2 = currentTimestamp().getTime();
		long diff = m2 - m1;

		return diff / (60 * 60 * 1000);
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
