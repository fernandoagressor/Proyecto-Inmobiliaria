# Arquitectura del Sistema Inmobiliario

## 1. Objetivo

Desarrollar una plataforma inmobiliaria compuesta por:

- Portal público.
- Panel administrativo.
- Panel de empleados.
- Portal privado para clientes.
- API central desarrollada con Spring Boot.
- Aplicación web desarrollada con Angular.

---

## 2. Tipos de usuario

### ADMIN

Acceso completo al sistema:

- Dashboard ejecutivo.
- Gestión de usuarios y roles.
- Gestión de clientes.
- Gestión de empleados.
- Gestión de propiedades.
- Gestión de reservas.
- Gestión de contratos.
- Gestión de pagos.
- Gestión de facturas.
- Reportes.
- Auditoría.
- Configuración.

### EMPLEADO

Acceso operativo limitado:

- Dashboard operativo.
- Consulta y gestión de clientes autorizados.
- Gestión de propiedades.
- Gestión de reservas.
- Consulta y gestión de contratos permitidos.
- Registro y consulta de pagos.
- Consulta de facturas.
- Seguimiento de actividades.

No podrá:

- Administrar roles.
- Modificar configuraciones generales.
- Administrar otros usuarios sin autorización.
- Acceder a auditoría completa.

### CLIENTE

Acceso únicamente a su propia información:

- Perfil personal.
- Contratos.
- Propiedades relacionadas.
- Reservas.
- Facturas.
- Historial de pagos.
- Documentos.
- Solicitudes.

---

## 3. Áreas de la aplicación

### Portal público

Rutas previstas:

- `/`
- `/propiedades`
- `/propiedades/:id`
- `/nosotros`
- `/servicios`
- `/contacto`
- `/login`

### Panel administrativo

Ruta base:

- `/admin`

Módulos:

- `/admin/dashboard`
- `/admin/usuarios`
- `/admin/empleados`
- `/admin/clientes`
- `/admin/propiedades`
- `/admin/reservas`
- `/admin/contratos`
- `/admin/pagos`
- `/admin/facturas`
- `/admin/reportes`
- `/admin/auditoria`
- `/admin/configuracion`

### Panel de empleados

Ruta base:

- `/empleado`

Módulos:

- `/empleado/dashboard`
- `/empleado/clientes`
- `/empleado/propiedades`
- `/empleado/reservas`
- `/empleado/contratos`
- `/empleado/pagos`
- `/empleado/facturas`
- `/empleado/actividades`

### Portal de clientes

Ruta base:

- `/cliente`

Módulos:

- `/cliente/inicio`
- `/cliente/perfil`
- `/cliente/contratos`
- `/cliente/propiedades`
- `/cliente/reservas`
- `/cliente/facturas`
- `/cliente/pagos`
- `/cliente/documentos`
- `/cliente/solicitudes`

---

## 4. Seguridad

La autenticación se realizará mediante JWT.

Roles principales:

- `ADMIN`
- `EMPLEADO`
- `CLIENTE`

La autorización debe aplicarse en dos niveles:

1. Backend con Spring Security.
2. Frontend con guards y validación de roles.

El frontend no será considerado una barrera de seguridad.  
Los permisos definitivos siempre serán validados en el backend.

---

## 5. Arquitectura del frontend

```

src/app
├── core
│   ├── guards
│   ├── interceptors
│   ├── services
│   └── models
├── shared
│   ├── components
│   ├── layouts
│   ├── pipes
│   └── directives
├── features
│   ├── public-site
│   ├── auth
│   ├── admin
│   ├── empleado
│   ├── cliente-portal
│   ├── propiedades
│   ├── reservas
│   ├── contratos
│   ├── pagos
│   └── facturas
└── app.routes.ts

```
## 6. Arquitectura del backend
```

controller
dto
├── mapper
├── request
└── response
entity
repository
security
service
└── impl

```
## 7. Principios del desarrollo

```
-No duplicar componentes.
-Reutilizar servicios, tablas, formularios y tarjetas.
-Mantener separación entre portal público y paneles privados.
-Aplicar permisos en backend y frontend.
-Trabajar mediante ramas por funcionalidad.
-Realizar commits pequeños y descriptivos.
-Compilar y probar antes de cada commit.
-No agregar módulos sin relación con los requisitos del proyecto.

```

## 8. Matriz de permisos

```

| Módulo | Administrador | Empleado | Cliente |
|---------|:-------------:|:--------:|:--------:|
| Dashboard | ✅ | ✅ | ✅ |
| Usuarios | ✅ | ❌ | ❌ |
| Roles | ✅ | ❌ | ❌ |
| Empleados | ✅ | ❌ | ❌ |
| Clientes | ✅ | ✅ | Solo su perfil |
| Propiedades | ✅ | ✅ | Consultar |
| Reservas | ✅ | ✅ | Sus reservas |
| Contratos | ✅ | ✅ | Sus contratos |
| Pagos | ✅ | ✅ | Sus pagos |
| Facturas | ✅ | ✅ | Sus facturas |
| Documentos | ✅ | ✅ | Sus documentos |
| Solicitudes | ✅ | ✅ | Crear y consultar |
| Reportes | ✅ | Limitados | ❌ |
| Auditoría | ✅ | ❌ | ❌ |
| Configuración | ✅ | ❌ | ❌ |