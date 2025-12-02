package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.MembresiaMapper;
import gym.vitae.model.Cliente;
import gym.vitae.model.DetallesFactura;
import gym.vitae.model.Empleado;
import gym.vitae.model.Factura;
import gym.vitae.model.Membresia;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaListadoDTO;
import gym.vitae.model.dtos.membresias.MembresiaUpdateDTO;
import gym.vitae.model.enums.EstadoFactura;
import gym.vitae.model.enums.TipoVenta;
import gym.vitae.repositories.ClienteRepository;
import gym.vitae.repositories.DetallesFacturaRepository; 
import gym.vitae.repositories.FacturaRepository;
import gym.vitae.repositories.MembresiaRepository;
import gym.vitae.repositories.TiposMembresiaRepository;
import java.util.List;
import java.time.LocalDate;
import java.util.Random;

public class MembresiasController extends BaseController {

    private final MembresiaRepository membresiaRepository;
    private final ClienteRepository clienteRepository;
    private final TiposMembresiaRepository tiposMembresiaRepository;
    private final FacturaRepository facturaRepository;
    private final DetallesFacturaRepository detallesFacturaRepository; 
    private final AuthController authController;

    public MembresiasController() {
        super();
        this.membresiaRepository = getRepository(MembresiaRepository.class);
        this.clienteRepository = getRepository(ClienteRepository.class);
        this.tiposMembresiaRepository = getRepository(TiposMembresiaRepository.class);
        this.facturaRepository = getRepository(FacturaRepository.class);
        this.detallesFacturaRepository = getRepository(DetallesFacturaRepository.class); 
        this.authController = new AuthController();
    }
    
/**
     * Constructor para pruebas.
     *
     * @param membresiaRepository Repositorio de Membresias.
     * @param clienteRepository Repositorio de Clientes.
     * @param tiposMembresiaRepository Repositorio de TiposMembresia.
     * @param facturaRepository Repositorio de Facturas.
     * @param detallesFacturaRepository Repositorio de DetallesFactura.
     * @param authController Controlador de Autenticación (para obtener empleado actual).
     */
    MembresiasController(
                        MembresiaRepository membresiaRepository,
                        ClienteRepository clienteRepository,
                        TiposMembresiaRepository tiposMembresiaRepository,
                        FacturaRepository facturaRepository,
                        DetallesFacturaRepository detallesFacturaRepository,
                        AuthController authController) { 
                    super(null);
                    this.membresiaRepository = membresiaRepository;
                    this.clienteRepository = clienteRepository;
                    this.tiposMembresiaRepository = tiposMembresiaRepository;
                    this.facturaRepository = facturaRepository;
                    this.detallesFacturaRepository = detallesFacturaRepository;
                    this.authController = authController;
                }
    


    public List<MembresiaListadoDTO> getMembresias() {
        return membresiaRepository.findAllListado();
    }

    public MembresiaDetalleDTO getMembresiaById(int id) {
        validateId(id);
        return membresiaRepository
                .findDetalleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Membresía no encontrada con ID: " + id));
    }


    
    public MembresiaDetalleDTO createMembresia(MembresiaCreateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la membresía no pueden ser nulos");
        }

        validateId(dto.getClienteId());
        validateId(dto.getTipoMembresiaId());
        
        if (dto.getFechaInicio() == null || dto.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        
        validateFechaSalida(dto.getFechaInicio(), dto.getFechaFin());

        if (dto.getPrecioPagado() == null || dto.getPrecioPagado().doubleValue() <= 0) {
            throw new IllegalArgumentException("El precio pagado debe ser mayor a 0");
        }


        Cliente cliente =
                clienteRepository
                        .findById(dto.getClienteId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("Cliente no encontrado con ID: " + dto.getClienteId()));

        TiposMembresia tipo =
                tiposMembresiaRepository
                        .findById(dto.getTipoMembresiaId())
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Tipo de membresía no encontrado con ID: " + dto.getTipoMembresiaId()));
        

        Empleado empleadoResponsable = authController.getEmpleadoActual();

        if (empleadoResponsable == null) {
            throw new IllegalStateException("No hay un empleado logueado para registrar la venta");
        }

        
        Factura factura = new Factura();
        String numeroFactura = "FAC-" + LocalDate.now().toString().replace("-", "") + new Random().nextInt(10000); 
        
        factura.setNumeroFactura(numeroFactura);
        factura.setCliente(cliente);
        factura.setEmpleadoResponsable(empleadoResponsable); 
        factura.setFechaEmision(LocalDate.now());
        factura.setTipoVenta(TipoVenta.MEMBRESIA);
        factura.setTotal(dto.getPrecioPagado());
        factura.setEstado(EstadoFactura.PAGADA);
        
        Factura savedFactura = facturaRepository.save(factura);
        Integer nuevaFacturaId = savedFactura.getId();
        

        DetallesFactura detalle = new DetallesFactura();
        detalle.setFactura(savedFactura);
        detalle.setTipoMembresia(tipo); 
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(dto.getPrecioPagado());
        detalle.setSubtotal(dto.getPrecioPagado());
        
        detallesFacturaRepository.save(detalle); 
        

        dto.setFacturaId(nuevaFacturaId); 

        Membresia membresia = MembresiaMapper.toEntity(dto, cliente, tipo, savedFactura); 
        Membresia saved = membresiaRepository.save(membresia);

        return membresiaRepository
                .findDetalleById(saved.getId())
                .orElseThrow(() -> new IllegalStateException("Error al recuperar la membresía creada"));
    }



    public MembresiaDetalleDTO updateMembresia(int id, MembresiaUpdateDTO dto) {
        validateId(id);

        if (dto == null) {
            throw new IllegalArgumentException("Los datos actualizados no pueden ser nulos");
        }

        Membresia membresia =
                membresiaRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException("Membresía no encontrada con ID: " + id));

        MembresiaMapper.updateEntity(membresia, dto);
        membresiaRepository.update(membresia);

        return membresiaRepository
                .findDetalleById(id)
                .orElseThrow(
                        () -> new IllegalStateException("Error al recuperar la membresía actualizada"));
    }



    public void cancelarMembresia(int id) {
        validateId(id);

        if (!membresiaRepository.existsById(id)) {
            throw new IllegalArgumentException("Membresía no encontrada con ID: " + id);
        }

        membresiaRepository.delete(id);
    }

    public List<MembresiaListadoDTO> getPaged(int page, int size) {
        var all = getMembresias();
        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        return all.subList(from, to);
    }

}