<!-- BADGES -->
<div>
  <a href="#">
    <img alt="" align="left" src="https://img.shields.io/github/stars/AndresFr56/GymVitae?color=244981&labelColor=0D1117&style=for-the-badge"/>
  </a>
  <a href="#">
    <img alt="" align="right" src="https://badges.pufler.dev/visits/AndresFr56/GymVitae?style=for-the-badge&color=244981&logoColor=white&labelColor=244981"/>
  </a>
</div>

<h1 align="center" style="font-weight:mediun; padding:24px;">GymVitae - Sistema de gestion de Gimnasio Open Source</h1>

Un sistema de gestion de Gimnasio Open Source con la licensia GPL cualquiera puede hacer uso de este o mantener este software mandando PR con sus soluciones.


## Instalacion

En esta sección se explica el proceso de instalación de todas las dependencias necesarias para ejecutar el proyecto.


### 📦 Requerimientos de Instalacion
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/install)
- [Apache Maven](https://maven.apache.org/install.html)
- [Java SKD 21 o superior](https://www.oracle.com/java/technologies/downloads/#java21)

### 📦 Requerimientos de desarrollo y Testing
- [Jetbrains o Netbeans](https://www.jetbrains.com/idea/)
- [PMD](https://pmd.github.io/)
- [SonarQube jetbrains plugin(optional)](https://plugins.jetbrains.com/plugin/7973-sonarqube-for-ide)
- [Google-java-format(optional)](https://plugins.jetbrains.com/plugin/8527-google-java-format)
- [Git](https://git-scm.com/install/)
- [DataGrid or cualquier DBSM](https://www.jetbrains.com/datagrip/)
- [CheckStyle Opcional](https://checkstyle.sourceforge.io/
)

### 📝 Clonar Repositorio

```sh
git clone https://github.com/AndresFr56/GymVitae.git
cd GymVitae
```

o tambien puedes decargar el zip de la ultima release/Commit del Repositorio

### Variables de Entorno .env

Actualmente tenemos un archivo `.env-template` copie el archivo y cambie el nombre por `.env`

ejemplo del contenido del archivo `.env`

```bash
MYSQL_ROOT_PASSWORD=gymvitae_root_2025
MYSQL_DATABASE=gym_system
MYSQL_USER=gym_admin
MYSQL_PASSWORD=gym_pass_2025
MYSQL_TEST_ROOT_PASSWORD=gymvitae_root_2025
MYSQL_TEST_DATABASE=gym_system
MYSQL_TEST_USER=gym_admin
MYSQL_TEST_PASSWORD=gym_pass_2025
```

> [!WARNING]  
> ⚠ Si no quiere tener problemas con Mysql, le recomendamos utilizar el docker-compose.yaml del proyecto y levantar la base de datos

## 🚀 Ejecución del Proyecto

En esta sección se explica el proceso de ejecución del proyecto.

```sh
cd App && nvm run
```

> [!WARNING]  
> ⚠ Se recomienda revisar el archivo `pom.xml` para revisar las dependencias del proyecto.
