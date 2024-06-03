package it.unicam.cs.asdl2324.es7;

import java.util.List;

/**
 * Implementazione dell'algoritmo di Insertion Sort integrata nel framework di
 * valutazione numerica. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 * @param <E>
 *                Una classe su cui sia definito un ordinamento naturale.
 */
public class InsertionSort<E extends Comparable<E>>
        implements SortingAlgorithm<E> {
	
	private int confronti;	//Contatore dei confronti effettuati

    public SortingAlgorithmResult<E> sort(List<E> l) {
        
    	if(l==null)	//Se la lista è nulla lancio un exception
    		throw new NullPointerException("Lista vuota!");
    	if(l.size()<=1)	//Se la lista ha un unico elemento non c'è bisogno di ordinare
    		return new SortingAlgorithmResult<E>(l, 0);
    	
    	for(int i=1;i<l.size();i++) {
            E key = l.get(i);     	//Ottiene valore
            int j = i-1;            //Ottiene indice del valore precedente
            
            while(j>=0 && l.get(j).compareTo(key)>0) {	// Se il valore precedente è maggiore dell'attuale
                l.set(j+1,l.get(j));       	//Imposta valore attuale come precedente
                j--;                        //Decrementa contatore
                this.confronti++;           //Aumento numero di confronti
            }
            
            if (j!=i-1) {	//Imposta come successivo il valore attuale
                l.set(j + 1, key);
            }
        }
        return new SortingAlgorithmResult<E>(l, confronti);
    }

    public String getName() {
        return "InsertionSort";
    }
}
