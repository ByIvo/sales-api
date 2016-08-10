# Configurando JNDI para acesso ao Banco de dados MySQL

> No seu arquivo context.xml (do servidor utilizado), adicione as seguintes linhas:
```xml
<Resource name="jdbc/salesApiDs" 
              auth="Container" 
              type="javax.sql.DataSource"
              maxActive="100" 
              maxIdle="30" 
              maxWait="10000"
              username="DB_USERNAME" 
              password="DB_PASSWORD" 
              driverClassName="com.mysql.jdbc.Driver"
              url="jdbc:mysql://DB_HOST:DB_PORT/DB_NAME"/>
```

> Substitua os parâmetros iniciados por **'DB_'** de acordo com o sugerido.

# Configurando JNDI para autenticação do Java Mail junto ao GMAIL

> No seu arquivo context.xml (do servidor utilizado), adicione as seguintes linhas:
```xml
<Resource name="mail/Session" auth="Container"
        type="javax.mail.Session"
        mail.smtp.user="GMAIL_USERNAME"
        password="GMAIL_PASSWORD"
        mail.debug="false"
        mail.transport.protocol="smtp"
        mail.smtp.host= "smtp.gmail.com"
        mail.smtp.socketFactory.port="465"
        mail.smtp.socketFactory.class="javax.net.ssl.SSLSocketFactory"
        mail.smtp.socketFactory.fallback="false"
        mail.smtp.auth= "true"
        mail.smtp.port= "465"
        mail.smtp.starttls.enable="true"
        description="Global E-Mail Resource"/>
```

> Substitua os parâmetros **GMAIL_USERNAME** e **GMAIL_PASSWORD** por uma conta de Gmail válida, que será remetente dos e-mails enviados; Você pode configurar os parâmetros para funcionar em outros servidores.

# Criando a base de dados

> Acesse o mysql
```bash
 mysql -u USER -p
 ****
```

