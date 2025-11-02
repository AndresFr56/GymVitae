CREATE TABLE `departamentos` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` TEXT,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `cargos` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL COMMENT 'Entrenador, Recepcionista, Limpieza, Administración, Gerente',
  `salario_base` DECIMAL(10,2) NOT NULL,
  `descripcion` TEXT,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `empleados` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `departamento_id` INT NOT NULL,
  `cargo_id` INT NOT NULL,
  `codigo_empleado` VARCHAR(20) UNIQUE NOT NULL,
  `nombres` VARCHAR(100) NOT NULL,
  `apellidos` VARCHAR(100) NOT NULL,
  `cedula` VARCHAR(10) UNIQUE NOT NULL,
  `genero` ENUM ('Masculino', 'Femenino', 'Otro') NOT NULL,
  `telefono` VARCHAR(10) NOT NULL,
  `direccion` VARCHAR(100),
  `email` VARCHAR(100) UNIQUE,
  `fecha_ingreso` DATE NOT NULL,
  `fecha_salida` DATE,
  `tipo_contrato` ENUM ('Tiempo completo', 'Medio tiempo') NOT NULL,
  `estado` ENUM ('activo', 'inactivo', 'vacaciones', 'licencia') DEFAULT 'activo',
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `nominas` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `empleado_id` INT NOT NULL,
  `mes` TINYINT NOT NULL CHECK (mes BETWEEN 1 AND 12),
  `anio` YEAR NOT NULL,
  `salario_base` DECIMAL(10,2) NOT NULL,
  `bonificaciones` DECIMAL(10,2) DEFAULT 0,
  `deducciones` DECIMAL(10,2) DEFAULT 0,
  `horas_extra` DECIMAL(10,2) DEFAULT 0,
  `valor_hora_extra` DECIMAL(10,2) DEFAULT 0,
  `total_pagar` DECIMAL(10,2) NOT NULL,
  `fecha_pago` DATE,
  `estado` ENUM ('pendiente', 'aprobada', 'pagada', 'anulada') DEFAULT 'pendiente',
  `observaciones` TEXT,
  `generada_por` INT,
  `aprobada_por` INT,
  `pagada_por` INT,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `asistencias` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `empleado_id` INT NOT NULL,
  `fecha` DATE NOT NULL,
  `hora_entrada` TIME,
  `hora_salida` TIME,
  `horas_trabajadas` DECIMAL(5,2),
  `observaciones` TEXT,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `clientes` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `codigo_cliente` VARCHAR(20) UNIQUE NOT NULL,
  `nombres` VARCHAR(100) NOT NULL,
  `apellidos` VARCHAR(100) NOT NULL,
  `cedula` VARCHAR(10) UNIQUE NOT NULL,
  `genero` ENUM ('Masculino', 'Femenino', 'Otro'),
  `telefono` VARCHAR(10) NOT NULL,
  `direccion` VARCHAR(100),
  `email` VARCHAR(100),
  `fecha_nacimiento` DATE,
  `contacto_emergencia` VARCHAR(100),
  `telefono_emergencia` VARCHAR(10),
  `estado` ENUM ('activo', 'inactivo', 'suspendido') DEFAULT 'activo',
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `beneficios` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL COMMENT 'Clases grupales, Área de pesas, Spa, etc.',
  `descripcion` TEXT,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `tipos_membresia` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `descripcion` VARCHAR(255),
  `duracion_dias` INT NOT NULL,
  `costo` DECIMAL(10,2) NOT NULL,
  `acceso_completo` BOOLEAN DEFAULT true,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `membresia_beneficios` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `tipo_membresia_id` INT NOT NULL,
  `beneficio_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `membresias` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `cliente_id` INT NOT NULL,
  `tipo_membresia_id` INT NOT NULL,
  `fecha_inicio` DATE NOT NULL,
  `fecha_fin` DATE NOT NULL,
  `precio_pagado` DECIMAL(10,2) NOT NULL,
  `estado` ENUM ('activa', 'vencida', 'cancelada', 'suspendida') DEFAULT 'activa',
  `observaciones` TEXT,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `metodos_pago` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `descripcion` TEXT,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `facturas` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `numero_factura` VARCHAR(50) UNIQUE NOT NULL,
  `cliente_id` INT NOT NULL,
  `empleado_responsable_id` INT NOT NULL,
  `fecha_emision` DATE NOT NULL,
  `tipo_venta` ENUM ('Membresía', 'Servicio', 'Producto/Equipo') NOT NULL,
  `subtotal` DECIMAL(10,2) NOT NULL,
  `impuestos` DECIMAL(10,2) DEFAULT 0,
  `descuento` DECIMAL(10,2) DEFAULT 0,
  `total` DECIMAL(10,2) NOT NULL,
  `estado` ENUM ('pendiente', 'pagada', 'parcial', 'anulada') DEFAULT 'pendiente',
  `observaciones` TEXT,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `detalles_factura` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `factura_id` INT NOT NULL,
  `concepto` VARCHAR(200) NOT NULL,
  `cantidad` INT NOT NULL DEFAULT 1,
  `precio_unitario` DECIMAL(10,2) NOT NULL,
  `subtotal` DECIMAL(10,2) NOT NULL,
  `membresia_id` INT,
  `producto_id` INT,
  `equipo_id` INT,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `pagos` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `factura_id` INT NOT NULL,
  `metodo_pago_id` INT NOT NULL,
  `monto` DECIMAL(10,2) NOT NULL,
  `fecha_pago` DATE NOT NULL,
  `referencia` VARCHAR(100),
  `estado` ENUM ('completado', 'pendiente', 'rechazado') DEFAULT 'completado',
  `observaciones` TEXT,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `proveedores` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `codigo` VARCHAR(20) UNIQUE NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `contacto` VARCHAR(100),
  `telefono` VARCHAR(20),
  `email` VARCHAR(100),
  `direccion` TEXT,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `categorias_productos` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `tipo_item` ENUM ('Producto', 'Accesorio', 'Material de limpieza') NOT NULL,
  `descripcion` TEXT,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `productos` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `categoria_id` INT NOT NULL,
  `proveedor_id` INT,
  `codigo` VARCHAR(50) UNIQUE NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(255),
  `precio_venta` DECIMAL(10,2) NOT NULL,
  `precio_compra` DECIMAL(10,2) NOT NULL,
  `stock_actual` INT DEFAULT 0,
  `stock_minimo` INT DEFAULT 0,
  `unidad_medida` VARCHAR(20) DEFAULT 'unidad',
  `fecha_ingreso` DATE,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `categorias_equipos` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL COMMENT 'Cardio, Pesas, Funcional',
  `descripcion` TEXT,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `equipos` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `categoria_id` INT NOT NULL,
  `codigo` VARCHAR(50) UNIQUE NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(255),
  `marca` VARCHAR(50),
  `modelo` VARCHAR(50),
  `fecha_adquisicion` DATE NOT NULL,
  `costo` DECIMAL(10,2),
  `estado` ENUM ('operativo', 'mantenimiento', 'dañado', 'fuera_servicio') DEFAULT 'operativo',
  `ubicacion` VARCHAR(100),
  `observaciones` TEXT,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `movimientos_inventario` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `producto_id` INT NOT NULL,
  `tipo_movimiento` ENUM ('entrada', 'salida', 'ajuste', 'venta') NOT NULL,
  `cantidad` INT NOT NULL,
  `precio_unitario` DECIMAL(10,2),
  `fecha_movimiento` DATE NOT NULL,
  `motivo` VARCHAR(200),
  `usuario_id` INT,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `clases` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` TEXT,
  `duracion_minutos` INT NOT NULL,
  `capacidad_maxima` INT NOT NULL,
  `nivel` ENUM ('principiante', 'intermedio', 'avanzado', 'todos') DEFAULT 'todos',
  `activa` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `horarios` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `clase_id` INT NOT NULL,
  `instructor_id` INT NOT NULL,
  `dia_semana` ENUM ('lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo') NOT NULL,
  `hora_inicio` TIME NOT NULL,
  `hora_fin` TIME NOT NULL,
  `cupos_disponibles` INT NOT NULL,
  `activo` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `inscripciones_clases` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `horario_id` INT NOT NULL,
  `cliente_id` INT NOT NULL,
  `fecha_inscripcion` DATE NOT NULL,
  `estado` ENUM ('activa', 'completada', 'cancelada', 'ausente') DEFAULT 'activa',
  `created_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE INDEX `idx_nombre` ON `departamentos` (`nombre`);

