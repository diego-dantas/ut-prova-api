package br.toledo.UTProva.model.dao.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import br.toledo.UTProva.model.RoleName;

@Entity
@Table(name = "roles")
public class RoleEntity {

    private Long id;
    private RoleName name;
    private List<UsuarioEntity> usuario;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    public List<UsuarioEntity> getUsuario() {
        return usuario;
    }

    public void setUsuario(List<UsuarioEntity> usuario) {
        this.usuario = usuario;
    }
}