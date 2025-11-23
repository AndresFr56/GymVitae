<div align="center">

## GymVitae - Sistema de gestion de Gimnasio Open Source

![Stargazers]
[![License]](LICENSE)

</div>

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
- [CheckStyle Opcional](https://checkstyle.sourceforge.io/)

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
```

Para levantar el contenedor de docker

```bash
docker-compose up -d
```

> [!WARNING]
> ⚠ Si no quiere tener problemas con Mysql, le recomendamos utilizar el docker-compose.yaml del proyecto y levantar la base de datos

## 🚀 Ejecución del Proyecto

En esta sección se explica el proceso de ejecución del proyecto.
Para una mejor experiencia instale [MAVEN](https://maven.apache.org/install.html)



```sh
cd App && mvn clean install
mvn clean package
mvn exec:java -Dexec.mainClass="com.gymvitae.Main"

```

> [!WARNING]
> ⚠ Se recomienda revisar el archivo `pom.xml` para revisar las dependencias del proyecto.

## QA y Testing

Para cumplir con la verificacion del software se usan las siguientes herramientas

- PMD
- SonarQube
- CheckStyle

### PMD

Para ejecutar PMD en el proyecto, utilice el siguiente comando Maven:

```sh
mvn pmd:pmd ./App/main/java -R rulesets/java/quickstart.xml -f html -r report.html
```
O ejecutar PMD instalado desde su equipo

```sh
pmd -d ./App/main/java -R rulesets/java/quickstart.xml -f html -r report.html
```

### SonarQube
Para ejecutar SonarQube en el proyecto, asegúrese de tener SonarQube instalado y en ejecución p utilizar la extension de SonarQube.

## Licencia

Este proyecto está bajo la Licencia GPL - vea el archivo [LICENSE](LICENSE) para más detalles.

[Stargazers]: https://img.shields.io/github/stars/AndresFr56/GymVitae?color=fafafa&logo=github&logoColor=fff&style=for-the-badge
[License]: https://img.shields.io/github/license/AndresFr56/GymVitae?color=0a0a0a&logo=github&logoColor=fff&style=for-the-badge
