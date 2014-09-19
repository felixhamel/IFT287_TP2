package ift287.tp2.exceptions;

public class InvalidParameterException extends Exception 
{
	private static final long serialVersionUID = -6552601371600645524L;

	public InvalidParameterException(String parameter, String message) 
	{
		super(String.format("Invalid parameter '%s' because '%s'.", parameter, message));
	}
}
