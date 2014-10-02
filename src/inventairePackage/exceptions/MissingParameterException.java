package inventairePackage.exceptions;

public class MissingParameterException extends Exception
{
	private static final long serialVersionUID = 4258546505747790699L;

	public MissingParameterException(String parameter, String message)
	{
		super(String.format("Missing parameter '%s' because '%s'.", parameter, message));
	}
}
