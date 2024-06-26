package meetween.backend.support.fixture.common;

import meetween.backend.member.domain.SocialType;
import meetween.backend.member.domain.Member;
import meetween.backend.member.dto.MemberResponse;

public class MemberFixtures {
    // 수현
    public static final String 수현_아이디 = "soozzang";
    public static final String 수현_프로필_이미지 = "https://avatars.githubusercontent.com/u/88240193?v=4";
    public static final String 수현_이름 = "수현";
    public static Member 수현_유저() {
        return new Member(
                수현_아이디,
                수현_프로필_이미지,
                수현_이름,
                SocialType.KAKAO
        );
    }

    // 민성
    public static final String 민성_아이디 = "msung99";
    public static final String 민성_프로필_이미지 = "https://avatars.githubusercontent.com/u/88240193?v=5";
    public static final String 민성_이름 = "민성";
    public static Member 민성_유저() {
        return new Member(
                민성_아이디,
                민성_프로필_이미지,
                민성_이름,
                SocialType.KAKAO
        );
    }

    // 주용
    public static final String 주용_아이디 = "wuzoo";
    public static final String 주용_프로필_이미지 = "https://avatars.githubusercontent.com/u/88240193?v=6";
    public static final String 주용_이름 = "주용";
    public static Member 주용_유저() {
        return new Member(
                주용_아이디,
                주용_프로필_이미지,
                주용_이름,
                SocialType.KAKAO
        );
    }

    public static MemberResponse 수현_응답() {
        return new MemberResponse(
                1L,
                "soozzang",
                "https://avatars.githubusercontent.com/u/88240193?v=4",
                "한수현"
        );
    }
}