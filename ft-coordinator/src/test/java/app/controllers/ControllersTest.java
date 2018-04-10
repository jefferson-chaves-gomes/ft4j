package app.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.test.web.servlet.MockMvc;

import app.conf.Routes;

// @RunWith(SpringRunner.class)
// @WebMvcTest
public class ControllersTest {

    //    @Autowired
    private MockMvc mock;

    //    @Test
    public void imalive() throws Exception {

        final String moduleId = ManagementFactory.getRuntimeMXBean().getName();
        final String path = Routes.IMALIVE.replace("{moduleId}", moduleId);
        this.mock.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //    @Test
    public void register() throws URISyntaxException, Exception {

        final String path = Routes.REGISTER;
        final String expected = "CREATED";
        this.mock.perform(post(new URI(path)).contentType(APPLICATION_JSON).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expected)));
    }
}