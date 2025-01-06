package com.create_builds.app.baserestcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.create_builds.app.authcontroller.CookieVerifier;
import com.create_builds.app.reposervice.RepoService;

// Base class for all rest controllers. Each individual controller will have their own request mapping.
public abstract class BaseRestController<T, ID, R extends CrudRepository<T, ID>, S extends RepoService<T, ID, R>> {
	// DI for the required service.
	
    @Autowired
    protected S modelrepo;

    protected List<T> getAll() {
        return modelrepo.readModels();
    }

    protected T getById(ID id) {
        return modelrepo.getModelById(id);
    }

    protected T create(T entity) {
        return modelrepo.saveModel(entity);
    }

    protected T update(ID id, T entity) {
        return modelrepo.updateModel(entity, id);
    }

    protected void delete(ID id) {
        modelrepo.delModel(id);
    }

    // Below is HTTP responses for error/success handling. Expand if needed.

    @GetMapping("/{id}")
    public ResponseEntity<T> fetchById(@PathVariable ID id) {
        try {
            T entity = getById(id);
            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<T> createEntity(@Validated @RequestBody T entity) {
        T createdEntity = create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> updateEntity(@PathVariable ID id, @RequestBody T entity) {
        try {
            T updatedEntity = update(id, entity);
            return ResponseEntity.ok(updatedEntity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable ID id) {
        try {
            delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