CREATE INDEX `idx_activo` ON `departamentos` (`activo`);

CREATE INDEX `idx_nombre` ON `cargos` (`nombre`);

CREATE INDEX `idx_activo` ON `cargos` (`activo`);

CREATE INDEX `idx_codigo` ON `empleados` (`codigo_empleado`);

CREATE INDEX `idx_cedula` ON `empleados` (`cedula`);

CREATE INDEX `idx_estado` ON `empleados` (`estado`);

CREATE INDEX `idx_nombres` ON `empleados` (`nombres`, `apellidos`);

CREATE UNIQUE INDEX `unique_nomina` ON `nominas` (`empleado_id`, `mes`, `anio`);

CREATE INDEX `idx_fecha_pago` ON `nominas` (`fecha_pago`);

CREATE INDEX `idx_estado` ON `nominas` (`estado`);

CREATE INDEX `idx_mes_anio` ON `nominas` (`mes`, `anio`);

CREATE UNIQUE INDEX `unique_asistencia` ON `asistencias` (`empleado_id`, `fecha`);

CREATE INDEX `idx_fecha` ON `asistencias` (`fecha`);

CREATE INDEX `idx_codigo` ON `clientes` (`codigo_cliente`);

CREATE INDEX `idx_cedula` ON `clientes` (`cedula`);

