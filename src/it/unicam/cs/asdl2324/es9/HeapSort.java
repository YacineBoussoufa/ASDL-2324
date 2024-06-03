/**
 * 
 */
package it.unicam.cs.asdl2324.es9;

import java.util.List;
import java.util.Collections; 

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

	private int countCompare=0;
	
    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // Nota: usare una variante dei metodi della classe
        // MaxHeap in modo da implementare l'algoritmo utilizzando solo un array
        // (arraylist) e alcune variabili locali di appoggio (implementazione
        // cosiddetta "in loco" o "in place", si veda
        // https://it.wikipedia.org/wiki/Algoritmo_in_loco)
        
    	// Costruisce l'heap a partire dalla lista
        int index = (l.size()/2)-1;
        // Heapify sui figli
        for (int i=index;i>=0;i--) 
            this.heapify(l,l.size(),i);
        
        // Itera dalla fine dell'heap alla radice
        for (int i=l.size()-1;i>0;i--) {
            // Scambia la radice con l'ultimo elemento dell'heap
            Collections.swap(l,0,i);
            // Richiama heapify sull'heap ridotto
            heapify(l,i,0);
        }
        return new SortingAlgorithmResult<E>(l, countCompare);
    }

    @Override
    public String getName() {
        return "HeapSort";
    }
    
    private void heapify(List<E> list, int heapSize, int index) {
        int largest=index;
        int left=2*index+1;
        int right=2*index+2;
        // Se il figlio di sinistra è più grande della radice, lo imposta come più grande
        if (left<heapSize && list.get(left).compareTo(list.get(largest))>0) {
            largest=left;
            // Incrementa numero di operazioni di confronto effettuate
            countCompare++;
        }
        // Se il figlio di destra è più grande dell'attuale più grande, lo imposta come più grande
        if (right<heapSize && list.get(right).compareTo(list.get(largest))>0) {
            largest=right;
            // Incrementa numero di operazioni di confronto effettuate
            countCompare++;
        }
        // Se il più grande non corrisponde alla radice, effettua lo scambio
        if (largest!=index) {
            Collections.swap(list,index,largest);
            // Chiama ricorsivamente heapify sul sottoalbero
            heapify(list,heapSize,largest);
        }
    }

}
