/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.List;
import java.util.ArrayList;

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

	private int confronti;	//Contatore dei confronti effettuati
	
    public SortingAlgorithmResult<E> sort(List<E> l) {
        
    	if(l==null)	//Se la lista è nulla lancio un exception
    		throw new NullPointerException("Lista vuota!");
    	if(l.size()<=1)	//Se la lista ha un unico elemento non c'è bisogno di ordinare
    		return new SortingAlgorithmResult<E>(l, 0);
    	
    	this.confronti=0;	//Inizializzazione a 0
        MergeSortAlg(l,0,l.size()-1);
        return new SortingAlgorithmResult<E>(l, this.confronti);
    }
    
    /**
     * Implementazione dell'algoritmo del MergeSort
     * 
     * @parm l			Lista da ordinare
     * @parm start 		Indice di inizio
     * @parm end		Indice di fine
     * 
     */
    private void MergeSortAlg(List<E> l, int start, int end) {
    	if(start==end)	//Se la lista  vuota non faccio nulla 
    		return;
    	
    	int centro=(start+end)/2;	//Centro della lista	
        
        this.MergeSortAlg(l, start, centro);	//Ordino Ricorsivamente (Parte Sinistra)
        this.MergeSortAlg(l, centro + 1, end);	//Ordino Ricorsivamente (Parte Sinistra)
        this.Merge(l, start, centro, end);		//Unione delle liste
    }
    
    /**
     * Implementazione del Merge
     * 
     * @parm	l			Lista da ordinare
     * @parm	start 		Indice di inizio
     * @param 	centro		Indice centrale
     * @parm 	end			Indice di fine
     * 
     */
    private void Merge(List<E> l, int start, int centro, int end) {
    	List<E> left=new ArrayList<E>();	//Liste di appoggio
    	List<E> right=new ArrayList<E>();
    	
    	for(int i=start;i<=centro;i++)	//Aggiungo gli elementi nella lista di sinistra
    		left.add(l.get(i));
    	
    	for(int i=centro+1;i<=end;i++)	//Aggiungo gli elementi nella lista di destra
    		right.add(l.get(i));
    	
    	int i,j,k;	//Variabili per lo scorrimento
    	i=0;	//Scorre a sinistra
    	j=0;	//Scorre a destra
    	k=start;	//Scorrimento della Lista
    	
    	while(i<left.size() && j<right.size()) {	//Scorro le due liste
    		this.confronti++;	//Incremento 
    		if(left.get(i).compareTo(right.get(j))<0) {	//Se l'elemento di sinistra è minore di quello a destra
    			l.set(k, left.get(i));	//Metto in posizione i l'elemento di sinistra
    			i++;	//Incremento i
    		}
    		else {	//Altrimenti: Se l'elemento di sinistra è maggiore di quello a destra
    			l.set(k, right.get(j));	//Metto in posizione i l'elemento di destra
    			j++;	//Incremento j
    		}
    		k++;	//Incremento k (scorro la lista di uno)
    	}
    	
    	while(i<left.size()) {	//Metto tutta la parte di sinistra nella lista
    		l.set(k, left.get(i));
    		i++;
    		k++;
    	}
    	
    	while(j<right.size()){	//Metto dopo la parte sinistra, tutta la parte destra nella lista
    		l.set(k, right.get(j));
    		j++;
    		k++;
    	}
    }

    public String getName() {
        return "MergeSort";
    }
}
