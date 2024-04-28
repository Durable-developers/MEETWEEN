package meetween.backend.appointment.dto.request;

import jakarta.validation.constraints.NotNull;
import meetween.backend.appointment.domain.Appointment;
import meetween.backend.category.domain.Category;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AppointmentCreateRequest {

    @NotNull(message = "Null일 수 없습니다.")
    private final String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private final LocalDateTime appointmentDateTime;

    @NotNull(message = "Null일 수 없습니다.")
    private final BigDecimal latitude;

    @NotNull(message = "Null일 수 없습니다.")
    private final BigDecimal longitude;

    @NotNull(message = "Null일 수 없습니다.")
    private final Long memberCount;

    @NotNull(message = "Null일 수 없습니다.")
    private final String categoryName;

    @NotNull(message = "Null일 수 없습니다.")
    private final String categoryColor;

    public AppointmentCreateRequest(final String title, final LocalDateTime appointmentDateTime, final BigDecimal latitude, final BigDecimal longitude, final Long memberCount, final String categoryName, final String categoryColor) {
        this.title = title;
        this.appointmentDateTime = appointmentDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memberCount = memberCount;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }

    public Appointment toEntity(Category category) {
        return new Appointment(title, appointmentDateTime, category, memberCount, latitude, longitude);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Long getMemberCount() {
        return memberCount;
    }
}
