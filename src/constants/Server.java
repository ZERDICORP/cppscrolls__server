package constants;



public interface Server
{
  int PORT = 8080;

	String API_PREFIX = "/api";

  String SECRET = "sdjhjk23jk4j3jk4jk34";

  String EMAIL_SENDER = "nikolaianikin2002@gmail.com";
  String EMAIL_SENDER_PASSWORD = "7|C]0JMyC,-`a^1oE$>w";

	String DATABASE_USER = "root";
  String DATABASE_NAME = "cppscrolls";
  String DATABASE_PASSWORD = "zerdicorp1937";

	String SQL_DRIVER = "org.mariadb.jdbc.Driver";
	String SQL_CONNECTION_STRING = "jdbc:mariadb://localhost:3306/" + Server.DATABASE_NAME + "?autoReconnect=true";
}
