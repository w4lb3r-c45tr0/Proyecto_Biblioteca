# 📚 Biblioteca POO 2.0

Proyecto integrador de Programación I desarrollado en Java con interfaz gráfica en Swing.  
La aplicación permite gestionar materiales de biblioteca, usuarios y operaciones de préstamo/devolución, aplicando conceptos de Programación Orientada a Objetos.

---

## Datos del proyecto

| Dato | Información |
|---|---|
| Interfaz gráfica | Java Swing |
| IDE Utilizado | Eclipse IDE |
| Opción elegida | Opción B — Biblioteca 2.0 |
| JDK recomendado | JDK 17 o superior |
| Persistencia | Archivos CSV (carpeta `datos/`) |
| Versión | v2.0 — 2025 |

---

## Objetivo general

Desarrollar una aplicación de escritorio que permita administrar una biblioteca básica mediante una interfaz gráfica en Swing, aplicando correctamente encapsulamiento, herencia, polimorfismo, clases abstractas, validaciones de negocio y persistencia de datos en archivos CSV.

---

## Funcionalidades principales

- Registrar libros y revistas con stock configurable.
- Registrar usuarios tipo Estudiante y Docente.
- Realizar préstamos con validación de disponibilidad y límites por usuario.
- Realizar devoluciones de materiales prestados.
- Agregar nuevos materiales y usuarios desde la interfaz gráfica.
- Eliminar materiales y usuarios (protegido con contraseña de administrador).
- Ver inventario completo de materiales con stock actual.
- Ver préstamos activos del sistema.
- Ver usuarios registrados separados por tipo (Estudiantes / Docentes).
- Validar usuarios y materiales inexistentes.
- Validar materiales sin copias disponibles.
- Validar límite máximo de préstamos por usuario.
- Persistencia automática: los datos se guardan en disco después de cada operación y se cargan al iniciar.

---

## Estructura del proyecto

```
src/
│
├── boot/
│   └── Main.java
│
├── modelo/
│   ├── Material.java
│   ├── Libro.java
│   ├── Revista.java
│   ├── Usuario.java
│   ├── Estudiante.java
│   ├── Docente.java
│   └── Prestamo.java
│
├── servicios/
│   └── Biblioteca.java
│
├── persistencia/
│   └── GestorDatos.java
│
└── ui/
    ├── PantallaBienvenida.java
    ├── VentanaBiblioteca.java
    ├── VentanaInventario.java
    ├── VentanaVerPrestamos.java
    └── VentanaVerUsuarios.java

datos/                  ← se genera automáticamente al correr el proyecto
    materiales.csv
    usuarios.csv
    prestamos.csv
```

---

## Descripción de paquetes

### `boot`
Contiene el punto de entrada del sistema.

| Clase | Descripción |
|---|---|
| `Main` | Arranca la aplicación lanzando `PantallaBienvenida`. |

### `modelo`
Contiene las entidades del sistema. Aquí vive la lógica de POO pura.

| Clase | Descripción |
|---|---|
| `Material` | Clase abstracta base para todos los materiales. Define stock y el método abstracto `diasPrestamoMaximo()`. |
| `Libro` | Hereda de `Material`. Agrega autor. Préstamo máximo: 7 días. |
| `Revista` | Hereda de `Material`. Agrega número de edición. Préstamo máximo: 3 días. |
| `Usuario` | Clase abstracta base para los usuarios. Define el método abstracto `maxPrestamos()`. |
| `Estudiante` | Hereda de `Usuario`. Límite de 3 préstamos simultáneos. Carnet: 3 dígitos. |
| `Docente` | Hereda de `Usuario`. Límite de 5 préstamos simultáneos. Carnet: D + números. |
| `Prestamo` | Representa la relación entre un usuario y un material prestado. Guarda fecha y estado activo/cerrado. |

### `servicios`
Contiene la lógica de negocio central.

| Clase | Descripción |
|---|---|
| `Biblioteca` | Gestiona las tres listas del sistema (materiales, usuarios, préstamos) y expone los métodos de negocio: prestar, devolver, eliminar, buscar. |

### `persistencia`
Maneja el guardado y carga de datos en disco.

| Clase | Descripción |
|---|---|
| `GestorDatos` | Guarda y carga los tres archivos CSV. Se llama automáticamente después de cada operación. |

### `ui`
Contiene toda la interfaz gráfica desarrollada con Swing.

| Clase | Descripción |
|---|---|
| `PantallaBienvenida` | Pantalla splash animada con efecto fade-in al iniciar. |
| `VentanaBiblioteca` | Ventana principal. Panel de control con sidebar, formulario de transacciones, botones de acción y terminal del sistema. |
| `VentanaInventario` | Ventana secundaria que muestra el catálogo completo de materiales con stock. |
| `VentanaVerPrestamos` | Ventana secundaria que muestra los préstamos activos. |
| `VentanaVerUsuarios` | Ventana secundaria que muestra estudiantes y docentes registrados por separado. |

---

## Persistencia — cómo funciona

Al iniciar el programa, si ya existe la carpeta `datos/`, carga los archivos CSV automáticamente. Si no existe (primera ejecución), carga datos de prueba y los guarda de inmediato.

Los archivos usan `|` como separador para evitar conflictos con títulos o nombres que contengan comas.

