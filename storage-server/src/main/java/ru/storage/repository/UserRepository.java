package ru.storage.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.storage.entity.User;
import ru.storage.hibernate.HibernateSessionFactory;

import javax.persistence.NoResultException;

public class UserRepository {

    public User findById(Long id){
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, id);

    }

    public User findByUserName(String userName, String password) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        System.out.println("Запрос в findUserByName");
        String query = "from User where userName = :value and password = :pass";
        Query queryObject = session.createQuery(query,User.class);
        queryObject.setParameter("value", userName);
        queryObject.setParameter("pass", password);
        User user = null;
        try {
            user = (User) queryObject.getSingleResult();
        }catch (NoResultException e){

        }
        return user;
    }
    public void save(User user){
        try {
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