CREATE INDEX `idx_nombres` ON `clientes` (`nombres`, `apellidos`);

CREATE INDEX `idx_estado` ON `clientes` (`estado`);

CREATE INDEX `idx_nombre` ON `beneficios` (`nombre`);

CREATE INDEX `idx_activo` ON `beneficios` (`activo`);

CREATE INDEX `idx_nombre` ON `tipos_membresia` (`nombre`);

CREATE INDEX `idx_activo` ON `tipos_membresia` (`activo`);

CREATE UNIQUE INDEX `unique_membresia_beneficio` ON `membresia_beneficios` (`tipo_membresia_id`, `beneficio_id`);

CREATE INDEX `idx_cliente` ON `membresias` (`cliente_id`);

CREATE INDEX `idx_estado` ON `membresias` (`estado`);

CREATE INDEX `idx_fechas` ON `membresias` (`fecha_inicio`, `fecha_fin`);

CREATE INDEX `idx_membresias_vigentes` ON `membresias` (`fecha_fin`, `estado`);

CREATE INDEX `idx_nombre` ON `metodos_pago` (`nombre`);

CREATE INDEX `idx_activo` ON `metodos_pago` (`activo`);

CREATE INDEX `idx_numero` ON `facturas` (`numero_factura`);

CREATE INDEX `idx_cliente` ON `facturas` (`cliente_id`);

CREATE INDEX `idx_fecha` ON `facturas` (`fecha_emision`);

CREATE INDEX `idx_estado` ON `facturas` (`estado`);

CREATE INDEX `idx_facturas_fecha_estado` ON `facturas` (`fecha_emision`, `estado`);

CREATE INDEX `idx_factura` ON `detalles_factura` (`factura_id`);

CREATE INDEX `idx_factura` ON `pagos` (`factura_id`);

CREATE INDEX `idx_fecha` ON `pagos` (`fecha_pago`);

CREATE INDEX `idx_estado` ON `pagos` (`estado`);

CREATE INDEX `idx_codigo` ON `proveedores` (`codigo`);

CREATE INDEX `idx_nombre` ON `proveedores` (`nombre`);

CREATE INDEX `idx_activo` ON `proveedores` (`activo`);

CREATE INDEX `idx_nombre` ON `categorias_productos` (`nombre`);

CREATE INDEX `idx_tipo` ON `categorias_productos` (`tipo_item`);

CREATE INDEX `idx_activo` ON `categorias_productos` (`activo`);

CREATE INDEX `idx_codigo` ON `productos` (`codigo`);

CREATE INDEX `idx_nombre` ON `productos` (`nombre`);

CREATE INDEX `idx_activo` ON `productos` (`activo`);

CREATE INDEX `idx_stock` ON `productos` (`stock_actual`);

CREATE INDEX `idx_productos_stock_critico` ON `productos` (`stock_actual`, `stock_minimo`);

CREATE INDEX `idx_nombre` ON `categorias_equipos` (`nombre`);

CREATE INDEX `idx_activo` ON `categorias_equipos` (`activo`);

