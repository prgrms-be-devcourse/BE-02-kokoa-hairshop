package com.prgms.kokoahairshop.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.dto.CreateMenuRequest;
import com.prgms.kokoahairshop.menu.dto.MenuResponse;
import com.prgms.kokoahairshop.menu.dto.ModifyMenuRequest;
import com.prgms.kokoahairshop.menu.entity.Gender;
import com.prgms.kokoahairshop.menu.entity.Type;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.menu.service.MenuService;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
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
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@WithMockUser(username = "example@gmail.com", roles = "USER")
class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private HairshopRepository hairshopRepository;

    private Hairshop hairshop;
    private MenuResponse menuResponse;

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
        CreateMenuRequest createMenuRequest = CreateMenuRequest.builder()
                .name("커트")
                .price(8000)
                .discount(0)
                .gender(Gender.공용.getGender())
                .type(Type.커트.getType())
                .image("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .exposedTime(30)
                .hairshopId(hairshop.getId())
                .build();
        menuResponse = menuService.insert(createMenuRequest);
    }

    @AfterEach
    void tearDown() {
        menuRepository.deleteAll();
        hairshopRepository.deleteAll();
    }

    @Test
    @DisplayName("메뉴 등록 테스트")
    void MENU_INSERT_TEST() throws Exception {
        CreateMenuRequest request = CreateMenuRequest.builder()
                .name("펌")
                .price(30000)
                .discount(0)
                .gender(Gender.공용.getGender())
                .type(Type.펌.getType())
                .image("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .exposedTime(30)
                .hairshopId(hairshop.getId())
                .build();
        this.mockMvc.perform(post("/menu")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andDo(
                        document("register-menu",
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("price"),
                                        fieldWithPath("discount").type(JsonFieldType.NUMBER).description("discount"),
                                        fieldWithPath("gender").type(JsonFieldType.STRING).description("gender"),
                                        fieldWithPath("type").type(JsonFieldType.STRING).description("type"),
                                        fieldWithPath("image").type(JsonFieldType.STRING).description("image"),
                                        fieldWithPath("exposedTime").type(JsonFieldType.NUMBER).description("exposedTime"),
                                        fieldWithPath("hairshopId").type(JsonFieldType.NUMBER).description("hairshopId")
                                )
                        ));
    }

    @Test
    @DisplayName("전체 메뉴 조회 테스트")
    void GET_MENU_LIST_TEST() throws Exception {
        this.mockMvc.perform(get("/menu")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andDo(document("getAll-menu",
                        responseFields(
                                fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("content[]"),
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("content[].id"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("content[].name"),
                                fieldWithPath("content[].price").type(JsonFieldType.NUMBER).description("content[].price"),
                                fieldWithPath("content[].discount").type(JsonFieldType.NUMBER).description("content[].discount"),
                                fieldWithPath("content[].gender").type(JsonFieldType.STRING).description("content[].gender"),
                                fieldWithPath("content[].type").type(JsonFieldType.STRING).description("content[].type"),
                                fieldWithPath("content[].image").type(JsonFieldType.STRING).description("content[].image"),
                                fieldWithPath("content[].exposedTime").type(JsonFieldType.NUMBER).description("content[].exposedTime"),
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
    @DisplayName("헤어샵 아이디로 메뉴 조회 테스트")
    void GET_MENU_BY_HAIRSHOP_ID_TEST() throws Exception {
        this.mockMvc.perform(get("/menu/hairshop/{id}", menuResponse.getId())
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andDo(document("getByHairshopId-menu",
                        responseFields(
                                fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("content[]"),
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("content[].id"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("content[].name"),
                                fieldWithPath("content[].price").type(JsonFieldType.NUMBER).description("content[].price"),
                                fieldWithPath("content[].discount").type(JsonFieldType.NUMBER).description("content[].discount"),
                                fieldWithPath("content[].gender").type(JsonFieldType.STRING).description("content[].gender"),
                                fieldWithPath("content[].type").type(JsonFieldType.STRING).description("content[].type"),
                                fieldWithPath("content[].image").type(JsonFieldType.STRING).description("content[].image"),
                                fieldWithPath("content[].exposedTime").type(JsonFieldType.NUMBER).description("content[].exposedTime"),
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
    @DisplayName("메뉴 아이디로 메뉴 조회 테스트")
    void GET_MENU_BY_ID_TEST() throws Exception {
        this.mockMvc.perform(get("/menu/{id}", menuResponse.getId())
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(jsonPath("$.id").value(menuResponse.getId()))
                .andExpect(jsonPath("$.name").value(menuResponse.getName()))
                .andExpect(jsonPath("$.price").value(menuResponse.getPrice()))
                .andExpect(jsonPath("$.discount").value(menuResponse.getDiscount()))
                .andExpect(jsonPath("$.gender").value(menuResponse.getGender()))
                .andExpect(jsonPath("$.type").value(menuResponse.getType()))
                .andExpect(jsonPath("$.image").value(menuResponse.getImage()))
                .andExpect(jsonPath("$.exposedTime").value(menuResponse.getExposedTime()))
                .andExpect(jsonPath("$.hairshopId").value(menuResponse.getHairshopId()))
                .andDo(document("getById-menu",
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("price"),
                                fieldWithPath("discount").type(JsonFieldType.NUMBER).description("discount"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("gender"),
                                fieldWithPath("type").type(JsonFieldType.STRING).description("type"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("image"),
                                fieldWithPath("exposedTime").type(JsonFieldType.NUMBER).description("exposedTime"),
                                fieldWithPath("hairshopId").type(JsonFieldType.NUMBER).description("hairshopId"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("createdAt"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("updatedAt")
                        )
                ));
    }

    @Test
    @DisplayName("해당 아이디의 메뉴가 없을 경우 테스트")
    void GET_HAIRSHOP_BY_ID_NOT_FOUND_TEST() throws Exception {
        this.mockMvc.perform(get("/menu/{id}", 999L)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("getById-notFound-menu"));
    }

    @Test
    @DisplayName("메뉴 정보를 수정 할 수 있다.")
    void MODIFY_MENU_TEST() throws Exception {
        // TODO : 유저 기능 작성 완료 후 리팩토링
        ModifyMenuRequest modifyMenuRequest = ModifyMenuRequest.builder()
                .id(menuResponse.getId())
                .name("커트")
                .price(10000)
                .discount(0)
                .gender(Gender.공용.getGender())
                .type(Type.커트.getType())
                .image("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .exposedTime(30)
                .hairshopId(hairshop.getId())
                .build();
        this.mockMvc.perform(patch("/menu")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyMenuRequest)))
                .andExpect(status().isNoContent())
                .andDo(document("modify-menu",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("price"),
                                fieldWithPath("discount").type(JsonFieldType.NUMBER).description("discount"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("gender"),
                                fieldWithPath("type").type(JsonFieldType.STRING).description("type"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("image"),
                                fieldWithPath("exposedTime").type(JsonFieldType.NUMBER).description("exposedTime"),
                                fieldWithPath("hairshopId").type(JsonFieldType.NUMBER).description("hairshopId")
                        )
                ));
    }

    @Test
    @DisplayName("수정하려는 메뉴가 없을 경우 테스트")
    void MODIFY_MENU_NOT_FOUND_TEST() throws Exception {
        ModifyMenuRequest modifyMenuRequest = ModifyMenuRequest.builder()
                .id(999L)
                .hairshopId(menuResponse.getHairshopId())
                .build();
        this.mockMvc.perform(patch("/menu")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyMenuRequest)))
                .andExpect(status().isNotFound())
                .andDo(document("modify-notFound-menu"));
    }

    @Test
    @DisplayName("해당 아이디의 메뉴를 삭제 할 수 있다.")
    void REMOVE_MENU_TEST() throws Exception {
        this.mockMvc.perform(delete("/menu/{id}", menuResponse.getId()))
                .andExpect(status().isNoContent())
                .andDo(document("remove-menu"));
    }
}