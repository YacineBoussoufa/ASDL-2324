package it.unicam.cs.asdl2324.mp1;

import java.util.Set;
import java.util.HashSet;

/**
 * La classe LinkedListDisjointSets implementa un insieme disgiunto {@link DisjointSets}
 * utilizzando una rappresentazione con liste concatenate. 
 * La classe utilizza un {@link HashSet} in modo da memorizzare i 
 * rappresentanti degli insiemi disgiunti permettendo
 * la rapida verifica della presenza degli elementi e di trovare
 * il rappresentante dell'insieme di cui fa parte un elemento.
 * 
 * @author Luca Tesei (template) 
 *		   Yacine Boussoufa yacine.boussoufa@studenti.unicam.it (implementazione)
 *
 */
public class LinkedListDisjointSets implements DisjointSets {

	/**
     * Collezione di una lista disgiunta.
     */
	private Set<DisjointSetElement> collezioneRappresentati;

    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
    	this.collezioneRappresentati=new HashSet<>();	//Creazione di un HashSet
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
        return e.getRef1()!=null;	//Se il rapppresentante è null, non è presente
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere
     * 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
    	if(e==null)	//Se l'elemento passato è nullo lancio un eccezzione
    		throw new NullPointerException("Elemento nullo!");
    	else if(this.isPresent(e))	//Mentre se l'elemento è ppresente lancio un'altra eccezione 
    		throw new IllegalArgumentException("Elemento già presente!");
    	
    	e.setRef1(e);	//Cambio il riferimento 1 dell'elemento ad e
        e.setRef2(null);//Cambia il riferimento 2 a null
        e.setNumber(1);	//Cambio l'intero associato a questo elemento ad 1
        this.collezioneRappresentati.add(e);	//Aggiungo alla lista dei rappresentanti
    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
    	if(e==null)	//Se l'elemento passato è nullo lancio un eccezzione
    		throw new NullPointerException("Elemento nullo!");
    	else if(!(this.isPresent(e)))	//Mentre se l'elemento è ppresente lancio un'altra eccezione 
    		throw new IllegalArgumentException("Elemento già presente!");
    	
    	return e.getRef1();	//Ritorno il riferimento al puntatore ref1
    }

    /*
     * Dopo l'unione di due insiemi effettivamente disgiunti il rappresentante
     * dell'insieme unito è il rappresentate dell'insieme che aveva il numero
     * maggiore di elementi tra l'insieme di cui faceva parte {@code e1} e
     * l'insieme di cui faceva parte {@code e2}. Nel caso in cui entrambi gli
     * insiemi avevano lo stesso numero di elementi il rappresentante
     * dell'insieme unito è il rappresentante del vecchio insieme di cui faceva
     * parte {@code e1}.
     * 
     * Questo comportamento è la risultante naturale di una strategia che
     * minimizza il numero di operazioni da fare per realizzare l'unione nel
     * caso di rappresentazione con liste concatenate.
     * 
     */
    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {
    	if((e1==null)||(e2==null))	//Se gli elementi passati sono nulli lancio un eccezzione
    		throw new NullPointerException("Elemento nullo!");
    	else if(!(isPresent(e1))||(!isPresent(e2)))	//Se gli elementi non sono presenti lancio un eccezione
    		throw new IllegalArgumentException("Elementi già presenti!");
    	else if(e1.getRef1()==e2.getRef1())	//Elementi con lo stesso rapresentative, quindi ritorno void
    		return;
    	
    	DisjointSetElement rapp1=e1.getRef1();	//Assegnazione a rapp1 il valore del primo rappresentante
    	DisjointSetElement rapp2=e2.getRef1();	//Assegnazione a rapp2 il valore del secondo rappresentante
    	
    	
    	if(rapp1.getNumber()>=rapp2.getNumber()) {	//Se il numero di rappresentanti uno è maggiore o uguale al secondo
    		rapp2.setRef1(rapp1);	//Setto il rappresentante 2 con il valore del rappresentante 1
            this.collezioneRappresentati.remove(rapp2);	//Rimuovo dalla collezione il rappresentante 2 in quanto non rappresenta più nessun insieme

            DisjointSetElement set2Elem=rapp2;	//Il rappresentante 2 deve ora essere aggiornato al rappresentante 1
            while(set2Elem.getRef2()!=null) {	//Ciclo finche non è più presente nessun rappresentante del valore di ref2
                set2Elem=set2Elem.getRef2();	
                set2Elem.setRef1(rapp1); 		//Ogni riferimento al rappresentante deve essere aggiornato
            }
            set2Elem.setRef2(rapp1.getRef2());	//set2Elem è ora l'ultimo elemento del secondo insieme, deve puntare al successivo di rapp1

            rapp1.setRef2(rapp2); //rapp1 è il primo elemento della lista concatenata e deve puntare a rapp2
            rapp1.setNumber(rapp1.getNumber()+rapp2.getNumber());
    	}
        else {
        	rapp1.setRef1(rapp2);	//Setto il rappresentante 1 con il valore del rappresentante 2
        	this.collezioneRappresentati.remove(rapp1);	//Rimuovo dalla collezione il rappresentante 1 in quanto non rappresenta più nessun insieme

        	DisjointSetElement set2Elem=rapp1;	//Il rappresentante 1 deve ora essere aggiornato al rappresentante 2
        	while(set2Elem.getRef2()!=null) {	//Ciclo finche non è più presente nessun rappresentante del valore di ref2
        		set2Elem=set2Elem.getRef2();
        		set2Elem.setRef1(rapp2); 		//Ogni riferimento al rappresentante deve essere aggiornato
        	}
        	set2Elem.setRef2(rapp2.getRef2());	//set2Elem è ora l'ultimo elemento del secondo insieme, deve puntare al successivo di rapp2

        	rapp2.setRef2(rapp1); //rapp2 è il primo elemento della lista concatenata e deve puntare a rapp2
        	rapp2.setNumber(rapp2.getNumber()+rapp1.getNumber());
        }
    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
        return this.collezioneRappresentati;	//Ritorno la collezione degli elementi
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(
            DisjointSetElement e) {
    	if (e==null)	//Se l'elemento è nullo lancio un eccezione
            throw new NullPointerException("Elemento nullo!");
    	else if (!isPresent(e))	//Se l'elemento non è presente lancio un eccezione
            throw new IllegalArgumentException("Elemento non presente!");
    	
    	DisjointSetElement rappresentante=e.getRef1();	//Ottengo il valore del rappresentante
    	
    	Set<DisjointSetElement> elementi=new HashSet<>(rappresentante.getNumber());	//Creo un nuovo set
    	
    	DisjointSetElement elementoCorrente=rappresentante;	//
    	while(elementoCorrente!=null) {	//Ciclo fino al termine degli elementi
    		elementi.add(elementoCorrente);	//Aggiungo l'elemento al  nuovo set
    		elementoCorrente=elementoCorrente.getRef2();	//Assegno all'elemento corrente il rappresentante 2
        }
    	
    	return elementi;	//Ritorno il set con i nuovi elementi
    }

    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {
    	if (e==null)	//Se l'elemento è nullo lancio un eccezione
            throw new NullPointerException("Elemento nullo!");
    	else if (!isPresent(e))	//Se l'elemento non è presente lancio un eccezione
            throw new IllegalArgumentException("Elemento non presente!");
    	
    	return e.getRef1().getNumber();	//Ritorno la cardinalità
    }

}
