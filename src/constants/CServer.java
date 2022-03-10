package constants;



public interface CServer
{
  int PORT = 8080;

	int POINTS_FOR_SCROLL_CREATING = 5;
	int SCROLL_CREATION_TIMEOUT = 24;
	int TOPICS_PAGE_SIZE = 15;
	int SCROLLS_PAGE_SIZE = 9;

	String API_PREFIX = "/api";

	String IMAGES_FOLDER_PATH = "images/";

  String SECRET = "sdjhjk23jk4j3jk4jk34";

  String EMAIL_SENDER = "nikolaianikin2002@gmail.com";
  String EMAIL_SENDER_PASSWORD = "7|C]0JMyC,-`a^1oE$>w";

	String DATABASE_USER = "root";
  String DATABASE_NAME = "cppscrolls";
  String DATABASE_PASSWORD = "zerdicorp1937";

	String SQL_DRIVER = "org.mariadb.jdbc.Driver";
	String SQL_CONNECTION_STRING = "jdbc:mariadb://localhost:3306/" + CServer.DATABASE_NAME + "?autoReconnect=true";
}
