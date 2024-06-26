package meetween.backend.authentication.application;

import static meetween.backend.support.fixture.common.AuthenticationFixtures.KAKAO_OAUTH_PROVIDER;
import static meetween.backend.support.fixture.common.AuthenticationFixtures.AUTHORIZATION_CODE;
import static meetween.backend.support.fixture.common.AuthenticationFixtures.FAKE_SOCIAL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import meetween.backend.authentication.domain.token.MemberToken;
import meetween.backend.authentication.dto.RenewalAccessTokenRequest;
import meetween.backend.authentication.dto.RenewalAccessTokenResponse;
import meetween.backend.authentication.exception.InvalidTokenException;
import meetween.backend.authentication.exception.NoSuchRefreshTokenException;
import meetween.backend.authentication.infrastructure.jwt.JwtTokenProvider;
import meetween.backend.authentication.application.AuthService;
import meetween.backend.member.domain.Member;
import meetween.backend.member.domain.MemberRepository;
import meetween.backend.support.annotation.ServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServiceTest extends ServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("카카오 소셜 로그인을 위한 링크를 생성한다.")
    @Test
    void 카카오_소셜_로그인을_위한_링크를_생성한다() {
        // given
        String kakaoLink = authService.getSocialLink(KAKAO_OAUTH_PROVIDER);

        // when, then
        assertThat(kakaoLink).isNotEmpty();
    }

    @DisplayName("토큰 생성을 하면 OAuth 서버에서 인증 후 토큰을 반환한다.")
    @Test
    void 토큰_생성을_하면_OAuth_서버에서_인증_후_토큰을_반환한다() {
        // given, when
        MemberToken memberToken = authService.generateTokenWithCode(AUTHORIZATION_CODE, KAKAO_OAUTH_PROVIDER);

        // then
        assertThat(memberToken.getAccessToken()).isNotEmpty();
    }


    @DisplayName("authorization code를 전달 받으면 회원이 데이터베이스에 저장된다.")
    @Test
    void authorization_code를_받으면_회원이_데이터베이스에_저장된다() {
        // given
        authService.generateTokenWithCode(AUTHORIZATION_CODE, KAKAO_OAUTH_PROVIDER);

        // when
        // actual = StubOAuthClient 가 반환하는 socialLoginId
        boolean actual = memberRepository.existsBySocialLoginId(FAKE_SOCIAL_ID);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이미 가입된 회원에 대한 authorization code를 전달 받으면 추가로 회원이 데이터베이스에 중복 생성되지 않는다.")
    @Test
    void 이미_가입된_회원에_대한_authorization_code를_전달_받으면_추가로_회원이_데이터베이스에_생성되지_않는다() {
        // given
        authService.generateTokenWithCode(AUTHORIZATION_CODE, KAKAO_OAUTH_PROVIDER);

        // when
        List<Member> actual = memberRepository.findAll();

        // then
        assertThat(actual).hasSize(1);
    }


    @DisplayName("이미 저장된 리프레시 토큰이 스토리지에 존재한다면 저장된 리프레시 토큰을 리턴한다.")
    @Test
    void 이미_저장된_리프레시_토큰이_스토리지에_존재한다면_저장된_리프레시_토큰을_리턴한다() {
        // given
        String code = "code";
        String provider = "kakao";
        MemberToken memberToken = authService.generateTokenWithCode(code, provider);

        // when
        MemberToken actual = authService.generateTokenWithCode(code, provider);

        // then
        assertThat(actual.getRefreshToken()).isEqualTo(memberToken.getRefreshToken());
    }

    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 갱신한다.")
    @Test
    void 리프레시_토큰으로_새로운_엑세스_토큰을_갱신한다() {
        // given
        String testRefreshToken = jwtTokenProvider.createRefreshToken(10L);

        RenewalAccessTokenRequest renewalAccessTokenRequest
                = new RenewalAccessTokenRequest(testRefreshToken);
        RenewalAccessTokenResponse renewalAccessTokenResponse
                = authService.generateRenewalAccessToken(renewalAccessTokenRequest);

        // when, then
        assertThat(renewalAccessTokenResponse.getAccessToken()).isNotEmpty();
    }

    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급시, 리프레시 토큰이 유효하지 않다면 예외를 던진다.")
    @Test
    void 리프레시_토큰으로_새로운_엑세스_토큰을_발급_할_때_리프레시_토큰이_유효하지_않으면_예외를_던진다() {
        // given
        String testRefreshToken = "invalid-refresh-token";
        RenewalAccessTokenRequest renewalAccessTokenRequest = new RenewalAccessTokenRequest(testRefreshToken);

        // when, then
        assertThatThrownBy(() -> authService.generateRenewalAccessToken(renewalAccessTokenRequest))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("데이터베이스에 존재하지 않는 리프레시 토큰으로 엑세스 토큰을 갱신할시 예외가 발생한다.")
    @Test
    void 존재하지_않는_리프레시_토큰으로_엑세스_토큰을_갱신할시_예외가_발생한다() {
        // given
        long memberId = -1L;
        String notExistToken = jwtTokenProvider.createToken(String.valueOf(memberId), 100000);
        RenewalAccessTokenRequest renewalAccessTokenRequest
                = new RenewalAccessTokenRequest(notExistToken);

        // when, then
        assertThatThrownBy(() -> authService.generateRenewalAccessToken(renewalAccessTokenRequest))
                .isInstanceOf(InvalidTokenException.class);
    }


    @DisplayName("로그아웃을 시도하면 리프레시 토큰을 삭제한다.")
    @Test
    void 로그아웃을_시도하면_리프레시_토큰을_삭제한다() {
        // given
        long memberId = 11L;
        String testRefreshToken = jwtTokenProvider.createRefreshToken(memberId);

        // when, then
        Assertions.assertDoesNotThrow(() -> authService.removeRefreshToken(memberId));
    }

    @DisplayName("데이터베이스에 존재하지 않는 리프레시 토큰을 삭제시 예외가 발생한다.")
    @Test
    void 데이터베이스에_존재하지_않는_라프레시_토큰을_삭제시_예외가_발생한다() {
        // given
        long memberId = -1L;

        // whem then
        assertThatThrownBy(() -> authService.removeRefreshToken(memberId))
                .isInstanceOf(NoSuchRefreshTokenException.class);
    }
}
