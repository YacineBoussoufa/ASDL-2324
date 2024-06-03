package it.unicam.cs.asdl2324.mp2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template)
 * 		   Yacine Boussoufa yacine.boussoufa@studenti.unicam.it (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMST<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    /*
     * Metodo che effettua l'ordinamento attraverso il QuickSort
     * in modo da ordinare gli archi
     */
    private void QuickSort(List<GraphEdge<L>> edges, int s, int p) {
        if(s<p) {
            int i=partition(edges,s,p);	//Effettuo la partizione

            //Chiamata ricorsiva
            QuickSort(edges,s,i-1);
            QuickSort(edges,i+1,p);
        }
    }
    
    /**
     * Metodo per effettuare la Partition di lista di archi
     * ordinandoli in base al loro peso
     */
    private int partition(List<GraphEdge<L>> edges,int s,int p) {
        GraphEdge<L> pivot=edges.get(p);
        int i=s-1;

        for(int j=s;j<p;j++) {
            GraphEdge<L> jEdge=edges.get(j);

            if(pivot.getWeight()>=jEdge.getWeight()) {
                i++;

                GraphEdge<L> iEdge=edges.set(i,jEdge);
                edges.set(j,iEdge);
            }
        }

        GraphEdge<L> iEdge=edges.set(i+1,pivot);
        edges.set(p,iEdge);
        return i+1;
    }
    
    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
        
        this.disjointSets=new ForestDisjointSets<GraphNode<L>>();	//Inizializzazione forest vuota
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        // TODO implementare
    	if(g==null)	//Se il grafo è nullo lancio un eccezione
    		throw new NullPointerException("Grafo nullo!");
    	else if(g.isDirected())	//Se il grafo non è orientato lancio un eccezione
    		throw new IllegalArgumentException("Il grafo è orientato!");
        for(GraphEdge<L> edge:g.getEdges())	//Ciclo il grafo
            if((!edge.hasWeight())||(edge.getWeight()<0))	//Se il grafo è pesato o con pesi negativi lancio un eccezione
                throw new IllegalArgumentException("Il grafo non è pesato o con pesi negativi");
        
        this.disjointSets=new ForestDisjointSets<GraphNode<L>>();	//Re-inizializzazione per evitare errore nel test MSP4
        
        for(GraphNode<L> element:g.getNodes())	//Esamino gli archi
            this.disjointSets.makeSet(element);	//Creo element alberi, uno per ogni albero
        
        //SORT
        ArrayList<GraphEdge<L>> edgesInAscendingOrder=new ArrayList<>(g.getEdges());	//Salvataggio degli edge
        //sort(edgesInAscendingOrder,new SortCompare());	//Ordinamento degli Edge
        QuickSort(edgesInAscendingOrder, 0, edgesInAscendingOrder.size() - 1);

        
        Set<GraphEdge<L>> tmp=new HashSet<>();	//Set di appoggio
        
        for(GraphEdge<L> element:edgesInAscendingOrder)	//Per ogni arco preso in ordine descrescente
        	//Se le estremità appartengono allo stesso albero, l'arco viene scartato, altrimenti viene aggiunto
        	//al set di appoggio
            if(this.disjointSets.findSet(element.getNode1())==this.disjointSets.findSet(element.getNode2())) {}
            else {
            	tmp.add(element);
                this.disjointSets.union(element.getNode1(),element.getNode2());
            }
        
        return tmp;	//Ritorno il set di appoggio
    }
    
}
