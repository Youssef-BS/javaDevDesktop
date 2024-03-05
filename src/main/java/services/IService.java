package services;

import entities.Abonnement;
import entities.reclamation;
import entities.repondre;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    void add(reclamation r) throws SQLException;

    void update(reclamation r) throws SQLException;

    void delete(reclamation reclamation) throws SQLException;

    void add(repondre rp) throws SQLException;

    void update(repondre rp) throws SQLException;

    void delete(repondre rp) throws SQLException;



    void ajouter(T t) throws SQLException;

//    String authentifier(T t) throws  SQLException ;

    void modifier(T t) throws SQLException;

    void supprimer(int id) throws SQLException;

    List<T> recuperer(String name) throws SQLException;
}
