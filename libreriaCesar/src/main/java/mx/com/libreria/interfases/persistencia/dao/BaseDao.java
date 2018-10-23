package mx.com.libreria.interfases.persistencia.dao;

import java.io.Serializable;

import java.util.List;

public interface BaseDao {

	public Object getByPK(Serializable clase, Serializable value);
    	
	public List<?> findByNamedParam(String query, String[] param, Object[] values);
	
	public List<?> find(String query, Object[] values);
		
	public List<?> find(Serializable clase);
	
    public Serializable save(Serializable serializable);
    
    public Serializable merge(Serializable serializable);
    	    
    public void saveOrUpdate(Serializable serializable);
    
    public void delete(Serializable serializable);
    
    public void update(Serializable serializable);
    
}
