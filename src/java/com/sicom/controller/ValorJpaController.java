/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicom.controller;

import com.sicom.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.sicom.entities.Codigo;
import com.sicom.entities.Valor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Pablo
 */
public class ValorJpaController implements Serializable {

    public ValorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Valor valor) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Codigo codigoId = valor.getCodigoId();
            if (codigoId != null) {
                codigoId = em.getReference(codigoId.getClass(), codigoId.getId());
                valor.setCodigoId(codigoId);
            }
            em.persist(valor);
            if (codigoId != null) {
                codigoId.getValorList().add(valor);
                codigoId = em.merge(codigoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Valor valor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Valor persistentValor = em.find(Valor.class, valor.getId());
            Codigo codigoIdOld = persistentValor.getCodigoId();
            Codigo codigoIdNew = valor.getCodigoId();
            if (codigoIdNew != null) {
                codigoIdNew = em.getReference(codigoIdNew.getClass(), codigoIdNew.getId());
                valor.setCodigoId(codigoIdNew);
            }
            valor = em.merge(valor);
            if (codigoIdOld != null && !codigoIdOld.equals(codigoIdNew)) {
                codigoIdOld.getValorList().remove(valor);
                codigoIdOld = em.merge(codigoIdOld);
            }
            if (codigoIdNew != null && !codigoIdNew.equals(codigoIdOld)) {
                codigoIdNew.getValorList().add(valor);
                codigoIdNew = em.merge(codigoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = valor.getId();
                if (findValor(id) == null) {
                    throw new NonexistentEntityException("The valor with id " + id + " no longer exists.");
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
            Valor valor;
            try {
                valor = em.getReference(Valor.class, id);
                valor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valor with id " + id + " no longer exists.", enfe);
            }
            Codigo codigoId = valor.getCodigoId();
            if (codigoId != null) {
                codigoId.getValorList().remove(valor);
                codigoId = em.merge(codigoId);
            }
            em.remove(valor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Valor> findValorEntities() {
        return findValorEntities(true, -1, -1);
    }

    public List<Valor> findValorEntities(int maxResults, int firstResult) {
        return findValorEntities(false, maxResults, firstResult);
    }

    private List<Valor> findValorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Valor.class));
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

    public Valor findValor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Valor.class, id);
        } finally {
            em.close();
        }
    }

    public int getValorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Valor> rt = cq.from(Valor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<String> findByCodeId(Integer codigo_id) {
        EntityManager em = getEntityManager();
        
        TypedQuery consulta = em.createNamedQuery("Valor.findByCodigoId", String.class);
        consulta.setParameter("codigoId", codigo_id);
        List<String> listaValores = consulta.getResultList();
        
        return listaValores;
    }
    
    
    
}
