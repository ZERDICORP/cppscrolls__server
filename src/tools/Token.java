package tools;



import java.util.Base64;



public class Token
{
  public static String build(String payload, String secret)
  {
    String base64Payload = Base64.getEncoder().encodeToString(payload.getBytes());
    String signature = Tools.sha256(payload + secret);

    return base64Payload + "." + signature;
  }

  public static String access(String token, String secret)
  {
    if (token == null)
      return null;

    if (!token.matches("^[^.]*\\.[^.]*$"))
      return null;

    String[] parts = token.split("\\.");
		String payload;

		try
		{
			payload = new String(Base64.getDecoder().decode(parts[0]));

			if (!parts[1].equals(Tools.sha256(payload + secret)))
				return null;
		}
		catch (Exception e) { return null; }

    return payload;
  }
}
