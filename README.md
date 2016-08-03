# Sales API

1. [Usuario] (#usuário)

#Usuário#

##Campos do usuário##

Campo | Tipo | Not Null | Tamanho | Formato
----- | ---- | -------- | ------- | -------
[**id**](#id) | Integer | [x] | |
[**name**](#id)  | STRING | [x] | 80 |
[**email**](#id)  | STRING | [x] | 100 | Formato padrão de e-mail
[**password**](#id)  | STRING | [x] | 512 |
[**confirmPassword**](#id)  | STRING | [ ] | |
[**cart**](#id)  | LIST | [ ] |  |

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

> Escopo: _**/user**_

Caminho | Método | Parametros | Retorno
------- | ------ | ---------- | ------
**/** | _GET_ | ----- | Lista todos os [usuários](#Usuário) existentes na base de dados
**/{USER_ID}** | _GET_ | Identificador do [usuário](#Usuário) desejado | Informações básicas do [usuário](#usuário) em questão.
**/** | _POST_ | Informações do novo [usuário](#campos-do-usuário) | Informações do novo [usuário](#usuário)criado.
**/{USER_ID}** | _PUT_ | [usuário](#campos-do-usuário) | Informações atualizadas do [usuário](#usuário).
**/{USER_ID}** | _DELETE_ | Identificador do [usuário](#usuário) a ser excluído | Informação do [usuário](#usuário) removido.
**/{USER_ID}/cart** | _GET_ | *USER_ID* - Identificador do [usuário](#usuário)| Informações do [carrinho](#campos-do-usuário) do cliente
**/{USER_ID}/cart/{ITEM_ID}/** | _POST_ | *USER_ID* - Identificador do [usuário](#usuário) <BR> [*ITEM_ID*](#item) à ser adicionado no [carrinho](#campos-do-usuário) | Informações do [carrinho](#campos-do-usuário) do [usuário](#usuário)
**/{USER_ID}/cart/{ITEM_ID}/** | _DELETE_ |  *USER_ID* - Identificador do [usuário](#usuário) <BR> [*ITEM_ID*](#item) à ser removido no [carrinho](#campos-do-usuário) | Informações do [carrinho](#campos-do-usuário) do [usuário](#usuário)
**/{USER_ID}/cart/{ITEM_ID}/{QUANTITY}** | _PUT_ |  *USER_ID* - Identificador do [usuário](#usuário) <BR> [*ITEM_ID*](#item) à ser alteado no [carrinho](#campos-do-usuário) <BR> *QUANTITY* - Quantidade a ser acrescida/decrescida | Informações do [carrinho](#campos-do-usuário) do [usuário](#usuário)
