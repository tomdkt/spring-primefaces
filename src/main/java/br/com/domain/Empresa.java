/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.domain;

import br.com.repository.Persistentable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author thomas
 */
@Entity
@Table
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Empresa implements Persistentable{
    @Id
    @Basic(optional = false)
    @Column(name = "COD_EMPRESA", nullable = false, length = 20)
    @Length(min = 1, max = 20, message="COD_EMPRESA nao pode estar vazio e maior que 20 caracteres")
    private String codEmpresa;
    
    @Basic(optional = false)
    @Column(name = "CNPJ", nullable = false, length = 14)
    @Length(min = 1, max = 14, message="CNPJ nao pode estar vazio e maior que 14 caracteres")
    private String cnpj;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa", fetch = FetchType.LAZY)    
    private Collection<Filial> filiais;

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Collection<Filial> getFiliais() {
        return filiais;
    }

    public void setFiliais(Collection<Filial> filiais) {
        this.filiais = filiais;
    }

    @Override
    public Object getPk() {
        return this.codEmpresa;
    }

    @Override
    public void setPk(Object pk) {
        this.codEmpresa = (String)pk;
    }
}
