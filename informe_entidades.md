# Informe de Análisis de Entidades - Sistema de Tienda (iStore)

## 1. Descripción General del Sistema

El sistema `appweb` es una aplicación de gestión de e-commerce que implementa un modelo de negocio completo para una tienda virtual. Las entidades modelan los aspectos clave de operación: catálogo de productos, gestión de usuarios, procesamiento de pedidos, pagos y auditoría de estados.

---

## 2. Análisis Detallado de Entidades

### **2.1 USUARIOS**
**Propósito:** Almacenar información de clientes y administradores del sistema.

**Atributos:**
- `idUsuario` (PK): Identificador único del usuario
- `nombre`: Nombre completo del usuario
- `apellido`: Apellido del usuario
- `email`: Correo electrónico único (para login y contacto)
- `password`: Contraseña hasheada
- `telefono`: Número de contacto
- `direccion`: Dirección de envío/facturación
- `fechaRegistro`: Timestamp de creación de cuenta
- `estado`: Activo/Inactivo (soft delete o control de acceso)
- `rolId` (FK): Referencia al rol asignado

**Justificación:** Es la entidad raíz del sistema. Todo usuario debe tener un rol definido para controlar permisos y acceso.

---

### **2.2 ROLES**
**Propósito:** Definir perfiles de acceso y permisos en el sistema.

**Atributos:**
- `idRol` (PK): Identificador único del rol
- `nombre`: Nombre descriptivo (ej: ADMIN, CLIENTE, VENDEDOR)
- `descripcion`: Detalles sobre qué acciones puede realizar
- `estado`: Activo/Inactivo

**Justificación:** Implementa control de acceso basado en roles (RBAC). Permite diferentes niveles de permisos sin duplicar lógica.

**Relaciones:**
- **1:N con USUARIOS** - Un rol puede ser asignado a múltiples usuarios

---

### **2.3 CATEGORIAS**
**Propósito:** Clasificar productos de manera jerárquica para organizar el catálogo.

**Atributos:**
- `idCategoria` (PK): Identificador único
- `nombre`: Nombre de la categoría (ej: Electrónica, Ropa, Accesorios)
- `descripcion`: Descripción detallada
- `imagen`: URL o referencia a imagen de categoría
- `estado`: Activo/Inactivo (para catálogo visible)
- `categoriapadreId` (FK opcional): Para subcategorías (relación reflexiva)

**Justificación:** Fundamental para navegación de tienda y organización de inventario. La relación reflexiva permite jerarquías multinivel.

**Relaciones:**
- **1:N con PRODUCTOS** - Una categoría contiene múltiples productos
- **N:1 (reflexiva) con CATEGORIAS** - Una categoría puede tener una categoría padre (subcategorías)

---

### **2.4 COLORES**
**Propósito:** Catálogo de variaciones de color disponibles para productos.

