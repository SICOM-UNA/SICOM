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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author WVQ
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
    
    public List<String> findByCodeId(Integer codigo) {
        List<String> listaValores = new ArrayList<>();

        switch(codigo){
            case 0://género
                listaValores.add("Masculino");
                listaValores.add("Femenino");
                
                break;
            case 1://estado civil
                listaValores.add("Soltero/a");
                listaValores.add("Casado/a");
                listaValores.add("Viudo/a");
                listaValores.add("Divorciado/a");
                
                break;
            case 2://vínculo
                listaValores.add("Padre");
                listaValores.add("Madre");
                listaValores.add("Familiar");
                listaValores.add("Esposo/a");
                listaValores.add("Amigo/a");
                listaValores.add("Otro");
                
                break;
            case 3://referencias
                listaValores.add("Periodico");
                listaValores.add("Internet");
                listaValores.add("Television");
                listaValores.add("Otro");
                
                break;
            case 4://tipoPeriodo
                listaValores.add("Regular");
                listaValores.add("Irregular");
                listaValores.add("No Presenta");
                
                break;
             case 5://tipoPlanificacion
                listaValores.add("GO");
                listaValores.add("GI");
                listaValores.add("Condón");
                listaValores.add("Ritmo");
                listaValores.add("Coito Interrumpido");
                listaValores.add("DIU");
                listaValores.add("SPB");
                listaValores.add("Vasectomia");
                listaValores.add("Otro");
                
                break;
                
             case 6://nivel autorizacion
                 listaValores.add("2 Subjefe");
                 listaValores.add("3 Medico");
                 listaValores.add("4 Asistente");
                 listaValores.add("5 Secretaria");
             
                 break;
           
        }

        return listaValores;
   }
}