package zer.file;



public enum FType
{
  JSON("application/json"),
  HTML("text/html"),
  CSS("text/css"),
  JS("text/javascript"),
  PNG("image/png"),
  ICO("image/x-icon");

  String mime;
  
  FType(String mime) { this.mime = mime; }

  public String mime() { return mime; }
}
