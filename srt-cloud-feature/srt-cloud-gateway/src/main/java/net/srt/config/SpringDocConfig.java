package net.srt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringDoc 配置
 *
 * @author 阿沐 babamu@126.com
 */
@Configuration
public class SpringDocConfig {

    /**
     * 创建并配置OpenAPI分组。
     * 该方法通过分析路由定义来为不同的服务创建OpenAPI分组，使得在Swagger UI中可以按服务进行文档的分组展示。
     *
     * @param swaggerUiConfigParameters Swagger UI的配置参数，用于动态添加分组信息。
     * @param locator                   负责定位路由定义的服务，用于获取所有路由定义。
     * @return List<GroupedOpenApi>     OpenAPI的分组列表，每个分组代表一个服务的API集合。
     */
    @Bean
    @Lazy(false)
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters, RouteDefinitionLocator locator) {
        List<GroupedOpenApi> groups = new ArrayList<>();
        // 获取所有路由定义
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        assert definitions != null;
        // 过滤并处理每个路由定义，忽略特定的路由，为其余路由创建分组
        definitions.stream().filter(routeDefinition -> !routeDefinition.getId().equals("openapi")).forEach(routeDefinition -> {
            // 忽略特定的路由定义
            if (routeDefinition.getId().startsWith("ReactiveCompositeDiscoveryClient")) {
                return;
            }
            // 从路由定义中提取服务名，并去除路径中的"/**"和"/"
            String name = routeDefinition.getPredicates().get(0).getArgs().values().stream().findFirst().get();
            name = name.replace("/**", "").replace("/", "");
            // 向Swagger UI配置中添加新的分组
            swaggerUiConfigParameters.addGroup(name);
            // 构建并添加OpenAPI的分组配置
            GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(routeDefinition.getId()).build();
        });
        return groups;
    }


    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact= new Contact();
        contact.setName("阿沐 babamu@126.com");

        return new OpenAPI().info(new Info()
            .title("SrtCloud")
            .description( "SrtCloud")
            .contact(contact)
            .version("1.0")
            .termsOfService("https://zrxlh.top")
            .license(new License().name("MIT")
            .url("https://zrxlh.top")));
    }
}
