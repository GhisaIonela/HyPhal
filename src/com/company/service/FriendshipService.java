package com.company.service;

import com.company.domain.FriendRequest;
import com.company.domain.FriendRequestStatus;
import com.company.domain.Friendship;
import com.company.domain.User;
import com.company.events.ChangeEventType;
import com.company.events.RequestChangeEvent;
import com.company.exceptions.ServiceException;
import com.company.exceptions.ValidationException;
import com.company.observer.Observable;
import com.company.observer.Observer;
import com.company.repository.Repository;
import com.company.repository.db.FriendRequestsDbRepository;
import com.company.repository.db.FriendshipDbRepository;
import com.company.repository.db.UserDbRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * FriendshipService is a service for Friendship class
 */
public class FriendshipService implements Observable<RequestChangeEvent> {
    private FriendshipDbRepository friendshipRepository;
    private UserDbRepository userRepository;
    private FriendRequestsDbRepository friendRequestsDbRepository;

    private List<Observer<RequestChangeEvent>> observers = new ArrayList<>();

    /**
     * Constructs a new FriendshipService
     * @param friendshipRepository - the repository for Friendship class
     * @param userRepository - the repository for User class
     */
    public FriendshipService(FriendshipDbRepository friendshipRepository, UserDbRepository userRepository, FriendRequestsDbRepository friendRequestsDbRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.friendRequestsDbRepository = friendRequestsDbRepository;
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
     * Search by id for a friendship
     * @param idUser1 - the id of the first user
     * @param idUser2 - the id of the second user
     * @return the friendship with the specified id
     *         or null if there is no friendship with the given user ids
     */
    public Friendship findOne(Long idUser1, Long idUser2){
        try{
            return friendshipRepository.findOne(idUser1, idUser2);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Search by id for a friendship, with no set order for the ids
     * @param idUser1 - the id of the first user
     * @param idUser2 - the id of the second user
     * @return the friendship with the specified id
     *         or null if there is no friendship with the given user ids
     */
    public Friendship findAny(Long idUser1, Long idUser2){
        try{
            return friendshipRepository.findAny(idUser1, idUser2);
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
     * @param idUser1 - first user's id
     * @param idUser2 - second user's id
     * @return null- if the given friendship is saved
     *         otherwise returns the friendship (id friendship exists)
     * @throws ServiceException if a friendship between this two users already exists
     */
    public Friendship save(Long idUser1, Long idUser2){
        try{
            if(userRepository.findOne(idUser1)==null || userRepository.findOne(idUser2)==null){
                throw new ServiceException("Doesn't exist any user with one(or both) of the ids provided for saving the friendship");
            }

            if(friendshipRepository.findOne(idUser1, idUser2)!=null)
                throw new ServiceException("A friendship between this two users already exists");

            return friendshipRepository.save(new Friendship(idUser1, idUser2));
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
        catch (ValidationException v){
            System.out.println(v.getMessage());
        }
        return null;
    }

    /**
     * Save a new friendship
     * @param idUser1 - first user's id
     * @param idUser2 - second user's id
     * @return the friendship if saved
     *         null if the friendship already exists
     * @throws ServiceException if a friendship between this two users already exists
     */
    public Friendship saveAndReturn(Long idUser1, Long idUser2){
        try{
            if(userRepository.findOne(idUser1)==null || userRepository.findOne(idUser2)==null){
                throw new ServiceException("Doesn't exist any user with one(or both) of the ids provided for saving the friendship");
            }

            if(friendshipRepository.findOne(idUser1, idUser2)!=null)
                throw new ServiceException("A friendship between this two users already exists");

            Friendship friendship =  friendshipRepository.saveAndReturn(new Friendship(idUser1, idUser2));
            notifyObservers(new RequestChangeEvent(ChangeEventType.ADD));
            return friendship;
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
            return friendshipRepository.delete(id);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Delete a friendship by the user's ids and the related friend request
     * @param idUser1 - the first user's id
     * @param idUser2 - the second user's id
     * @return the removed friendship or null if there is no friendship with the given id
     */
    public Friendship delete(Long idUser1, Long idUser2){
        try{
            FriendRequest friendRequest = friendRequestsDbRepository.findAny(idUser1, idUser2);
            if(friendRequest == null)
                throw new ServiceException("There is no friend request for this friendship to delete");

            friendRequestsDbRepository.delete(friendRequest.getId());

            Friendship friendship = friendshipRepository.delete(friendshipRepository.findAny(friendRequest.getIdFrom(), friendRequest.getIdTo()).getId());
            notifyObservers(new RequestChangeEvent(ChangeEventType.DELETE));
            return friendship;
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
    public Friendship update(Long id, Long idUser1, Long idUser2, LocalDateTime dateTime){

        //to be continued

        try{
            if(userRepository.findOne(idUser1)==null || userRepository.findOne(idUser2)==null){
                throw new ServiceException("Doesn't exist any user with one(or both) of the ids provided for update");
            }
            Friendship updatedFriendship = new Friendship(idUser1, idUser2, dateTime);
            updatedFriendship.setId(id);
            return friendshipRepository.update(updatedFriendship);
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ValidationException v){
            System.out.println(v.getMessage());
        }
        return null;
    }

    @Override
    public void addObserver(Observer<RequestChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<RequestChangeEvent> e) {

    }

    @Override
    public void notifyObservers(RequestChangeEvent requestChangeEvent) {
        observers.forEach(requestChangeEventObserver -> requestChangeEventObserver.update(requestChangeEvent));
    }
}
