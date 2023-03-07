DESAFIO AUTOMAÇÃO DE TESTES DE API
SICREDI

DESCRIÇÃO DE TODOS OS ENDPOINTS, SEUS STATUS CODE E SUAS VALIDAÇÕES.


ENDPOINT DE RESTRIÇÃO:

	Endpoint: api/v1/restricoes/{cpf}
	Status Code: 200 + mensagem no body: “O CPF {cpf} possui restrição”
	Status Code: 204 + mensagem no body: “Não possui restrição” (de acordo com o swagger existe essa mensagem, contudo de acordo com a documentação, não existe tal mensagem, seria algo para ser discutido)

	Resultados:
		Status Code: 200
		Response: Body: “O CPF {cpf} tem problema”
		Issue: De acordo com a documentação e também com o response no swagger, a mensagem no body deveria ser: “O CPF {cpf} possui restrição”.

		Status Code: 204
		Response: Header: timestamp
		Issue: É retornado o header com o timestamp, em vez de retornar a mensagem de não possuir restrição ou o que for acordado após discutido sobre o conflito entre a documentação e o swagger.


ENDPOINT DE SIMULAÇÃO:
	
	
“GET ALL” Simulações
	Endpoint: “api/v1/simulacoes”
	Status Code: 200 + JSON no body com todas as simulações existentes
	Status Code: 204 (não existe no swagger esse Status Code e de acordo com a documentação, não existe nenhuma mensagem atrelada a esse statuscode, seria algo para ser discutido)

	Resultados:
		Status Code: 200
		Response: Body: JSON com todas as simulações existentes
		Issue: De acordo com o response, dentro de cada simulação não deve aparecer o id, contudo além dos dados, atualmente também está aparecendo o id em cada simulação. E não temos nenhuma informação de como deve ser retornado esse endpoint pela documentação, seria algo a ser inserido na documentação.

		Issue2: Não é possível acessar o Status Code 204, pois quando não há simulações a API retorna uma lista vazia “[]” e o Status Code 200.





“POST” Simulações
Endpoint: “api/v1/simulacoes”
Status Code: 201 + JSON no body com as informações que foi inserida da nova simulação.
Status Code: 400 + Mensagem: “Falta de informações” (de acordo com o swagger) e 400 + lista de erros (de acordo com a documentação, logo mais um conflito encontrado entre o Swagger e a documentação) 
Status Code: 409 + Mensagem: “CPF já existente”.
	
Resultados: 
	Status Code: 201
	Response: Body: É retornado um JSON com todas informações inseridas na criação da simulação.
	Issue: De acordo com o Swagger, no JSON não deve aparecer o id que a simulação tem, contudo no nosso response, é retornado também o campo id.

	Tentativa de retornar erro 400 por falta de informações no nome (“nome”: “”,)
	Status Code: 201
	Response: Body: É retornado um JSON com todas informações inseridas na criação da simulação.
	Issue: Além do Status Code estar errado e ter conseguido criar a simulação sem um nome. Uma vez que o nome está vazio, deveria retornar a lista de erros, informando que o nome está vazio OU deveria retornar a mensagem de “Falta de informações”.

	Tentativa de retornar erro 400 por falta de informações no nome (“nome”: ,)
	Status Code: 400
	Response: Header: informando que a connection foi fechada, que o content-length é 0 e o timestamp.
	Issue: No response deveria ser retornado a lista de erro, informando que o nome está vazio.

	Tentativa de retornar erro 400 por falta de informações no CPF (“cpf”: “970.932.360-14”)
	Status Code: 400
	Response: Header: informando que a connection foi fechada, que o content-length é 0 e o timestamp.
	Issue: No response deveria ser retornado a lista de erro, informando que o CPF está digitado de forma errada.

	Tentativa de retornar erro 400 por falta de informações no email (“email”: “email2email.com”)
	Status Code: 400
	Response: Body: {“erros”: {“email”: “não é um endereço de e-mail”}}
	Issue: Se for para informar que está faltando informações, segundo o swagger, temos uma issue, contudo o response está seguindo o que a documentação pede.

	






Tentativa de retornar erro 400 pelo valor estar fora do range (>= R$1.000 <= R$40.000) segundo a documentação (“valor”: “999” e “valor”: “40001”) 
	Status Code: 201
	Response: Body: JSON com as informações que acabaram de ser inseridas.
	Issue: Range de >= 1000 não funcionando, onde o valor inserido foi de 999 e o response deveria ter sido a lista de erros informando o range do valor e/ou a mensagem de “Falta de informações” segundo o swagger.

	Status Code: 400
	Response: Body: {“erros”: {“valor”: “Valor deve ser menor ou igual a R$ 40.000”}}
	Issue: Erro apenas no range do valor, onde não existe somente a regra de menor ou igual ao valor máximo.

	Tentativa de retornar erro 400 pelo números de parcelas estar fora do range (>= 2 e <= 48) segundo a documentação (“parcelas”: “1” e “parcelas”: “49”)
	Status Code: 400
	Response: Body: {“erros”: {“parcelas”: “Parcelas deve ser igual ou maior que 2”}}
	Issue: Erro apenas no range das parcelas, onde existe um valor máximo que é de 48, contudo na mensagem não fala sobre.

	Status Code: 201
	Response: Body: JSON com as informações que acabaram de ser inseridas.
	Issue: Range de <= 48 não funcionando, onde o número de parcelas inserido foi de 49 e o response deveria ter sido a lista de erros informando o range das parcelas e/ou a mensagem de “Falta de informações” segundo o swagger.


	Tentativa de retornar erro 400 quando o seguro não retornar um valor booleano (“seguro”: “sim”)
	Status Code: 400
	Response: Header: informando que a connection foi fechada, que o content-length é 0 e o timestamp.
	Issue: No response deveria ser retornado a lista de erro, informando que o seguro está digitado de forma errada e deve ser um valor booleano.
	Tentativa de retornar erro 400 quando email, valor e parcelar estão fora da regra (“email”: “email2email.com”, “valor”: “40001” e “parcelas”: “1”)
	Status Code: 400
	Response: Body: {“erros”: {“parcelas”: “Parcelas deve ser igual ou maior que 2”, “valor”: “Valor deve ser menor ou igual a R$ 40.000”, “email”: “não é um endereço de e-mail”}}
	Issue: Além das reportadas anteriormente por conta das regras, uma nova mensagem é encontrada na validação do email, onde difere quando apenas o email está fora da regra. 
	Tentativa de retornar erro 409 quando o CPF já existe.
	Status Code: 409 (não é possível acessar ao Status Code)
	Response: Body: “mensagem”: “CPF duplicado”
	Issue: Status Code retornado é 400 e a mensagem no response deveria ser: “CPF já existente”.

	
	


