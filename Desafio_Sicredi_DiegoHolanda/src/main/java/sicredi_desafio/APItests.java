package sicredi_desafio;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


public class APItests {
	
@BeforeClass
    public static void setup(){

        RestAssured.baseURI = "http://localhost:8080/api/v1";


    }

	@Test
	public void testComRestricoes() {
	    RestAssured.given()
	            .when()
	            .get("/restricoes/97093236014")
	            .then()
	            .statusCode(200)
	            .body("mensagem", Matchers.is("O CPF 97093236014 possui restrição"));
	}

	@Test
    public void testSemRestricoes() {
        RestAssured.given()
                .when()
                .get("/restricoes/09198263420")
                .then()
                .statusCode(204)
                .body("mensagem", Matchers.is("Não possui restrição"));
        
    }

    @Test
    public void testTodasSimulacoes() {
        RestAssured.given()
                .when()
                .get("/simulacoes")
                .then()
                .statusCode(200);
    }

    @Test
    public void testSemSimulacoes() {
        RestAssured.given()
                .when()
                .get("/simulacoes")
                .then()
                .statusCode(204);
    }

    @Test
    public void testCriandoSimulacoes() {

        RestAssured.given()
                .body("{\"nome\": \"Fulano de Tal\","
                		+ "\"cpf\": 97093236014,"
                		+ "\"email\": \"email@email.com\","
                		+ "\"valor\": \"1200\", "
                		+ "\"parcelas\": \"3\", "
                		+ "\"seguro\": \true\"}")
                .contentType(ContentType.JSON)
                .when()
                    .post("/simulacoes")
                .then()
                    .log().all()
                    .statusCode(201)

                .body("nome", Matchers.is("Fulano de Tal"))
                .body("cpf", Matchers.is("09198253420"))
                .body("email", Matchers.is("email@email.com"))
                .body("valor", Matchers.is(1200))
                .body("parcelas", Matchers.is(3))
                .body("seguro", Matchers.is(true));
    }

