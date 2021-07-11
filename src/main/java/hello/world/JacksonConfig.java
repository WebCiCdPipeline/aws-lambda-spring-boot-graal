package hello.world;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JacksonConfig implements WebMvcConfigurer {
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(0,getConverter());
	}
	@Bean
    public HttpMessageConverter<Object> getConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
       List<MediaType> mediaTypes = new ArrayList<>();
       mediaTypes.add(MediaType.APPLICATION_JSON);
       converter.setSupportedMediaTypes(mediaTypes);
       ObjectMapper mapper = new ObjectMapper();
       converter.setObjectMapper(mapper);
       mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
       

        return converter;
    }
}