package com.prgms.kokoahairshop.designer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgms.kokoahairshop.designer.dto.CreateDesignerRequest;
import com.prgms.kokoahairshop.designer.dto.DesignerResponse;
import com.prgms.kokoahairshop.designer.dto.ModifyDesignerRequest;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.designer.service.DesignerService;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@DisplayName("헤어샵 CRUD API 테스트")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class DesignerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DesignerService designerService;

    @Autowired
    private DesignerRepository designerRepository;

    @Autowired
    private HairshopRepository hairshopRepository;

    private Hairshop hairshop;
    private DesignerResponse designerResponse;

    @BeforeEach
    void setup() throws NotFoundException {
        hairshop = Hairshop.builder()
                .name("데브헤어")
                .phoneNumber("010-1234-1234")
                .startTime("11:00")
                .endTime("20:00")
                .closedDay("화")
                .reservationRange("1")
                .reservationStartTime("11:00")
                .reservationEndTime("19:30")
                .sameDayAvailable(true)
                .roadNameNumber("대구 중구 동성로2가 141-9 2층3층")
                .profileImg("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .introduction("시간 여유 충분히 가지고 여유롭게 와주시면 감사하겠습니다 :)")
                .userId(1L)
                .build();
        hairshopRepository.save(hairshop);
        CreateDesignerRequest createDesignerRequest = CreateDesignerRequest.builder()
                .name("나그맨")
                .image("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .introduction("안녕하세요.")
                .position(Position.DESIGNER.getPosition())
                .hairshopId(hairshop.getId())
                .build();
        designerResponse = designerService.insert(createDesignerRequest);
    }

    @AfterEach
    void tearDown() {
        designerRepository.deleteAll();
        hairshopRepository.deleteAll();
    }

    @Test
    @DisplayName("디자이너 등록 테스트")
    void DESIGNER_INSERT_TEST() throws Exception {
        CreateDesignerRequest request = CreateDesignerRequest.builder()
                .name("데브")
                .image("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .introduction("안녕하세요.")
                .position(Position.MANAGER.getPosition())
                .hairshopId(hairshop.getId())
                .build();
        this.mockMvc.perform(post("/designers")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andDo(
                        document("register-designer",
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                        fieldWithPath("image").type(JsonFieldType.STRING).description("phoneNumber"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("startTime"),
                                        fieldWithPath("position").type(JsonFieldType.STRING).description("endTime"),
                                        fieldWithPath("hairshopId").type(JsonFieldType.NUMBER).description("closedDay")
                                )
                        ));
    }

    @Test
    @DisplayName("전체 디자이너 조회 테스트")
    void GET_DESIGNER_LIST_TEST() throws Exception {
        this.mockMvc.perform(get("/designers")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andDo(document("getAll-designer",
                        responseFields(
                                fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("content[]"),
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("content[].id"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("content[].name"),
                                fieldWithPath("content[].image").type(JsonFieldType.STRING).description("content[].image"),
                                fieldWithPath("content[].introduction").type(JsonFieldType.STRING).description("content[].introduction"),
                                fieldWithPath("content[].position").type(JsonFieldType.STRING).description("content[].position"),
                                fieldWithPath("content[].hairshopId").type(JsonFieldType.NUMBER).description("content[].hairshopId"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("content[].createdAt"),
                                fieldWithPath("content[].updatedAt").type(JsonFieldType.STRING).description("content[].updatedAt"),
                                fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("pageable"),
                                fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("pageable.sort"),
                                fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("pageable.sort.empty"),
                                fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("pageable.sort.sorted"),
                                fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("pageable.sort.unsorted"),
                                fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("pageable.offset"),
                                fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("pageable.pageNumber"),
                                fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("pageable.pageSize"),
                                fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("pageable.paged"),
                                fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("pageable.unpaged"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("last"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("totalPages"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("totalElements"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("size"),
                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("number"),
                                fieldWithPath("sort").type(JsonFieldType.OBJECT).description("sort"),
                                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("sort.empty"),
                                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("sort.sorted"),
                                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("sort.unsorted"),
                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("first"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("numberOfElements"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("empty")
                        )
                ));
    }

    @Test
    @DisplayName("헤어샵 아이디로 디자이너 조회 테스트")
    void GET_DESIGNER_BY_HAIRSHOP_ID_TEST() throws Exception {
        this.mockMvc.perform(get("/designers/hairshop/{id}", designerResponse.getId())
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andDo(document("getByHairshopId-designer",
                        responseFields(
                                fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("content[]"),
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("content[].id"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("content[].name"),
                                fieldWithPath("content[].image").type(JsonFieldType.STRING).description("content[].image"),
                                fieldWithPath("content[].introduction").type(JsonFieldType.STRING).description("content[].introduction"),
                                fieldWithPath("content[].position").type(JsonFieldType.STRING).description("content[].position"),
                                fieldWithPath("content[].hairshopId").type(JsonFieldType.NUMBER).description("content[].hairshopId"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("content[].createdAt"),
                                fieldWithPath("content[].updatedAt").type(JsonFieldType.STRING).description("content[].updatedAt"),
                                fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("pageable"),
                                fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("pageable.sort"),
                                fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("pageable.sort.empty"),
                                fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("pageable.sort.sorted"),
                                fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("pageable.sort.unsorted"),
                                fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("pageable.offset"),
                                fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("pageable.pageNumber"),
                                fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("pageable.pageSize"),
                                fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("pageable.paged"),
                                fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("pageable.unpaged"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("last"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("totalPages"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("totalElements"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("size"),
                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("number"),
                                fieldWithPath("sort").type(JsonFieldType.OBJECT).description("sort"),
                                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("sort.empty"),
                                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("sort.sorted"),
                                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("sort.unsorted"),
                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("first"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("numberOfElements"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("empty")
                        )
                ));
    }

    @Test
    @DisplayName("디자이너 아이디로 디자이너 조회 테스트")
    void GET_DESIGNER_BY_ID_TEST() throws Exception {
        this.mockMvc.perform(get("/designers/{id}", designerResponse.getId())
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(jsonPath("$.id").value(designerResponse.getId()))
                .andExpect(jsonPath("$.name").value(designerResponse.getName()))
                .andExpect(jsonPath("$.image").value(designerResponse.getImage()))
                .andExpect(jsonPath("$.introduction").value(designerResponse.getIntroduction()))
                .andExpect(jsonPath("$.position").value(designerResponse.getPosition()))
                .andExpect(jsonPath("$.hairshopId").value(designerResponse.getHairshopId()))
                .andDo(document("getById-designer",
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("image"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("introduction"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("position"),
                                fieldWithPath("hairshopId").type(JsonFieldType.NUMBER).description("hairshopId"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("createdAt"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("updatedAt")
                        )
                ));
    }

    @Test
    @DisplayName("해당 아이디의 디자이너가 없을 경우 테스트")
    void GET_HAIRSHOP_BY_ID_NOT_FOUND_TEST() throws Exception {
        this.mockMvc.perform(get("/designers/{id}", 999L)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("getById-notFound-designer"));
    }

    @Test
    @DisplayName("디자이너 정보를 수정 할 수 있다.")
    void MODIFY_DESIGNER_TEST() throws Exception {
        // TODO : 유저 기능 작성 완료 후 리팩토링
        ModifyDesignerRequest modifyDesignerRequest = ModifyDesignerRequest.builder()
                .id(designerResponse.getId())
                .name(designerResponse.getName())
                .image(designerResponse.getImage())
                .introduction(designerResponse.getIntroduction())
                .position(Position.DIRECTOR.getPosition())
                .hairshopId(designerResponse.getHairshopId())
                .build();
        this.mockMvc.perform(patch("/designers")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyDesignerRequest)))
                .andExpect(status().isNoContent())
                .andDo(document("modify-hairshop",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("image"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("introduction"),
                                fieldWithPath("position").type(JsonFieldType.STRING).description("position"),
                                fieldWithPath("hairshopId").type(JsonFieldType.NUMBER).description("hairshopId")
                        )
                ));
    }

    @Test
    @DisplayName("수정하려는 디자이너가 없을 경우 테스트")
    void MODIFY_HAIRSHOP_NOT_FOUND_TEST() throws Exception {
        ModifyDesignerRequest modifyDesignerRequest = ModifyDesignerRequest.builder()
                .id(999L)
                .hairshopId(designerResponse.getHairshopId())
                .build();
        this.mockMvc.perform(patch("/designers")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyDesignerRequest)))
                .andExpect(status().isNotFound())
                .andDo(document("modify-notFound-designer"));
    }

    @Test
    @DisplayName("해당 아이디의 디자이너를 삭제 할 수 있다.")
    void REMOVE_USER_TEST() throws Exception {
        this.mockMvc.perform(delete("/designers/{id}", designerResponse.getId()))
                .andExpect(status().isNoContent())
                .andDo(document("remove-designer"));
    }
}