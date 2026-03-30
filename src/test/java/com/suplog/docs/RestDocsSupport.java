package com.suplog.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.UriConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
public abstract class RestDocsSupport {

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {

        // RestDocs의 URI 설정 추가
        UriConfigurer configurer = documentationConfiguration(provider)
                .uris()
                .withScheme("https")
                .withHost("api.seoplog.com")
                .withPort(443);

        // MockMvc 초기화
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
                .apply(configurer)
                .build();
    }

    protected abstract Object initController();
}
