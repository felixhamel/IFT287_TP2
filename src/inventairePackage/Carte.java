package inventairePackage;

import java.util.Comparator;

import inventairePackage.exceptions.InvalidParameterException;
import inventairePackage.utils.Strings;

public class Carte implements Comparable<Carte>
{
	private String titreCarte;
	private String nomEquipe;
	private int anneeSortie = 0;

	public Carte(String titreCarte, String nomEquipe, int anneeSortie) throws InvalidParameterException
	{
		setTitreCarte(titreCarte);
		setNomEquipe(nomEquipe);
		setAnneeSortie(anneeSortie);
	}

	/**
	 * Get the title of the card.
	 * 
	 * @return String - Title of the card.
	 */
	public final String getTitreCarte()
	{
		return titreCarte;
	}

	/**
	 * Set the title of the card.
	 * 
	 * @param titreCarte
	 *            Title of the card.
	 * @throws InvalidParameterException
	 */
	public void setTitreCarte(String titreCarte) throws InvalidParameterException
	{
		if (Strings.isNullOrEmpty(titreCarte)) {
			throw new InvalidParameterException("titreCarte", "cannot be null or empty.");
		}
		this.titreCarte = titreCarte;
	}

	/**
	 * Get the name of the team the player is in in the year.
	 * 
	 * @return String Name of the team the player was in during the year.
	 */
	public final String getNomEquipe()
	{
		return nomEquipe;
	}

	/**
	 * Set the name of the team the player was in on that year.
	 * 
	 * @param nomEquipe
	 *            Name of the team.
	 * @throws InvalidParameterException
	 */
	public void setNomEquipe(String nomEquipe) throws InvalidParameterException
	{
		if (Strings.isNullOrEmpty(nomEquipe)) {
			throw new InvalidParameterException("nomEquipe", "cannot be null or empty.");
		}
		this.nomEquipe = nomEquipe;
	}

	/**
	 * Get the year for this card.
	 * 
	 * @return int Year when this card was published.
	 */
	public final int getAnneeSortie()
	{
		return anneeSortie;
	}

	/**
	 * Set the year of the card.
	 * 
	 * @param anneeSortie
	 *             Year of the card.
	 * @throws InvalidParameterException
	 *             Given year is lower than 0.
	 */
	public void setAnneeSortie(int anneeSortie) throws InvalidParameterException
	{
		if (anneeSortie < 0) {
			throw new InvalidParameterException("anneeSortie", "cannot set a year lower than 0.");
		}
		this.anneeSortie = anneeSortie;
	}

	@Override
	public int compareTo(Carte o)
	{
		if (o.getAnneeSortie() < getAnneeSortie()) {
			return 1;
		} else if (o.getAnneeSortie() > getAnneeSortie()) {
			return -1;
		}
		return 0;
	}
}

/**
 * Class used to compare 2 cards.
 */
class CarteComparateur implements Comparator<Carte>
{
	@Override
	public int compare(Carte o1, Carte o2)
	{
		return o1.compareTo(o2);
	}
}