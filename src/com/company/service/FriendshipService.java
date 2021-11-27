package com.company.service;

import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.exceptions.ServiceException;
import com.company.exceptions.ValidationException;
import com.company.repository.Repository;

import java.util.List;

/**
 * FriendshipService is a service for Friendship class
 */
public class FriendshipService {
    private Repository<Long, Friendship> friendshipRepository;
    private Repository<Long, User> userRepository;

    /**
     * Constructs a new FriendshipService
     * @param friendshipRepository - the repository for Friendship class
     * @param userRepository - the repository for User class
     */
    public FriendshipService(Repository<Long, Friendship> friendshipRepository, Repository<Long, User> userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    /**
     * Search by id for a friendship
     * @param id - the friendship's id
     * @return the friendship with the specified id
     *          or null - if there is no friendship with the given id
     */
    public Friendship findOne(Long id){
        try{
            return friendshipRepository.findOne(id);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Gets all friendships
     * @return all friendships
     */
    public Iterable<Friendship> findAll(){
        return friendshipRepository.findAll();
    }

    /**
     * Save a new friendship
     * @param id - the friendship's id
     * @param idUser1 - first user's id
     * @param idUser2 - second user's id
     * @return null- if the given friendship is saved
     *         otherwise returns the friendship (id friendship exists)
     * @throws ServiceException if a friendship between this two users already exists
     */
    public Friendship save(Long id, Long idUser1, Long idUser2){
        try{
            if(userRepository.findOne(idUser1)==null || userRepository.findOne(idUser2)==null){
                throw new ServiceException("Doesn't exist any user with one(or both) of the ids provided for saveing the friendship");
            }
            Friendship friendship = new Friendship(idUser1, idUser2);
            friendship.setId(id);

            for(Friendship f:findAll()){
                if(f.equals(friendship)){
                    throw new ServiceException("A friendship between this two users already exists");
                }
            }
            Friendship saved =  friendshipRepository.save(friendship);

            if(saved == null){
                User user1 = userRepository.findOne(idUser1);
                User user2 = userRepository.findOne(idUser2);

                List<User> friends1 = user1.getFriends();
                User user2Copy = new User(user2.getFirstName(), user2.getLastName());
                user2Copy.setId(user2.getId());
                if(friends1!=null) {
                    friends1.add(user2Copy);
                    user1.setFriends(friends1);
                }

                List<User> friends2 = user2.getFriends();
                User user1Copy = new User(user1.getFirstName(), user1.getLastName());
                user1Copy.setId(user1.getId());
                if(friends2!=null){
                    friends2.add(user1Copy);
                    user2.setFriends(friends2);
                }
            }
            return saved;
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
        catch (ValidationException v){
            System.out.println(v.getMessage());
        }
        return null;
    }

    /**
     * Delete a friendship
     * @param id - the friendship's id
     * @return the removed friendship or null if there is no friendship with the given id
     */
    public Friendship delete(Long id){
        try{
            Friendship deleted = friendshipRepository.delete(id);
            if(deleted!=null){
                    User user1 = userRepository.findOne(deleted.getIdUser1());
                    User user2 = userRepository.findOne(deleted.getIdUser2());
                    List<User> friends1 = user1.getFriends();
                    List<User> friends2 = user2.getFriends();

                    friends1.removeIf(friend -> friend.getId() == user2.getId());
                    friends2.removeIf(friend -> friend.getId() == user1.getId());
                }
            return deleted;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Update a friendship
     * @param id - friendship id
     * @param idUser1 - new first user's id
     * @param idUser2 - new second user's id
     * @return null - if the friendship is updated,
     *                otherwise returns the friendship  - (e.g id does not exist).
     * @throws ServiceException if doesn't exist any user with one(or both) of the ids provided for update
     */
    public Friendship update(Long id, Long idUser1, Long idUser2){

        //to be continued

        try{
            if(userRepository.findOne(idUser1)==null || userRepository.findOne(idUser2)==null){
                throw new ServiceException("Doesn't exist any user with one(or both) of the ids provided for update");
            }
            Friendship updatedFriendship = new Friendship(idUser1, idUser2);
            updatedFriendship.setId(id);
            return friendshipRepository.update(updatedFriendship);
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ValidationException v){
            System.out.println(v.getMessage());
        }
        return null;
    }
}
