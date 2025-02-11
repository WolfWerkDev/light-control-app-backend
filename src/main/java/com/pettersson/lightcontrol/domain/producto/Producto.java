package com.pettersson.lightcontrol.domain.producto;

import com.pettersson.lightcontrol.domain.luz.Luz;
import com.pettersson.lightcontrol.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity(name = "producto")
@Table(name = "productos")
@EqualsAndHashCode(of = "id")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modulo")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = true)
    private Usuario usuario;

    @Column(name = "capacidad_modulo")
    private int capacidadModulo;

    @Column(name = "nombre_modulo")
    private String nombreModulo;

    @Column(name = "codigo_validacion", length = 9, unique = true, nullable = false)
    private String codigoValidacion;

    // Relación con las luces
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Luz> luces; // Relación con las luces del módulo

    //Constructors

    public Producto(){}

    public Producto(DatosRegistroProducto datosRegistroProducto){
        this.capacidadModulo = datosRegistroProducto.capacidad();
        this.codigoValidacion = datosRegistroProducto.codigoValidacion();
    }

    //Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getCapacidadModulo() {
        return capacidadModulo;
    }

    public void setCapacidadModulo(int capacidadModulo) {
        this.capacidadModulo = capacidadModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    public String getCodigoValidacion() {
        return codigoValidacion;
    }

    public void setCodigoValidacion(String codigoValidacion) {
        this.codigoValidacion = codigoValidacion;
    }

    public List<Luz> getLuces() {
        return luces;
    }

    public void setLuces(List<Luz> luces) {
        this.luces = luces;
    }
}
