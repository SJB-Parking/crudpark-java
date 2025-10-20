# Interfaz Swing para CrudPark

## 📋 Resumen de Cambios

Se ha creado una interfaz gráfica profesional usando Swing con JFrame en lugar de JOptionPane.

## 🎨 Nuevas Vistas Creadas

### 1. **LoginFrame** (`app/view/swing/LoginFrame.java`)
- Ventana de login con campos para email y contraseña
- Ambos campos en la misma ventana
- Botones de Login y Cancel
- Enter key para enviar el formulario

### 2. **MainMenuFrame** (`app/view/swing/MainMenuFrame.java`)
- Menú principal con 3 botones grandes y coloridos:
  - 🚗 Vehicle Entry (azul)
  - 🚪 Vehicle Exit (verde)
  - ❌ Exit System (rojo)
- Muestra el nombre del operador logueado
- Efectos hover en los botones

### 3. **VehicleEntryFrame** (`app/view/swing/VehicleEntryFrame.java`)
- Ventana para ingresar la placa del vehículo
- Muestra instrucciones de formato
- Ventana de éxito con:
  - Detalles completos del ticket
  - QR code embebido (200x200px)
  - Diseño profesional con HTML styling

### 4. **VehicleExitFrame** (`app/view/swing/VehicleExitFrame.java`)
- Ventana para ingresar el ID del ticket
- Ventana de éxito con:
  - Detalles de entrada y salida
  - Duración del estacionamiento
  - Total a pagar (destacado con color)
  - Diseño profesional

## 🎯 Controladores Swing

Se crearon controladores wrapper que conectan las vistas Swing con los controladores existentes:

- `SwingAuthController.java`
- `SwingVehicleEntryController.java`
- `SwingVehicleExitController.java`

## 🚀 Cómo Ejecutar la Versión Swing

### Opción 1: Ejecutar MainSwing.java

```bash
# Compilar
mvn clean compile

# Ejecutar
mvn exec:java -Dexec.mainClass="app.MainSwing"
```

### Opción 2: Desde VS Code

1. Abre el archivo `src/main/java/app/MainSwing.java`
2. Presiona F5 o haz clic en "Run"

## ✨ Características

### Mejoras visuales:
- ✅ Ventanas modernas con JFrame
- ✅ Todos los campos en la misma ventana (no diálogos separados)
- ✅ Botones con colores y efectos hover
- ✅ QR Code embebido en la ventana (200x200px)
- ✅ Diseño responsive y centrado
- ✅ Estilo profesional con HTML en los labels
- ✅ Look and Feel nativo del sistema operativo

### Flujo de la aplicación:
1. **Login**: Email y contraseña en la misma ventana
2. **Menú Principal**: 3 botones grandes con íconos
3. **Entrada de Vehículo**: 
   - Campo de placa
   - Ventana de éxito con QR y detalles
4. **Salida de Vehículo**:
   - Campo de ticket ID
   - Ventana con detalles y total a pagar

## 🔧 Manteniendo JOptionPane (Versión Actual)

El Main.java actual sigue funcionando con JOptionPane. Para usarlo:

```bash
mvn exec:java -Dexec.mainClass="app.Main"
```

## 📝 Notas

- **QR Code**: Se genera dinámicamente desde el texto plano almacenado en BD
- **Tamaño**: 200x200px (modificable en `QRCodeGenerator.java`)
- **Formato de Fecha**: `yyyy-MM-dd hh:mm a` (ej: 2025-10-14 09:45 AM)
- **Formato de QR**: `TICKET:000001|PLATE:ABC123|DATE:1697500000`

## 🎨 Colores Usados

- **Login Button**: #0078D7 (Azul Windows)
- **Vehicle Entry**: #0078D7 (Azul)
- **Vehicle Exit**: #009688 (Verde Teal)
- **Exit System**: #C83232 (Rojo)
- **Success**: #009600 (Verde)
- **Background**: #F0F0F0 (Gris claro)

## 🔜 Próximos Pasos (Opcional)

Si deseas más mejoras:
- [ ] Agregar íconos reales en lugar de emojis
- [ ] Implementar JavaFX para una UI aún más moderna
- [ ] Agregar animaciones en las transiciones
- [ ] Theme switcher (claro/oscuro)
- [ ] Historial de tickets en tabla
