package ru.storage.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.storage.entity.User;
import ru.storage.hibernate.HibernateSessionFactory;

public class UserRepository {

    public User findById(Long id){
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, id);

    }

    public User findByUserName(String userName) {
        System.out.println("Запрос в findUserByName");
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        String query = "from User where userName = :value";
        Query queryObject = session.createQuery(query,User.class);
        queryObject.setParameter("value", userName);
        User user =(User) queryObject.getSingleResult();
        System.out.println(user.getUserName());
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