“GET” simulação específica by CPF.
Endpoint: api/v1/simulacoes/{cpf}
Status Code: 200
Status Code: 404

Resultados:
	Status Code: 200
	Response: Body: JSON com as informações da simulação do CPF buscado.
	Issue: Como citado anteriormente, segundo o Swagger, não deve ser retornado o id da simulação, contudo está sendo retornado.

	Status Code: 404
	Response: Body: {“mensagem” : “CPF 09198253420 não encontrado”}
	Issue: Segundo o swagger, quando não é encontrada uma simulação, é para ser retornada a mensagem: “O CPF 09198253420 possui restrição” e de acordo com a documentação não há nenhuma mensagem a ser retornada. Algo a ser discutido, pois há mais um conflito entre swagger e documentação e mensagem diferente da esperada.

“PUT” simulação específica by CPF + JSON com as informações que serão atualizadas.
	Endpoint: api/v1/simulacoes
	Status Code: 200
	Status Code: 404
	Status Code: 409 (segundo o swagger existe esse status code, segundo a documentação não existe esse status code)

	Resultados:
		Tentativa de retornar status code 200, ao alterar o valor no json.
		Status Code: 200
		Response: Body: JSON com as informações da simulação
		Issue: Além da issue já conhecida do id está sendo retornado junto às demais informações no JSON, também é possível identificar que o valor da simulação não foi alterado.
		
Tentativa de retornar status code 200, ao alterar o as parcelas no json.
		Status Code: 200
		Response: Body: JSON com as informações da simulação
		Issue: Além da issue já conhecida do id está sendo retornado junto às demais informações no JSON, não foi possível identificar nenhuma outra issue.

		Tentativa de retornar status code 200, ao alterar o email no json.
		Status Code: 200
		Response: Body: JSON com as informações da simulação
		Issue: Além da issue já conhecida do id está sendo retornado junto às demais informações no JSON, não foi possível identificar nenhuma outra issue.

		Tentativa de retornar status code 200, ao alterar o nome no json.
		Status Code: 200
		Response: Body: JSON com as informações da simulação
		Issue: Além da issue já conhecida do id está sendo retornado junto às demais informações no JSON, não foi possível identificar nenhuma outra issue.	

		


Tentativa de retornar status code 200, ao alterar o cpf no json.
		Status Code: 200
		Response: Body: JSON com as informações da simulação
		Issue: Além da issue já conhecida do id está sendo retornado junto às demais informações no JSON, não foi possível identificar nenhuma outra issue.	
		
		Tentativa de retornar status code 200, ao alterar o seguro no json.
		Status Code: 200
		Response: Body: JSON com as informações da simulação
		Issue: Além da issue já conhecida do id está sendo retornado junto às demais informações no JSON, não foi possível identificar nenhuma outra issue.

		Tentativa de retornar erro 404, utilizando um CPF que não existe na base de dados (“cpf”: “09189767320”)
		Status Code: 400
		Response: Header: informando que a connection foi fechada, que o content-length é 0 e o timestamp.
		Issue: Deveria ser retornado o Status Code 404 e, de acordo com a documentação, a mensagem: “CPF não encontrado” e de acordo com o swagger a mensagem seria “O CPF 09189767320 possui restrição”, logo mais um conflito de mensagens entre o Swagger e a documentação.


		Tentativa de retornar erro 409, trocando o CPF de uma simulação para o mesmo CPF de outra simulação.	
		Status Code: 400
		Response: Body: {“mensagem”: “CPF duplicado”}
		Issue: Segundo o swagger, quando isso acontecer, o status code deve ser 409 e a mensagem deve ser “CPF já existente”.

“DELETE” Simulações by id
	Endpoint: “api/v1/simulacoes/{id}” 
	Status Code: 204
	Body: De acordo com o swagger, deve retornar a mensagem: “Simulação removida com sucesso” e de acordo com a documentação apenas o Status Code é retornado.
	Resultados:
		Status Code: 200
		Response: Body: Mensagem com apenas “OK”
		Issue: De acordo com a documentação, o Status Code deve ser o 204 e não o 200. E de acordo com o swagger, deve ser retornada também a mensagem: “Simulação removida com sucesso”.

		Issue2: Não é possível acessar o Status Code 404, pois mesmo que uma simulação não exista, a API continua retornando o Status Code 200 e a mensagem de OK. 
