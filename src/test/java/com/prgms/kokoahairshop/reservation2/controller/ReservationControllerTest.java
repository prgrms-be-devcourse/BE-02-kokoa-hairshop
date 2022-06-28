package com.prgms.kokoahairshop.reservation2.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgms.kokoahairshop.reservation2.exception.ReservationCancelTimeoutException;
import com.prgms.kokoahairshop.reservation2.exception.ReservationNotFoundException;
import com.prgms.kokoahairshop.reservation2.exception.ReservationNotReservedException;
import com.prgms.kokoahairshop.reservation2.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @InjectMocks
    ReservationController controller;

    @Mock
    ReservationService service;

    MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(ReservationControllerAdvice.class)
            .build();
    }

    @Test
    void 사용자는_예약을_취소할_수_있다() throws Exception {
        doNothing().when(service).cancelReservation(1L);

        mockMvc.perform(patch("/reservations/1/user"))
            .andExpect(status().isNoContent());
    }

    @Test
    void 헤어샵은_예약을_취소할_수_있다() throws Exception {
        doNothing().when(service).cancelReservation(1L);

        mockMvc.perform(patch("/reservations/1/hairshop"))
            .andExpect(status().isNoContent());
    }

    @Test
    void 예약_취소_엔터티_없으면_404에러_발생() throws Exception {
        doThrow(ReservationNotFoundException.class).when(service).cancelReservation(1L);

        mockMvc.perform(patch("/reservations/1/user"))
            .andExpect(status().isNotFound());
    }

    @Test
    void 예약_취소_RESERVED_아니면_400에러_발생() throws Exception {
        doThrow(ReservationNotReservedException.class).when(service).cancelReservation(1L);

        mockMvc.perform(patch("/reservations/1/user"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void 예약_취소_가능시간_아니면_400에러_발생() throws Exception {
        doThrow(ReservationCancelTimeoutException.class).when(service).cancelReservation(1L);

        mockMvc.perform(patch("/reservations/1/user"))
            .andExpect(status().isBadRequest());
    }
}