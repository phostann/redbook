package cc.phos.properties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
@Data
@RequiredArgsConstructor
public class AuthProperties {
    private String[] whiteList;
}
