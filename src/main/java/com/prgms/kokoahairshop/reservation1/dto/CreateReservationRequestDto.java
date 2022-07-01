package com.prgms.kokoahairshop.reservation1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
public class CreateReservationRequestDto {

    @Positive(message = "사용자 id를 양수로 입력해주세요.")
    private Long userId;

    @Positive(message = "헤어샵 id를 양수로 입력해주세요.")
    private Long hairshopId;

    @Positive(message = "디자이너 id를 양수로 입력해주세요.")
    private Long designerId;

    @Positive(message = "메뉴 id를 양수로 입력해주세요.")
    private Long menuId;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 20, message = "이름을 20자 이하로 작성해주세요.")
    private String name;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Size(max = 20, message = "휴대폰 번호를 20자 이하로 작성해주세요.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "올바르지 않은 휴대폰 번호입니다.")
    private String phoneNumber;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @NotNull(message = "날짜를 입력해주세요.")
    private LocalDate date;

    @NotNull(message = "시간을 입력해주세요.")
    @Size(max = 5, message = "시간을 HH:mm으로 입력해주세요.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "시간을 HH:mm으로 입력해주세요.")
    private String time;

    @Size(min = 10, max = 100, message = "요청사항은 10자 이상 100로 입력해주세요.")
    private String request;

    @PositiveOrZero(message = "결제 금액은 양수와 0만 가능합니다.")
    private int paymentAmount;
}
