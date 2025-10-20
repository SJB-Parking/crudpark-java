# 🖨️ Guía de Impresión de Tickets - CrudPark

## Descripción General

El sistema CrudPark ahora cuenta con funcionalidad de impresión física de tickets, permitiendo imprimir:
- **Tickets de Entrada**: Con información del vehículo y código QR
- **Recibos de Salida**: Con detalles de pago y tiempo de estancia

## Características de Impresión

### 🎫 Ticket de Entrada
El ticket de entrada incluye:
- Logo y título "CRUDPARK"
- ID del ticket (formato: 000001)
- Folio único
- Placa del vehículo (destacada en color azul)
- Tipo de vehículo
- Tipo de ticket
- Fecha y hora de entrada (formato: 2025-10-14 09:45 AM)
- Código QR (150x150px) con información del ticket
- Nota informativa sobre conservar el ticket

### 🧾 Recibo de Salida
El recibo de salida incluye:
- Logo y título "PAYMENT RECEIPT"
- Toda la información del ticket
- Hora de entrada y salida
- Duración total del estacionamiento (horas y minutos)
- **Monto a pagar** (destacado en grande):
  - Verde si es GRATIS (con razón)
  - Rojo si hay cargo
- Fecha y hora de impresión
- Mensaje de despedida

## Cómo Usar

### Para Operadores

1. **Registrar Entrada de Vehículo**:
   - Ingresar placa del vehículo
   - Hacer clic en "Register Entry"
   - Aparecerá un diálogo preguntando si desea imprimir
   - Seleccionar "Yes" para imprimir

2. **Procesar Salida de Vehículo**:
   - Ingresar ID del ticket
   - Hacer clic en "Process Exit"
   - El sistema calculará el monto
   - Aparecerá un diálogo con resumen y opción de impresión
   - Seleccionar "Yes" para imprimir

### Selección de Impresora

Cuando selecciona "Yes" para imprimir:
1. Se abrirá el diálogo de impresión del sistema
2. Podrá seleccionar la impresora deseada
3. Configurar opciones de impresión (copias, orientación, etc.)
4. Hacer clic en "Print" para imprimir
5. O "Cancel" para cancelar la impresión

## Formato de Impresión

### Tamaño de Página
- Formato estándar: Letter/A4
- Orientación: Vertical (Portrait)
- Márgenes: Configurables en el diálogo de impresión

### Fuentes Utilizadas
- **Título**: Arial Bold 24pt
- **Encabezados**: Arial Bold 16pt
- **Información**: Arial Plain 12pt
- **Placa**: Arial Bold 14pt (destacada)
- **Monto**: Arial Bold 20pt (muy destacado)

### Códigos QR
- Tamaño: 150x150 píxeles
- Formato: QR Code estándar
- Contenido: `TICKET:000001|PLATE:ABC123|DATE:timestamp`
- Posición: Centrado en el ticket

## Ventajas de la Impresión

✅ **Para Operadores**:
- Proceso rápido y eficiente
- Menos errores manuales
- Registro físico de transacciones
- Profesionalismo en el servicio

✅ **Para Clientes**:
- Ticket físico para conservar
- QR code para salida rápida
- Recibo detallado de pago
- Comprobante de estacionamiento

✅ **Para el Negocio**:
- Respaldo físico de operaciones
- Mejor imagen profesional
- Cumplimiento de requisitos contables
- Trazabilidad de transacciones

## Impresoras Compatibles

El sistema es compatible con cualquier impresora instalada en Windows que soporte:
- Impresión Java (javax.print)
- Gráficos AWT (java.awt.print)

### Tipos Recomendados
- **Impresoras de Tickets POS**: Para tickets de tamaño reducido
- **Impresoras Láser**: Para recibos estándar
- **Impresoras Térmicas**: Para alta velocidad y bajo costo
- **Impresoras de Inyección**: Para uso general

## Solución de Problemas

### La impresora no aparece en el diálogo
- Verificar que la impresora esté instalada en Windows
- Comprobar que la impresora esté encendida
- Verificar los drivers de la impresora

### El ticket no se imprime
- Verificar que haya papel en la impresora
- Comprobar que no haya atascos
- Revisar la cola de impresión de Windows
- Verificar permisos de impresión

### El QR code no se imprime correctamente
- Asegurar que la impresora soporte gráficos
- Verificar la resolución de impresión
- Comprobar que el cartucho/tóner tenga tinta suficiente

### Formato incorrecto
- Ajustar configuración de página en el diálogo
- Verificar orientación (debe ser vertical)
- Comprobar márgenes de la impresora

## Archivos Relacionados

### Código Fuente
- `app/util/TicketPrinter.java`: Clase principal de impresión
- `app/view/swing/VehicleEntryFrame.java`: Diálogo de confirmación de entrada
- `app/view/swing/VehicleExitFrame.java`: Diálogo de confirmación de salida
- `app/util/QRCodeGenerator.java`: Generación de códigos QR

### Dependencias
- `javax.swing`: Diálogos de confirmación
- `java.awt.print`: API de impresión Java
- `ZXing`: Generación de códigos QR

## Personalización

### Modificar el Diseño del Ticket
Editar la clase `TicketPrinter.EntryTicketPrintable`:
```java
// Cambiar fuentes
Font titleFont = new Font("Arial", Font.BOLD, 24);

// Cambiar colores
g2d.setColor(new Color(0, 120, 215));

// Cambiar tamaño del QR
int qrSize = 150; // Modificar aquí
```

### Modificar el Diseño del Recibo
Editar la clase `TicketPrinter.ExitTicketPrintable`:
```java
// Ajustar tamaño del monto
Font amountFont = new Font("Arial", Font.BOLD, 20);

// Cambiar colores de pago
Color costColor = new Color(200, 0, 0); // Rojo para pagos
```

## Notas Técnicas

- El sistema usa la API estándar de Java Print Service
- Los tickets se generan dinámicamente en tiempo de impresión
- No se almacenan archivos temporales de impresión
- El QR code se genera desde la base de datos en tiempo real
- Compatible con Windows Print Spooler

## Recomendaciones

1. **Configurar impresora predeterminada** en Windows para agilizar el proceso
2. **Mantener papel suficiente** en la impresora
3. **Probar impresión** antes de iniciar operaciones del día
4. **Conservar recibos** como respaldo contable
5. **Hacer mantenimiento** regular a las impresoras

---

**Versión**: 1.0  
**Fecha**: Octubre 2025  
**Sistema**: CrudPark - Parking Management System
