package com.prgms.kokoahairshop.hairshop.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyHairshopRequest {

    @NotNull(message = "헤어샵 ID가 필요합니다.")
    private Long id;

    @NotBlank(message = "공백이 입력되었습니다.")
    @Size(min = 1, max = 30, message = "이름은 1자 이상 30자 이하로 해주세요.")
    private String name;

    @Size(min = 12, max = 14, message = "번호는 12자 이상 14이하로 해주세요.")
    private String phoneNumber;

    @NotNull(message = "영업시작시간을 입력해주세요.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "시간을 HH:mm으로 입력해주세요.")
    private String startTime;

    @NotNull(message = "영업종료시간을 입력해주세요.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "시간을 HH:mm으로 입력해주세요.")
    private String endTime;

    @Size(min = 1, max = 7, message = "휴무일은 월요일에서 일요일 사이여야 합니다.")
    private String closedDay;

    @NotNull
    @Size(min = 1, max = 3, message = "예약범위는 3가지 중 하나여야 합니다.")
    private String reservationRange;

    @NotNull(message = "예약시작시간을 입력해주세요.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "시간을 HH:mm으로 입력해주세요.")
    private String reservationStartTime;

    @NotNull(message = "예약마감시간을 입력해주세요.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "시간을 HH:mm으로 입력해주세요.")
    private String reservationEndTime;

    @NotNull(message = "당일예약가능 여부를 선택해주세요.")
    private Boolean sameDayAvailable;

    @Size(min = 1, max = 100, message = "도로명 주소를 입력해주세요.")
    private String roadNameNumber;

    @NotNull(message = "대표사진을 등록해주세요.")
    @Size(max = 200, message = "이미지 URL은 200자 이하로 해주세요.")
    private String profileImg;

    @Size(max = 200, message = "매장 소개는 200자 이하로 해주세요.")
    private String introduction;

    @NotNull(message = "유저 ID가 필요합니다.")
    private Long userId;
}
