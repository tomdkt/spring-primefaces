/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.StringPath;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author thomas
 */
public interface IQueryDslSupport<T, ID> {
    public void create( T entity);
    public void update( T entity);
    public void deleteByID( Class<T> clazz, ID id);
    public Long countByCriteria(Predicate predicate, EntityPath entityPath);
    public <T> Collection<T> findByCriteria(Predicate predicate, EntityPath entityPath, Integer actualSize, Integer batchSize);
    public <T> Collection<T> findByCriteria(Predicate predicate, EntityPath entityPath);    
    public <T> Collection<T> findByDetachedCriteria( JPAQuery query, EntityPath entityPath);    
    public <T> Collection<T> findByDetachedCriteria( JPAQuery query, EntityPath entityPath, Integer actualSize, Integer batchSize );
    public <T> T findOneByCriteria(Predicate predicate, EntityPath path);                       
    public DateTimePath<Date> getEntityPath(Field field, Object entity) throws IllegalAccessException, IllegalArgumentException;
    public StringPath getStringEntityPath(Field field, Object entity) throws IllegalAccessException, IllegalArgumentException;
    public String property(Path path);
    public Long removeByCriteria(Predicate predicate, EntityPath entityPath);
    public Long updateByCriteria(Predicate predicate, EntityPath entityPath);
    public JPAQuery getAttachedQuery();
    
    public Long updateByCriteria(final JPAUpdateClause updateClause);        
    public JPAUpdateClause buildUpdateClause(EntityPath entityPath);
}
