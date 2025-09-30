package br.edu.iff.ccc.webappigormonteiro.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.MethodAttributes;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OperationService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponses;

/**
 * Temporary workaround for springdoc incompatibility with Spring Framework 6.2.x.
 *
 * <p>Spring Framework 6.2 changed the signature of {@code ControllerAdviceBean}'s
 * constructors. At the time of writing, springdoc still calls the old single-argument
 * constructor, which triggers a {@link NoSuchMethodError} when the OpenAPI document
 * is generated. By wrapping the default {@link GenericResponseService} we can catch
 * that error, log it once per intercepted call, and fall back to returning the existing
 * operation responses so that the documentation endpoint keeps working.</p>
 */
@Configuration
public class SpringDocCompatibilityConfig {

    private static final Logger log = LoggerFactory.getLogger(SpringDocCompatibilityConfig.class);

    @Bean
    @Primary
    GenericResponseService genericResponseService(OperationService operationService,
                                                   List<ReturnTypeParser> returnTypeParsers,
                                                   SpringDocConfigProperties springDocConfigProperties,
                                                   PropertyResolverUtils propertyResolverUtils) {
        return new GenericResponseService(operationService, returnTypeParsers, springDocConfigProperties, propertyResolverUtils) {

            private volatile boolean warned;

            @Override
            public ApiResponses build(Components components,
                                      HandlerMethod handlerMethod,
                                      Operation operation,
                                      MethodAttributes methodAttributes) {
                try {
                    return super.build(components, handlerMethod, operation, methodAttributes);
                } catch (NoSuchMethodError ex) {
                    if (!warned) {
                        warned = true;
                        log.warn("springdoc compatibility: falling back to default responses because of {}", ex.toString());
                    }
                    ApiResponses fallback = operation != null ? operation.getResponses() : null;
                    return fallback != null ? fallback : new ApiResponses();
                }
            }
        };
    }
}
