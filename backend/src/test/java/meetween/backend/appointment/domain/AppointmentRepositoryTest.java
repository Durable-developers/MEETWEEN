package meetween.backend.appointment.domain;

import static meetween.backend.support.fixture.common.MemberFixtures.*;

import meetween.backend.appointment.exception.NoExistAppointmentException;
import meetween.backend.category.domain.Category;
import meetween.backend.category.domain.CategoryColor;
import meetween.backend.category.domain.CategoryRepository;
import meetween.backend.global.config.JpaAuditConfig;
import meetween.backend.member.domain.Member;
import meetween.backend.member.domain.MemberRepository;
import meetween.backend.member.domain.SocialType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@DataJpaTest
@Import(JpaAuditConfig.class)
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AppointmentUserRepository appointmentUserRepository;

    @DisplayName("초대코드를 통해 약속을 찾는다")
    @Test
    void 초대코드를_통해_약속을_찾는다() {
        //given
        Appointment appointment = new Appointment("수현의 약속", 123456L, LocalDateTime.now().plusDays(1), 3L, BigDecimal.valueOf(126.99597295767953), BigDecimal.valueOf(37.5280674292228));
        Long inviteCode = appointment.getInviteCode();
        Category category = new Category("스터디", CategoryColor._9A61D2, appointment);
        appointment.setCategory(category);

        appointmentRepository.save(appointment);
        categoryRepository.save(category);

        //when
        Appointment actual = appointmentRepository.getByInviteCode(inviteCode);

        //then
        Assertions.assertThat(actual).isEqualTo(appointment);
    }

    @DisplayName("존재하지 않는 초대코드로 약속을 조회하면 예외를 발생시킨다.")
    @Test
    void 존재하지_않는_초대코드를_약속을_조회하면_예외를_발생시킨다() {
        //given
        Appointment appointment = new Appointment("수현의 약속", 123456L, LocalDateTime.now().plusDays(1), 3L, BigDecimal.valueOf(126.99597295767953), BigDecimal.valueOf(37.5280674292228));
        Category category = new Category("스터디", CategoryColor._9A61D2, appointment);
        appointment.setCategory(category);

        appointmentRepository.save(appointment);
        categoryRepository.save(category);

        //when & then
        Assertions.assertThatThrownBy(() -> {
            appointmentRepository.getByInviteCode(111111L);
        }).isInstanceOf(NoExistAppointmentException.class);
    }

    @DisplayName("존재하는 초대 코드 라면 참을 반환한다.")
    @Test
    void 존재하는_초대_코드라면_참을_반환한다() {
        //given
        Appointment appointment = new Appointment("수현의 약속", 123456L, LocalDateTime.now().plusDays(1), 3L, BigDecimal.valueOf(126.99597295767953), BigDecimal.valueOf(37.5280674292228));

        appointmentRepository.save(appointment);

        //when, then
        Assertions.assertThat(appointmentRepository.existsByInviteCode(appointment.getInviteCode())).isEqualTo(true);
    }

    @DisplayName("카테고리 이름을 통해 로그인 된 유저가 속한 약속들을 조회한다.")
    @Test
    void 카테고리_이름을_통해_로그인된_유저가_속한_약속들을_조회한다() {
        //given
        Member member = new Member(수현_아이디, 수현_프로필_이미지, 수현_이름, SocialType.KAKAO);
        Appointment appointment1 = new Appointment("수현의 약속", 123456L, LocalDateTime.now().plusDays(1), 3L, BigDecimal.valueOf(126.99597295767953), BigDecimal.valueOf(37.5280674292228));
        Appointment appointment2 = new Appointment("만성의 약속", 123456L, LocalDateTime.now().plusDays(1), 3L, BigDecimal.valueOf(126.99597295767953), BigDecimal.valueOf(37.5280674292228));
        Category category1 = new Category("스터디", CategoryColor._9A61D2, appointment1);
        Category category2 = new Category("스터디", CategoryColor._9A61D2, appointment2);
        AppointmentUser appointmentUser1 = new AppointmentUser(appointment1, member, MemberAuthority.ADMIN);
        AppointmentUser appointmentUser2 = new AppointmentUser(appointment2, member, MemberAuthority.ADMIN);
        appointment1.setCategory(category1);
        appointment2.setCategory(category2);

        memberRepository.save(member);
        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);
        appointmentUserRepository.save(appointmentUser1);
        appointmentUserRepository.save(appointmentUser2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        //when
        List<Appointment> actual = appointmentRepository.findByUserAndCategoryName(member, category1.getName());

        //then
        Assertions.assertThat(actual).hasSize(2);
    }
}