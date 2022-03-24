package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.UploadedFile;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UploadFileRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public List<UploadedFile> getAllFiles() {
        Session session = sessionFactory.getCurrentSession();
        Query<UploadedFile> query = session.createQuery("from UploadedFile", UploadedFile.class);
        List<UploadedFile> allUploadedFiles = query.getResultList();

        return allUploadedFiles;
    }

    @Transactional
    public UploadedFile getFileViaId(int id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            UploadedFile uploadedFile = session.get(UploadedFile.class, id);
            return uploadedFile;
        } catch (Exception e) {
            throw new FileNotFoundException("File not found with id " + id);
        }
    }

    @Transactional
    public void addNewFile(UploadedFile uploadedFile) {
        Session session = sessionFactory.getCurrentSession();
        session.save(uploadedFile);
    }

    @Transactional
    public UploadedFile getLastFile() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UploadedFile.class).setProjection(Projections.max("id"));
        int id = (int) criteria.uniqueResult();
        UploadedFile uploadedFile = getFileViaId(id);
        return uploadedFile;
    }

    @Transactional
    public void deleteFile(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query<UploadedFile> query = session.createQuery("delete from UploadedFile " + "where id=" + id);
        query.executeUpdate();
    }
}
