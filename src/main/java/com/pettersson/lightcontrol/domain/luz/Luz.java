package com.pettersson.lightcontrol.domain.luz;

import com.pettersson.lightcontrol.domain.producto.Producto;
import com.pettersson.lightcontrol.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@Entity(name = "luz")
@Table(name = "luces")
@EqualsAndHashCode(of = "id")
public class Luz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_luz")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_modulo", nullable = true)
    private Producto producto;

    @Column(name = "numero_luz")
    private int numeroLuz;

    @Column(name = "estado")
    private boolean estadoLuz;

    @Column(name = "nombre_luz")
    private String nombreLuz;

    //Constructors

    public Luz(){}

    public Luz(ActualizarNombreLuces actualizarNombreLuces){
        this.numeroLuz = actualizarNombreLuces.numeroLuz();
        this.nombreLuz = actualizarNombreLuces.nombreLuz();
    }

    //Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getNumeroLuz() {
        return numeroLuz;
    }

    public void setNumeroLuz(int numeroLuz) {
        this.numeroLuz = numeroLuz;
    }

    public boolean isEstadoLuz() {
        return estadoLuz;
    }

    public void setEstadoLuz(boolean estadoLuz) {
        this.estadoLuz = estadoLuz;
    }

    public String getNombreLuz() {
        return nombreLuz;
    }

    public void setNombreLuz(String nombreLuz) {
        this.nombreLuz = nombreLuz;
    }
}
