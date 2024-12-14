/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import Notifications.Notifications;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public final class DbConnect {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static synchronized EntityManager getEntityManager() {
        if (em == null) {
            try {
                emf = Persistence.createEntityManagerFactory("HYPEX");
                em = emf.createEntityManager();
            } catch (org.hibernate.service.spi.ServiceException | org.hibernate.exception.JDBCConnectionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Check Database connection");
                alert.setHeaderText("Error while trying to connect to Database!");
                alert.setContentText("Something went wrong, no DB connection.\nTry reconnecting and run the application.");
                alert.showAndWait();
                System.exit(0);
            }
        }
        return em;
    }

    public static void performTransaction(Runnable transactionalLogic) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            transactionalLogic.run();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    public static <T> void updateItem(T t) {
        performTransaction(() -> getEntityManager().merge(t));
    }

    public static <T> void addItem(T t) {
        performTransaction(() -> getEntityManager().persist(t));
    }

    public static <T> void removeItem(T t) {
        performTransaction(() -> getEntityManager().remove(t));
    }

    public static boolean loginCheck(String email, String password, Scene owner) {
        if (isValidInput(email, password, owner))
            try {
                DbConnect.getEntityManager().createNamedQuery("User.loginCheck").setParameter("email", email).setParameter("password", hashPassword(password)).getSingleResult();
                return true;
            } catch (NoResultException ignored) {
                Notifications.createFailNotification("Login Failed", "Check your user name or password", owner, Pos.TOP_CENTER);
            }
        return false;
    }

    public static boolean isValidInput(String username, String password, Scene scene) {
        if (username.isEmpty() || password.isEmpty()) {
            Notifications.createFailNotification("Something Went Wrong!", "You have to fill fields with valid inputs.", scene, Pos.TOP_CENTER);
            return false;
        } else if (!username.matches("^.{6,30}$") || !password.matches("^.{6,15}$")) {
            Notifications.createFailNotification("Something Went Wrong!", "Username and password must be between 6 and 15 characters.", scene, Pos.TOP_CENTER);
            return false;
        } else if (!username.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") || !password.matches("^[a-zA-Z0-9.]+$")) {
            Notifications.createFailNotification("Something Went Wrong!", "Username and password must contain only letters and numbers.", scene, Pos.TOP_CENTER);
            return false;
        }
        return true;
    }

    public static String hashPassword(String password) {
        try {
            return new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(password.getBytes())).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull String getMacAddress() {
        byte[] hardwareAddress = new byte[0];
        try {
            hardwareAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        } catch (SocketException | UnknownHostException ignored) {
        }
        String[] hexadecimal = new String[hardwareAddress.length];
        for (int i = 0; i < hardwareAddress.length; i++)
            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
        return String.join("-", hexadecimal);
    }
}