**Atributos:**
- `idColor` (PK): Identificador único
- `nombre`: Nombre del color (ej: Rojo, Azul, Negro)
- `codigo`: Código hexadecimal (#FF0000) o código de inventario
- `descripcion`: Descripción adicional
- `estado`: Activo/Inactivo

**Justificación:** Los productos suelen tener múltiples opciones de color. Centralizar colores evita duplicación y permite análisis de preferencias.

**Relaciones:**
- **N:M con PRODUCTOS** (implícito en variantes) - Un producto puede tener múltiples colores, un color en múltiples productos

---

### **2.5 PRODUCTOS**
**Propósito:** Catálogo maestro de artículos disponibles para la venta.

**Atributos:**
- `idProducto` (PK): Identificador único
- `nombre`: Nombre del producto
- `descripcion`: Descripción larga del producto
- `precio`: Precio unitario actual (en moneda base)
- `precioOriginal`: Precio antes de descuentos (para referencia)
- `stock`: Cantidad disponible en inventario
- `imagenUrl`: URL principal de producto
- `categoriaId` (FK): Referencia a categoría
- `sku`: Código único de identificación de inventario
- `peso`: Peso del producto (para cálculo de envío)
- `dimensiones`: Dimensiones del producto
- `estado`: Activo/Inactivo (disponible para venta)
- `fechaCreacion`: Cuando se agregó al catálogo
- `fechaActualizacion`: Última modificación

**Justificación:** Centro del sistema de e-commerce. Cada producto es un artículo vendible con sus propiedades comerciales.

**Relaciones:**
- **N:1 con CATEGORIAS** - Muchos productos en una categoría
- **1:N con PEDIDOS_ITEMS** - Un producto puede aparecer en múltiples órdenes
- **N:M implícito con COLORES** - Un producto puede ofrecerse en múltiples colores

---

### **2.6 PEDIDOS**
**Propósito:** Registro maestro de transacciones de compra.

**Atributos:**
- `idPedido` (PK): Número de orden único
- `usuarioId` (FK): Quién realizó la compra
- `fechaPedido`: Timestamp de creación del pedido
- `fechaEntrega`: Fecha estimada o real de entrega
- `direccionEntrega`: Dirección específica del envío
- `montoTotal`: Suma de todos los items + impuestos + envío
- `descuentoAplicado`: Cantidad de descuento aplicado
- `estadoCompraId` (FK): Estado actual del pedido
- `estadoPagoId` (FK): Estado del pago asociado
- `metodoPagoId` (FK): Método usado para pagar
- `numeroSeguimiento`: Tracking del envío
- `notas`: Notas especiales del cliente o admin

**Justificación:** Documento de negocio central. Vincula usuario, productos, pago y logística. Immutable para auditoría.

**Relaciones:**
- **N:1 con USUARIOS** - Un usuario realiza múltiples pedidos
- **1:N con PEDIDOS_ITEMS** - Un pedido contiene múltiples líneas de producto
- **N:1 con ESTADOS_COMPRAS** - Un pedido tiene un estado de compra
- **N:1 con ESTADOS_PAGOS** - Un pedido tiene un estado de pago
- **N:1 con METODOS_PAGOS** - Un pedido usa un método de pago

---

### **2.7 PEDIDOS_ITEMS**
**Propósito:** Línea de detalle dentro de un pedido (tabla intermedia desdenormalizada).

**Atributos:**
- `idPedidoItem` (PK): Identificador único de línea
- `pedidoId` (FK): Referencia al pedido padre
- `productoId` (FK): Referencia al producto vendido
- `cantidad`: Unidades de este producto en el pedido
- `precioUnitario`: Precio al momento de la compra (snapshot histórico)
- `subtotal`: cantidad × precioUnitario
- `color`: Color seleccionado (puede ser almacenado como texto o FK a Colores)
- `descuentoLinea`: Descuento específico de este item

**Justificación:** Implementa el patrón de Order Line Items. Mantiene un snapshot del precio en el momento de compra (importante para historial y análisis).

**Relaciones:**
- **N:1 con PEDIDOS** - Muchos items en un pedido
- **N:1 con PRODUCTOS** - Referencia al catálogo (pero precio es histórico)

---

### **2.8 ESTADOS_COMPRAS**
**Propósito:** Diccionario de estados posibles en el ciclo de vida de una compra.

**Atributos:**
- `idEstadoCompra` (PK): Identificador único
- `nombre`: Nombre del estado (ej: PENDIENTE, CONFIRMADO, DESPACHADO, ENTREGADO, CANCELADO)
- `descripcion`: Qué significa cada estado
- `orden`: Número para ordenamiento secuencial
- `color`: Color para UI (ej: #FF0000 para cancelado)

**Justificación:** Diccionario que define la máquina de estados para pedidos. Permite trazabilidad y auditoría del flujo de compras.

**Relaciones:**
- **1:N con PEDIDOS** - Un estado puede aplicarse a múltiples pedidos

---

### **2.9 ESTADOS_PAGOS**
**Propósito:** Diccionario de estados en el ciclo de pagos.

**Atributos:**
- `idEstadoPago` (PK): Identificador único
- `nombre`: Nombre del estado (ej: PENDIENTE, PROCESANDO, APROBADO, RECHAZADO, REEMBOLSADO)
- `descripcion`: Detalle del estado
- `requiereAccion`: Boolean - ¿Requiere intervención manual?
- `orden`: Número secuencial

**Justificación:** Máquina de estados para transacciones de pago. Crítico para conciliación contable y resolución de disputas.

**Relaciones:**
- **1:N con PEDIDOS** - Un estado de pago puede aplicarse a múltiples pedidos

---

### **2.10 METODOS_PAGOS**
**Propósito:** Catálogo de formas de pago aceptadas por la tienda.

**Atributos:**
- `idMetodoPago` (PK): Identificador único
- `nombre`: Nombre del método (ej: TARJETA_CREDITO, TRANSFERENCIA, EFECTIVO, PAYPAL, BITCOIN)
- `descripcion`: Detalles sobre cómo funciona
- `comision`: Porcentaje de comisión cobrado por el método
- `tiempoConfirmacion`: Tiempo promedio para confirmar el pago
- `estado`: Activo/Inactivo (disponible para nuevas compras)
- `requerirVerificacion`: ¿Necesita validación adicional?

**Justificación:** Centraliza configuración de métodos de pago. Permite agregar/quitar opciones sin cambiar código.

**Relaciones:**
- **1:N con PEDIDOS** - Un método de pago puede usarse en múltiples pedidos

---

### **2.11 TIPOS_COMPROBANTES**
**Propósito:** Tipos de documentos fiscales que puede generar la tienda.

**Atributos:**
- `idTipoComprobante` (PK): Identificador único
- `nombre`: Nombre (ej: FACTURA, BOLETA, NOTA_CREDITO, REMISION)
- `abreviatura`: Código corto (ej: F, B, NC, REM)
- `descripcion`: Requisitos legales/fiscales
- `serie`: Serie numerada del comprobante
- `ultimoNumero`: Último número usado (para secuencia)
- `estado`: Activo/Inactivo

**Justificación:** Cumplimiento fiscal/legal. Diferentes jurisdicciones requieren distintos tipos de comprobantes. Los números de comprobante son secuenciales por ley.

**Relaciones:**
- **Implícita con PEDIDOS** (no hay FK explícita) - Un pedido puede generar un comprobante de un tipo específico

---

## 3. Diagrama de Relaciones

```
┌─────────────────────────────────────────────────────────────┐
│                         USUARIOS                             │
│  (idUsuario, nombre, email, password, rolId)               │
└──────────┬──────────────────────────────────────────────────┘
           │ N:1
           │
           ├─────────────────────────────────────────────────┐
           │                                                   │
┌──────────▼──────────┐                          ┌────────────▼────────┐
│      ROLES          │                          │  PEDIDOS            │
│  (idRol, nombre)    │                          │  (idPedido,         │
└─────────────────────┘                          │   usuarioId,        │
                                                 │   estadoCompraId,   │
                                                 │   estadoPagoId,     │
                                                 │   metodoPagoId)     │
                                                 └────────┬─┬──────┬───┘
                                                          │ │      │
                                   ┌──────────────────────┘ │      │
                                   │       ┌────────────────┘      │
                                   │       │      ┌────────────────┘
                                   │       │      │
                         ┌─────────▼─┐ ┌──▼──────▼──┐ ┌─────────────────┐
                         │ESTADOS_  │ │ESTADOS_   │ │METODOS_PAGOS    │
                         │COMPRAS   │ │PAGOS      │ │(idMetodoPago)   │
                         │(idEstado)│ │(idEstado) │ └─────────────────┘
                         └──────────┘ └───────────┘
           
┌──────────────────────────┐
│   PEDIDOS_ITEMS          │
│  (idPedidoItem,          │
│   pedidoId FK,           │
│   productoId FK,         │
│   cantidad, precio)      │
└────────┬────────────────┬┘
         │                │
    ┌────▼────┐    ┌──────▼──────────┐
    │PRODUCTOS│    │(references hist)│
    │(idProd, │    │ PRODUCTOS       │
    │nombre,  │    │ snapshot price  │
    │precio,  │    └─────────────────┘
    │categId) │
    └────┬────┘
         │ N:1
         │
    ┌────▼──────────────┐
    │  CATEGORIAS       │
    │ (idCategoria,     │
    │  nombre,          │
    │  categoriaPadreId)│  ◄── Relación reflexiva
    └───────────────────┘

┌──────────────────┐
│    COLORES       │
│  (idColor, nom)  │◄─── N:M con PRODUCTOS (implícito)
└──────────────────┘     Variantes de producto

┌──────────────────────────┐
│ TIPOS_COMPROBANTES       │
│ (idTipoComprobante,      │
│  nombre, serie)          │
└──────────────────────────┘
```

---

## 4. Restricciones y Reglas de Negocio

| Entidad | Restricción | Razón |
|---------|------------|-------|
| **USUARIOS** | Email único | Identificación y login |
| **USUARIOS** | Password con hash salted | Seguridad |
| **PRODUCTOS** | Stock ≥ 0 | No vender inventario inexistente |
| **PRODUCTOS** | Precio > 0 | Transacción válida |
| **PEDIDOS** | montoTotal = Σ(subtotal items) | Integridad contable |
| **PEDIDOS_ITEMS** | precioUnitario es snapshot | Auditoría histórica |
| **ESTADOS_COMPRAS** | Transiciones predefinidas | Control de flujo |
| **ESTADOS_PAGOS** | Vinculado a metodoPago | Coherencia |
| **METODOS_PAGOS** | comision ≥ 0 | Cálculo correcto |
| **CATEGORIAS** | Evitar ciclos recursivos | Árbol válido |

---

## 5. Consideraciones para Simulación de Datos

### **Volúmenes Recomendados:**
- **USUARIOS**: 100-500 (clientes activos)
- **ROLES**: 3-5 (ADMIN, CLIENTE, VENDEDOR, etc.)
- **CATEGORIAS**: 10-30 (jerarquía 2-3 niveles)
- **PRODUCTOS**: 200-1000 por categoría (stock variado)
- **COLORES**: 15-20 opciones
- **PEDIDOS**: 50-200 por usuario (2-3 años de datos)
- **PEDIDOS_ITEMS**: 3-5 items promedio por pedido
- **ESTADOS_COMPRAS**: 6-8 estados fijos
- **ESTADOS_PAGOS**: 5-6 estados fijos
- **METODOS_PAGOS**: 4-6 opciones fijas
- **TIPOS_COMPROBANTES**: 3-4 tipos fijos

### **Coherencias Críticas:**
1. Los precios en `PEDIDOS_ITEMS` deben ser ≤ precio actual en `PRODUCTOS`
2. Los estados deben seguir secuencias lógicas (ej: PENDIENTE → CONFIRMADO → DESPACHADO)
3. `PEDIDOS.montoTotal` debe validarse contra suma de items
4. Stock en `PRODUCTOS` debe decrementarse coherentemente
5. Fechas: `fechaPedido` ≤ `fechaEntrega` ≤ `fechaActual`
6. Usuarios debe existir antes de crear `PEDIDOS`

---

## 6. Propósito General del Sistema

Este es un **sistema de e-commerce completo** que maneja:
- 🏪 **Catálogo**: Productos organizados por categorías con variantes
- 👥 **Usuarios**: Registro y gestión de perfiles con control de acceso
- 📦 **Pedidos**: Compras con trazabilidad y múltiples estados
- 💳 **Pagos**: Métodos variados con estados de confirmación
- 📋 **Fiscal**: Generación de comprobantes por regulación

La arquitectura permite escalabilidad, auditoría completa y cumplimiento normativo.