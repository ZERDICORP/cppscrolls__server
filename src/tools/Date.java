import java.time.LocalDate;

public class Date
{
  public static String updateTokenDate(String date)
  {
    LocalDate nowDate = LocalDate.now();
    LocalDate tokenDate = LocalDate.parse(date);

    if (tokenDate.equals(tokenDate))
      return date;

    return nowDate.toString();
  }

  public static void main(String[] args) throws InterruptedException
  {
    String s = LocalDate.now().toString();

    System.out.println(s);

    LocalDate date = LocalDate.parse(s);

    System.out.println(date.toString());
  }
}
