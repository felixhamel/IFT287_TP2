package inventairePackage;

import inventairePackage.exceptions.InvalidParameterException;
import inventairePackage.utils.Strings;

import java.util.ArrayList;
import java.util.Comparator;

public class Joueur implements Comparable<Joueur>
{
	private String cle;
	private String nomJoueur;
	private ArrayList<Carte> cards = new ArrayList<Carte>();

	public Joueur(String cle, String nomJoueur) throws InvalidParameterException
	{
		setCle(cle);
		setNomJoueur(nomJoueur);
	}

	/**
	 * Get the username of the player.
	 * 
	 * @return String - Username of the player.
	 */
	public String getCle()
	{
		return cle;
	}

	/**
	 * Set the username of the player.
	 * 
	 * @param String
	 *            - username of the player.
	 * @throws InvalidParameterException
	 */
	public void setCle(String cle) throws InvalidParameterException
	{
		if (Strings.isNullOrEmpty(cle)) {
			throw new InvalidParameterException("cle", "cannot be null or empty.");
		}
		this.cle = cle;
	}

	/**
	 * Get name of the player.
	 * 
	 * @return String - Name of the player.
	 */
	public final String getNomJoueur()
	{
		return nomJoueur;
	}

	/**
	 * Set the name of the player.
	 * 
	 * @param nomJoueur
	 *            - Name of the player.
	 * @throws InvalidParameterException
	 */
	public void setNomJoueur(String nomJoueur) throws InvalidParameterException
	{
		if (Strings.isNullOrEmpty(nomJoueur)) {
			throw new InvalidParameterException("nomJoueur", "cannot be null or empty");
		}
		this.nomJoueur = nomJoueur;
	}

	/**
	 * Get the number of cards this player have.
	 * 
	 * @return int - Number of cards.
	 */
	public int getNombreCartes()
	{
		return cards.size();
	}

	/**
	 * Add a new card.
	 * 
	 * @param card
	 *            - The new card.
	 * @throws InvalidParameterException
	 *             If the card is null, throw an exception.
	 */
	public void addCarte(Carte card) throws InvalidParameterException
	{
		if (card == null) {
			throw new InvalidParameterException("card", "cannot be null");
		}

		cards.add(card);
		cards.sort(new CarteComparateur());
	}

	/**
	 * Get all the cards for this player.
	 * 
	 * @return ArrayList<Carte> - All the cards for this player.
	 */
	public final ArrayList<Carte> getCartes()
	{
		return cards;
	}

	/**
	 * Data structure that we want to put into the file storage.
	 * 
	 * @return String - CSV representation of the player and it cards.
	 */
	public String toCSVEntryFormat()
	{
		StringBuffer buffer = new StringBuffer();

		// Put useful informations
		buffer.append("\"" + cle + "\";");
		buffer.append("\"" + nomJoueur + "\";");
		buffer.append("\"" + cards.size() + "\";");

		// Print all the cards
		for (Carte card : cards) {
			buffer.append("\"" + card.getTitreCarte() + "\";");
			buffer.append("\"" + card.getNomEquipe() + "\";");
			buffer.append("\"" + card.getAnneeSortie() + "\";");
		}

		return buffer.toString();
	}

	@Override
	public int compareTo(Joueur o)
	{
		for (int i = 0; i < Math.min(getNomJoueur().length(), o.getNomJoueur().length()); ++i) {
			if (o.getNomJoueur().charAt(i) < getNomJoueur().charAt(i)) {
				return 1;
			} else if (o.getNomJoueur().charAt(i) > getNomJoueur().charAt(i)) {
				return -1;
			}
		}
		return 0;
	}
}

/**
 * Comparator used to compare two players.
 */
class JoueurComparateur implements Comparator<Joueur>
{
	@Override
	public int compare(Joueur o1, Joueur o2)
	{
		return o1.compareTo(o2);
	}
}
