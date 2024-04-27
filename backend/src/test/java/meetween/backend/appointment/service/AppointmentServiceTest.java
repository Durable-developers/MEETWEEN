package meetween.backend.appointment.service;

import meetween.backend.appointment.domain.Appointment;
import meetween.backend.appointment.domain.AppointmentRepository;
import meetween.backend.appointment.domain.AppointmentUser;
import meetween.backend.appointment.domain.AppointmentUserRepository;
import meetween.backend.appointment.dto.response.AppointmentResponse;
import meetween.backend.category.domain.Category;
import meetween.backend.category.domain.CategoryRepository;
import meetween.backend.support.fixture.UserFixtures;
import meetween.backend.user.domain.User;
import meetween.backend.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static meetween.backend.support.fixture.AppointmentFixtures.수현_약속;
import static meetween.backend.support.fixture.AppointmentFixtures.수현_약속_요청;
import static meetween.backend.support.fixture.AppointmentUserFixtures.일반_약속_유저_생성;
import static meetween.backend.support.fixture.CategoryFixtures.스터디_카테고리;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentUserRepository appointmentUserRepository;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = Mockito.spy(UserFixtures.수현_유저());
        when(mockUser.getId()).thenReturn(1L);
    }

    @DisplayName("약속을 생성한다.")
    @Test
    void 약속을_생성한다() {
        // given
        given(userRepository.getById(anyLong()))
                .willReturn(mockUser);
        given(categoryRepository.save(any(Category.class)))
                .willReturn(스터디_카테고리());
        given(appointmentRepository.save(any(Appointment.class)))
                .willReturn(수현_약속());
        given(appointmentUserRepository.save(any(AppointmentUser.class)))
                .willReturn(일반_약속_유저_생성());

        // when
        final AppointmentResponse actual = appointmentService.save(mockUser.getId(), 수현_약속_요청);

        // then
        Assertions.assertThat(actual.getTitle()).isEqualTo("수현의 약속");
    }
}