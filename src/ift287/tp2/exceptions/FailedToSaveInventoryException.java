package ift287.tp2.exceptions;

public class FailedToSaveInventoryException extends Exception 
{
	private static final long serialVersionUID = -8855058480345733031L;

	public FailedToSaveInventoryException(String filename, Throwable cause)
	{
		super(String.format("Failed to save inventory to file with name '%s'.", filename), cause);
	}
}