CREATE INDEX `idx_codigo` ON `equipos` (`codigo`);

CREATE INDEX `idx_nombre` ON `equipos` (`nombre`);

CREATE INDEX `idx_estado` ON `equipos` (`estado`);

CREATE INDEX `idx_producto` ON `movimientos_inventario` (`producto_id`);

CREATE INDEX `idx_fecha` ON `movimientos_inventario` (`fecha_movimiento`);

CREATE INDEX `idx_tipo` ON `movimientos_inventario` (`tipo_movimiento`);

CREATE INDEX `idx_nombre` ON `clases` (`nombre`);

CREATE INDEX `idx_activa` ON `clases` (`activa`);

CREATE INDEX `idx_clase` ON `horarios` (`clase_id`);

CREATE INDEX `idx_instructor` ON `horarios` (`instructor_id`);

CREATE INDEX `idx_dia` ON `horarios` (`dia_semana`);

CREATE INDEX `idx_activo` ON `horarios` (`activo`);

CREATE INDEX `idx_horario` ON `inscripciones_clases` (`horario_id`);

CREATE INDEX `idx_cliente` ON `inscripciones_clases` (`cliente_id`);

CREATE INDEX `idx_fecha` ON `inscripciones_clases` (`fecha_inscripcion`);

CREATE INDEX `idx_estado` ON `inscripciones_clases` (`estado`);

ALTER TABLE `empleados` ADD FOREIGN KEY (`departamento_id`) REFERENCES `departamentos` (`id`);

ALTER TABLE `empleados` ADD FOREIGN KEY (`cargo_id`) REFERENCES `cargos` (`id`);

ALTER TABLE `nominas` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `nominas` ADD FOREIGN KEY (`generada_por`) REFERENCES `empleados` (`id`);

ALTER TABLE `nominas` ADD FOREIGN KEY (`aprobada_por`) REFERENCES `empleados` (`id`);

ALTER TABLE `nominas` ADD FOREIGN KEY (`pagada_por`) REFERENCES `empleados` (`id`);

ALTER TABLE `asistencias` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `membresia_beneficios` ADD FOREIGN KEY (`tipo_membresia_id`) REFERENCES `tipos_membresia` (`id`) ON DELETE CASCADE;

ALTER TABLE `membresia_beneficios` ADD FOREIGN KEY (`beneficio_id`) REFERENCES `beneficios` (`id`) ON DELETE CASCADE;

ALTER TABLE `membresias` ADD FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

ALTER TABLE `membresias` ADD FOREIGN KEY (`tipo_membresia_id`) REFERENCES `tipos_membresia` (`id`);

ALTER TABLE `facturas` ADD FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

ALTER TABLE `facturas` ADD FOREIGN KEY (`empleado_responsable_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `detalles_factura` ADD FOREIGN KEY (`factura_id`) REFERENCES `facturas` (`id`) ON DELETE CASCADE;

ALTER TABLE `pagos` ADD FOREIGN KEY (`factura_id`) REFERENCES `facturas` (`id`);

ALTER TABLE `pagos` ADD FOREIGN KEY (`metodo_pago_id`) REFERENCES `metodos_pago` (`id`);

ALTER TABLE `productos` ADD FOREIGN KEY (`categoria_id`) REFERENCES `categorias_productos` (`id`);

ALTER TABLE `productos` ADD FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`);

ALTER TABLE `equipos` ADD FOREIGN KEY (`categoria_id`) REFERENCES `categorias_equipos` (`id`);

ALTER TABLE `movimientos_inventario` ADD FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

ALTER TABLE `movimientos_inventario` ADD FOREIGN KEY (`usuario_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `horarios` ADD FOREIGN KEY (`clase_id`) REFERENCES `clases` (`id`);

ALTER TABLE `horarios` ADD FOREIGN KEY (`instructor_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `inscripciones_clases` ADD FOREIGN KEY (`horario_id`) REFERENCES `horarios` (`id`);

ALTER TABLE `inscripciones_clases` ADD FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

ALTER TABLE `detalles_factura` ADD FOREIGN KEY (`membresia_id`) REFERENCES `membresias` (`id`);

ALTER TABLE `detalles_factura` ADD FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

ALTER TABLE `detalles_factura` ADD FOREIGN KEY (`equipo_id`) REFERENCES `equipos` (`id`);

