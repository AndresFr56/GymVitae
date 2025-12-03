package gym.vitae.model.dtos.inventario;

import gym.vitae.model.enums.EstadoCliente;

public record InventarioListadoDTO (
        String codigo,
        String tipo,
        String nombre,
        String descripcion,
        String fecha_Adquision){}
