package service;

import entity.User;
import lombok.NoArgsConstructor;
import repository.UserRepository;

@NoArgsConstructor
public class UserService {

    UserRepository userRepository = new UserRepository();

    public User findUser(long id){
        return userRepository.findById(id);
        
    }
}
