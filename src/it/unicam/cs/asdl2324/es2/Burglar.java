package it.unicam.cs.asdl2324.es2;

//import java.util.Random;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei
 *
 */
public class Burglar {

   //Variabili istanza che servono
	private CombinationLock aCombinationLock;
	private int attempts;

    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     * 
     * @param aCombinationLock
     * @throw NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
        if(aCombinationLock==null) {
        	throw new NullPointerException("Cassaforte passata è nulla");
        }
        this.aCombinationLock=aCombinationLock;
        this.attempts=0;
    }

    /**
     * Forza la cassaforte e restituisce la combinazione.
     * 
     * @return la combinazione della cassaforte forzata.
     */
    public String findCombination() {
        String combination ="";
    	      
        //Metodo incrementale
        for (char guess1 = 'A'; guess1 <= 'Z'; guess1++) {	//Ciclo primo carattere da A a Z
            for (char guess2 = 'A'; guess2 <= 'Z'; guess2++) {	//Ciclo secondo carattere da A a Z
                for (char guess3 = 'A'; guess3 <= 'Z'; guess3++) {	//Ciclo terzo carattere da A a Z
                    this.attempts++;	//Aumento i numeri di tentativi
                	this.aCombinationLock.setPosition(guess1);	//Posizione il valore del primo carattere
                	this.aCombinationLock.setPosition(guess2);	//Posizione il valore del secondo carattere
                	this.aCombinationLock.setPosition(guess3);	//Posizione il valore del secondo carattere
                	combination = "" + guess1 + guess2 + guess3; //Salvo il valore della combinazione
                	this.aCombinationLock.open();	//Apro la cassaforte
                    System.out.println(combination);	//Stampa di debug del TestAMAno
                	if(this.aCombinationLock.isOpen())	//Se si è aperta correttamente ritorno il valore della combinazione
                		return combination;
                }
            } 
        }
        throw new NullPointerException("Nessuna combinazione trovata.");
    }

    /**
     * Restituisce il numero di tentativi che ci sono voluti per trovare la
     * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
     * 
     * @return il numero di tentativi che ci sono voluti per trovare la
     *         combinazione, oppure -1 se la cassaforte non è stata ancora
     *         forzata.
     */
    public long getAttempts() {
        if(this.aCombinationLock.isOpen())
        	return this.attempts;
    	
        return -1;
    }
}