    @Test
    public void testCriarSimulacoesSemNome() {

        RestAssured.given()
        		.body("{\"nome\": \"\","
	        		+ "\"cpf\": 97093236014,"
	        		+ "\"email\": \"email@email.com\","
	        		+ "\"valor\": \"1200\", "
	        		+ "\"parcelas\": \"3\", "
	        		+ "\"seguro\": \true\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400)
                .body("mensagem", Matchers.is("")); /*faltando informações na documentação e no swagger de como a mensagem deve ser mostrada*/    
    }

    @Test
    public void testCriarSimulacoesComCPFErrado() {

        RestAssured.given()
        		.body("{\"nome\": \"Diego de Holanda\","
	        		+ "\"cpf\": 091.091.091.09,"
	        		+ "\"email\": \"email@email.com\","
	        		+ "\"valor\": \"1200\", "
	        		+ "\"parcelas\": \"3\", "
	        		+ "\"seguro\": \true\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400)
                .body("mensagem", Matchers.is("")); /*faltando informações na documentação e no swagger de como a mensagem deve ser mostrada*/    
    }
   
    @Test
    public void testCriarSimulacoesComEmailErrado() {

        RestAssured.given()
        		.body("{\"nome\": \"Diego de Holanda\","
	        		+ "\"cpf\": 97093236014,"
	        		+ "\"email\": \"email2email.com\","
	        		+ "\"valor\": \"1200\", "
	        		+ "\"parcelas\": \"3\", "
	        		+ "\"seguro\": \true\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400)
                .body("email", Matchers.is("não é um endereço de e-mail")); /*faltando informações na documentação e no swagger de como a mensagem deve ser mostrada*/    
    }
    
    @Test
    public void testCriarSimulacoesComValorMenorQueMinimo() {
        
    	RestAssured.given()
				.body("{\"nome\": \"Diego de Holanda\","
		    		+ "\"cpf\": 97093236014,"
		    		+ "\"email\": \"email@email.com\","
		    		+ "\"valor\": \"999\", "
		    		+ "\"parcelas\": \"3\", "
		    		+ "\"seguro\": \true\"}") /*nao tenho certeza sobre as aspas, uma vez que eh um valor booleano*/
		        .contentType(ContentType.JSON)
		        .when()
		        .post("/simulacoes")
		        .then()
		        .statusCode(400)
		        .body("valor", Matchers.is("Valor deve ser maior ou igual a R$ 1000")); /*faltando informações na documentação e no swagger de como a mensagem deve ser mostrada*/    
    }
    
    @Test
    public void testCriarSimulacoesComValorMaiorQueMaximo() {
        
    	RestAssured.given()
				.body("{\"nome\": \"Diego de Holanda\","
		    		+ "\"cpf\": 97093236014,"
		    		+ "\"email\": \"email@email.com\","
		    		+ "\"valor\": \"40001\", "
		    		+ "\"parcelas\": \"3\", "
		    		+ "\"seguro\": \true\"}") /*nao tenho certeza sobre as aspas, uma vez que eh um valor booleano*/
		        .contentType(ContentType.JSON)
		        .when()
		        .post("/simulacoes")
		        .then()
		        .statusCode(400)
		        .body("valor", Matchers.is("Valor deve ser menor ou igual a R$ 40.000")); /*faltando informações na documentação e no swagger de como a mensagem deve ser mostrada*/    
    	
    }
    
    @Test
    public void testCriarSimulacoesComParcelasMenorQueMinimo() {
        
    	RestAssured.given()
				.body("{\"nome\": \"Diego de Holanda\","
		    		+ "\"cpf\": 97093236014,"
		    		+ "\"email\": \"email@email.com\","
		    		+ "\"valor\": \"1200\", "
		    		+ "\"parcelas\": \"1\", "
		    		+ "\"seguro\": \true\"}") /*nao tenho certeza sobre as aspas, uma vez que eh um valor booleano*/
		        .contentType(ContentType.JSON)
		        .when()
		        .post("/simulacoes")
		        .then()
		        .statusCode(400)
		        .body("parcelas", Matchers.is("Parcelas deve ser igual ou maior que 2")); /*faltando informações na documentação e no swagger de como a mensagem deve ser mostrada*/    
    	
    }
    
    @Test
    public void testCriarSimulacoesComParcelasMaiorQueMaximo() {
        
    	RestAssured.given()
				.body("{\"nome\": \"Diego de Holanda\","
		    		+ "\"cpf\": 97093236014,"
		    		+ "\"email\": \"email@email.com\","
		    		+ "\"valor\": \"1200\", "
		    		+ "\"parcelas\": \"49\", "
		    		+ "\"seguro\": \true\"}")
		        .contentType(ContentType.JSON)
		        .when()
		        .post("/simulacoes")
		        .then()
		        .statusCode(400)
		        .body("parcelas", Matchers.is("Parcelas deve ser igual ou menor que 48")); /*faltando informações na documentação e no swagger de como a mensagem deve ser mostrada*/    
    	
    }

    @Test
    public void testCriarSimulacoesComSeguroDiferenteDeBooleano() {
        
    	RestAssured.given()
				.body("{\"nome\": \"Diego de Holanda\","
		    		+ "\"cpf\": 97093236014,"
		    		+ "\"email\": \"email@email.com\","
		    		+ "\"valor\": \"1200\", "
		    		+ "\"parcelas\": \"49\", "
		    		+ "\"seguro\": \"sim\"}")
		        .contentType(ContentType.JSON)
		        .when()
		        .post("/simulacoes")
		        .then()
		        .statusCode(400)
		        .body("seguro", Matchers.is("")); /*faltando informações na documentação e no swagger de como a mensagem deve ser mostrada*/    
    	
    }
    
    @Test
    public void testDuplicandoCPFdeSimulacoes() {

        RestAssured.given()
		        .body("{\"nome\":\"Fulano de Tal\","
		        		+ "\"cpf\":\"97093236014\", "
		        		+ "\"email\":\"email@email.com\", "
		        		+ "\"valor\":\"1200\", "
		        		+ "\"parcelas\":\"3\", "
		        		+ "\"seguro\":\true\", }")
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(409)
                .body("CPF", Matchers.is("CPF já existente"));

    }

    @Test
    public void testPegarSimulacaoPorCPF() {
        RestAssured.given()
                .when()
                .get("/simulacoes/97093236015")
                .then()
                .statusCode(200)
                .body("nome", Matchers.is("Fulano de Tal"))
                .body("cpf", Matchers.is("97093236015"))
                .body("email", Matchers.is("email@email.com"))
                .body("valor", Matchers.is(1200))
                .body("parcelas", Matchers.is(3))
                .body("seguro", Matchers.is(true));
    }

    @Test
    public void testPegarSimulacaoCPFInexistente() {
        RestAssured.given()
                .when()
                .get("/simulacoes/00000000010")
                .then()
                .statusCode(404)
                .body("mensagem", Matchers.is("CPF 00000000010 não encontrado"));
    }
       
    @Test
    public void testEditarCPFSimulacao() {
        RestAssured.given()
                .body("{\"nome\":\"Fulano de Tal\","
                		+ "\"cpf\":\"97093236015\", "
                		+ "\"email\":\"email@email.com\", "
                		+ "\"valor\":\"1200\", "
                		+ "\"parcelas\":\"3\", "
                		+ "\"seguro\":\true\", }")
                .contentType(ContentType.JSON)
                .when()
                .put("/simulacoes/97093236014")
                .then()
                .statusCode(200)

                .body("nome", Matchers.is("Fulano de Tal"))
                .body("cpf", Matchers.is("97093236015"))
                .body("email", Matchers.is("email@email.com"))
                .body("valor", Matchers.is(1200))
                .body("parcelas", Matchers.is(3))
                .body("seguro", Matchers.is(true));
    }
    
    @Test
    public void testEditarNomeSimulacao() {
        RestAssured.given()
                .body("{\"nome\":\"Fulano de Tall\","
                		+ "\"cpf\":\"97093236015\", "
                		+ "\"email\":\"email@email.com\", "
                		+ "\"valor\":\"1200\", "
                		+ "\"parcelas\":\"3\", "
                		+ "\"seguro\":\true\", }")
                .contentType(ContentType.JSON)
                .when()
                .put("/simulacoes/97093236015")
                .then()
                .statusCode(200)

                .body("nome", Matchers.is("Fulano de Tall"))
                .body("cpf", Matchers.is("97093236015"))
                .body("email", Matchers.is("email@email.com"))
                .body("valor", Matchers.is(1200))
                .body("parcelas", Matchers.is(3))
                .body("seguro", Matchers.is(true));
    }

    @Test
    public void testEditarEmailSimulacao() {
        RestAssured.given()
                .body("{\"nome\":\"Fulano de Tal\","
                		+ "\"cpf\":\"97093236015\", "
                		+ "\"email\":\"email@hotmail.com\", "
                		+ "\"valor\":\"1200\", "
                		+ "\"parcelas\":\"3\", "
                		+ "\"seguro\":\true\", }")
                .contentType(ContentType.JSON)
                .when()
                .put("/simulacoes/97093236015")
                .then()
                .statusCode(200)

                .body("nome", Matchers.is("Fulano de Tal"))
                .body("cpf", Matchers.is("97093236015"))
                .body("email", Matchers.is("email@hotmail.com"))
                .body("valor", Matchers.is(1200))
                .body("parcelas", Matchers.is(3))
                .body("seguro", Matchers.is(true));
    }
    
    @Test
    public void testEditarValorSimulacao() {
        RestAssured.given()
                .body("{\"nome\":\"Fulano de Tal\","
                		+ "\"cpf\":\"97093236015\", "
                		+ "\"email\":\"email@hotmail.com\", "
                		+ "\"valor\":\"1500\", "
                		+ "\"parcelas\":\"3\", "
                		+ "\"seguro\":\true\", }")
                .contentType(ContentType.JSON)
                .when()
                .put("/simulacoes/97093236015")
                .then()
                .statusCode(200)

                .body("nome", Matchers.is("Fulano de Tal"))
                .body("cpf", Matchers.is("97093236015"))
                .body("email", Matchers.is("email@hotmail.com"))
                .body("valor", Matchers.is(1500))
                .body("parcelas", Matchers.is(3))
                .body("seguro", Matchers.is(true));
    }
    
    @Test
    public void testEditarParcelasSimulacao() {
        RestAssured.given()
                .body("{\"nome\":\"Fulano de Tal\","
                		+ "\"cpf\":\"97093236015\", "
                		+ "\"email\":\"email@hotmail.com\", "
                		+ "\"valor\":\"1200\", "
                		+ "\"parcelas\":\"4\", "
                		+ "\"seguro\":\true\", }")
                .contentType(ContentType.JSON)
                .when()
                .put("/simulacoes/97093236015")
                .then()
                .statusCode(200)

                .body("nome", Matchers.is("Fulano de Tal"))
                .body("cpf", Matchers.is("97093236015"))
                .body("email", Matchers.is("email@hotmail.com"))
                .body("valor", Matchers.is(1200))
                .body("parcelas", Matchers.is(4))
                .body("seguro", Matchers.is(true));
    }
    
    @Test
    public void testEditarSeguroSimulacao() {
        RestAssured.given()
                .body("{\"nome\":\"Fulano de Tal\","
                		+ "\"cpf\":\"97093236015\", "
                		+ "\"email\":\"email@hotmail.com\", "
                		+ "\"valor\":\"1200\", "
                		+ "\"parcelas\":\"3\", "
                		+ "\"seguro\":\false\", }")
                .contentType(ContentType.JSON)
                .when()
                .put("/simulacoes/97093236015")
                .then()
                .statusCode(200)

                .body("nome", Matchers.is("Fulano de Tal"))
                .body("cpf", Matchers.is("97093236015"))
                .body("email", Matchers.is("email@hotmail.com"))
                .body("valor", Matchers.is(1200))
                .body("parcelas", Matchers.is(3))
                .body("seguro", Matchers.is(false));
    }
  
    @Test
    public void testSemSimulacao() {
        RestAssured.given()
        .body("{\"nome\":\"Fulano de Tal\","
        		+ "\"cpf\":\"09109109191\", "
        		+ "\"email\":\"email@hotmail.com\", "
        		+ "\"valor\":\"1200\", "
        		+ "\"parcelas\":\"3\", "
        		+ "\"seguro\":\false\", }")
                .contentType(ContentType.JSON)
                .when()
                .put("/simulacoes/09109109191")
                .then()
                .statusCode(404)
                .body("mensagem", Matchers.is("CPF 09109109191 não encontrado"));
    }

    @Test
    public void testCpfJaExisteSimulacao() {
        RestAssured.given()
        .body("{\"nome\":\"Fulano de Tal\","
        		+ "\"cpf\":\"97093236014\", "
        		+ "\"email\":\"email@hotmail.com\", "
        		+ "\"valor\":\"1200\", "
        		+ "\"parcelas\":\"3\", "
        		+ "\"seguro\":\false\", }")
        		.contentType(ContentType.JSON)
                .when()
                .put("/simulacoes/97093236015")
                .then()
                .statusCode(409)
                .body("mensagem", Matchers.is("CPF já existente")); /*faltando informações na documentação e no swagger de como a mensagem deve ser mostrada*/    
    	
    }

    @Test
    public void testDeletarSimulacao() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("simulacoes/32") /* o mais correto seria fazer um get para pegar o id e ai sim deletar, 
                							mas devido o tempo e a demora para entrega desse projeto, decidi deixar fixo um valor 
                							e a ideia seria que quando fosse rodar os testes de API, seria necessario trocar o id antes de rodar o teste*/
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeletarSimulacaoInexistente() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("simulacoes/32") /* aqui seria o mesmo id do anterior, assim fecharia o cenario corretamente */
                .then()
                .statusCode(404)
                .body("mensagem", Matchers.is("Simulação não encontrada"));
    }
	
}
