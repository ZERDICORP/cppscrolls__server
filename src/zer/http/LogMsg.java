package zer.http;



interface LogMsg
{
	String NO_ANNOTATION = "handler \"?\" have no \"zer.http.HTTPRoute\" annotation.. ignore";
	String CONNECTION_CLOSED = "connection closed, threw an exception";
	String BAD_REQUEST = "bad request from \"?\".. ignore";
}
