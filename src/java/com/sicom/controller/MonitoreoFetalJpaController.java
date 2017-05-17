/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import com.sicom.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Expediente;
import com.sicom.entities.MonitoreoFetal;
import com.sicom.entities.Personal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
 */
public class MonitoreoFetalJpaController implements Serializable {

    public MonitoreoFetalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MonitoreoFetal monitoreoFetal) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Expediente expedienteid = monitoreoFetal.getExpedienteid();
            if (expedienteid != null) {
                expedienteid = em.getReference(expedienteid.getClass(), expedienteid.getId());
                monitoreoFetal.setExpedienteid(expedienteid);
            }
            Personal personalcedula = monitoreoFetal.getPersonalcedula();
            if (personalcedula != null) {
                personalcedula = em.getReference(personalcedula.getClass(), personalcedula.getCedula());
                monitoreoFetal.setPersonalcedula(personalcedula);
            }
            em.persist(monitoreoFetal);
            if (expedienteid != null) {
                expedienteid.getMonitoreoFetalList().add(monitoreoFetal);
                expedienteid = em.merge(expedienteid);
            }
            if (personalcedula != null) {
                personalcedula.getMonitoreoFetalList().add(monitoreoFetal);
                personalcedula = em.merge(personalcedula);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMonitoreoFetal(monitoreoFetal.getId()) != null) {
                throw new PreexistingEntityException("MonitoreoFetal " + monitoreoFetal + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MonitoreoFetal monitoreoFetal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MonitoreoFetal persistentMonitoreoFetal = em.find(MonitoreoFetal.class, monitoreoFetal.getId());
            Expediente expedienteidOld = persistentMonitoreoFetal.getExpedienteid();
            Expediente expedienteidNew = monitoreoFetal.getExpedienteid();
            Personal personalcedulaOld = persistentMonitoreoFetal.getPersonalcedula();
            Personal personalcedulaNew = monitoreoFetal.getPersonalcedula();
            if (expedienteidNew != null) {
                expedienteidNew = em.getReference(expedienteidNew.getClass(), expedienteidNew.getId());
                monitoreoFetal.setExpedienteid(expedienteidNew);
            }
            if (personalcedulaNew != null) {
                personalcedulaNew = em.getReference(personalcedulaNew.getClass(), personalcedulaNew.getCedula());
                monitoreoFetal.setPersonalcedula(personalcedulaNew);
            }
            monitoreoFetal = em.merge(monitoreoFetal);
            if (expedienteidOld != null && !expedienteidOld.equals(expedienteidNew)) {
                expedienteidOld.getMonitoreoFetalList().remove(monitoreoFetal);
                expedienteidOld = em.merge(expedienteidOld);
            }
            if (expedienteidNew != null && !expedienteidNew.equals(expedienteidOld)) {
                expedienteidNew.getMonitoreoFetalList().add(monitoreoFetal);
                expedienteidNew = em.merge(expedienteidNew);
            }
            if (personalcedulaOld != null && !personalcedulaOld.equals(personalcedulaNew)) {
                personalcedulaOld.getMonitoreoFetalList().remove(monitoreoFetal);
                personalcedulaOld = em.merge(personalcedulaOld);
            }
            if (personalcedulaNew != null && !personalcedulaNew.equals(personalcedulaOld)) {
                personalcedulaNew.getMonitoreoFetalList().add(monitoreoFetal);
                personalcedulaNew = em.merge(personalcedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = monitoreoFetal.getId();
                if (findMonitoreoFetal(id) == null) {
                    throw new NonexistentEntityException("The monitoreoFetal with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MonitoreoFetal monitoreoFetal;
            try {
                monitoreoFetal = em.getReference(MonitoreoFetal.class, id);
                monitoreoFetal.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The monitoreoFetal with id " + id + " no longer exists.", enfe);
            }
            Expediente expedienteid = monitoreoFetal.getExpedienteid();
            if (expedienteid != null) {
                expedienteid.getMonitoreoFetalList().remove(monitoreoFetal);
                expedienteid = em.merge(expedienteid);
            }
            Personal personalcedula = monitoreoFetal.getPersonalcedula();
            if (personalcedula != null) {
                personalcedula.getMonitoreoFetalList().remove(monitoreoFetal);
                personalcedula = em.merge(personalcedula);
            }
            em.remove(monitoreoFetal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MonitoreoFetal> findMonitoreoFetalEntities() {
        return findMonitoreoFetalEntities(true, -1, -1);
    }

    public List<MonitoreoFetal> findMonitoreoFetalEntities(int maxResults, int firstResult) {
        return findMonitoreoFetalEntities(false, maxResults, firstResult);
    }

    private List<MonitoreoFetal> findMonitoreoFetalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MonitoreoFetal.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MonitoreoFetal findMonitoreoFetal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MonitoreoFetal.class, id);
        } finally {
            em.close();
        }
    }

    public int getMonitoreoFetalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MonitoreoFetal> rt = cq.from(MonitoreoFetal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
