# Evaluador

**Evaluador** es una aplicación Java de escritorio desarrollada con Swing, que genera exámenes aleatorios basados en la taxonomía de Bloom. Utiliza una base de datos MySQL con 144 preguntas categorizadas por asignatura, tipo de pregunta y nivel cognitivo. Permite al usuario responder un examen multidisciplinario y obtener un resumen detallado de su rendimiento.

## Funcionalidades

- Generación aleatoria de preguntas desde base de datos MySQL.
- Soporte para:
  - **Asignaturas:**
    1. Matemáticas
    2. Programación
    3. Bases de Datos
    4. Sistemas Operativos y Redes
  - **Tipos de Pregunta:**
    - Selección Múltiple
    - Verdadero o Falso
  - **Taxonomía de Bloom:**
    1. Recordar
    2. Comprender
    3. Aplicar
    4. Analizar
    5. Evaluar
    6. Crear
- Evaluación automática de respuestas y resumen con porcentajes.
- Revisión de preguntas tras la evaluación (respuesta correcta e incorrecta destacadas).

## Requisitos

- **Java JDK 21**
- **NetBeans** (u otro IDE compatible)
- **MySQL** instalado localmente
- Archivo `.jar` del conector JDBC: `mysql-connector-j-9.3.0.jar`

## Estructura del Proyecto

src/  
├── backend/  
│ ├── Conexion.java  
│ ├── Control.java  
│ ├── Pregunta.java  
│ ├── config.properties ← Configuración de conexión a la BD  
├── frontend/  
│ ├── Evaluacion.java  
│ └── Principal.java  
├── data/  
│ └── pregs.sql ← Dump de base de datos con 144 preguntas


## Configuración Inicial

1. **Importar base de datos**
   - En tu gestor MySQL local, importa el archivo `src/data/pregs.sql` para crear la base de datos `Bloom` y las preguntas.

2. **Editar archivo de configuración**
   - Abre el archivo `src/backend/config.properties` y ajusta las credenciales de tu base de datos local:

     ```properties
     db.url=jdbc:mysql://localhost:3306/Bloom
     db.user=root
     db.password=tu_contraseña
     ```

3. **Agregar JDBC**
   - En NetBeans:
     - Haz clic derecho en el proyecto → **Properties** → **Libraries**.
     - Agrega el `mysql-connector-j-9.3.0.jar`.

## Ejecución

1. Abre el proyecto en NetBeans.
2. Asegúrate de tener la base de datos `Bloom` activa.
3. Corre `Principal.java` para lanzar la aplicación.

## Licencia

Este proyecto está bajo licencia MIT. Puedes modificarlo y distribuirlo libremente.

---
