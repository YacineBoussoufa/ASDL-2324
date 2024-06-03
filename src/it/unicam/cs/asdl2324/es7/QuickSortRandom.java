/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.List;
import java.util.Random;
import java.util.Collections;

/**
 * Implementazione del QuickSort con scelta della posizione del pivot scelta
 * randomicamente tra le disponibili. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSortRandom<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    private static final Random randomGenerator = new Random();
    private int confronti;	//Contatore dei confronti effettuati
    
    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
    	if(l==null)	//Se la lista è nulla lancio un exception
    		throw new NullPointerException("Lista vuota!");
    	if(l.size()<=1)	//Se la lista ha un unico elemento non c'è bisogno di ordinare
    		return new SortingAlgorithmResult<E>(l, 0);
    	
    	this.confronti=0;	//Inizializzazione a 0
    	QuickSortAlgRand(l,0,l.size()-1);	//Ordino
    	
    	return new SortingAlgorithmResult<E>(l, this.confronti);
    }
    
    /**
     * Implementazione dell'algoritmo del QuickSort
     * 
     * @parm l			Lista da ordinare
     * @parm start 		Indice di inizio
     * @parm end		Indice di fine
     * 
     */
    private void QuickSortAlgRand(List<E> l, int start, int end) {
    	if(start<end) {
    		int pivot=this.PartitionRand(l,start,end);	//Effetuo la partizione
    		
    		QuickSortAlgRand(l,start,pivot-1);	//Ordino Ricorsivamente (Parte Sinistra)
    		
    		QuickSortAlgRand(l,pivot+1,end);	//Ordino Ricorsivamente (Parte Destra)
    	}
    }
    
    /**
     * Scelgo un pivot casuale
     *
     * @param l             Sequenza da ordinare
     * @param start		    Indice di inizio
     * @param end		    Indice di fine
     * @return              Posizione del pivot
     */
    private int PartitionRand(List<E> l, int start, int end) {
    	// Consideriamo come pivot uno elemento a caso tra l'inizio e la fine
        int random = randomGenerator.nextInt(end-start+1);
        
        // sposto l'elemento in posizione p + shift con l'elemento in posizione
        // r, in modo che diventi lui il pivot; a meno che p+shift non sia già r
        if (start+random!=end) {
            // Scambio
            Collections.swap(l,start+random,end);
        }
        
        // Chiamo la partizione normale
        return Partition(l, start, end);
    }
    
    /**
     * Calcolo pivot e ordinamento dell'array
     *
     * @param l             Sequenza da ordinare
     * @param start		    Indice di inizio
     * @param end		    Indice di fine
     * @return              Posizione del pivot
     */
    private int Partition(List<E> l, int start, int end) {
    	E pivot=l.get(end);	//Scelgo l'ultimo elemento come pivot
    	int i = (start-1);  // Indice dell'elemento più grande (parte dal precedente al primo)
    	
        for(int j=start; j<end; j++) {
            if(l.get(j).compareTo(pivot)<0) {   // Se l'elemento corrente è minore del pivot
                i++;                            // Incrementa indice dell'elemento più grande
                Collections.swap(l,i,j);      	// Scambia elementi
                this.confronti++;           	// Aumenta numero di confronti
            }
        }
        
        Collections.swap(l,i+1,end);    		// Scambia elemento più grande con pivot
        return (i+1);                           // Ritorna posizione del pivot
    }
    
    @Override
    public String getName() {
        return "QuickSortRandom";
    }

}
