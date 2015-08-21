/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.repository;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author thomas
 */
public class BaseCRUDLazyModelManagedBean<T, ID extends Serializable> implements Serializable{

    protected T subject;
    private LazyDataModel<T> subjects;
    private T currentRow;
    private Class<T> subjectClazz;
    
    
    @ManagedProperty(value="#{queryDslSupportJpaImpl}")
    protected IQueryDslSupport service;
                    
    public BaseCRUDLazyModelManagedBean() {
        
    }

    public BaseCRUDLazyModelManagedBean(T subject) {
        this.subject = subject;
        Class<T> clazz;
        try {
            clazz = (Class<T>) Class.forName(subject.getClass().getName());
            subjectClazz = clazz;
            currentRow = (T) clazz.getConstructor().newInstance(new Object[0]);            
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BaseCRUDLazyModelManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
//
//    public T findById(Class<T> clazz, String id) {
//        return (T) service.fetchOne(subject.getClass(), (ID) id);
//    }

    public void salvar() {
        try {
            if (this.isSubjectValid(subject)) {
                service.create(subject);
                addGlobalMessage("Dados salvos com sucesso");
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ocorrido durante a inclusao de novo registro", ex.getMessage()));
        }
    }

    public void alterar() {
        try {
            if (this.isSubjectValid(currentRow)) {
                service.update(currentRow);
                addGlobalMessage("Dados alterados com sucesso");
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ocorrido durante a alteracao", ex.getMessage()));
        }
    }
    
    public void updateWithSelectedRow() {
        try {            
            if (this.isSubjectValid(currentRow)) {
                service.update(currentRow);
                addGlobalMessage("Dados alterados com sucesso");
            }
        } catch (Exception ex) {
            addGlobalMessage("Houve um erro ao alterar os dados");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ocorrido durante a alteracao", ex.getMessage()));
        }
    }

    public void remover() {        
        try {
            ID id = ((ID) ((Persistentable) this.currentRow).getPk());
            service.deleteByID(currentRow.getClass(), id);
            addGlobalMessage("Dados excluidos com sucesso");

        } catch (Exception ex) {
            addGlobalMessage("Erro ocorrido durante a remocao : " + ex.getMessage());
        }
    }

    private boolean isSubjectValid(T objectToValidate) {
        boolean isValid = true;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate( objectToValidate );
        for (ConstraintViolation value : constraintViolations) {
            this.addGlobalMessage(value.getPropertyPath().toString(), value.getMessage(), FacesMessage.SEVERITY_ERROR);
            isValid = false;
        }
        return isValid;
    }
    
    
    public void addGlobalMessage(String mensagem) {
        System.out.println("Show messages!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "SISTEMA:", mensagem);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    public void addGlobalMessage(String summary, String mensagem, FacesMessage.Severity severity) {
        System.out.println("Show messages!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        FacesMessage facesMessage = new FacesMessage(severity, summary, mensagem);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    public T getSubject() {
        return subject;
    }

    public void setSubject(T subject) {
        this.subject = subject;
    }

    public LazyDataModel<T> getSubjects() {
        return subjects;
    }

    public void setSubjects(LazyDataModel<T> subjects) {
        this.subjects = subjects;
    }

    public T getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(T currentRow) {
        this.currentRow = currentRow;
    }

    public Class<T> getSubjectClazz() {
        return subjectClazz;
    }

    public void setSubjectClazz(Class<T> subjectClazz) {
        this.subjectClazz = subjectClazz;
    }

    public IQueryDslSupport getService() {
        return service;
    }

    public void setService(IQueryDslSupport service) {
        this.service = service;
    }
}