/**
 * 
 */
package it.unicam.cs.asdl2324.es12;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementazione della classe astratta {@code Graph<L>} che realizza un grafo
 * non orientato. Per la rappresentazione viene usata una variante della
 * rappresentazione a liste di adiacenza. A differenza della rappresentazione
 * standard si usano strutture dati più efficienti per quanto riguarda la
 * complessità in tempo della ricerca se un nodo è presente (pseudocostante, con
 * tabella hash) e se un arco è presente (pseudocostante, con tabella hash). Lo
 * spazio occupato per la rappresentazione risultà tuttavia più grande di quello
 * che servirebbe con la rappresentazione standard.
 * 
 * Le liste di adiacenza sono rappresentate con una mappa (implementata con
 * tabelle hash) che associa ad ogni nodo del grafo i nodi adiacenti. In questo
 * modo il dominio delle chiavi della mappa è il set dei nodi, su cui è
 * possibile chiamare il metodo contains per testare la presenza o meno di un
 * nodo. Ad ogni chiave della mappa, cioè ad ogni nodo del grafo, non è
 * associata una lista concatenata dei nodi collegati, ma un set di oggetti
 * della classe GraphEdge<L> che rappresentano gli archi connessi al nodo: in
 * questo modo la rappresentazione riesce a contenere anche l'eventuale peso
 * dell'arco (memorizzato nell'oggetto della classe GraphEdge<L>). Per
 * controllare se un arco è presente basta richiamare il metodo contains in
 * questo set. I test di presenza si basano sui metodi equals ridefiniti per
 * nodi e archi nelle classi GraphNode<L> e GraphEdge<L>.
 * 
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                etichette dei nodi del grafo
 */
public class MapAdjacentListUndirectedGraph<L> extends Graph<L> {

    /*
     * Le liste di adiacenza sono rappresentate con una mappa. Ogni nodo viene
     * associato con l'insieme degli archi uscenti. Nel caso in cui un nodo non
     * abbia archi uscenti è associato con un insieme vuoto.
     */
    private final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    /**
     * Crea un grafo vuoto.
     */
    public MapAdjacentListUndirectedGraph() {
        // Inizializza la mappa con la mappa vuota
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
    	return adjacentLists.size();
    }

    @Override
    public int edgeCount() {
    	int count = 0;
        for (GraphNode<L> node : adjacentLists.keySet()) {
            count += adjacentLists.get(node).size();
        }
        return count;

    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa grafi non orientati
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
    	return adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
    	if (node == null) {
            throw new NullPointerException("Passed parameter cannot be null");
        }
        if (adjacentLists.containsKey(node)) {
            return false;
        }
        adjacentLists.put(node, new HashSet<>());
        return true;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException(
                "Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
    	if (node == null) {
            throw new NullPointerException("Passed parameter cannot be null");
        }
        return adjacentLists.containsKey(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
    	if (label == null) {
            throw new NullPointerException("Passed parameter cannot be null");
        }
        for (GraphNode<L> node : adjacentLists.keySet()) {
            if (node.getLabel().equals(label)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
    	if (node == null) {
            throw new NullPointerException("Passed parameter cannot be null");
        }
        if (!adjacentLists.containsKey(node)) {
            throw new IllegalArgumentException("Passed parameter should be part of the graph");
        }
        Set<GraphNode<L>> out = new HashSet<>();
        for (GraphNode<L> node1 : adjacentLists.keySet()) {
            for (GraphEdge<L> edge : adjacentLists.get(node1)) {
                if (node.equals(edge.getNode2())) {
                    out.add(edge.getNode1());
                } else if (node.equals(edge.getNode1())) {
                    out.add(edge.getNode2());
                }
            }
        }
        return out;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
    	throw new UnsupportedOperationException(
                "Ricerca dei nodi predecessori non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
    	Set<GraphEdge<L>> out = new HashSet<>();
        for (GraphNode<L> node : adjacentLists.keySet()) {
            out.addAll(adjacentLists.get(node));
        }
        return out;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
    	if (edge == null) {
            throw new NullPointerException("Passed parameter cannot be null");
        }
        if (!adjacentLists.containsKey(edge.getNode1()) || !adjacentLists.containsKey(edge.getNode2())) {
            throw new IllegalArgumentException("All nodes of the edge must belong to the graph");
        }
        if (edge.isDirected() != isDirected()) {
            throw new IllegalArgumentException("Invalid edge passed");
        }
        for (GraphEdge<L> edge1 : getEdges()) {
            if (edge.equals(edge1)) {
                return false;
            }
        }
        adjacentLists.get(edge.getNode1()).add(edge);
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
    	if (edge == null) {
            throw new NullPointerException("Parameter passed cannot be null");
        }
        if (!adjacentLists.containsKey(edge.getNode1()) || !adjacentLists.containsKey(edge.getNode2())) {
            throw new IllegalArgumentException("All nodes of the edge have to be part of the graph");
        }
        for (GraphEdge<L> edge1 : adjacentLists.get(edge.getNode1())) {
            if (edge.getNode2().equals(edge1.getNode2())) {
                return true;
            }
        }
        for (GraphEdge<L> edge1 : adjacentLists.get(edge.getNode2())) {
            if (edge.getNode1().equals(edge1.getNode2())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
    	if (node == null) {
            throw new NullPointerException("Passed parameter cannot be null");
        }
        if (!adjacentLists.containsKey(node)) {
            throw new IllegalArgumentException("Passed parameter should be part of graph");
        }
        Set<GraphEdge<L>> out = new HashSet<>();
        for (GraphNode<L> node1 : adjacentLists.keySet()) {
            if (node.equals(node1)) {
                out.addAll(adjacentLists.get(node1));
            } else {
                for (GraphEdge<L> edge : adjacentLists.get(node1)) {
                    if (node.equals(edge.getNode2())) {
                        out.add(edge);
                    }
                }
            }
        }
        return out;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Archi entranti non significativi in un grafo non orientato");
    }

}
