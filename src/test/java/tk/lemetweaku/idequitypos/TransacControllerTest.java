package tk.lemetweaku.idequitypos;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tk.lemetweaku.idequitypos.controller.TransactionController;

public class TransacControllerTest extends BaseSpringBootTest{
    @Autowired
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void orderTest() throws Exception {

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/order").contentType(MediaType.APPLICATION_JSON)
                        .param("tradeID", "1")
                        .param("version", "1")
                        .param("quantity", "50")
                        .param("securityCode", "REL")
                        .param("command", "INSERT")
                        .param("tradeMark", "Buy"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        logger.info(mvcResult.getResponse().getContentAsString());

    }

    @Test
    public void getAllPositions() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getRTPositions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        logger.info(mvcResult.getResponse().getContentAsString());
    }


}
