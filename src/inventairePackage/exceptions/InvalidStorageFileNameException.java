package inventairePackage.exceptions;

public class InvalidStorageFileNameException extends Exception
{
	private static final long serialVersionUID = 8988478841278540248L;

	public InvalidStorageFileNameException(String nameGiven)
	{
		super(String.format("File with name '%s' is invalid or null.", nameGiven));
	}
}
