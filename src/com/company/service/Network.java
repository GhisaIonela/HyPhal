package com.company.service;

import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.graph.Graph;
import com.company.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Network models the structure of the social network as a graph
 */
public class Network {
    private Graph<User> graph;
    private Repository<Long, User> userRepository;
    private Repository<Long, Friendship> friendshipRepository;
    private static final Network networkInstance = new Network();

    /**
     * Creates a new Network
     */
    private Network(){
        this.graph = new Graph<>();
    }

    /**
     * Get the network's graph
     * @return the graph
     */
    public Graph<User> getGraph(){
        return this.graph;
    }

    /**
     * Get the instance of Network
     * @return the instance
     */
    public static Network getInstance(){
        return networkInstance;
    }

    /**
     * Set a user repository
     * @param userRepository the user repository
     */
    public void setUserRepository(Repository<Long, User> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Set a friendship repository
     * @param friendshipRepository the friendship repository
     */
    public void setFriendshipRepository(Repository<Long, Friendship> friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * Add a user to the network
     * @param user - the user to add
     */
    public void addUserToNetwork(User user){
        getGraph().addVertex(user);
    }

    /**
     * Delete a user from the network
     * @param user - the user to be deleted
     */
    public void deleteUserFromNetwork(User user){
        getGraph().deleteVertex(user);
    }

    /**
     * Add a friendship to the network
     * @param friendship - the friendship to add
     */
    public void addFriendshipToNetwork(Friendship friendship){
        User user1 = userRepository.findOne(friendship.getIdUser1());
        User user2 = userRepository.findOne(friendship.getIdUser2());
        getGraph().addEdge(user1, user2);
    }

    /**
     * Delete a friendship from the network
     * @param friendship - the friendship to be deleted
     */
    public void deleteFriendshipFromNetwork(Friendship friendship){
        User user1 = userRepository.findOne(friendship.getIdUser1());
        User user2 = userRepository.findOne(friendship.getIdUser2());
        getGraph().deleteEdge(user1, user2);
    }

    /**
     * Load all the users and friendships to the network
     */
    public void loadNetwork(){
        getGraph().clearGraph();

        if(userRepository.findAll()!=null){
            for(User user: userRepository.findAll()){
                addUserToNetwork(user);
            }
        }

        if(friendshipRepository.findAll()!=null)
        {
            for(Friendship friendship: friendshipRepository.findAll()){
                addFriendshipToNetwork(friendship);
            }
        }
    }

    /**
     * Perform a dfs on the network's graph
     * @param userVertex - the vertex
     * @param visited - a map of vertex marked as visited or unvisited
     * @param connectedUsers - the connected vertices to userVertex
     */
    public void dfs(User  userVertex, Map<User, Boolean> visited, List<Long> connectedUsers){
        visited.put(userVertex, true);
        connectedUsers.add(userVertex.getId());
        for(User vertex: graph.getConnectedVertices(userVertex)){
            if(!visited.get(vertex)){
                dfs(vertex, visited, connectedUsers);
            }
        }
    }

    /**
     * Get the connected(convex) components from the network's graph
     * @return a list of all connected components
     */
    public List<List<Long>> getConnectedComponents(){
        List<List<Long>> allConnectedComponents = new ArrayList<>();
        List<Long> connectedUsers = new ArrayList<>();
        Map<User, Boolean> visited = new HashMap<>();
        for(User vertex: graph.getVertices()){
            visited.put(vertex, false);
        }
        for(User user: graph.getVertices()){
            if(!visited.get(user)){
                dfs(user, visited, connectedUsers);
                List<Long> copyConnectedUsers = new ArrayList<>(connectedUsers);
                allConnectedComponents.add(copyConnectedUsers);
                connectedUsers.clear();
            }
        }
        return allConnectedComponents;
    }

    /**
     * Count the number of connected components in the network's graph
     * @return the number of connected components
     */
    public int getNumberOfConnectedComponents(){
        List<List<Long>> allConnectedComponents = getConnectedComponents();
        for(List<Long> component: allConnectedComponents){
            System.out.println(component);
        }
        return allConnectedComponents.size();
    }

    /**
     * Get the most sociable community frm the network
     * The most sociable community is the connected component with the greatest number of vertices
     * @return the connected component with the greatest number of vertices
     */
    public List<Long> getMostSociableCommunity(){
        List<Long> mostSociable = new ArrayList<>();
        List<List<Long>> allConnectedComponents = getConnectedComponents();
        for(List<Long> component: allConnectedComponents){
            if(component.size() > mostSociable.size()){
                mostSociable = component;
            }
        }
        return mostSociable;
    }

}
