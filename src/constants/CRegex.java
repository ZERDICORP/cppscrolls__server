package constants;



public interface CRegex
{
	String SIDE = "[01]";
	String UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
	String EMAIL = "^[.a-zA-Z0-9_-]{1,64}@[a.-zA-Z0-9_-]{1,63}\\.[a.-zA-Z0-9_-]{1,192}$";
	String NICKNAME = "^[a-zA-Z0-9_]{1,255}$";
	String PASSWORD = "^.{6,}$";
}
