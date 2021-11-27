package com.company.graph;

import java.util.HashMap;
import java.util.HashSet;

public class Graph<T> {
    private HashMap<T, HashSet<T>> adjacencyList;

    public Graph(){
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Add a new vertex in graph
     * @param vertex the vertex to be added
     * @throws IllegalArgumentException if the vertex already exists
     */
    public void addVertex(T vertex){
        if (this.adjacencyList.containsKey(vertex)){
            throw new IllegalArgumentException("This vertex exists");
        }
        this.adjacencyList.put(vertex, new HashSet<>());
    }

    /**
     * Delete a vertex from the graph
     * @param vertex the vertex to be deleted
     * @throws IllegalArgumentException if the vertex doesn't exist
     */
    public void deleteVertex(T vertex){
        if (!this.adjacencyList.containsKey(vertex)){
            throw new IllegalArgumentException("This vertex doesn't exist");
        }
        this.adjacencyList.remove(vertex);
        for(T elem: this.getVertices()){
            this.adjacencyList.get(elem).remove(vertex);
         }
    }

    /**
     * Gets all vertices from the graph
     * @return An iterable which contains all the vertices
     */
    public Iterable<T> getVertices(){
        return this.adjacencyList.keySet();
    }

    /**
     * Add a new edge into the graph between two vertices
     * @param vertex1 first vertex
     * @param vertex2 second vertex
     * @throws IllegalArgumentException if one of the vertices doesn't exist(or both)
     */
    public void addEdge(T vertex1, T vertex2){
        if (!this.adjacencyList.containsKey(vertex1) || !this.adjacencyList.containsKey(vertex2))
            throw new IllegalArgumentException("One of the vertices doesn't exist in graph(or both)");
        this.adjacencyList.get(vertex1).add(vertex2);  //add returneaza true daca obiectul s-a adaugat in set
        this.adjacencyList.get(vertex2).add(vertex1);
    }

    /**
     * Verify if two vertices are adjacent
     * @param vertex1 first vertex
     * @param vertex2 second vertex
     * @return true if the vertices are adjacents, false otherwise
     */
    public boolean isAdjacent(T vertex1, T vertex2){
        return this.adjacencyList.get(vertex1).contains(vertex2);
    }

    /**
     * Delete an edge
     * @param vertex1 first vertex that forms the edge
     * @param vertex2 second vertex that forms the edge
     * @throws IllegalArgumentException if there's no edge between vertex1 and vertex2
     */
    public void deleteEdge(T vertex1, T vertex2){
        if(isAdjacent(vertex1, vertex2)){
            this.adjacencyList.get(vertex1).remove(vertex2);
            this.adjacencyList.get(vertex2).remove(vertex1);
        }
        else
            throw new IllegalArgumentException("Doesn't exist an edge between this two vertices");
    }

    /**
     * Gets all the connected vertices with a given vertex
     * @param vertex the vertex
     * @return An iterable with all the connections
     */
    public Iterable<T> getConnectedVertices(T vertex){
        return this.adjacencyList.get(vertex);
    }

    /**
     * Clears the graph. Removes all the vertices and endges from the graph
     */
    public void clearGraph(){
        this.adjacencyList.clear();
    }


}
