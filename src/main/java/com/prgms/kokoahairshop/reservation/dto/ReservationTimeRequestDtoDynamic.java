package com.prgms.kokoahairshop.reservation.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTimeRequestDtoDynamic {

    @NotNull(message = "날짜를 입력해주세요.")
    private LocalDate date;

    @NotBlank(message = "시간을 입력해주세요.")
    @Size(min = 5, max = 5, message = "시간을 5자로 입력해주세요.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "시간을 HH:mm으로 입력해주세요.")
    private String reservationStartTime;

    @NotBlank(message = "시간을 입력해주세요.")
    @Size(min = 5, max = 5, message = "시간을 5자로 입력해주세요.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "시간을 HH:mm으로 입력해주세요.")
    private String reservationEndTime;
}