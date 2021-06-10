package repository;

import entity.User;
import hibernate.HibernateSessionFactory;

public class UserRepository {

    public User findById(Long id){
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, id);
    }
}
