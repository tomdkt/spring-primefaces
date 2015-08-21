/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.domain;

import br.com.repository.Persistentable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author thomas
 */
@Entity
@Table
@NoArgsConstructor
@EqualsAndHashCode

public class Filial implements Persistentable{
    
    @Id
    @Basic(optional = false)
    @Column(name = "COD_FILIAL", nullable = false, length = 20)
    @NotNull
    @Length(min = 1, max = 20, message="COD_FILIAL nao pode estar vazio e maior que 20 caracteres")
    private String codFilial;
    
    @Basic(optional = false)
    @Column(name = "CNPJ", nullable = false, length = 14)
    @NotNull
    @Length(min = 1, max = 14, message="CNPJ nao pode estar vazio e maior que 14 caracteres")
    private String cnpj;
    
    @Basic(optional = false)
    @Column(name = "RAZAO_SOCIAL", nullable = false, length = 60)
    @NotNull
    @Length(min = 1, max = 60, message="razaoSocial nao pode estar vazio e maior que 60 caracteres")
    private String razaoSocial;
    
    @JoinColumn(name = "COD_EMPRESA", referencedColumnName = "COD_EMPRESA")
    @ManyToOne(optional = true, fetch= FetchType.LAZY)
    private Empresa empresa;

    public String getCodFilial() {
        return codFilial;
    }

    public void setCodFilial(String codFilial) {
        this.codFilial = codFilial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public Object getPk() {
        return this.codFilial;
    }

    @Override
    public void setPk(Object pk) {
        this.codFilial = (String) pk;
    }
    
    
    
    
}
