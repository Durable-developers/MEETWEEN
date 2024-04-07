package meetween.backend.user.service;

import meetween.backend.user.domain.User;
import meetween.backend.user.domain.UserRepository;
import meetween.backend.user.dto.UserResponse;
import meetween.backend.user.exception.NoExistUserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse findById(final Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(NoExistUserException::new);
        return new UserResponse(user);
    }
}