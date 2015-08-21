/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.repository;

import com.mysema.query.SimpleQuery;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.StringPath;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 *
 * @author thomas
 */
@Repository(value = "queryDslSupportJpaImpl")
public class QueryDslSupportJpaImpl<T, ID> implements IQueryDslSupport<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    public QueryDslSupportJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public QueryDslSupportJpaImpl() {
    }
    
    @Override
    public void create( T entity){
        this.entityManager.persist(entity);
    }
    
    @Override
    public void update( T entity){
        this.entityManager.merge(entity);
    }
    
    @Override
    public void deleteByID( Class<T> clazz, ID id){
        T t = this.entityManager.find(clazz, id);
        this.entityManager.remove(t);
    }

    @Override
    public <T> Collection<T> findByDetachedCriteria(JPAQuery query, EntityPath entityPath) {
        List<T> resultSet = new ArrayList<>();
        JPAQuery newQuery = query.clone(entityManager);
        resultSet.addAll(newQuery.list(entityPath));
        return resultSet;
    }

    @Override
    public <T> Collection<T> findByDetachedCriteria(JPAQuery query, EntityPath entityPath, Integer actualSize, Integer batchSize) {
        List<T> resultSet = new ArrayList<>();
        JPAQuery newQuery = query.clone(entityManager);
        if (actualSize != null) {
            newQuery = newQuery.offset(actualSize);
        }
        if (batchSize != null) {
            newQuery = newQuery.limit(batchSize);
        }
        resultSet.addAll(newQuery.list(entityPath));
        return resultSet;
    }

    @Override
    public <T> Collection<T> findByCriteria(Predicate predicate, EntityPath entityPath, Integer actualSize, Integer batchSize) {
        if (entityPath == null) {
            throw new IllegalArgumentException(" The EntityPath parameter must not be null");
        }
        if (predicate == null) {
            throw new IllegalArgumentException(" The Predicate parameter must not be null. OtherWise use the generic findAll method");
        }

        List<T> resultSet = new ArrayList<>();        
        JPAQuery query = (JPAQuery) this.createQuery(predicate, entityPath, actualSize, batchSize);
        resultSet.addAll(query.list(entityPath));
        return resultSet;
    }

    @Override
    public <T> Collection<T> findByCriteria(Predicate predicate, EntityPath entityPath) {
        List<T> resultSet = new ArrayList<>();
        JPAQuery query = (JPAQuery) this.createQuery(predicate, entityPath,  null, null);
        resultSet.addAll(query.list(entityPath));
        return resultSet;
    }

    @Override
    public <T> T findOneByCriteria(Predicate predicate, EntityPath path) {
        Collection<T> resultSet = this.findByCriteria(predicate, path);
        if (resultSet != null && !resultSet.isEmpty()) {
            return resultSet.iterator().next();
        }
        return null;
    }
    @Override
    public Long updateByCriteria(Predicate predicate, EntityPath entityPath) {
        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, entityPath);
        updateClause = updateClause.where(predicate);        
        return updateClause.execute();
    }
    
    @Override
    public Long updateByCriteria(final JPAUpdateClause updateClause) {             
        return updateClause.execute();
    }
    
    @Override
    public JPAUpdateClause buildUpdateClause(EntityPath entityPath) {
        return new JPAUpdateClause(entityManager, entityPath);
    }

    @Override
    public Long removeByCriteria(Predicate predicate, EntityPath entityPath) {
        JPADeleteClause deleteClause = new JPADeleteClause(this.entityManager, entityPath);
        deleteClause = deleteClause.where(predicate);
        return deleteClause.execute();
    }
    
    @Override
    public DateTimePath<Date> getEntityPath(Field field, Object entity) throws IllegalAccessException, IllegalArgumentException {
        DateTimePath<Date> initialDatePath;
        initialDatePath = (DateTimePath<Date>) field.get(entity);
        return initialDatePath;
    }

    @Override
    public StringPath getStringEntityPath(Field field, Object entity) throws IllegalAccessException, IllegalArgumentException {
        StringPath path = (StringPath) field.get(entity);
        return path;
    }

    @Override
    public String property(Path path) {
        String temp = path.toString();
        int index = temp.indexOf(".");
        temp = temp.subSequence(index + 1, temp.length()).toString();
        return temp;
    }

    
    public SimpleQuery createQuery( Predicate predicate, EntityPath entityPath, Integer actualSize, Integer batchSize){        
        JPAQuery query = new JPAQuery(this.entityManager);
        query = query.from(entityPath).where(predicate);
        if (actualSize != null)
            query = query.offset(actualSize);        
        if (batchSize != null)
            query = query.limit(batchSize);        
        return query;
    }

    @Override
    public Long countByCriteria(Predicate predicate, EntityPath entityPath) {
        return ((JPAQuery) this.createQuery(predicate, entityPath, null, null)).count();
    }

    
    @Override
    public JPAQuery getAttachedQuery(){
        return new JPAQuery(this.entityManager);
    }
}
