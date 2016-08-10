# Configurando JNDI

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

> Escopo: _**/users**_

Caminho | Método | Parametros | Retorno
------- | ------ | ---------- | ------
**/** | _GET_ | ----- | Lista todos os [usuários](#Usuário) existentes na base de dados
**/{USER_ID}** | _GET_ | Identificador do [usuário](#Usuário) desejado | Informações básicas do [usuário](#usuário) em questão.
**/** | _POST_ | Informações do novo [usuário](#campos-do-usuário) | Informações do novo [usuário](#usuário)criado.
**/{USER_ID}** | _PUT_ | [usuário](#campos-do-usuário) | Informações atualizadas do [usuário](#usuário).
**/{USER_ID}** | _DELETE_ | Identificador do [usuário](#usuário) a ser excluído | Informação do [usuário](#usuário) removido.
**/{USER_ID}/cart** | _GET_ | *USER_ID* - Identificador do [usuário](#usuário)| Informações do [carrinho](#campos-do-usuário) do cliente
**/{USER_ID}/cart/{ITEM_ID}/{QUANTITY}** | _POST_ | *USER_ID* - Identificador do [usuário](#usuário) <BR> [*ITEM_ID*](#item) à ser alteado no [carrinho](#campos-do-usuário) <BR> *QUANTITY* - Quantidade a ser acrescida/decrescida | Informações do [carrinho](#campos-do-usuário) do [usuário](#usuário)
**/{USER_ID}/cart/{ITEM_ID}/** | _DELETE_ |  *USER_ID* - Identificador do [usuário](#usuário) <BR> [*ITEM_ID*](#item) à ser removido no [carrinho](#campos-do-usuário) | Informações do [carrinho](#campos-do-usuário) do [usuário](#usuário)
**/{USER_ID}/cart/{ITEM_ID}/{QUANTITY}** | _PUT_ |  *USER_ID* - Identificador do [usuário](#usuário) <BR> [*ITEM_ID*](#item) à ser alteado no [carrinho](#campos-do-usuário) <BR> *QUANTITY* - Quantidade a ser acrescida/decrescida | Informações do [carrinho](#campos-do-usuário) do [usuário](#usuário)

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

### Alterar um item existente
> **HOST**: _host:port/sales/api/items/16_
<BR>**METHOD**: _PUT_
<BR>**BODY**: ``` {"name":"Novo nome","image":"NOVA_IMAGEM","price":20}```

Código | Status | Exemplo de resposta 
------ | ------ | --------------------
_200_ | OK | ``` {"id":16,"name":"Novo nome","image":"NOVA_IMAGEM","price":20.0}```|
_422_ | UNPROCESSABLE_ENTITY | ``` {"price":"O preço deve ser maior que R$ 0,00!","name":"Você deve informar o nome do produto!"}```|
_404_ | NOT_FOUND | ``` ```|


