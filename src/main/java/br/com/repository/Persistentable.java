/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.repository;

import java.io.Serializable;
import javax.persistence.Id;

/**
 *
 * @author thomas
 */
public interface Persistentable extends Serializable{
    @Id
    public Object getPk();
    
    public void setPk( Object pk );    
}
