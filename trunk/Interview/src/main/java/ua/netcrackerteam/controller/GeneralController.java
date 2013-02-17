package ua.netcrackerteam.controller;


import ua.netcrackerteam.DAO.UserList;
import ua.netcrackerteam.configuration.HibernateFactory;
import ua.netcrackerteam.configuration.Logable;
import ua.netcrackerteam.configuration.ShowHibernateSQLInterceptor;

import javax.interceptor.Interceptors;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GeneralController implements Logable {

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static String passwordHashing(String pass){
        String hashedPass = null;
        try {
            hashedPass = String.valueOf(("user".hashCode() * pass.hashCode() + (pass.hashCode() + pass.hashCode()) * 10 + pass.hashCode() * "pass".hashCode()) * 10);
        } catch (Exception e) {
            e.printStackTrace();
            logger.getLog().error(e);
        }
        return hashedPass;
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static String passwordHashingMD5(String pass) {
        String hashedPass = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(pass.getBytes(), 0, pass.length());
            hashedPass = new BigInteger(1, m.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            logger.getLog().error(e);
        }
        return hashedPass;
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static int checkLogin(String user, String pass){
        List<UserList> listOfForms = null;
        String userName = null;
        String userPass = null;
        String hashedPass = null;
        int idUserCategory = 0;
        try {
            listOfForms = HibernateFactory.getInstance().getCommonDao().getUser();
            hashedPass = GeneralController.passwordHashing(pass);
            for (UserList userList : listOfForms) {
                userName = userList.getUserName();
                userPass = userList.getPassword();
                if (user.equals(userName) && hashedPass.equals(userPass)){
                    idUserCategory = userList.getIdUserCategory().getIdUSerCategory();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.getLog().error(e);
        }
        return idUserCategory;
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static List<Integer> checkLoginIdUser(String user, String pass){
        List<UserList> listOfForms = null;
        List<Integer> checkedUserIds = new ArrayList<Integer>();
        String userName = null;
        String userPass = null;
        String hashedPass = null;
        try {
            listOfForms = HibernateFactory.getInstance().getCommonDao().getUser();
            hashedPass = GeneralController.passwordHashing(pass);
            for (UserList userList : listOfForms) {
                userName = userList.getUserName();
                userPass = userList.getPassword();
                if (user.equals(userName) && hashedPass.equals(userPass)){
                    checkedUserIds.add(userList.getIdUser());
                    checkedUserIds.add(userList.getIdUserCategory().getIdUSerCategory());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.getLog().error(e);
        }
        return checkedUserIds;
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static void setUsualUser(String userName, String userPassword, String userEmail){
        String active = "active";
        int idUserCategory = 4;
        String hashPassword = passwordHashing(userPassword);
        try {
            HibernateFactory.getInstance().getCommonDao().setUser(userName, hashPassword, userEmail, active, idUserCategory);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.getLog().error(e);
        }
    }

    /*public static String userNameSplitFromEmail(String email){
        String[] splitedString = email.split("@");
        String splitedName = splitedString[0];
        return splitedName;
    }*/

    public static void main(String[] args) throws SQLException {
        setUsualUser("gglex34e", "1234556", "sdfsdf@sdfsdf.df");
        /*String nickName = userNameSplitFromEmail("fdgdfg@gdfgdf.com");
        System.out.println(nickName);*/
        /*List<Integer> ids = checkLoginIdUser("admin", "abyrabyrabyr");
        for(int i = 0; i < ids.size(); i++){
            System.out.println(ids.get(i));
        }*/

//        String hashedPass = passwordHashingMD5("abyrabyrabyr");
//        String hashedPass2 = passwordHashingMD5("abyrabyraby");
//        String hashedPass3 = passwordHashingMD5("abyrabyrabyr");
//        String hashedPass4 = passwordHashingMD5("abyrabyrabr");
//        System.out.println(hashedPass);
//        System.out.println(hashedPass2);
//        System.out.println(hashedPass3);
//        System.out.println(hashedPass4);
//
//        List<UserList> userLists = HibernateFactory.getInstance().getCommonDao().getUser();
//        for (UserList userList : userLists){
//            System.out.println(userList.getUserName() + "   " + userList.getPassword());
//        }
    }
}
