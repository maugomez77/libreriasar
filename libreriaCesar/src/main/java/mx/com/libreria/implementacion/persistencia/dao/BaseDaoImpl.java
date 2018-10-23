package mx.com.libreria.implementacion.persistencia.dao;

import java.io.Serializable;

import java.util.List;

import mx.com.libreria.interfases.persistencia.dao.BaseDao;

import mx.com.libreria.manager.Logs;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {

    public Object getByPK(Serializable clase, Serializable value) {    	
        return this.getHibernateTemplate().get(clase.getClass(), value);    	 
    }
    
    public List<?> findByNamedParam(String query, String[] param, Object[] values) {
    	return this.getHibernateTemplate().findByNamedParam(query, param, values);
    }
    
    public List<?> find(String query, Object[] values) {
        return getHibernateTemplate().find(query, values);
    }
    
    public List<?> find(Serializable clase) {
        return getHibernateTemplate().find("from " + clase.getClass().getName());
    }

    public void delete(Serializable serializable) {
    	Session session = null;
    	Transaction tx = null;    	
    	try { 
    		session = this.getHibernateTemplate().getSessionFactory().openSession();
    		tx = session.beginTransaction();
    		tx.begin();    		
    		session.delete(serializable);    		
    		tx.commit();    		    		
    	} catch (HibernateException e) {
    		Logs.error(BaseDaoImpl.class, "Error en el metodo delete " + e.toString());
    		if (tx != null && tx.isActive()) {    			
    			tx.rollback();    			
    		}
    		throw e;
    	 } finally {
    		 if (session != null) { 
    			 session.close();
    		 }
    	 }
    }    	
        
    public Serializable save(Serializable serializable) {
    	Session session = null;
    	Transaction tx = null;
    	Serializable obj = null;
    	try { 
    		session = this.getHibernateTemplate().getSessionFactory().openSession();
    		tx = session.beginTransaction();
    		tx.begin();    		
    		obj = session.save(serializable);    		
    		tx.commit();    		    		
    	} catch (HibernateException e) {
    		Logs.error(BaseDaoImpl.class, "Error en el metodo save " + e.toString());
    		obj = null;
    		if (tx != null && tx.isActive()) {    			
    			tx.rollback();    			    			
    		}
    		throw e;
    	 } finally {
    		 if (session != null) { 
    			 session.close();
    		 }
    	 }
    	 return obj;
    }

    public Serializable merge(Serializable serializable) {
    	Session session = null;
    	Transaction tx = null;
    	Serializable obj = null;    	
    	try { 
    		session = this.getHibernateTemplate().getSessionFactory().openSession();
    		tx = session.beginTransaction();
    		tx.begin();    		
    		obj = (Serializable) session.merge(serializable);    		
    		tx.commit();    		    		
    	} catch (HibernateException e) {
    		Logs.error(BaseDaoImpl.class, "Error en el metodo merge " + e.toString());
    		obj = null;
    		if (tx != null && tx.isActive()) {    			
    			tx.rollback();    			    			
    		}
    		throw e;
    	 } finally {
    		 if (session != null) { 
    			 session.close();
    		 }
    	 }
    	 return obj;
	}
    
    public void update(Serializable serializable) {
    	Session session = null;
    	Transaction tx = null;    	    
    	try { 
    		session = this.getHibernateTemplate().getSessionFactory().openSession();
    		tx = session.beginTransaction();
    		tx.begin();    		
    		session.update(serializable);    		
    		tx.commit();    		    		
    	} catch (HibernateException e) {
    		Logs.error(BaseDaoImpl.class, "Error en el metodo update " + e.toString());
    		if (tx != null && tx.isActive()) {    			
    			tx.rollback();    			    			
    		}
    		throw e;
    	 } finally {
    		 if (session != null) { 
    			 session.close();
    		 }
    	 }    	        
    }

	public void saveOrUpdate(Serializable serializable) {
		Session session = null;
    	Transaction tx = null;    	    
    	try { 
    		session = this.getHibernateTemplate().getSessionFactory().openSession();
    		tx = session.beginTransaction();
    		tx.begin();    		
    		session.saveOrUpdate(serializable);    		
    		tx.commit();    		    		
    	} catch (HibernateException e) {
    		Logs.error(BaseDaoImpl.class, "Error en el metodo saveOrUpdate " + e.toString());
    		if (tx != null && tx.isActive()) {    			
    			tx.rollback();    			    			
    		}
    		throw e;
    	 } finally {
    		 if (session != null) { 
    			 session.close();
    		 }
    	 }
	}
}
