create table users(
        id          bigserial primary key,
        user_name   varchar(50),
        password    varchar(20)
        user_email  varchar(30),
        user_src varchar(300)
);

insert into user(user_name,password,user_email,user_src) values
    ('user1','1','email@gmail.com', 'storage-server/src/main/resources/userFolder');