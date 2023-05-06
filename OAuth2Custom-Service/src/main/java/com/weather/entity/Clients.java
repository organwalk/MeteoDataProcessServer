package com.weather.entity;

import lombok.Data;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;

@Data
public class Clients {
    private Integer id;
    private String clientId;
    private String secret;
    private String scope;
    private String authMethod;
    private String grantType;
    private String redirectUri;

    public static Clients from(RegisteredClient registeredClient){
        Clients clients = new Clients();

        clients.setClientId(registeredClient.getClientId());
        clients.setSecret(registeredClient.getClientSecret());

        clients.setRedirectUri(
                registeredClient.getRedirectUris().stream().findAny().get()
        );
        clients.setScope(
                registeredClient.getScopes().stream().findAny().get()
        );
        clients.setAuthMethod(
                registeredClient.getClientAuthenticationMethods().stream().findAny().get().getValue()
        );
        clients.setGrantType(
                registeredClient.getAuthorizationGrantTypes().stream().findAny().get().getValue()
        );

        return clients;
    }

    public static RegisteredClient from(Clients client) {
        return RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(client.getClientId())
                .clientSecret(client.getSecret())
                .scope(client.getScope())
                .redirectUri(client.getRedirectUri())
                .clientAuthenticationMethod(new ClientAuthenticationMethod(client.getAuthMethod()))
                .authorizationGrantType(new AuthorizationGrantType(client.getGrantType()))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenSettings(TokenSettings.builder()
//            .accessTokenFormat(OAuth2TokenFormat.REFERENCE) // opaque
                        .accessTokenTimeToLive(Duration.ofHours(12)).build())
                .build();
    }
}