> execute o comando passando o caminho para o [ARQUIVO DE CRIAÇÃO](https://github.com/ByIvo/sales-api/blob/master/src/main/resources/creation.sql) do banco de dados.

```bash
 source PATH/TO/CREATION.SQL
```

# Sales API

1. [Usuario] (#usuário)
2. [Item] (#item)

#Usuário#

##Campos do usuário##

Campo | Tipo | Not Null | Tamanho | Formato
----- | ---- | -------- | ------- | -------
[**id**](#id) | Integer | [x] | |
[**name**](#name)  | STRING | [x] | 80 |
[**email**](#email)  | STRING | [x] | 100 | Formato padrão de e-mail
[**password**](#password)  | STRING | [x] | 512 |
[**confirmPassword**](#confirmPassword)  | STRING | [ ] | |
[**cart**](#cart)  | LIST | [ ] |  |

###id###
Identificador numérico sequencial do usuário

###name###
Nome do usuário; Não pode ser nullo, vazio ou maior que 80 caracteres.

###email###
E-mail do usuário; Não pode ser nullo, vazio, maior que 100 caracteres ou fora do padrão aceito:
```
"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
```
###password###
Hash com a senha do usuário. Não é retornada nos serviços de busca.

###confirmPassword###
Usado para confirmar a digitação da senha do usuário durante seu primeiro cadastro OU alteração.

###cart###
Carrinho de compras que armazena os produtos desejados e a quantidade individual da futura compra

## Gerenciamento via serviços ##

> Escopo: _**/users**_

Caminho | Método | Parametros | Retorno
------- | ------ | ---------- | ------
**/** | _POST_ | Informações do novo [usuário](#campos-do-usuário) | Informações do novo [usuário](#usuário)criado. Esse serviço tem por utilidade a criação por iniciativa própria do usuário.

##Exemplos de chamadas##

### Criar um novo Usuário
> **HOST**: _host:port/sales/api/users/_
<BR>**METHOD**: _POST_
<BR>**BODY**: ``` {"name":"novo nome", email: "mail@mail.com", password:"password", confirmPassword: "password"}```

Código | Status | Exemplo de resposta 
------ | ------ | --------------------
_200_ | OK | ``` {"id":7,"name":"novo nome","email":"mail@mail.test","password":null,"confirmPassword":null,"cart":[]}```|
_422_ | UNPROCESSABLE_ENTITY | ``` {"email":"Você deve informar seu email!","name":"Você deve informar seu nome!","password":"Informe uma senha"}```|

### Errors existentes para o código 422

> Campo NOME vazio -> name: "Você deve informar seu nome!"; 
> Campo NOME extrapolado -> name: "Seu nome deve conter no máximo 80 caracteres!"; 

> Campo EMAIL vazio -> email: "Você deve informar seu email!"; 
> Campo EMAIL extrapolado -> email: "Seu e-mail deve contern o máximo 100 caracteres!"; 
> Campo EMAIL inválido -> email: "Você deve informar um e-mail válido!"; 
> Campo EMAIL duplicado -> email: "O e-mail escolhido já está registrado!"; 

> Campo SENHA vazio -> password: "Informe uma senha";
> Campo SENHA e CONFIRMAÇÃO dispares -> password: "As senhas não conferem";
> Erro desconhecido ao criptografar a senha -> password: "Não foi possível criptografar sua senha!";


#Item#

##Campos dos items##

Campo | Tipo | Not Null | Tamanho | Formato
----- | ---- | -------- | ------- | -------
[**id**](#id) | Integer | [x] | |
[**name**](#item-name)  | STRING | [x] | 80 |
[**price**](#price)  | DOUBLE | [x] |  |
[**image**](#image)  | STRING | [] | 512 |

###id###
Identificador numérico sequencial do item

###name###
Nome do item; Não pode ser nullo, vazio ou maior que 80 caracteres.

###price###
O preço do item não pode ser menor que R$ 0,00.

###image###
A única restrição do campo imagem é não ter mais de 512 caracteres.

## Gerenciamento via serviços ##

> Escopo: _**/items**_

Caminho | Método | Parametros | Retorno
------- | ------ | ---------- | ------
**/** | _GET_ | ----- | Lista todos os [itens](#item) existentes na base de dados.
**/{ITEM_ID}** | _GET_ | ----- | Busca um [itens](#item) na base de dados pelo seu identificador.
**/** | _POST_ | Informações do novo [item](#item) | Cria um novo [item](#item) na base de dados
**/{ITEM_ID}** | _PUT_ | *ITEM_ID* - Identificador do [item](#item) na base de dados <BR> Novas informações contindas em um [*ITEM*](#item), que serão atualizadas | Atualiza o conteúdo do [item](#item) de identificador *ITEM_ID* com o conteúdo do [item](#item) passado no corpo da requisição

##Exemplos de chamadas##

### Listar todos os itens
> **HOST**: _host:port/sales/api/items/_
<BR>**METHOD**: _GET_
<BR>**BODY**: _VAZIO_

Código | Status | Exemplo de resposta 
------ | ------ | --------------------
_200_ | OK | ``` [{"id":12,"name":"calculadora","image":"","price":100.0},{"id":13,"name":"pneu","image":"","price":100.0},{"id":14,"name":"teclado","image":"","price":102.0},{"id":15,"name":"celular","image":"","price":0.01}]```|
_500_ | INTERNAL_SERVER_ERROR | ``` {}```|

### Buscar por ID
> **HOST**: _host:port/sales/api/items/12_
<BR>**METHOD**: _GET_
<BR>**BODY**: _VAZIO_

Código | Status | Exemplo de resposta 
------ | ------ | --------------------
_200_ | OK | ``` {"id":12,"name":"calculadora","image":"","price":100.0}```|
_404_ | NOT_FOUND | ``` {}```|

### Criar novo Item
> **HOST**: _host:port/sales/api/items/_
<BR>**METHOD**: _POST_
<BR>**BODY**: ``` {"name":"Cabo de rede","image":"URL_TO_IMG","price":10}```

Código | Status | Exemplo de resposta 
------ | ------ | --------------------
_201_ | CREATED | ``` {"id":16,"name":"Cabo de rede","image":"URL_TO_IMG","price":10.0}```|
_422_ | UNPROCESSABLE_ENTITY | ``` {"price":"O preço deve ser maior que R$ 0,00!","name":"Você deve informar o nome do produto!"}```|

### Errors existentes para o código 422

> Campo NOME vazio -> name: "Você deve informar o nome do produto!"; 
> Campo NOME extrapolado -> name: "O nome do produto deve conter no máximo 80 caracteres!"; 

> Campo LINK extrapolado -> image: "O link informado é muito grande!"

> Campo PREÇO inválido ou menor que 0 -> price: "O preço deve ser maior que R$ 0,00!";


### Alterar um item existente
> **HOST**: _host:port/sales/api/items/16_
<BR>**METHOD**: _PUT_
<BR>**BODY**: ``` {"name":"Novo nome","image":"NOVA_IMAGEM","price":20}```

Código | Status | Exemplo de resposta 
------ | ------ | --------------------
_200_ | OK | ``` {"id":16,"name":"Novo nome","image":"NOVA_IMAGEM","price":20.0}```|
_422_ | UNPROCESSABLE_ENTITY | ``` {"price":"O preço deve ser maior que R$ 0,00!","name":"Você deve informar o nome do produto!"}```|
_404_ | NOT_FOUND | ``` ```|


