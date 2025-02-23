<h1 align="center">
  Insure Compare
  <br>
</h1>
<p align="center">
  <a href="https://badge.fury.io/js/electron-markdownify">
    <img src="https://github.com/Ismaelfaria/InsureCompare/blob/master/imgReadme.PNG"
         alt="Gitter">
  </a>

<h4 align="center">Uma solução backend para gestão e comparação de planos de seguro.</h4>

<p align="center">
  <a href="#key-features">Key Features</a> •
  <a href="#how-to-use">How To Use</a> •
  <a href="#credits">Credits</a> •
  <a href="#related">Related</a> •
  <a href="#license">License</a>
</p>

## Key Features

* Clientes - Gerencie informações detalhadas sobre clientes e seus planos de seguro de forma eficiente.
* Comparação de Seguros - Compare diferentes planos de seguro com base em cobertura, preço e benefícios. 
* APIs RESTful - Fornece endpoints bem estruturados para interação com outras aplicações.
* Mensageria com RabbitMQ - Processamento assíncrono para eventos como processamento de apólices.
* Persistência de Dados - Utiliza Spring Data JPA e MySQL para armazenamento e recuperação de informações.
* Docker & Docker Compose - Fácil deploy e escalabilidade com containerização.

## How To Use

Para clonar e executar esta aplicação, você precisará do [Git](https://git-scm.com), [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) e [Docker](https://www.docker.com/products/docker-desktop/) instalados em seu computador.

```bash
# Clone este repositório
$ git clone https://github.com/Ismaelfaria/InsureCompare.git

# Acesse o diretório do projeto
$ cd insure-compare

# Suba os containers do banco de dados e do RabbitMQ
$ docker-compose up -d

# Execute a aplicação
$ ./mvnw spring-boot:run

# Acesse a API no navegador ou Postman
http://localhost:8080/api/seguros
```

> **Note**
> Se você estiver utilizando Linux WSL (Windows Subsystem for Linux), siga este [guia](https://www.howtogeek.com/261575/how-to-run-graphical-linux-desktop-applications-from-windows-10s-bash-shell/) para configurar corretamente o ambiente, ou utilize o terminal do Windows para executar os comandos.

## Credits

Este software utiliza as seguintes tecnologias open-source:

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Docker](https://www.docker.com/)
- [RabbitMQ](https://www.rabbitmq.com/)
- [MySQL](https://www.mysql.com/)
- [JUnit](https://junit.org/junit5/)

## Related

[ServiceProcessingRequest](https://github.com/Ismaelfaria/ServiceProcessingRequest) - Serviço externo de processamento de apólices.

## Support

<a href="https://buymeacoffee.com/amitmerchant" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/purple_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>

<p>Or</p> 

<a href="https://www.patreon.com/amitmerchant">
	<img src="https://c5.patreon.com/external/logo/become_a_patron_button@2x.png" width="160">
</a>

## License

MIT

---

> GitHub [@Ismaelfaria](https://github.com/Ismaelfaria) &nbsp;&middot;&nbsp;


