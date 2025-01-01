package com.create_builds.app.reposervice;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

// Abstract class to define "CRD" for each service, "U" needs to be manually defined.
public abstract class RepoService<T, ID, R extends CrudRepository<T, ID>> {
	@Autowired
	protected R modelrepo;
	
	public T saveModel(T model) {
		return modelrepo.save(model);
	}
	public List<T> readModels(){
		return (List<T>)modelrepo.findAll();
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getGenericClass() {
	    return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
	            .getActualTypeArguments()[0];
	}

	public T getModelById(ID id) {
	    return modelrepo.findById(id).orElseThrow(() ->
	        new IllegalArgumentException(getGenericClass().getSimpleName() 
	                + " Model not found with Id: " + id)
	    );
	}
	
	public abstract T updateModel(T model, ID id);
	
	public void delModel(ID id) {
		modelrepo.deleteById(id);
	}
}