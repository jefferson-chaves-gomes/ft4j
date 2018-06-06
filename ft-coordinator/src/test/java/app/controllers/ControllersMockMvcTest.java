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
import java.util.ArrayList;

import org.springframework.test.web.servlet.MockMvc;

import app.conf.Routes;
import app.models.CloudInstance;
import app.models.Credentials;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.models.ZooInstance;

// @RunWith(SpringRunner.class)
// @WebMvcTest
public class ControllersMockMvcTest {

    //    @Autowired
    private MockMvc mock;

    private final static String STARTUP_COMMAND = "java -jar ../ft-coordinator/target/ft-coordinator-0.0.1.jar";
    private final static String ZOO_IP = "0.0.0.0";
    private final static int ZOO_PORT = 2181;
    private final ZooInstance zooInstance = new ZooInstance(ZOO_IP, ZOO_PORT, new Credentials("userZoo", "passZoo01"));
    private final Level level = new Level(STARTUP_COMMAND, this.zooInstance);
    private final ArrayList<CloudInstance> lstFakeNodes = new ArrayList<>();
    private final static String MODULE_ID = ManagementFactory.getRuntimeMXBean().getName();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public ControllersMockMvcTest() {
        super();
        this.lstFakeNodes.add(new CloudInstance("10.0.0.1", 21, new Credentials("user01", "pass01")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.2", 22, new Credentials("user02", "pass02")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.3", 23, new Credentials("user03", "pass03")));
        this.level.setModuleId(MODULE_ID);
        this.level.addTechnique(new SoftwareRejuvenation());
        this.level.addTechnique(new Retry());
        this.level.addTechnique(new TaskResubmission());
        this.level.addTechnique(new Replication(this.lstFakeNodes));
    }

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
        this.mock.perform(post(new URI(path)).contentType(APPLICATION_JSON).content(this.level.toJson()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expected)));
    }
}