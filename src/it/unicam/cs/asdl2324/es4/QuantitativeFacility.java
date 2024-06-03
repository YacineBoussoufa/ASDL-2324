/**
 * 
 */
package it.unicam.cs.asdl2324.es4;

/**
 * Una Quantitative Facility è una facility che contiene una informazione
 * quantitativa. Ad esempio la presenza di 50 posti a sedere oppure la presenza
 * di 20 thin clients.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class QuantitativeFacility extends Facility {

    private final int quantity;

    /**
     * Costruisce una facility quantitativa.
     * 
     * @param codice
     *                        codice identificativo della facility
     * @param descrizione
     *                        descrizione testuale della facility
     * @param quantity
     *                        quantita' associata alla facility
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  {@code codice} e {@code descrizione} è
     *                                  nulla.
     */
    public QuantitativeFacility(String codice, String descrizione,
            int quantity) {
        super(codice, descrizione);
        this.quantity = quantity;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /*
     * Questa quantitative facility soddisfa una facility data se e solo se la
     * facility data è una quantitative facility, ha lo stesso codice e la
     * quantità associata a questa facility è maggiore o uguale alla quantità
     * della facility data.
     */
    @Override
    public boolean satisfies(Facility o) {
    	if(o==null)	//Se l'oggetto passato o è nullo, lancio un exception
                throw new NullPointerException("Facility nulla!");
    	
    	if(o instanceof QuantitativeFacility) {	//Se o è un istanza di quantative facility
            QuantitativeFacility qf = (QuantitativeFacility) o;	//Creo un oggetto Quantitative facility a cui assegno il casting dell'oggetto o
            if ((this.getCodice().equals(qf.getCodice())) && (this.getQuantity() >= qf.getQuantity()))	//Se il codice e la quantità corrispondono ritorno true
                return true;            
    	}
    	
        return false;	//Altrimenti ritorno false
    }

}
