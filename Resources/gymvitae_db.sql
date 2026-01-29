SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;

SET
    SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

START TRANSACTION;

SET
    time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS gym_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gym_system;

-- ============================================
-- CREATE TABLES
-- ============================================
DROP TABLE IF EXISTS `cargos`;

CREATE TABLE `cargos`
(
    `id`           INT PRIMARY KEY AUTO_INCREMENT,
    `nombre`       VARCHAR(100)   NOT NULL COMMENT 'Entrenador, Recepcionista, Limpieza, Administración, Gerente',
    `salario_base` DECIMAL(10, 2) NOT NULL,
    `descripcion`  TEXT,
    `activo`       BOOLEAN   DEFAULT true,
    `created_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `empleados`;

CREATE TABLE `empleados`
(
    `id`              INT PRIMARY KEY AUTO_INCREMENT,
    `cargo_id`        INT                                      NOT NULL,
    `codigo_empleado` VARCHAR(20) UNIQUE                       NOT NULL,
    `nombres`         VARCHAR(100)                             NOT NULL,
    `apellidos`       VARCHAR(100)                             NOT NULL,
    `cedula`          VARCHAR(10) UNIQUE                       NOT NULL,
    `genero`          ENUM ('MASCULINO', 'FEMENINO', 'OTRO')   NOT NULL,
    `telefono`        VARCHAR(10)                              NOT NULL,
    `direccion`       VARCHAR(100),
    `email`           VARCHAR(100) UNIQUE,
    `fecha_ingreso`   DATE                                     NOT NULL,
    `fecha_salida`    DATE,
    `tipo_contrato`   ENUM ('TIEMPO_COMPLETO', 'MEDIO_TIEMPO') NOT NULL,
    `estado`          ENUM ('ACTIVO', 'INACTIVO', 'VACACIONES') DEFAULT 'ACTIVO',
    `created_at`      TIMESTAMP                                 DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP                                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `nominas`;

CREATE TABLE `nominas`
(
    `id`             INT PRIMARY KEY AUTO_INCREMENT,
    `empleado_id`    INT            NOT NULL,
    `mes`            TINYINT        NOT NULL CHECK (
        mes BETWEEN 1
            AND 12
        ),
    `anio`           YEAR           NOT NULL,
    `salario_base`   DECIMAL(10, 2) NOT NULL,
    `bonificaciones` DECIMAL(10, 2)                                      DEFAULT 0,
    `deducciones`    DECIMAL(10, 2)                                      DEFAULT 0,
    `total_pagar`    DECIMAL(10, 2) NOT NULL,
    `fecha_pago`     DATE,
    `estado`         ENUM ('PENDIENTE', 'APROBADA', 'PAGADA', 'ANULADA') DEFAULT 'PENDIENTE',
    `observaciones`  TEXT,
    `generada_por`   INT,
    `aprobada_por`   INT,
    `pagada_por`     INT,
    `created_at`     TIMESTAMP                                           DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP                                           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `clientes`;

CREATE TABLE `clientes`
(
    `id`                  INT PRIMARY KEY AUTO_INCREMENT,
    `codigo_cliente`      VARCHAR(20) UNIQUE NOT NULL,
    `nombres`             VARCHAR(100)       NOT NULL,
    `apellidos`           VARCHAR(100)       NOT NULL,
    `cedula`              VARCHAR(10) UNIQUE NOT NULL,
    `genero`              ENUM ('MASCULINO', 'FEMENINO', 'OTRO'),
    `telefono`            VARCHAR(10)        NOT NULL,
    `direccion`           VARCHAR(100),
    `email`               VARCHAR(100),
    `fecha_nacimiento`    DATE,
    `contacto_emergencia` VARCHAR(100),
    `telefono_emergencia` VARCHAR(10),
    `estado`              ENUM ('ACTIVO', 'SUSPENDIDO') DEFAULT 'ACTIVO',
    `created_at`          TIMESTAMP                     DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          TIMESTAMP                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- Tabla: Beneficios
DROP TABLE IF EXISTS `beneficios`;

CREATE TABLE `beneficios`
(
    `id`          INT PRIMARY KEY AUTO_INCREMENT,
    `nombre`      VARCHAR(100) NOT NULL COMMENT 'Clases grupales, Área de pesas, Spa, etc.',
    `descripcion` TEXT,
    `activo`      BOOLEAN   DEFAULT true,
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- Tabla: Tipos de Membresía
DROP TABLE IF EXISTS `tipos_membresia`;

CREATE TABLE `tipos_membresia`
(
    `id`              INT PRIMARY KEY AUTO_INCREMENT,
    `nombre`          VARCHAR(50)    NOT NULL,
    `descripcion`     VARCHAR(255),
    `duracion_dias`   INT            NOT NULL,
    `costo`           DECIMAL(10, 2) NOT NULL,
    `acceso_completo` BOOLEAN   DEFAULT true,
    `activo`          BOOLEAN   DEFAULT true,
    `created_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- Tabla: Relación Membresía-Beneficios
DROP TABLE IF EXISTS `membresia_beneficios`;

CREATE TABLE `membresia_beneficios`
(
    `id`                INT PRIMARY KEY AUTO_INCREMENT,
    `tipo_membresia_id` INT NOT NULL,
    `beneficio_id`      INT NOT NULL,
    `created_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `membresias`;

CREATE TABLE `membresias`
(
    `id`                INT PRIMARY KEY AUTO_INCREMENT,
    `cliente_id`        INT            NOT NULL,
    `tipo_membresia_id` INT            NOT NULL,
    `factura_id`        INT            NOT NULL,
    `fecha_inicio`      DATE           NOT NULL,
    `fecha_fin`         DATE           NOT NULL,
    `precio_pagado`     DECIMAL(10, 2) NOT NULL,
    `estado`            ENUM ('ACTIVA', 'VENCIDA', 'CANCELADA', 'SUSPENDIDA') DEFAULT 'ACTIVA',
    `observaciones`     TEXT,
    `created_at`        TIMESTAMP                                             DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP                                             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;


DROP TABLE IF EXISTS `iva`;

CREATE TABLE `iva`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `porcentaje` DECIMAL(5, 2) NOT NULL COMMENT 'Ejemplo: 12.00 para 12%',
    `activo`     BOOLEAN   DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_activo (activo)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `facturas`;

CREATE TABLE `facturas`
(
    `id`                      INT PRIMARY KEY AUTO_INCREMENT,
    `numero_factura`          VARCHAR(50) UNIQUE                                NOT NULL,
    `cliente_id`              INT                                               NOT NULL,
    `empleado_responsable_id` INT                                               NOT NULL,
    `fecha_emision`           DATE                                              NOT NULL,
    `tipo_venta`              ENUM ('MEMBRESIA', 'SERVICIO', 'PRODUCTO_EQUIPO') NOT NULL,
    `total`                   DECIMAL(10, 2)                                    NOT NULL,
    `estado`                  ENUM ('PENDIENTE', 'PAGADA', 'ANULADA') DEFAULT 'PENDIENTE',
    `observaciones`           TEXT,
    `created_at`              TIMESTAMP                               DEFAULT CURRENT_TIMESTAMP,
    `updated_at`              TIMESTAMP                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- Tabla: Detalles de Factura
DROP TABLE IF EXISTS `detalles_factura`;

CREATE TABLE `detalles_factura`
(
    `id`                INT PRIMARY KEY AUTO_INCREMENT,
    `factura_id`        INT            NOT NULL,
    `cantidad`          INT            NOT NULL DEFAULT 1,
    `precio_unitario`   DECIMAL(10, 2) NOT NULL,
    `subtotal`          DECIMAL(10, 2) NOT NULL,
    `aplica_iva`        BOOLEAN                 DEFAULT FALSE,
    `iva_id`            INT,
    `tipo_membresia_id` INT,
    `producto_id`       INT,
    `equipo_id`         INT,
    `created_at`        TIMESTAMP               DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `pagos`;

CREATE TABLE `pagos`
(
    `id`            INT PRIMARY KEY AUTO_INCREMENT,
    `factura_id`    INT            NOT NULL,
    `monto`         DECIMAL(10, 2) NOT NULL,
    `fecha_pago`    DATE           NOT NULL,
    `referencia`    VARCHAR(100),
    `estado`        ENUM ('COMPLETADO', 'PENDIENTE', 'RECHAZADO', 'ANULADO') DEFAULT 'COMPLETADO',
    `observaciones` TEXT,
    `created_at`    TIMESTAMP                                                DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP                                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- Tabla: Proveedores
DROP TABLE IF EXISTS `proveedores`;

CREATE TABLE `proveedores`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `codigo`     VARCHAR(20) UNIQUE NOT NULL,
    `nombre`     VARCHAR(100)       NOT NULL,
    `contacto`   VARCHAR(100),
    `telefono`   VARCHAR(20),
    `email`      VARCHAR(100),
    `direccion`  TEXT,
    `activo`     BOOLEAN   DEFAULT true,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `categorias`;

CREATE TABLE `categorias`
(
    `id`          INT PRIMARY KEY AUTO_INCREMENT,
    `nombre`      VARCHAR(100)               NOT NULL,
    `tipo`        ENUM ('PRODUCTO','EQUIPO') NOT NULL COMMENT 'Discriminador: PRODUCTO o EQUIPO',
    `subtipo`     VARCHAR(50),
    CHECK (
        (tipo = 'PRODUCTO' AND subtipo IN ('PRODUCTO', 'ACCESORIO', 'MATERIAL_DE_LIMPIEZA'))
            OR
        (tipo = 'EQUIPO' AND subtipo IN ('CARDIO', 'PESAS', 'FUNCIONAL'))
        ),
    `descripcion` TEXT,
    `activo`      BOOLEAN   DEFAULT true,
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tipo (tipo),
    INDEX idx_subtipo (subtipo),
    INDEX idx_nombre (nombre),
    INDEX idx_activo (activo)
) ENGINE = InnoDB;

-- Tabla: Productos
DROP TABLE IF EXISTS `productos`;

CREATE TABLE `productos`
(
    `id`              INT PRIMARY KEY AUTO_INCREMENT,
    `categoria_id`    INT                NOT NULL,
    `proveedor_id`    INT,
    `codigo`          VARCHAR(50) UNIQUE NOT NULL,
    `nombre`          VARCHAR(100)       NOT NULL,
    `descripcion`     VARCHAR(255),
    `precio_unitario` DECIMAL(10, 2)     NOT NULL,
    `stock`           INT         DEFAULT 0,
    `unidad_medida`   VARCHAR(20) DEFAULT 'unidad',
    `fecha_ingreso`   DATE,
    `activo`          BOOLEAN     DEFAULT true,
    `created_at`      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `equipos`;

CREATE TABLE `equipos`
(
    `id`                INT PRIMARY KEY AUTO_INCREMENT,
    `categoria_id`      INT                NOT NULL,
    `codigo`            VARCHAR(50) UNIQUE NOT NULL,
    `nombre`            VARCHAR(100)       NOT NULL,
    `descripcion`       VARCHAR(255),
    `marca`             VARCHAR(50),
    `modelo`            VARCHAR(50),
    `fecha_adquisicion` DATE               NOT NULL,
    `costo`             DECIMAL(10, 2),
    `estado`            ENUM ('OPERATIVO', 'DANADO', 'FUERA_SERVICIO') DEFAULT 'OPERATIVO',
    `ubicacion`         VARCHAR(100),
    `observaciones`     TEXT,
    `created_at`        TIMESTAMP                                      DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `movimientos_inventario`;

CREATE TABLE `movimientos_inventario`
(
    `id`               INT PRIMARY KEY AUTO_INCREMENT,
    `producto_id`      INT                                           NOT NULL,
    `tipo_movimiento`  ENUM ('ENTRADA', 'SALIDA', 'AJUSTE', 'VENTA') NOT NULL,
    `cantidad`         INT                                           NOT NULL,
    `precio_unitario`  DECIMAL(10, 2),
    `fecha_movimiento` DATE                                          NOT NULL,
    `motivo`           VARCHAR(200),
    `usuario_id`       INT,
    `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `clases`;

CREATE TABLE `clases`
(
    `id`               INT PRIMARY KEY AUTO_INCREMENT,
    `nombre`           VARCHAR(100) NOT NULL,
    `descripcion`      TEXT,
    `duracion_minutos` INT          NOT NULL,
    `capacidad_maxima` INT          NOT NULL,
    `nivel`            ENUM (
        'PRINCIPIANTE',
        'INTERMEDIO',
        'AVANZADO',
        'TODOS'
        )                        DEFAULT 'TODOS',
    `activa`           BOOLEAN   DEFAULT true,
    `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `horarios`;

CREATE TABLE `horarios`
(
    `id`                INT PRIMARY KEY AUTO_INCREMENT,
    `clase_id`          INT  NOT NULL,
    `instructor_id`     INT  NOT NULL,
    `dia_semana`        ENUM (
        'LUNES',
        'MARTES',
        'MIERCOLES',
        'JUEVES',
        'VIERNES',
        'SABADO',
        'DOMINGO'
        )                    NOT NULL,
    `hora_inicio`       TIME NOT NULL,
    `hora_fin`          TIME NOT NULL,
    `cupos_disponibles` INT  NOT NULL,
    `activo`            BOOLEAN   DEFAULT true,
    `created_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `inscripciones_clases`;

CREATE TABLE `inscripciones_clases`
(
    `id`                INT PRIMARY KEY AUTO_INCREMENT,
    `horario_id`        INT  NOT NULL,
    `cliente_id`        INT  NOT NULL,
    `fecha_inscripcion` DATE NOT NULL,
    `estado`            ENUM ('ACTIVA', 'COMPLETADA', 'CANCELADA', 'AUSENTE') DEFAULT 'ACTIVA',
    `created_at`        TIMESTAMP                                             DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP                                             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- ============================================
-- ÍNDICES
-- ============================================
-- Índices Cargos
CREATE INDEX `idx_nombre` ON `cargos` (`nombre`);

CREATE INDEX `idx_activo` ON `cargos` (`activo`);

-- Índices Empleados
CREATE INDEX `idx_codigo` ON `empleados` (`codigo_empleado`);

CREATE INDEX `idx_cedula` ON `empleados` (`cedula`);

CREATE INDEX `idx_estado` ON `empleados` (`estado`);

CREATE INDEX `idx_nombres` ON `empleados` (`nombres`, `apellidos`);

-- Índices Nóminas
CREATE UNIQUE INDEX `unique_nomina` ON `nominas` (`empleado_id`, `mes`, `anio`);

CREATE INDEX `idx_fecha_pago` ON `nominas` (`fecha_pago`);

CREATE INDEX `idx_estado` ON `nominas` (`estado`);

CREATE INDEX `idx_mes_anio` ON `nominas` (`mes`, `anio`);

-- Índices Clientes
CREATE INDEX `idx_codigo` ON `clientes` (`codigo_cliente`);

CREATE INDEX `idx_cedula` ON `clientes` (`cedula`);

CREATE INDEX `idx_nombres` ON `clientes` (`nombres`, `apellidos`);

CREATE INDEX `idx_estado` ON `clientes` (`estado`);

-- Índices Beneficios
CREATE INDEX `idx_nombre` ON `beneficios` (`nombre`);

CREATE INDEX `idx_activo` ON `beneficios` (`activo`);

-- Índices Tipos Membresía
CREATE INDEX `idx_nombre` ON `tipos_membresia` (`nombre`);

CREATE INDEX `idx_activo` ON `tipos_membresia` (`activo`);

-- Índices Membresía-Beneficios
CREATE UNIQUE INDEX `unique_membresia_beneficio` ON `membresia_beneficios` (`tipo_membresia_id`, `beneficio_id`);

-- Índices Membresías
CREATE INDEX `idx_cliente` ON `membresias` (`cliente_id`);

CREATE INDEX `idx_estado` ON `membresias` (`estado`);

CREATE INDEX `idx_fechas` ON `membresias` (`fecha_inicio`, `fecha_fin`);

CREATE INDEX `idx_membresias_vigentes` ON `membresias` (`fecha_fin`, `estado`);

-- Índices Facturas
CREATE INDEX `idx_numero` ON `facturas` (`numero_factura`);

CREATE INDEX `idx_cliente` ON `facturas` (`cliente_id`);

CREATE INDEX `idx_fecha` ON `facturas` (`fecha_emision`);

CREATE INDEX `idx_estado` ON `facturas` (`estado`);

CREATE INDEX `idx_facturas_fecha_estado` ON `facturas` (`fecha_emision`, `estado`);

-- Índices Detalles Factura
CREATE INDEX `idx_factura` ON `detalles_factura` (`factura_id`);

-- Índices Pagos
CREATE INDEX `idx_factura` ON `pagos` (`factura_id`);

CREATE INDEX `idx_fecha` ON `pagos` (`fecha_pago`);

CREATE INDEX `idx_estado` ON `pagos` (`estado`);

-- Índices Proveedores
CREATE INDEX `idx_codigo` ON `proveedores` (`codigo`);

CREATE INDEX `idx_nombre` ON `proveedores` (`nombre`);

CREATE INDEX `idx_activo` ON `proveedores` (`activo`);

-- Índices Productos
CREATE INDEX `idx_codigo` ON `productos` (`codigo`);

CREATE INDEX `idx_nombre` ON `productos` (`nombre`);

CREATE INDEX `idx_activo` ON `productos` (`activo`);

CREATE INDEX `idx_stock` ON `productos` (`stock`);

CREATE INDEX `idx_productos_stock_critico` ON `productos` (`stock`);

-- Índices Equipos
CREATE INDEX `idx_codigo` ON `equipos` (`codigo`);

CREATE INDEX `idx_nombre` ON `equipos` (`nombre`);

CREATE INDEX `idx_estado` ON `equipos` (`estado`);

-- Índices Movimientos Inventario
CREATE INDEX `idx_producto` ON `movimientos_inventario` (`producto_id`);

CREATE INDEX `idx_fecha` ON `movimientos_inventario` (`fecha_movimiento`);

CREATE INDEX `idx_tipo` ON `movimientos_inventario` (`tipo_movimiento`);

-- Índices Clases
CREATE INDEX `idx_nombre` ON `clases` (`nombre`);

CREATE INDEX `idx_activa` ON `clases` (`activa`);

-- Índices Horarios
CREATE INDEX `idx_clase` ON `horarios` (`clase_id`);

CREATE INDEX `idx_instructor` ON `horarios` (`instructor_id`);

CREATE INDEX `idx_dia` ON `horarios` (`dia_semana`);

CREATE INDEX `idx_activo` ON `horarios` (`activo`);

-- Índices Inscripciones Clases
CREATE INDEX `idx_horario` ON `inscripciones_clases` (`horario_id`);

CREATE INDEX `idx_cliente` ON `inscripciones_clases` (`cliente_id`);

CREATE INDEX `idx_fecha` ON `inscripciones_clases` (`fecha_inscripcion`);

CREATE INDEX `idx_estado` ON `inscripciones_clases` (`estado`);

CREATE INDEX `idx_tipo_membresia` ON `detalles_factura` (`tipo_membresia_id`);

CREATE INDEX `idx_iva` ON `detalles_factura` (`iva_id`);

-- ============================================
-- RELACIONES (FOREIGN KEYS)
-- ============================================
ALTER TABLE
    `empleados`
    ADD
        FOREIGN KEY (`cargo_id`) REFERENCES `cargos` (`id`);

ALTER TABLE
    `nominas`
    ADD
        FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);

ALTER TABLE
    `nominas`
    ADD
        FOREIGN KEY (`generada_por`) REFERENCES `empleados` (`id`);

ALTER TABLE
    `nominas`
    ADD
        FOREIGN KEY (`aprobada_por`) REFERENCES `empleados` (`id`);

ALTER TABLE
    `nominas`
    ADD
        FOREIGN KEY (`pagada_por`) REFERENCES `empleados` (`id`);

ALTER TABLE
    `membresia_beneficios`
    ADD
        FOREIGN KEY (`tipo_membresia_id`) REFERENCES `tipos_membresia` (`id`) ON DELETE CASCADE;

ALTER TABLE
    `membresia_beneficios`
    ADD
        FOREIGN KEY (`beneficio_id`) REFERENCES `beneficios` (`id`) ON DELETE CASCADE;

ALTER TABLE
    `membresias`
    ADD
        FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

ALTER TABLE
    `membresias`
    ADD
        FOREIGN KEY (`tipo_membresia_id`) REFERENCES `tipos_membresia` (`id`);

ALTER TABLE
    `membresias`
    ADD
        FOREIGN KEY (factura_id) REFERENCES facturas (id);


ALTER TABLE
    `facturas`
    ADD
        FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

ALTER TABLE
    `facturas`
    ADD
        FOREIGN KEY (`empleado_responsable_id`) REFERENCES `empleados` (`id`);

ALTER TABLE
    `detalles_factura`
    ADD
        FOREIGN KEY (`factura_id`) REFERENCES `facturas` (`id`) ON DELETE CASCADE;

ALTER TABLE
    `detalles_factura`
    ADD
        FOREIGN KEY (`iva_id`) REFERENCES `iva` (`id`);

ALTER TABLE
    `detalles_factura`
    ADD
        FOREIGN KEY (`tipo_membresia_id`) REFERENCES `tipos_membresia` (`id`);

ALTER TABLE
    `detalles_factura`
    ADD
        FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

ALTER TABLE
    `detalles_factura`
    ADD
        FOREIGN KEY (`equipo_id`) REFERENCES `equipos` (`id`);

ALTER TABLE
    `pagos`
    ADD
        FOREIGN KEY (`factura_id`) REFERENCES `facturas` (`id`);

ALTER TABLE
    `productos`
    ADD
        FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);

ALTER TABLE
    `productos`
    ADD
        FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`);

ALTER TABLE
    `equipos`
    ADD
        FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);

ALTER TABLE
    `movimientos_inventario`
    ADD
        FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

ALTER TABLE
    `movimientos_inventario`
    ADD
        FOREIGN KEY (`usuario_id`) REFERENCES `empleados` (`id`);

ALTER TABLE
    `horarios`
    ADD
        FOREIGN KEY (`clase_id`) REFERENCES `clases` (`id`);

ALTER TABLE
    `horarios`
    ADD
        FOREIGN KEY (`instructor_id`) REFERENCES `empleados` (`id`);

ALTER TABLE
    `inscripciones_clases`
    ADD
        FOREIGN KEY (`horario_id`) REFERENCES `horarios` (`id`);

ALTER TABLE
    `inscripciones_clases`
    ADD
        FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);


-- ============================================
-- DATOS INICIALES O SEED
-- ============================================

-- Cargos
INSERT INTO cargos (nombre, salario_base, descripcion)
VALUES ('Gerente',
        2500.00,
        'Gerente general del gimnasio'),
       ('Administración',
        1200.00,
        'Personal administrativo'),
       ('Recepcionista',
        600.00,
        'Atención al cliente y ventas'),
       ('Entrenador',
        800.00,
        'Instructor de clases grupales'),
       ('Limpieza',
        500.00,
        'Personal de limpieza');

-- Beneficios
INSERT INTO beneficios (nombre, descripcion)
VALUES ('Clases grupales',
        'Acceso a todas las clases grupales'),
       ('Área de pesas',
        'Acceso al área de pesas y musculación'),
       ('Spa', 'Acceso al área de spa y relajación'),
       ('Sauna', 'Uso de sauna'),
       ('Nutricionista', 'Consultas con nutricionista'),
       ('Entrenador personal',
        '2 sesiones mensuales con entrenador'),
       ('Estacionamiento', 'Estacionamiento gratuito');


-- Empleados
INSERT INTO empleados (cargo_id, codigo_empleado, nombres, apellidos, cedula, genero, telefono, direccion, email,
                       fecha_ingreso, tipo_contrato, estado)
VALUES (1, 'EMP-2024000001', 'Carlos', 'Mendoza Silva', '0912345678', 'MASCULINO', '0987654321', 'Av. 9 de Octubre 123',
        'carlos.mendoza@gymvitae.com', '2023-01-15', 'TIEMPO_COMPLETO', 'ACTIVO'),
       (2, 'EMP-2024000002', 'María', 'González Pérez', '0923456789', 'FEMENINO', '0976543210', 'Cdla. Kennedy Mz 45',
        'maria.gonzalez@gymvitae.com', '2023-03-20', 'TIEMPO_COMPLETO', 'ACTIVO'),
       (3, 'EMP-2024000003', 'Ana', 'Torres Vega', '0945678901', 'FEMENINO', '0954321098', 'Samborondón Km 2.5',
        'ana.torres@gymvitae.com', '2023-08-01', 'TIEMPO_COMPLETO', 'ACTIVO'),
       (4, 'EMP-2024000004', 'José', 'Ramírez López', '0934567890', 'MASCULINO', '0965432109', 'Urdesa Norte 789',
        'jose.ramirez@gymvitae.com', '2023-06-10', 'MEDIO_TIEMPO', 'ACTIVO'),
       (5, 'EMP-2024000005', 'Luis', 'Paredes Castro', '0956789012', 'MASCULINO', '0943210987', 'Alborada 7ma Etapa',
        'luis.paredes@gymvitae.com', '2024-01-15', 'MEDIO_TIEMPO', 'ACTIVO');


-- Tipos de Membresía
INSERT INTO tipos_membresia (nombre,
                             descripcion,
                             duracion_dias,
                             costo,
                             acceso_completo)
VALUES ('Mensual Básica',
        'Acceso a gimnasio sin clases',
        30,
        35.00,
        TRUE),
       ('Mensual Premium',
        'Acceso completo con clases ilimitadas',
        30,
        50.00,
        TRUE),
       ('Trimestral',
        'Membresía de 3 meses con descuento',
        90,
        135.00,
        TRUE),
       ('Semestral',
        'Membresía de 6 meses',
        180,
        250.00,
        TRUE),
       ('Anual',
        'Membresía de 12 meses con máximo descuento',
        365,
        480.00,
        TRUE);

-- Relación Membresías-Beneficios
INSERT INTO membresia_beneficios (tipo_membresia_id, beneficio_id)
VALUES (1, 2),
       -- Básica: solo pesas
       (2, 1),
       (2, 2),
       (2, 4),
       -- Premium: clases, pesas, sauna
       (3, 1),
       (3, 2),
       (3, 4),
       (3, 5),
       -- Trimestral: + nutricionista
       (4, 1),
       (4, 2),
       (4, 3),
       (4, 4),
       (4, 5),
       (4, 7),
       -- Semestral: + spa, estacionamiento
       (5, 1),
       (5, 2),
       (5, 3),
       (5, 4),
       (5, 5),
       (5, 6),
       (5, 7);


-- Iva
INSERT INTO iva (porcentaje)
VALUES (16);

-- Clases
INSERT INTO clases (nombre,
                    descripcion,
                    duracion_minutos,
                    capacidad_maxima,
                    nivel)
VALUES ('Spinning',
        'Clase de ciclismo indoor de alta intensidad',
        45,
        20,
        'TODOS'),
       ('Yoga',
        'Clase de yoga para flexibilidad y relajación',
        60,
        15,
        'TODOS'),
       ('CrossFit',
        'Entrenamiento funcional de alta intensidad',
        60,
        12,
        'INTERMEDIO'),
       ('Pilates',
        'Fortalecimiento del core y postura',
        55,
        12,
        'TODOS'),
       ('Box', 'Entrenamiento de boxeo', 45, 15, 'TODOS'),
       ('Funcional',
        'Entrenamiento funcional para todo el cuerpo',
        50,
        18,
        'TODOS'),
       ('Aeróbicos',
        'Clase de ejercicios cardiovasculares',
        45,
        20,
        'PRINCIPIANTE');

INSERT INTO horarios (clase_id, instructor_id, dia_semana, hora_inicio, hora_fin, cupos_disponibles, activo)
VALUES (1, 4, 'LUNES', '06:00:00', '06:45:00', 20, TRUE),
       (1, 4, 'MIERCOLES', '06:00:00', '06:45:00', 20, TRUE),
       (1, 4, 'VIERNES', '06:00:00', '06:45:00', 20, TRUE),
       (2, 4, 'MARTES', '18:00:00', '19:00:00', 15, TRUE),
       (2, 4, 'JUEVES', '18:00:00', '19:00:00', 15, TRUE),
       (3, 4, 'LUNES', '19:00:00', '19:50:00', 25, TRUE),
       (3, 4, 'MIERCOLES', '19:00:00', '19:50:00', 25, TRUE);

-- Clientes

INSERT INTO clientes (codigo_cliente, nombres, apellidos, cedula, genero, telefono, direccion, email, fecha_nacimiento,
                      contacto_emergencia, telefono_emergencia, estado)
VALUES ('CLI-2024000001', 'Pedro', 'Sánchez Ruiz', '0967890123', 'MASCULINO', '0932109876', 'Cdla. Guayacanes Mz 10',
        'pedro.sanchez@email.com', '1990-05-15', 'Rosa Ruiz', '0921098765', 'ACTIVO'),
       ('CLI-2024000002', 'Laura', 'Martínez Flores', '0978901234', 'FEMENINO', '0910987654', 'Urdesa Central 456',
        'laura.martinez@email.com', '1985-08-22', 'Juan Martínez', '0909876543', 'ACTIVO'),
       ('CLI-2024000003', 'Roberto', 'Díaz Moreno', '0989012345', 'MASCULINO', '0998765432', 'Alborada 10ma Etapa',
        'roberto.diaz@email.com', '1992-11-30', 'Carmen Moreno', '0987654321', 'ACTIVO'),
       ('CLI-2024000004', 'Sofía', 'Castro Ortiz', '0990123456', 'FEMENINO', '0976543210', 'Samborondón Km 5',
        'sofia.castro@email.com', '1988-03-12', 'Miguel Castro', '0965432109', 'ACTIVO'),
       ('CLI-2024000005', 'Miguel', 'Herrera Vásquez', '0901234567', 'MASCULINO', '0954321098', 'Kennedy Norte Mz 88',
        'miguel.herrera@email.com', '1995-07-18', 'Elena Vásquez', '0943210987', 'ACTIVO');


-- Inscripciones a clases
INSERT INTO inscripciones_clases (horario_id, cliente_id, fecha_inscripcion, estado)
VALUES (1, 1, '2024-11-01', 'ACTIVA'),
       (1, 3, '2024-11-01', 'ACTIVA'),
       (4, 2, '2024-10-20', 'ACTIVA'),
       (4, 4, '2024-11-05', 'ACTIVA'),
       (6, 1, '2024-11-02', 'ACTIVA');

-- Proveedores
INSERT INTO proveedores (codigo, nombre, contacto, telefono, email, direccion, activo)
VALUES ('PROV-001', 'Suplementos Ecuador S.A.', 'Carlos López', '042345678', 'ventas@suplementosec.com',
        'Av. Francisco de Orellana Km 2', TRUE),
       ('PROV-002', 'Equipos Fitness Pro', 'Ana Mora', '042456789', 'info@fitnesspro.com',
        'Cdla. Urdesa Circunvalación Sur 123', TRUE),
       ('PROV-003', 'Distribuidora Deportiva', 'Juan Pérez', '042567890', 'ventas@distdeportiva.com',
        'Av. Carlos Julio Arosemena Km 1.5', TRUE),
       ('PROV-004', 'Nutrición y Vida', 'María Torres', '042678901', 'contacto@nutrivida.com', 'Mall del Sol Local 45',
        TRUE),
       ('PROV-005', 'Maquinarias Gym Import', 'Roberto Silva', '042789012', 'importaciones@gymimport.com',
        'Puerto Marítimo Zona Industrial', TRUE);

-- Facturas para membresías (insertar ANTES de las membresías)
INSERT INTO facturas (numero_factura, cliente_id, empleado_responsable_id, fecha_emision, tipo_venta, total, estado)
VALUES
    ('FAC-202511000001', 1, 3, '2025-11-01', 'MEMBRESIA', 50.00, 'PAGADA'),
    ('FAC-202510000002', 2, 3, '2025-10-15', 'MEMBRESIA', 135.00, 'PAGADA'),
    ('FAC-202509000003', 3, 3, '2025-09-01', 'MEMBRESIA', 480.00, 'PAGADA'),
    ('FAC-202511000004', 4, 3, '2025-11-05', 'MEMBRESIA', 50.00, 'PAGADA'),
    ('FAC-202511000005', 5, 3, '2025-11-10', 'MEMBRESIA', 35.00, 'PAGADA');

-- Membresías activas (con factura_id)
INSERT INTO membresias (cliente_id, tipo_membresia_id, factura_id, fecha_inicio, fecha_fin, precio_pagado, estado)
VALUES (1, 2, 1, '2025-11-01', '2025-11-30', 50.00, 'ACTIVA'),
       (2, 3, 2, '2025-10-15', '2026-01-15', 135.00, 'ACTIVA'),
       (3, 5, 3, '2025-09-01', '2026-08-31', 480.00, 'ACTIVA'),
       (4, 2, 4, '2025-11-05', '2025-12-05', 50.00, 'ACTIVA'),
       (5, 1, 5, '2025-11-10', '2025-12-10', 35.00, 'ACTIVA');


-- ============================================
-- VISTAS
-- ============================================
-- Vista: Empleados Activos
CREATE OR REPLACE VIEW v_empleados_activos AS
SELECT e.id,
       e.codigo_empleado,
       CONCAT(e.nombres, ' ', e.apellidos) AS nombre_completo,
       e.cedula,
       e.genero,
       e.telefono,
       e.direccion,
       e.email,
       c.nombre                            AS cargo,
       e.tipo_contrato,
       c.salario_base,
       e.fecha_ingreso,
       e.estado
FROM empleados e
         INNER JOIN cargos c ON e.cargo_id = c.id
WHERE e.estado = 'ACTIVO';

-- Vista: Clientes con Membresías Activas
CREATE OR REPLACE VIEW v_clientes_membresias_activas AS
SELECT c.id                                AS cliente_id,
       c.codigo_cliente,
       CONCAT(c.nombres, ' ', c.apellidos) AS nombre_completo,
       c.cedula,
       c.telefono,
       c.email,
       m.id                                AS membresia_id,
       tm.nombre                           AS tipo_membresia,
       m.fecha_inicio,
       m.fecha_fin,
       DATEDIFF(m.fecha_fin, CURDATE())    AS dias_restantes,
       m.precio_pagado,
       m.estado
FROM clientes c
         INNER JOIN membresias m ON c.id = m.cliente_id
         INNER JOIN tipos_membresia tm ON m.tipo_membresia_id = tm.id
WHERE m.estado = 'ACTIVA'
ORDER BY m.fecha_fin ASC;

-- Vista: Membresías por Vencer (próximos 7 días)
CREATE OR REPLACE VIEW v_membresias_por_vencer AS
SELECT c.id                                AS cliente_id,
       CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
       c.telefono,
       c.email,
       tm.nombre                           AS tipo_membresia,
       m.fecha_fin,
       DATEDIFF(m.fecha_fin, CURDATE())    AS dias_restantes
FROM clientes c
         INNER JOIN membresias m ON c.id = m.cliente_id
         INNER JOIN tipos_membresia tm ON m.tipo_membresia_id = tm.id
WHERE m.estado = 'ACTIVA'
  AND m.fecha_fin BETWEEN CURDATE()
    AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
ORDER BY m.fecha_fin ASC;

-- Vista: Inventario con Estado de Stock
CREATE OR REPLACE VIEW v_inventario_estado AS
SELECT p.id,
       p.codigo,
       p.nombre,
       cp.nombre                     AS categoria,
       p.stock                       AS stock_actual,
       p.precio_unitario,
       CASE
           WHEN p.stock = 0 THEN 'Sin Stock'
           WHEN p.stock <= 10 THEN 'Crítico'
           WHEN p.stock <= 30 THEN 'Bajo'
           ELSE 'Normal'
           END                       AS estado_stock,
       (p.stock * p.precio_unitario) AS valor_inventario
FROM productos p
         INNER JOIN categorias cp ON p.categoria_id = cp.id
WHERE p.activo = TRUE
ORDER BY CASE
             WHEN p.stock = 0 THEN 1
             WHEN p.stock <= 10 THEN 2
             WHEN p.stock <= 30 THEN 3
             ELSE 4
             END;

-- Vista: Facturas Pendientes de Pago
CREATE OR REPLACE VIEW v_facturas_pendientes AS
SELECT f.id,
       f.numero_factura,
       CONCAT(c.nombres, ' ', c.apellidos)   AS cliente,
       c.telefono,
       c.email,
       f.fecha_emision,
       f.tipo_venta,
       f.total,
       f.estado,
       COALESCE(SUM(p.monto), 0)             AS pagado,
       (f.total - COALESCE(SUM(p.monto), 0)) AS saldo_pendiente,
       DATEDIFF(CURDATE(), f.fecha_emision)  AS dias_transcurridos
FROM facturas f
         INNER JOIN clientes c ON f.cliente_id = c.id
         LEFT JOIN pagos p ON f.id = p.factura_id
    AND p.estado = 'COMPLETADO'
WHERE f.estado = 'PENDIENTE'
GROUP BY f.id,
         f.numero_factura,
         cliente,
         c.telefono,
         c.email,
         f.fecha_emision,
         f.tipo_venta,
         f.total,
         f.estado
ORDER BY dias_transcurridos DESC;

-- Vista: Nóminas Pendientes
CREATE OR REPLACE VIEW v_nominas_pendientes AS
SELECT n.id,
       CONCAT(e.nombres, ' ', e.apellidos) AS empleado,
       c.nombre                            AS cargo,
       n.mes,
       n.anio,
       n.salario_base,
       n.bonificaciones,
       n.deducciones,
       n.total_pagar,
       n.estado,
       n.created_at                        AS fecha_generacion
FROM nominas n
         INNER JOIN empleados e ON n.empleado_id = e.id
         INNER JOIN cargos c ON e.cargo_id = c.id
WHERE n.estado IN ('PENDIENTE', 'APROBADA')
ORDER BY n.anio DESC,
         n.mes DESC,
         e.apellidos;

-- Vista: Ventas Mensuales
CREATE OR REPLACE VIEW v_ventas_mensuales AS
SELECT YEAR(f.fecha_emision)  AS anio,
       MONTH(f.fecha_emision) AS mes,
       f.tipo_venta,
       COUNT(*)               AS total_facturas,
       SUM(f.total)           AS total_vendido,
       SUM(
               CASE
                   WHEN f.estado = 'PAGADA' THEN f.total
                   ELSE 0
                   END
       )                      AS total_cobrado,
       SUM(
               CASE
                   WHEN f.estado = 'PENDIENTE' THEN f.total
                   ELSE 0
                   END
       )                      AS total_pendiente
FROM facturas f
WHERE f.estado != 'ANULADA'
GROUP BY YEAR(f.fecha_emision),
         MONTH(f.fecha_emision),
         f.tipo_venta
ORDER BY anio DESC,
         mes DESC,
         f.tipo_venta;

-- Vista: Equipos por Estado
CREATE OR REPLACE VIEW v_equipos_estado AS
SELECT e.id,
       e.codigo,
       e.nombre,
       ce.nombre                                           AS categoria,
       e.marca,
       e.modelo,
       e.estado,
       e.fecha_adquisicion,
       TIMESTAMPDIFF(YEAR, e.fecha_adquisicion, CURDATE()) AS anios_uso,
       e.ubicacion,
       e.costo
FROM equipos e
         INNER JOIN categorias ce ON e.categoria_id = ce.id
ORDER BY e.estado,
         e.nombre;

-- Vista: Clases Populares
CREATE OR REPLACE VIEW v_clases_populares AS
SELECT cl.id,
       cl.nombre            AS clase,
       cl.nivel,
       COUNT(ic.id)         AS total_inscripciones,
       COUNT(DISTINCT h.id) AS horarios_disponibles,
       cl.capacidad_maxima
FROM clases cl
         LEFT JOIN horarios h ON cl.id = h.clase_id
    AND h.activo = TRUE
         LEFT JOIN inscripciones_clases ic ON h.id = ic.horario_id
    AND ic.estado = 'ACTIVA'
WHERE cl.activa = TRUE
GROUP BY cl.id,
         cl.nombre,
         cl.nivel,
         cl.capacidad_maxima
ORDER BY total_inscripciones DESC;

-- Vista: Horarios de la Semana
CREATE OR REPLACE VIEW v_horarios_semana AS
SELECT h.id,
       h.dia_semana,
       h.hora_inicio,
       h.hora_fin,
       cl.nombre                                   AS clase,
       CONCAT(e.nombres, ' ', e.apellidos)         AS instructor,
       cl.capacidad_maxima,
       h.cupos_disponibles,
       (cl.capacidad_maxima - h.cupos_disponibles) AS inscritos
FROM horarios h
         INNER JOIN clases cl ON h.clase_id = cl.id
         INNER JOIN empleados e ON h.instructor_id = e.id
WHERE h.activo = TRUE
ORDER BY FIELD(
                 h.dia_semana,
                 'LUNES',
                 'MARTES',
                 'MIERCOLES',
                 'JUEVES',
                 'VIERNES',
                 'SABADO',
                 'DOMINGO'
         ),
         h.hora_inicio;

-- =======================================================================================
-- PROCEDIMIENTOS ALMACENADOS
-- =======================================================================================


-- SP: Crear Cliente

DELIMITER //
CREATE PROCEDURE sp_crear_cliente(
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_cedula VARCHAR(10),
    IN p_genero ENUM ('MASCULINO', 'FEMENINO', 'OTRO'),
    IN p_telefono VARCHAR(10),
    IN p_direccion VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_fecha_nacimiento DATE,
    IN p_contacto_emergencia VARCHAR(100),
    IN p_telefono_emergencia VARCHAR(10)
)
BEGIN
    DECLARE v_codigo VARCHAR(20);

-- Generar código único
    SET
        v_codigo = CONCAT(
                'CLI-',
                YEAR(CURDATE()),
                LPAD(
                        (SELECT COALESCE(MAX(id), 0) + 1
                         FROM clientes),
                        6,
                        '0'
                )
                   );

    INSERT INTO clientes (codigo_cliente,
                          nombres,
                          apellidos,
                          cedula,
                          genero,
                          telefono,
                          direccion,
                          email,
                          fecha_nacimiento,
                          contacto_emergencia,
                          telefono_emergencia)
    VALUES (v_codigo,
            p_nombres,
            p_apellidos,
            p_cedula,
            p_genero,
            p_telefono,
            p_direccion,
            p_email,
            p_fecha_nacimiento,
            p_contacto_emergencia,
            p_telefono_emergencia);

    SELECT LAST_INSERT_ID()                   AS cliente_id,
           v_codigo                           AS codigo_cliente,
           'Cliente registrado correctamente' AS mensaje;

END //

-- SP: Crear Membresía con Factura
CREATE PROCEDURE sp_crear_membresia_con_factura(
    IN p_cliente_id INT,
    IN p_tipo_membresia_id INT,
    IN p_empleado_id INT,
    OUT p_membresia_id INT,
    OUT p_factura_id INT
)
BEGIN
    DECLARE v_precio DECIMAL(10, 2);

    DECLARE v_duracion INT;

    DECLARE v_fecha_fin DATE;

    DECLARE v_numero_factura VARCHAR(50);

    DECLARE v_tipo_nombre VARCHAR(100);

-- Obtener datos del tipo de membresía
    SELECT costo,
           duracion_dias,
           nombre
    INTO v_precio,
        v_duracion,
        v_tipo_nombre
    FROM tipos_membresia
    WHERE id = p_tipo_membresia_id
      AND activo = TRUE;

-- Calcular fecha fin
    SET
        v_fecha_fin = DATE_ADD(CURDATE(), INTERVAL v_duracion DAY);

-- Insertar membresía
    INSERT INTO membresias (cliente_id,
                            tipo_membresia_id,
                            fecha_inicio,
                            fecha_fin,
                            precio_pagado)
    VALUES (p_cliente_id,
            p_tipo_membresia_id,
            CURDATE(),
            v_fecha_fin,
            v_precio);

    SET
        p_membresia_id = LAST_INSERT_ID();

-- Generar número de factura
    SET
        v_numero_factura = CONCAT(
                'FAC-',
                YEAR(CURDATE()),
                LPAD(MONTH(CURDATE()), 2, '0'),
                LPAD(
                        (SELECT COALESCE(MAX(id), 0) + 1
                         FROM facturas),
                        6,
                        '0'
                )
                           );

-- Crear factura
    INSERT INTO facturas (numero_factura,
                          cliente_id,
                          empleado_responsable_id,
                          fecha_emision,
                          tipo_venta,
                          total,
                          estado)
    VALUES (v_numero_factura,
            p_cliente_id,
            p_empleado_id,
            CURDATE(),
            'MEMBRESIA',
            v_precio,
            'PENDIENTE');

    SET
        p_factura_id = LAST_INSERT_ID();

-- Insertar detalle
    INSERT INTO detalles_factura (factura_id,
                                  cantidad,
                                  precio_unitario,
                                  subtotal,
                                  tipo_membresia_id)
    VALUES (p_factura_id,
            1,
            v_precio,
            v_precio,
            p_membresia_id);

    SELECT p_membresia_id                              AS membresia_id,
           p_factura_id                                AS factura_id,
           v_numero_factura                            AS numero_factura,
           'Membresía y factura creadas correctamente' AS mensaje;

END //

-- SP: Registrar Pago
CREATE PROCEDURE sp_registrar_pago(
    IN p_factura_id INT,
    IN p_monto DECIMAL(10, 2),
    IN p_referencia VARCHAR(100)
)
BEGIN
    DECLARE v_total DECIMAL(10, 2);

    DECLARE v_pagado DECIMAL(10, 2);

    DECLARE v_nuevo_estado VARCHAR(20);

-- Obtener total de la factura
    SELECT total
    INTO v_total
    FROM facturas
    WHERE id = p_factura_id;

-- Calcular total pagado
    SELECT COALESCE(SUM(monto), 0)
    INTO v_pagado
    FROM pagos
    WHERE factura_id = p_factura_id
      AND estado = 'COMPLETADO';

-- Agregar el nuevo pago
    SET
        v_pagado = v_pagado + p_monto;

-- Determinar estado
    IF v_pagado >= v_total THEN
        SET
            v_nuevo_estado = 'PAGADA';

    ELSE
        SET
            v_nuevo_estado = 'PENDIENTE';

    END IF;

-- Insertar pago
    INSERT INTO pagos (factura_id,
                       monto,
                       fecha_pago,
                       referencia)
    VALUES (p_factura_id,
            p_monto,
            CURDATE(),
            p_referencia);

-- Actualizar estado de factura
    UPDATE
        facturas
    SET estado = v_nuevo_estado
    WHERE id = p_factura_id;

    SELECT 'Pago registrado correctamente' AS mensaje,
           v_nuevo_estado                  AS estado_factura,
           (v_total - v_pagado)            AS saldo_pendiente;

END //

-- SP: Renovar Membresía
CREATE PROCEDURE sp_renovar_membresia(
    IN p_membresia_id INT,
    IN p_empleado_id INT,
    OUT p_nueva_membresia_id INT,
    OUT p_factura_id INT
)
BEGIN
    DECLARE v_cliente_id INT;

    DECLARE v_tipo_membresia_id INT;

-- Obtener datos de la membresía actual
    SELECT cliente_id,
           tipo_membresia_id
    INTO v_cliente_id,
        v_tipo_membresia_id
    FROM membresias
    WHERE id = p_membresia_id;

-- Marcar membresía anterior como vencida
    UPDATE
        membresias
    SET estado = 'VENCIDA'
    WHERE id = p_membresia_id;

-- Crear nueva membresía con factura
    CALL sp_crear_membresia_con_factura(
            v_cliente_id,
            v_tipo_membresia_id,
            p_empleado_id,
            p_nueva_membresia_id,
            p_factura_id
         );

END //

-- SP: Vender Producto
CREATE PROCEDURE sp_vender_producto(
    IN p_cliente_id INT,
    IN p_producto_id INT,
    IN p_cantidad INT,
    IN p_empleado_id INT,
    OUT p_factura_id INT
)
BEGIN
    DECLARE v_stock INT;

    DECLARE v_precio DECIMAL(10, 2);

    DECLARE v_nombre VARCHAR(200);

    DECLARE v_subtotal DECIMAL(10, 2);

    DECLARE v_numero_factura VARCHAR(50);

-- Verificar stock
    SELECT stock,
           precio_unitario,
           nombre
    INTO v_stock,
        v_precio,
        v_nombre
    FROM productos
    WHERE id = p_producto_id
      AND activo = TRUE;

    IF v_stock < p_cantidad THEN
        SIGNAL SQLSTATE '45000'
            SET
                MESSAGE_TEXT = 'Stock insuficiente';

    END IF;

    SET
        v_subtotal = v_precio * p_cantidad;

-- Generar número de factura
    SET
        v_numero_factura = CONCAT(
                'FAC-',
                YEAR(CURDATE()),
                LPAD(MONTH(CURDATE()), 2, '0'),
                LPAD(
                        (SELECT COALESCE(MAX(id), 0) + 1
                         FROM facturas),
                        6,
                        '0'
                )
                           );

-- Crear factura
    INSERT INTO facturas (numero_factura,
                          cliente_id,
                          empleado_responsable_id,
                          fecha_emision,
                          tipo_venta,
                          total,
                          estado)
    VALUES (v_numero_factura,
            p_cliente_id,
            p_empleado_id,
            CURDATE(),
            'PRODUCTO_EQUIPO',
            v_subtotal,
            'PENDIENTE');

    SET
        p_factura_id = LAST_INSERT_ID();

-- Insertar detalle
    INSERT INTO detalles_factura (factura_id,
                                  cantidad,
                                  precio_unitario,
                                  subtotal,
                                  producto_id)
    VALUES (p_factura_id,
            p_cantidad,
            v_precio,
            v_subtotal,
            p_producto_id);

-- Registrar movimiento de inventario
    INSERT INTO movimientos_inventario (producto_id,
                                        tipo_movimiento,
                                        cantidad,
                                        precio_unitario,
                                        fecha_movimiento,
                                        motivo,
                                        usuario_id)
    VALUES (p_producto_id,
            'VENTA',
            p_cantidad,
            v_precio,
            CURDATE(),
            CONCAT('Venta en factura ', v_numero_factura),
            p_empleado_id);

    SELECT p_factura_id                     AS factura_id,
           v_numero_factura                 AS numero_factura,
           'Venta registrada correctamente' AS mensaje;

END //

-- SP: Inscribir Cliente a Clase
CREATE PROCEDURE sp_inscribir_clase(
    IN p_horario_id INT,
    IN p_cliente_id INT
)
BEGIN
    DECLARE v_cupos INT;

    DECLARE v_tiene_membresia INT;

-- Verificar que el cliente tenga membresía activa
    SELECT COUNT(*)
    INTO v_tiene_membresia
    FROM membresias
    WHERE cliente_id = p_cliente_id
      AND estado = 'ACTIVA'
      AND fecha_fin >= CURDATE();

    IF v_tiene_membresia = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET
                MESSAGE_TEXT = 'El cliente no tiene membresía activa';

    END IF;

-- Verificar cupos disponibles
    SELECT cupos_disponibles
    INTO v_cupos
    FROM horarios
    WHERE id = p_horario_id
      AND activo = TRUE;

    IF v_cupos <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET
                MESSAGE_TEXT = 'No hay cupos disponibles';

    END IF;

-- Verificar que no esté ya inscrito
    IF EXISTS (SELECT 1
               FROM inscripciones_clases
               WHERE horario_id = p_horario_id
                 AND cliente_id = p_cliente_id
                 AND estado = 'ACTIVA') THEN
        SIGNAL SQLSTATE '45000'
            SET
                MESSAGE_TEXT = 'El cliente ya está inscrito en este horario';

    END IF;

-- Inscribir
    INSERT INTO inscripciones_clases (horario_id,
                                      cliente_id,
                                      fecha_inscripcion)
    VALUES (p_horario_id,
            p_cliente_id,
            CURDATE());

    SELECT 'Inscripción exitosa' AS mensaje;

END //

-- SP: Generar Nóminas del Mes
CREATE PROCEDURE sp_generar_nominas_mes(
    IN p_mes INT,
    IN p_anio INT,
    IN p_generada_por INT
)
BEGIN
    DECLARE done INT DEFAULT FALSE;

    DECLARE v_empleado_id INT;

    DECLARE v_salario_base DECIMAL(10, 2);

    DECLARE v_total DECIMAL(10, 2);

    DECLARE cur_empleados CURSOR FOR
        SELECT e.id,
               c.salario_base
        FROM empleados e
                 INNER JOIN cargos c ON e.cargo_id = c.id
        WHERE e.estado = 'ACTIVO'
          AND NOT EXISTS (SELECT 1
                          FROM nominas n
                          WHERE n.empleado_id = e.id
                            AND n.mes = p_mes
                            AND n.anio = p_anio);

    DECLARE CONTINUE HANDLER FOR NOT FOUND
        SET
            done = TRUE;

    OPEN cur_empleados;

    read_loop:
    LOOP
        FETCH cur_empleados INTO v_empleado_id,
            v_salario_base;

        IF done THEN
            LEAVE read_loop;

        END IF;

        SET
            v_total = v_salario_base;

-- Insertar nómina
        INSERT INTO nominas (empleado_id,
                             mes,
                             anio,
                             salario_base,
                             bonificaciones,
                             deducciones,
                             total_pagar,
                             estado,
                             generada_por)
        VALUES (v_empleado_id,
                p_mes,
                p_anio,
                v_salario_base,
                0,
                0,
                0,
                0,
                v_total,
                'PENDIENTE',
                p_generada_por);

    END LOOP;

    CLOSE cur_empleados;

    SELECT CONCAT('Se generaron nóminas para ', p_mes, p_anio) AS mensaje;

END //


-- SP: Aprobar Nómina
CREATE PROCEDURE sp_aprobar_nomina(
    IN p_nomina_id INT,
    IN p_aprobada_por INT
)
BEGIN
    UPDATE
        nominas
    SET estado       = 'APROBADA',
        aprobada_por = p_aprobada_por
    WHERE id = p_nomina_id
      AND estado = 'PENDIENTE';

    IF ROW_COUNT() > 0 THEN
        SELECT 'Nómina aprobada correctamente' AS mensaje;

    ELSE
        SELECT 'No se pudo aprobar la nómina' AS mensaje;

    END IF;

END //

-- SP: Pagar Nómina
DELIMITER //
CREATE PROCEDURE sp_pagar_nomina(
    IN p_nomina_id INT,
    IN p_fecha_pago DATE,
    IN p_pagada_por INT
)
BEGIN
    UPDATE
        nominas
    SET estado     = 'PAGADA',
        fecha_pago = p_fecha_pago,
        pagada_por = p_pagada_por
    WHERE id = p_nomina_id
      AND estado = 'APROBADA';

    IF ROW_COUNT() > 0 THEN
        SELECT 'Pago de nómina registrado correctamente' AS mensaje;

    ELSE
        SELECT 'No se pudo registrar el pago. Verifique que esté aprobada' AS mensaje;

    END IF;

END //

-- ============================================
-- TRIGGERS
-- ============================================
-- Trigger: Actualizar stock después de movimiento
DELIMITER / /
CREATE TRIGGER trg_actualizar_stock_producto
    AFTER
        INSERT
    ON movimientos_inventario
    FOR EACH ROW
BEGIN
    IF NEW.tipo_movimiento IN ('ENTRADA', 'AJUSTE') THEN
        UPDATE
            productos
        SET stock = stock + NEW.cantidad
        WHERE id = NEW.producto_id;
    ELSEIF NEW.tipo_movimiento IN ('SALIDA', 'VENTA') THEN
        UPDATE
            productos
        SET stock = stock - NEW.cantidad
        WHERE id = NEW.producto_id;

    END IF;

END //

DELIMITER ;

-- Trigger: Actualizar cupos después de inscripción
DELIMITER //

CREATE TRIGGER trg_actualizar_cupos_clase
    AFTER
        INSERT
    ON inscripciones_clases
    FOR EACH ROW
BEGIN
    IF NEW.estado = 'ACTIVA' THEN
        UPDATE
            horarios
        SET cupos_disponibles = cupos_disponibles - 1
        WHERE id = NEW.horario_id
          AND cupos_disponibles > 0;

    END IF;

END //

-- Trigger: Restaurar cupos al cancelar inscripción
CREATE TRIGGER trg_restaurar_cupos_clase
    AFTER
        UPDATE
    ON inscripciones_clases
    FOR EACH ROW
BEGIN
    IF OLD.estado = 'ACTIVA'
        AND NEW.estado = 'CANCELADA' THEN
        UPDATE
            horarios
        SET cupos_disponibles = cupos_disponibles + 1
        WHERE id = NEW.horario_id;

    END IF;

END //

-- Trigger: Verificar membresías vencidas
CREATE TRIGGER trg_verificar_membresia_vencida
    BEFORE
        UPDATE
    ON membresias
    FOR EACH ROW
BEGIN
    IF NEW.fecha_fin < CURDATE()
        AND NEW.estado = 'ACTIVA' THEN
        SET
            NEW.estado = 'VENCIDA';

    END IF;

END //

DELIMITER ;
