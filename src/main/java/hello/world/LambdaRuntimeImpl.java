package hello.world;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
@Configuration
public class LambdaRuntimeImpl {
	private static final String REQUEST_ID_HEADER = "lambda-runtime-aws-request-id";

	@Async
	@EventListener(ApplicationReadyEvent.class)
	public void run(ApplicationReadyEvent event) {

		String endpoint = System.getenv("AWS_LAMBDA_RUNTIME_API");
		String getUrl = String.format("http://%s/2018-06-01/runtime/invocation/next", endpoint);
		
		String x ="{\"key1\": \"value1\",\"key2\": \"value2\",\"key3\": \"value3\"}";
		
		RestTemplate templatea = new RestTemplate();
		HttpHeaders apiRequestHeadersa = new HttpHeaders();
		apiRequestHeadersa.setContentType(MediaType.APPLICATION_JSON);
		List<MediaType> mediaTypeLista = new ArrayList<>();
		mediaTypeLista.add(MediaType.APPLICATION_JSON);
		apiRequestHeadersa.setAccept(mediaTypeLista);
		HttpEntity<Object> apiRequesta = new HttpEntity<Object>(x,apiRequestHeadersa);
		//TODO timeout and errors
		ResponseEntity<String> apiResponsea = templatea.exchange("http://localhost:8080", HttpMethod.GET,apiRequesta,String.class);
		String apiResponseBodya = apiResponsea.getBody();
		System.out.println(apiResponseBodya);
		System.out.println("entered");
		while(true) {

			
			if(endpoint ==null) {
				return;
				
				
			}
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> getResponse = request(getUrl, template);
			HttpHeaders getResponseHeaders = getResponse.getHeaders();
			String requestId = getResponseHeaders.getFirst(REQUEST_ID_HEADER);
			String getResponseBody = getResponse.getBody();

			String apiResponseBody = getApiResponse(template, getResponseBody);

			//TODO handle error
			lambdaResponse(endpoint, template, requestId, apiResponseBody);
		}


	}

	private void lambdaResponse(String endpoint, RestTemplate template, String requestId, String apiResponseBody) {
		String responseUrl = String.format("http://%s/2018-06-01/runtime/invocation/%s/response", endpoint, requestId);
		HttpHeaders responseRequestHeaders = new HttpHeaders();
		HttpEntity<Object> responseRequestEntity = new HttpEntity<Object>(apiResponseBody, responseRequestHeaders);
		template.exchange(responseUrl,HttpMethod.POST,responseRequestEntity,Object.class);
	}

	private ResponseEntity<String> request(String getUrl, RestTemplate template) {
		HttpHeaders getHeaders = new HttpHeaders();
		HttpEntity<Object> getRequest = new HttpEntity<Object>(null, getHeaders);
		//TODO prevent timeout
		ResponseEntity<String> getResponse = template.exchange(getUrl,HttpMethod.GET,getRequest,String.class);
		return getResponse;
	}

	private String getApiResponse(RestTemplate template, String getResponseBody) {
		HttpHeaders apiRequestHeaders = new HttpHeaders();
		apiRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
		List<MediaType> mediaTypeList = new ArrayList<>();
		mediaTypeList.add(MediaType.APPLICATION_JSON);
		apiRequestHeaders.setAccept(mediaTypeList);
		HttpEntity<Object> apiRequest = new HttpEntity<Object>(getResponseBody,apiRequestHeaders);
		//TODO timeout and errors
		ResponseEntity<String> apiResponse = template.exchange("http://localhost:8080", HttpMethod.GET,apiRequest,String.class);
		String apiResponseBody = apiResponse.getBody();
		return apiResponseBody;
	}

}
