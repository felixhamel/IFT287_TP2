package ift287.tp2.entities;

import ift287.tp2.exceptions.InvalidParameterException;

import java.util.ArrayList;

import com.google.common.base.Strings;

public class Joueur 
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
	 * @return String - Username of the player.
	 */
	public String getCle()
	{
		return cle;
	}
	
	/**
	 * Set the username of the player.
	 * @param String - username of the player.
	 * @throws InvalidParameterException 
	 */
	public void setCle(String cle) throws InvalidParameterException 
	{
		if(Strings.isNullOrEmpty(cle)) {
			throw new InvalidParameterException("cle", "cannot be null or empty.");
		}
		this.cle = cle;
	}
	
	/**
	 * Get name of the player.
	 * @return String - Name of the player.
	 */
	public final String getNomJoueur()
	{
		return nomJoueur;
	}
	
	/**
	 * Set the name of the player.
	 * @param nomJoueur - Name of the player.
	 * @throws InvalidParameterException 
	 */
	public void setNomJoueur(String nomJoueur) throws InvalidParameterException
	{
		if(Strings.isNullOrEmpty(nomJoueur)) {
			throw new InvalidParameterException("nomJoueur", "cannot be null or empty");
		}
		this.nomJoueur = nomJoueur;
	}
	
	/**
	 * Get the number of cards this player have.
	 * @return int - Number of cards.
	 */
	public int getNombreCartes()
	{
		return cards.size();
	}
	
	/**
	 * Add a new card.
	 * @param card - The new card.
	 * @throws InvalidParameterException - If the card is null, throw an exception.
	 */
	public void addCarte(Carte card) throws InvalidParameterException 
	{
		if(card == null) {
			throw new InvalidParameterException("card", "cannot be null");
		}
		
		this.cards.add(card);
	}
	
	/**
	 * Get all the cards for this player.
	 * @return ArrayList<Carte> - All the cards for this player.
	 */
	public final ArrayList<Carte> getCartes()
	{
		return cards;
	}
	
	/**
	 * Data structure that we want to put into the file storage.
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		
		// Put useful informations
		buffer.append("\"" + this.cle + "\";");
		buffer.append("\"" + this.nomJoueur + "\";");
		buffer.append(this.cards.size() + ";");
		
		// Print all the cards
		for(Carte card : cards) {
			buffer.append("\"" + card.getTitreCarte() + "\";");
			buffer.append("\"" + card.getNomEquipe() + "\";");
			buffer.append(card.getAnneeSortie() + ";");
		}
		
		return buffer.toString();
	}
	
}
