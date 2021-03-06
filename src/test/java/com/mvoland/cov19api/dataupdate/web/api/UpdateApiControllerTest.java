package com.mvoland.cov19api.dataupdate.web.api;

import com.mvoland.cov19api.AbstractControllerTest;
import com.mvoland.cov19api.covidstat.locality.web.assembler.DepartmentModelAssembler;
import com.mvoland.cov19api.covidstat.locality.web.assembler.RegionModelAssembler;
import com.mvoland.cov19api.dataupdate.data.UpdateRequest;
import com.mvoland.cov19api.dataupdate.service.UpdateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;

import java.time.LocalDate;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UpdateApiController.class)
@ExtendWith(value = {RestDocumentationExtension.class})
@Import({RegionModelAssembler.class, DepartmentModelAssembler.class})
class UpdateApiControllerTest extends AbstractControllerTest {

    @MockBean
    private UpdateService updateService;

    @Test
    void requestUpdateDays() throws Exception {
        Mockito.when(updateService.requestUpdateSince(LocalDate.now().minusDays(3)))
                .thenReturn(UpdateRequest.accepted());

        mockMvc.perform(get("/api/update/days")
                .param("days", "3")
        )
                .andExpect(status().isOk())
                .andDo(document("update/days",
                        requestParameters(
                                parameterWithName("days").description("Fetch data <days> before today")),
                        responseFields(
                                fieldWithPath("accepted").description("The update request was accepted"),
                                fieldWithPath("acceptedStartTime").description("Start time of update")
                        )));
    }

    @Test
    void requestAutoUpdate() throws Exception {
        Mockito.when(updateService.requestAutoUpdate())
                .thenReturn(UpdateRequest.accepted());

        mockMvc.perform(get("/api/update/auto")
                .param("days", "3")
        )
                .andExpect(status().isOk())
                .andDo(document("update/auto",
                        responseFields(
                                fieldWithPath("accepted").description("The update request was accepted"),
                                fieldWithPath("acceptedStartTime").description("Start time of update")
                        )));
    }

    @Test
    void requestFullUpdate_rejected() throws Exception {
        Mockito.when(updateService.requestFullUpdate())
                .thenReturn(UpdateRequest.rejected("Some reason"));

        mockMvc.perform(get("/api/update/full"))
                .andExpect(status().isOk())
                .andDo(document("update/full-rejected",
                        responseFields(
                                fieldWithPath("accepted").description("The update request was rejected"),
                                fieldWithPath("rejectionCause").description("Reason, if rejected")
                        )));
    }

    @Test
    void requestFullUpdate_accepted() throws Exception {
        Mockito.when(updateService.requestFullUpdate())
                .thenReturn(UpdateRequest.accepted());

        mockMvc.perform(get("/api/update/full"))
                .andExpect(status().isOk())
                .andDo(document("update/full-accepted",
                        responseFields(
                                fieldWithPath("accepted").description("The update request was accepted"),
                                fieldWithPath("acceptedStartTime").description("Start time of update")
                        )));
    }
}