**Ejemplo — `materiales.csv`:**
```
tipo|codigo|titulo|anio|totalCopias|copiasDisponibles|extra
LIBRO|L1|Java POO|2020|3|2|Juan Perez
REVISTA|R1|Tecnología Hoy|2024|2|1|5
```

**Ejemplo — `usuarios.csv`:**
```
tipo|carnet|nombre
ESTUDIANTE|201|Jery
DOCENTE|D01|Maria
```

El guardado ocurre automáticamente después de cada préstamo, devolución, alta o baja, y también al cerrar la ventana principal.

> **Nota para el equipo:** la carpeta `datos/` está en `.gitignore` porque cambia constantemente. Cada integrante genera la suya localmente al correr el proyecto.

---

## Validaciones principales

| Validación | Dónde se aplica |
|---|---|
| Campos vacíos | `VentanaBiblioteca` — formularios de agregar |
| Código/carnet duplicado | `Biblioteca.buscarMaterial()` / `buscarUsuario()` |
| Formato carnet Estudiante | Regex `\d{3}` — exactamente 3 dígitos |
| Formato carnet Docente | Regex `D\d+` — D seguido de números |
| Año entre 1900 y 2026 | Formulario de agregar material |
| Stock mayor a 0 | Formulario de agregar material |
| Material no disponible | `Biblioteca.prestarMaterial()` |
| Usuario no existe | `Biblioteca.prestarMaterial()` |
| Límite de préstamos alcanzado | `Biblioteca.prestamosActivosUsuario()` |
| Préstamos activos al eliminar | `Biblioteca.eliminarMaterial()` / `eliminarUsuario()` |
| Contraseña de administrador | `VentanaBiblioteca.accionAdmin()` — clave: **123** |

---

## Flujo de uso

### Prestar material
1. Ingresar el código del material y el carnet del usuario en el formulario principal.
2. Presionar **▶ Préstamo**.
3. `Biblioteca.prestarMaterial()` valida existencia, disponibilidad y límite.
4. Si todo está bien, se crea el `Prestamo`, se reduce el stock y se guarda en disco.

### Devolver material
1. Ingresar el código del material en el campo de código.
2. Presionar **⏪ Devolver**.
3. `Biblioteca.devolverMaterial()` busca el préstamo activo, lo cierra y restituye el stock.

### Agregar material o usuario
1. Presionar **📖 Agregar Material** o **👤 Agregar Usuario**.
2. Completar el formulario con los datos requeridos.
3. El sistema valida el formato y que no existan duplicados.
4. El nuevo registro queda guardado en disco automáticamente.

### Eliminar (Administrador)
1. Presionar **🔐 Zona de Administrador** (esquina inferior derecha).
2. Ingresar la contraseña de administrador.
3. Elegir si eliminar material o usuario.
4. Confirmar la operación.
5. El sistema verifica que no haya préstamos activos antes de eliminar.

---

## Datos de prueba precargados

Se cargan automáticamente en la primera ejecución:

| Tipo | Código | Título / Nombre | Extra | Stock |
|---|---|---|---|---|
| Libro | L1 | Java POO | Autor: Juan Perez | 3 |
| Libro | L2 | El arte de la guerra | Autor: Sun Tzu | 5 |
| Revista | R1 | Tecnología Hoy | Edición 5 | 2 |
| Estudiante | 201 | Jery | — | — |
| Estudiante | 202 | Walber | — | — |
| Docente | D01 | Maria | — | — |
| Docente | D02 | Jose | — | — |

---

## Conceptos POO aplicados

| Concepto | Dónde se aplica |
|---|---|
| **Herencia** | `Libro` y `Revista` extienden `Material`. `Estudiante` y `Docente` extienden `Usuario`. |
| **Abstracción** | `Material` y `Usuario` son clases abstractas. Definen la forma de sus subclases sin implementar todo. |
| **Polimorfismo** | `Biblioteca` trabaja con `Material` y `Usuario` sin saber el tipo exacto. `material.prestar()` funciona igual para libros y revistas. |
| **Encapsulamiento** | Las listas en `Biblioteca` son privadas. Solo se acceden a través de métodos públicos. |

---

## División del trabajo

### Semana 1

| Rol | Integrante | Responsabilidad |
|---|---|---|
| Dominio | Jose Julian Osorio Oliva | Clases principales del paquete `modelo` y lógica de `Biblioteca`. |
| Swing | Walber Elián Castro Sandoval | Ventana principal, splash screen y diseño de la interfaz. |
| Integración | Jery Alexander Barrientos Peraza | Conexión entre lógica y UI, organización del proyecto. |

### Semana 2

| Rol | Integrante | Responsabilidad |
|---|---|---|
| Persistencia | Jose Julian Osorio Oliva | Implementación de `GestorDatos` y archivos CSV. |
| Swing | Jery Alexander Barrientos Peraza | Ventanas secundarias: inventario, préstamos, usuarios. |
| Repositorio | Walber Elián Castro Sandoval | Gestión de ramas, commits, resolución de conflictos y merge. |

---

## Integrantes

- **Jery Alexander Barrientos Peraza**
- **Jose Julian Osorio Oliva**
- **Walber Elián Castro Sandoval**

---

*Universidad Mariano Gálvez — Programación I — 2025*
