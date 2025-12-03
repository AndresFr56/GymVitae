package gym.vitae.mapper;

import gym.vitae.model.Categoria;
import gym.vitae.model.Producto;
import gym.vitae.model.Proveedore;
import gym.vitae.model.dtos.inventario.ProductoCreateDTO;
import gym.vitae.model.dtos.inventario.ProductoDetalleDTO;
import gym.vitae.model.dtos.inventario.ProductoListadoDTO;
import gym.vitae.model.dtos.inventario.ProductoUpdateDTO;
import java.util.List;

public class ProductoMapper {

    private ProductoMapper() {}

    public static ProductoListadoDTO toListadoDTO(Producto producto) {
        if (producto == null) return null;

        return new ProductoListadoDTO(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecioUnitario(),
                producto.getStock(),
                producto.getUnidadMedida(),
                producto.getActivo(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null,
                producto.getProveedor() != null ? producto.getProveedor().getNombre() : null,
                producto.getFechaIngreso()
        );
    }

    // Mapea a detalle
    public static ProductoDetalleDTO toDetalleDTO(Producto producto) {
        if (producto == null) return null;

        ProductoDetalleDTO dto = new ProductoDetalleDTO();
        dto.setId(producto.getId());
        dto.setCodigo(producto.getCodigo());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecioUnitario(producto.getPrecioUnitario());
        dto.setStock(producto.getStock());
        dto.setUnidadMedida(producto.getUnidadMedida());
        dto.setFechaIngreso(producto.getFechaIngreso());
        dto.setActivo(producto.getActivo());
        dto.setCreatedAt(producto.getCreatedAt());
        dto.setUpdatedAt(producto.getUpdatedAt());

        // Categoria info
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }

        // Proveedor info
        if (producto.getProveedor() != null) {
            dto.setProveedorId(producto.getProveedor().getId());
            dto.setProveedorNombre(producto.getProveedor().getNombre());
        }

        return dto;
    }

    // Crear entidad
    public static Producto toEntity(
            ProductoCreateDTO dto, Categoria categoria, Proveedore proveedor) {
        if (dto == null) return null;

        Producto producto = new Producto();
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        producto.setStock(dto.getStock() != null ? dto.getStock() : 0);
        producto.setUnidadMedida(dto.getUnidadMedida() != null ? dto.getUnidadMedida() : "unidad");
        producto.setFechaIngreso(dto.getFechaIngreso());

        return producto;
    }

    // Actualizar entidad
    public static void updateEntity(
            Producto producto, ProductoUpdateDTO dto, Categoria categoria, Proveedore proveedor) {
        if (producto == null || dto == null) return;

        if (categoria != null) producto.setCategoria(categoria);
        if (proveedor != null) producto.setProveedor(proveedor);
        if (dto.getCodigo() != null) producto.setCodigo(dto.getCodigo());
        if (dto.getNombre() != null) producto.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) producto.setDescripcion(dto.getDescripcion());
        if (dto.getPrecioUnitario() != null) producto.setPrecioUnitario(dto.getPrecioUnitario());
        if (dto.getStock() != null) producto.setStock(dto.getStock());
        if (dto.getUnidadMedida() != null) producto.setUnidadMedida(dto.getUnidadMedida());
        if (dto.getFechaIngreso() != null) producto.setFechaIngreso(dto.getFechaIngreso());
        if (dto.getActivo() != null) producto.setActivo(dto.getActivo());
    }

    public static List<ProductoListadoDTO> toListadoDTOList(List<Producto> productos) {
        if (productos == null) return List.of();
        return productos.stream().map(ProductoMapper::toListadoDTO).toList();
    }
}
